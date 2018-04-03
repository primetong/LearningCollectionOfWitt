<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE HTML>
<html>
<head>
</head>

<body>
	<c:if test="${not empty sessionScope.user}">
		<form class="newArticle" action="paper/edit.html" method="get">
			<input type="submit" value="写点什么" />
		</form>
	</c:if>
</body>
</html>
