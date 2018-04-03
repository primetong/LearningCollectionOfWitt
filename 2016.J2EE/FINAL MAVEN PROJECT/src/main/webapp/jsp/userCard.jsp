<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 

<!DOCTYPE HTML>
<html>
<head></head>

<body>
	<c:if test="${not empty hostUser}">
		<div class="usercard">
			<a href="${'user/'}${hostUser.username}${'.html'}"><img src="${hostUser.avatar}" /></a>
			<h1>${hostUser.username}</h1>
			<span>注册日期：<fmt:formatDate value="${hostUser.registrationdate}" pattern="yyyy-MM-dd" /></span>
			<c:if test="${not empty sessionScope.user and hostUser.uid == sessionScope.user.uid and userDisplayType == 'READ'}">
				<p><a href="${'user/'}${hostUser.username}${'/settings.html'}">修改资料</a></p>
			</c:if>
		</div>
	</c:if>
</body>
</html>
