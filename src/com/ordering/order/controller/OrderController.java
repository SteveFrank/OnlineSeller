package com.ordering.order.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ordering.order.po.Order;
import com.ordering.order.service.OrderService;
import com.ordering.user.po.User;

@Controller
public class OrderController {
	
	@Resource(name="orderService")
	private OrderService orderService;
	
	@RequestMapping(value="/myOrders")
	public String myOrders(HttpServletRequest request, HttpServletResponse response){
		/*
		 * 1、从session得到当前的用户，再获取其他的uid
		 * 2、使用uid调用OrderService#myOrders(uid)得到该用户的所有订单List<Order>
		 * 3、把订单表保存到request域中，转发到/jsps/order/list.jsp
		 */
		User user = (User) request.getSession().getAttribute("session_user");
		List<Order> orderList = orderService.myOrders(user.getUid());
		request.setAttribute("orderList", orderList);
		return "jsps/order/list";
	}
	
	@RequestMapping(value="/load")
	public String load(HttpServletRequest request, HttpServletResponse response) {
		String oid = request.getParameter("oid");
		Order order = orderService.load(oid);
		request.setAttribute("order", order);
		return "jsps/order/desc";
	}
}
