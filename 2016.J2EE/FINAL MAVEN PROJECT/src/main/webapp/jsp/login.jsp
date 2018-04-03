<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE HTML>
<html>
<head>
</head>

<body>
	<c:if test="${empty sessionScope.user}">
		<form class="login" action="console/login.html" method="post">
			<label> 
				<span>用户名：${login_feedback_username}</span> 
				<input type="text" name="username" placeholder="由数字、字母、下划线组成" />
			</label>
			<label>
				<span>密码：${login_feedback_password}</span> 
				<input type="password" name="password" />
			</label>
			<label>
				<span>${login_feedback_message}</span>
			</label>
			<label>
				<input type="submit" value="登陆" />
			</label>
		</form>
	</c:if>
</body>
</html>
