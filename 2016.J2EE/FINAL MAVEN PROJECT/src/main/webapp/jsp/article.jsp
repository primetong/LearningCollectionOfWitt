<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE HTML>
<html>
<head>
</head>

<body>
	<article role="main">${article.content}</article>
	<c:if test="${not empty sessionScope.user and sessionScope.user.uid == article.uid}">
		<div class="edit">
			<a href="${'paper/edit/'}${article.aid}${'.html'}" target="_blank">修改</a>
		</div>
		<div class="delete">
			<a href="${'paper/delete/'}${article.aid}${'.html'}">删除</a>
		</div>
	</c:if>
</body>
</html>