<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script language="JavaScript" src="js/jquery.js"></script>

<script type="text/javascript">
$(function(){	
	//导航切换
	$(".menuson .header").click(function(){
		var $parent = $(this).parent();
		$(".menuson>li.active").not($parent).removeClass("active open").find('.sub-menus').hide();
		
		$parent.addClass("active");
		if(!!$(this).next('.sub-menus').size()){
			if($parent.hasClass("open")){
				$parent.removeClass("open").find('.sub-menus').hide();
			}else{
				$parent.addClass("open").find('.sub-menus').show();	
			}
		}
	});
	
	// 三级菜单点击
	$('.sub-menus li').click(function(e) {
        $(".sub-menus li.active").removeClass("active")
		$(this).addClass("active");
    });
	
	$('.title').click(function(){
		var $ul = $(this).next('ul');
		$('dd').find('.menuson').slideUp();
		if($ul.is(':visible')){
			$(this).next('.menuson').slideUp();
		}else{
			$(this).next('.menuson').slideDown();
		}
	});
})	
</script>


</head>

<body style="background:#f0f9fd;">
	<div class="lefttop"><span></span>后台管理系统</div>
    <dl class="leftmenu">
    <dd>
    <div class="title">
    <span><img src="images/leftico01.png" /></span>审批信息管理
    </div>
    	<ul class="menuson">
        <li class="active"><cite></cite><a href="<c:url value='/showSaleOwn?sid=${sessionScope.seller.sid }' />" target="rightFrame">查看审批信息</a><i></i></li>
        <li><cite></cite><a href="flow.html" target="rightFrame">查看审批流程</a><i></i></li>
        <li class="active"><cite></cite><a href="<c:url value='/refreshQueue?sid=${sessionScope.seller.sid }' />" target="rightFrame">刷新审批队列</a><i></i></li>
        </ul>    
    </dd>
    <dd><div class="title"><span><img src="images/leftico03.png" /></span>店铺信息管理</div>
    <ul class="menuson">
    	<li><cite></cite><a href="<c:url value='/showProductsAll?sid=${sessionScope.seller.sid }' />" target="rightFrame">查看当前食品</a><i></i></li>
    	<li><cite></cite><a href="<c:url value='/preAddProduct'/>" target="rightFrame">添加新的食品</a><i></i></li>
        <li><cite></cite><a href="<c:url value='/showCategorysAll'/>" target="rightFrame">查看食品分类</a><i></i></li>
        <li><cite></cite><a href="#">添加食品分类</a><i></i></li>
    </ul>    
    </dd>  
    <dd>
    <div class="title">
    <span><img src="images/leftico02.png" /></span>顾客订单管理
    </div>
    <ul class="menuson">
        <li><cite></cite><a href="<c:url value='/refreshQueue?sid=${sessionScope.seller.sid }' />" target="rightFrame">查看顾客订单</a><i></i></li>
        </ul>     
    </dd> 
    </dl>
</body>
</html>
