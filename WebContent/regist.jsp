<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<!--[if IE 8 ]> <html lang="en" class="ie8"> <![endif]-->
<!--[if IE 9 ]> <html lang="en" class="ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en"> <!--<![endif]-->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>商家入驻注册页面</title>

<!--[if lt IE 9]>
<script src="js/html5shiv.js"></script>
<![endif]-->

<link href="css/normalize.css" rel="stylesheet"/>
<link href="css/jquery-ui.css" rel="stylesheet"/>
<link href="css/jquery.idealforms.min.css" rel="stylesheet" media="screen"/>

<style type="text/css">
body{font:normal 15px/1.5 Arial, Helvetica, Free Sans, sans-serif;color: #222;background:url(images/pattern.png);overflow-y:scroll;padding:60px 0 0 0;}
#my-form{width:755px;margin:0 auto;border:1px solid #ccc;padding:3em;border-radius:3px;box-shadow:0 0 2px rgba(0,0,0,.2);}
#comments{width:350px;height:100px;}
</style>

</head>
<body>


<div class="row">

  <div class="eightcol last">

    <!-- Begin Form -->

    <form id="my-form" method="post" action="<c:url value='/registSeller'/>" enctype="multipart/form-data">

        <section name="第一步">

          <div><label>商家名称:</label><input id="salename" name="salename" type="text"/></div>
          <div><label>登录密码:</label><input id="password" name="password" type="password"/></div>
          <div><label>注册邮箱:</label><input id="email" name="email" data-ideal="required email" type="email"/></div>
          <div><label>成立日期:</label><input id="date" name="date" class="datepicker" data-ideal="date" type="text" placeholder="月/日/年"/></div>
          <div><label>身份证号:</label><input id="verfiyCard" name="verfiyCard" multiple type="file"/></div>
          <div><label>商家地址:</label><input id="address" name="address" type="text"/></div>
        </section>

        <section name="第二步">
         <div><label>联系电话:</label><input type="tel" id="phone" name="phone"/></div>
          <div><label>营业执照:</label><input id="salefile" name="salefile" multiple type="file"/></div>
          <div><label>卫生执照:</label><input id="safefile" name="safefile" multiple type="file"/></div>
          <div><label>商家规模:</label>
           <select id="states" name="states">
            <option value="default">&ndash; 选择规模 &ndash;</option>
            <option value="AL">01-10人</option>
            <option value="AK">11-50人</option>
            <option value="AZ">50-100人</option>
          </select>
          </div>
        <div><label>邮编:</label><input type="text" name="zip" data-ideal="zip"/></div>
        <div><label>备注:</label><textarea id="comments" name="comments"></textarea></div>
        </section>

        <section name="第三步">
          <div id="languages">
            <label>营业项目:</label>
            <label><input type="checkbox" id="types" name="types" value="breakfast"/>早餐</label>
            <label><input type="checkbox" id="types" name="types" value="lunch"/>午餐</label>
            <label><input type="checkbox" id="types" name="types" value="dinner"/>晚餐</label>
            <label><input type="checkbox" id="types" name="types" value="fast"/>快餐</label>
          </div>
          <div><label>主营项目:</label>
            <label><input value='b' type="radio" id="perfect" name="perfect" checked/>早餐</label>
            <label><input value='l' type="radio" id="perfect" name="perfect"/>午餐</label>
            <label><input value='d' type="radio" id="perfect" name="perfect"/>晚餐</label>
            <label><input value='f' type="radio" id="perfect" name="perfect"/>快餐</label>
          </div>
      </section>

      <div><hr/></div>

      <div>
        <button type="submit">提交</button>
        <button id="reset" type="button">重置</button>
      </div>

    </form>

    <!-- End Form -->

  </div>

</div>


<script type="text/javascript" src="js/jquery-1.8.2.min.js"></script>
<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/jquery.idealforms.js"></script>
<script type="text/javascript">
var options = {

	onFail: function(){
		alert( $myform.getInvalid().length +' invalid fields.' )
	},

	inputs: {
		'password': {
			filters: 'required pass',
		},
		'salename': {
			filters: 'required username',
			data: {
			//ajax: { url:'validate.php' }
			}
		},
		'vfile': {
			filters: 'extension',
			data: { extension: ['jpg'] }
		},
		'comments': {
			filters: 'min max',
			data: { min: 50, max: 200 }
		},
		'states': {
			filters: 'exclude',
			data: { exclude: ['default'] },
			errors : {
				exclude: '选择营业规模.'
			}
		},
		'langs[]': {
			filters: 'min max',
			data: { min: 2, max: 3 },
			errors: {
				min: 'Check at least <strong>2</strong> options.',
				max: 'No more than <strong>3</strong> options allowed.'
			}
		}
	}
	
};

var $myform = $('#my-form').idealforms(options).data('idealforms');

$('#reset').click(function(){
	$myform.reset().fresh().focusFirst()
});

$myform.focusFirst();
</script>
<div style="text-align:center;">
<p>第七组：<a href="" title="卖家系统" target="_blank">卖家系统</a></p>
</div>
</body>
</html>