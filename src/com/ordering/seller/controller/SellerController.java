package com.ordering.seller.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.jms.Connection;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.TextMessage;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ordering.seller.po.Seller;
import com.ordering.seller.service.SellerService;
import com.ordering.utils.CommonUtils;
import com.ordering.utils.Mail;
import com.ordering.utils.MailUtils;

import cn.dsna.util.images.ValidateCode;

@Controller
public class SellerController {

	@Resource(name="sellerService")
	private SellerService sellerService;
	@Resource(name="pooledConnectionFactory")
	private PooledConnectionFactory factory;
	@Resource(name="queueDestination")
	private ActiveMQQueue queue;
	
	/**
	 * 验证码的生成与保存
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/verifyCode")
	public String VerifyCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ValidateCode vc = new ValidateCode(100,45,4,20);
		String code = vc.getCode();
		System.out.println(code);
		HttpSession session = request.getSession();
		session.setAttribute("verifyCode", code);
		BufferedImage image = vc.getBuffImg();
		ImageIO.write(image, "bmp", response.getOutputStream());
		return null;
	}
	
	@RequestMapping(value="/login")
	public String login(HttpServletRequest request, HttpServletResponse response, Model model) {
		
		HttpSession session = request.getSession();
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String verifyCode = request.getParameter("verifyCode");
		String _verifyCode = (String) session.getAttribute("verifyCode");
		
		if(session.getAttribute("seller")!=null) {
			return "/main";
		}
		
		Seller seller = new Seller();
		seller.setEmail(email);
		seller.setPassword(password);
		if (verifyCode.equalsIgnoreCase(_verifyCode)) {
			/**
			 * 1、如果用户名或者密码不正确返回
			 * 2、如果正确查看邮箱是否激活
			 * 3、如果三个都符合才语序登录
			 */
			int login_flag = sellerService.login(seller);
			
