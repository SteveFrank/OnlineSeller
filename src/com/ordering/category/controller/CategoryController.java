package com.ordering.category.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ordering.category.po.Category;
import com.ordering.category.service.CategoryService;
import com.ordering.utils.PageBean;

@Controller
public class CategoryController {
	@Resource(name="categoryService")
	private CategoryService categoryService;
	
	@RequestMapping(value="/showCategorysAll")
	public String showCategorysAll(HttpServletRequest request, HttpServletResponse response) {
		/**
		 * 1、获取页面传递的PageCurrent
		 * 2、给定PageSize的值
		 * 3、使用PageCurrent和PageSize调用Service方法，得到PageBean，保存request域
		 * 4、转发到/admin/dept_info.jsp
		 */
		int PageCurrent = getPageCurrent(request);//获取当前页
		int PageSize = 4;//获取每页的记录数

		PageBean<Category> pageBean = categoryService.queryAll_PageCategory(PageCurrent, PageSize);
		pageBean.setUrl(getUrl(request));
		request.setAttribute("pb", pageBean);
		return "/showCategorysAll";
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
}
