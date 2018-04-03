<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE HTML>
<html>
<head>
</head>

<body>
	<c:if test="${empty sessionScope.user}">
		<form class="signup" action="console/signup.html" method="post">
			<label> 
				<span>邮箱：${signup_feedback_email}</span> 
				<input type="email" name="email" placeholder="您可以使用的邮箱" />
			</label>
			<label> 
				<span>用户名：${signup_feedback_username}</span> 
				<input type="text" name="username" placeholder="4~16 位，可以是数字、英文字母、下划线、中文" />
			</label>
			<label> 
				<span>密码：${signup_feedback_password}</span> 
				<input type="password" name="password" placeholder="6~32 位，可以是数字、英文字母、下划线"/>
				<input type="password" name="password2nd" placeholder="再次输入密码" />
			</label>
			<label>
				<span>${signup_feedback_message}</span>
			</label>
			<label>
				<input type="submit" value="注册" />
			</label>
		</form>
	</c:if>
</body>
</html>
