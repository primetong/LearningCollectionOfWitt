<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML>
<html>

<head>
</head>

<body>
	<c:if test="${not empty sessionScope.user}">
		<form class="setting" action="console/update.html" method="post">
			<label> 
				<span>更换邮箱：${update_feedback_email}</span> 
				<input type="email" name="email" placeholder="填写您的新邮箱" value="${user.mail}"/>
			</label>
			<label> 
				<span>更换用户名：${update_feedback_username}</span> 
				<input type="text" name="username" placeholder="修改您的用户名" value="${user.username}"/>
			</label>
			<label> 
				<span>更换密码：${update_feedback_password}</span>
				<input type="password" name="passwordold" placeholder="旧密码" />
				<input type="password" name="password1st" placeholder="新密码，6~32 位，可以是数字、英文字母、下划线"/>
				<input type="password" name="password2nd" placeholder="再次输入密码以确认" />
			</label>
			<label>
				<span>${update_feedback_message}</span>
			</label>
			<label>
				<input type="submit" value="修改" />
			</label>
		</form>
	</c:if>
</body>

</html>