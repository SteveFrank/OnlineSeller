package com.ordering.product.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ordering.category.service.CategoryService;
import com.ordering.product.po.Product;
import com.ordering.product.service.ProductService;
import com.ordering.seller.po.Seller;
import com.ordering.utils.CommonUtils;
import com.ordering.utils.PageBean;

@Controller
public class ProductController {
	
	@Resource(name="productService")
	private ProductService productService;
	@Resource(name="categoryService")
	private CategoryService categoryService;
	
	@RequestMapping(value="/showProductsAll")
	public String showProductsAll(HttpServletRequest request, HttpServletResponse response) {
		String sid = request.getParameter("sid");
		/**
		 * 1、获取页面传递的PageCurrent
		 * 2、给定PageSize的值
		 * 3、使用PageCurrent和PageSize调用Service方法，得到PageBean，保存request域
		 * 4、转发到/admin/dept_info.jsp
		 */
		int PageCurrent = getPageCurrent(request);//获取当前页
		int PageSize = 4;//获取每页的记录数

		PageBean<Product> pageBean = productService.queryAll_PageProduct(sid, PageCurrent, PageSize);
		pageBean.setUrl(getUrl(request));
		request.setAttribute("pb", pageBean);
		return "/showProductsAll";
	}
	
	/**
	 * 获取当前的页面值
	 * @param request
	 * @return
	 */
	private int getPageCurrent(HttpServletRequest request){
		String value = request.getParameter("PageCurrent");
		if(value == null || value.trim().isEmpty()){
			return 1;
		} else{
			return Integer.parseInt(value);
		}
	}
	/**
	 * 截取url
	 * @param request
	 * @return
	 */
	public String getUrl(HttpServletRequest request){
		String contextPath = request.getContextPath();//获取项目名
		String servletPath = request.getServletPath();//获取servletPath，即/CustomerServlet
		String queryString = request.getQueryString();//获取问号之后的参数部份
		if(queryString == null) {
			return contextPath + servletPath + "?" + queryString;
		}
		//  判断参数部份中是否包含pc这个参数，如果包含，需要截取下去，不要这一部份。
		if(queryString.contains("&PageCurrent=")) {
			int index = queryString.lastIndexOf("&PageCurrent=");
			queryString = queryString.substring(0, index);
		}
		
		return contextPath + servletPath + "?" + queryString;
	}
	
	@RequestMapping(value="/preAddProduct")
	public String preAddProduct(HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute("categoryList", categoryService.queryAllCategory());
		return "/AddProduct";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/addProduct")
	public String addProduct(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		/**
		 * 1、使用上传三步，把表单数据封装到Product对象中
		 */
		//创建工厂
		DiskFileItemFactory factory = new DiskFileItemFactory();
		//获得解析器
		ServletFileUpload sfu = new ServletFileUpload(factory);
		//使用解析器解析request对象，得到List<FileItem>
		List<FileItem> fileItemList = sfu.parseRequest(request);
		//封装fileItemList中的相关的数据到Product对象中
		
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
		
		Product product = new Product();
		product.setPname(map.get("pname"));
		product.setDescrible(map.get("describle"));
		product.setPrice(Double.parseDouble(map.get("price")));
		product.setCategory(categoryService.findCategoryByCid(map.get("category")));
		product.setPid(CommonUtils.uuid());
		
		//保证上传文件的前缀和用户的ID是一致的，并且保证唯一性
		String savePath_image = request.getServletContext().getRealPath("/uploadProduct");
		String image = product.getPid()+"_"+fileItemList.get(4).getName();
		
		File destFile_image = new File(savePath_image,image);
		
		fileItemList.get(4).write(destFile_image);
		product.setImage("http://127.0.0.1:8080/OnlineSeller/uploadProduct/"+image);
		
		Seller seller = (Seller)request.getSession().getAttribute("seller");
		String sid = seller.getSid();
		
		productService.addProduct(product,sid);
		
		return "/showProductsAll?sid="+sid;
	}
}
