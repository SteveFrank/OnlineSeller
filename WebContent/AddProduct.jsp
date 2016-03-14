<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>添加食品</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
</head>

<body>

	<div class="place">
    <span>位置：</span>
    <ul class="placeul">
    <li><a href="#">首页</a></li>
    <li><a href="#">添加食品</a></li>
    </ul>
    </div>
    
    <div class="formbody">
    
    <div class="formtitle"><span>添加食品信息</span></div>
    
    <ul class="forminfo">
    <form method="post" action="<c:url value='/addProduct?sid=${sessionScope.seller.sid }' />" enctype="multipart/form-data">
    	
    	<li><label>美食名称</label><input name="pname" id="pname" type="text" class="dfinput" /><i>名称不能超过10个字符</i></li>
    	<li><label>美食简介</label><input name="describle" id="describle" type="text" class="dfinput" /><i>简介不能超过50个字符</i></li>
    	<li><label>美食价格</label><input name="price" id="price" type="text" class="dfinput" /><i>请仔细核对</i></li>
    	<li>
    		<label>美食种类</label>
			 <select name="category" id="category">   
        		<c:forEach items='${categoryList }' var='category'>
        			 <option value="${category.cid }">${category.cname }</option>
        		</c:forEach>  
      		</select>   	
			<i>请仔细核对</i>
		</li>
    	<li><label>美食剪影</label><input name="image" id="image" multiple type="file" class="dfinput" /><i>请仔细比对，确保真实性</i></li>
    	<li><label>&nbsp;</label><input type="submit" class="btn" value="确认保存"/></li>
    </form>
    </ul>
    
    
    </div>


</body>

</html>