			if (login_flag == 0) {
				model.addAttribute("msg","用户名或密码错误");
				return "/login";
			} else if (login_flag == 1){
				model.addAttribute("msg","邮箱没有激活");
				return "/login";
			} else {
				model.addAttribute("msg","登录成功");
				session.setAttribute("seller", sellerService.findSellerByEmail(seller.getEmail()));
				return "/main";
			}
			
		} else {
			model.addAttribute("msg","验证码错误");
			return "/login";
		}
		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/registSeller")
	public String RegistSeller(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		//创建工厂
		DiskFileItemFactory factory = new DiskFileItemFactory();
		//获得解析器
		ServletFileUpload sfu = new ServletFileUpload(factory);
		
		List<FileItem> fileItemList = sfu.parseRequest(request);
		//封装fileItemList中的相关的数据到Book对象中
		/**
		 * 先将普通表单字段封装到Map中
		 * 再把Map中的数据封装到Seller对象中
		 */
		//封装普通数据到Seller对象中之后在进行文件路径的保存和文件的自身的上传
		Map<String , String> map = new HashMap<String,String>();
		for (FileItem fileItem : fileItemList) {
			if(fileItem.isFormField()){
				map.put(fileItem.getFieldName(), fileItem.getString("utf-8"));
			}
		}
		
		Seller seller = CommonUtils.toBean(map, Seller.class);
		seller.setSid(CommonUtils.uuid());
		
		//保证上传文件的前缀和用户的ID是一致的，并且保证唯一性
		String savePath_verfiyCard = request.getServletContext().getRealPath("/uploadPerson");
		String verfiyCard_name = seller.getSid() + "_" + fileItemList.get(4).getName();
		seller.setVerfiyCard(verfiyCard_name);
		String savePath_file = request.getServletContext().getRealPath("/uploadVerify");
		String salefile_name = "sale_" + seller.getSid() + "_" + fileItemList.get(7).getName();
		String safefile_name = "safe_" + seller.getSid() + "_" + fileItemList.get(8).getName();
		seller.setSalefile(salefile_name);
		seller.setSafefile(safefile_name);
		
		File destFile_verfiyCard = new File(savePath_verfiyCard,verfiyCard_name);
		File destFile_salefile = new File(savePath_file,salefile_name);
		File destFile_safefile = new File(savePath_file,safefile_name);
		
		fileItemList.get(4).write(destFile_verfiyCard);
		fileItemList.get(7).write(destFile_salefile);
		fileItemList.get(8).write(destFile_safefile);
		
		seller.setVerfiyCard("uploadPerson/"+verfiyCard_name);
		seller.setSalefile("uploadVerify/"+salefile_name);
		seller.setSafefile("uploadVerify/"+safefile_name);
		//设置邮箱激活码
		seller.setActiveCode(CommonUtils.uuid());
		//设置邮箱没有激活
		seller.setEmailActive("0");
		//设置未审核
		seller.setAccpetActive("0");
		
		System.out.println(seller);
		
		sellerService.registSeller(seller);
		
		/**
		 * 开始发送邮箱激活码
		 */
		//发邮件激活
		//准备配置文件
		Properties props = new Properties();
		props.load(this.getClass().getClassLoader().getResourceAsStream("email_template.properties"));
		String host = props.getProperty("host");
		String uname = props.getProperty("uname");
		String pwd = props.getProperty("pwd");
		String from = props.getProperty("from");
		String to = seller.getEmail();
		String subject = props.getProperty("subject");
		String content = props.getProperty("content");
		content = MessageFormat.format(content, seller.getActiveCode());
		Session session = MailUtils.createSession(host, uname, pwd);
		Mail mail = new Mail(from,to,subject,content);
		try {
			MailUtils.send(session, mail);
		} catch (MessagingException e) {
			System.out.println("=====邮件发送出现异常=====");
		}
		
		model.addAttribute("msg",seller.getSalename());
		
		return "activePlease";
	}
	
	@RequestMapping(value="/activeEmail")
	public String activeEmail(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		String code = request.getParameter("code");
		/**
		 * 1、通过邮箱激活码查询商户
		 */
		Seller seller = sellerService.findSellerByEmailCode(code);
		/**
		 * 2、通过查询到的商户进行激活
		 */
		sellerService.active(code);
		
		JSONObject json = JSONObject.fromObject(seller);
		String seller_json = json.toString();
		
		Connection connection = factory.createConnection();
		connection.start();
		
		javax.jms.Session sessionJMS = connection.createSession(true, javax.jms.Session.CLIENT_ACKNOWLEDGE);
		MessageProducer producer = sessionJMS.createProducer(queue);
		TextMessage message = sessionJMS.createTextMessage();
		
		System.out.println(seller_json);
		
		message.setText(seller_json);
		producer.send(message);
		
		sessionJMS.commit();
		producer.close();
		sessionJMS.close();
		connection.close();
		
		model.addAttribute("msg","邮箱已经激活，已经提交");
		return "login";
	}
	
	@RequestMapping(value="/showSaleOwn")
	public String showSaleOwn(HttpServletRequest request, HttpServletResponse response, Model model) {
		String sid = request.getParameter("sid");
		System.out.println("Contronller====sid===="+sid);
		Seller seller = sellerService.findSellerBySid(sid);
		System.out.println("Contronller====seller===="+seller);
		request.setAttribute("seller", seller);
		return "form";
	}
	
	@RequestMapping(value="/refreshQueue")
	public String refreshQueue(HttpServletRequest request,HttpServletResponse response,Model model) 
			throws Exception {
		/**
		 * 1、查看队列中是否存在数据
		 * 2、如果有数据就添加在库中
		 * 3、显示所有的数据
		 */
		Connection connection = factory.createConnection();
		connection.start();
		javax.jms.Session session = connection.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
		
		MessageConsumer consumer = session.createConsumer(queue);
		TextMessage message = (TextMessage) consumer.receive();
		
		String str_seller = message.getText();
		String[] infos = str_seller.split("-");
		
		String sid = infos[0];
		String b = infos[1];
		
		sellerService.updateAccpetState(sid,b);
		return "queueFresh";
	}
	
}
