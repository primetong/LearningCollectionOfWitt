<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	pageContext.setAttribute("headerIndex", 2);
%>

<!DOCTYPE HTML>
<html>

<head>
<title>用户</title>
<base href="<%=basePath%>" />
<link rel="stylesheet" type="text/css" href="css/basic.css" />
<link rel="stylesheet" type="text/css" href="css/explore.css" />
</head>

<body>
	<div id="wrapper">
		<header>
			<%@ include file="/jsp/header.jsp"%>
		</header>
		<div id="container">
			<div id="content">
				<div id="left">
					<c:choose>
						<c:when test="${userDisplayType == 'EDIT'}">
							<!-- 编辑模式 -->
							<%@ include file="/jsp/settings.jsp"%>
						</c:when>
						<c:when test="${userDisplayType == 'READ'}">
							<!-- 浏览模式 -->
							<%@ include file="/jsp/articleList.jsp"%>
							<c:if test="${hostUser.uid == sessionScope.user.uid}">
								<%@ include file="/jsp/newArticle.jsp"%>
							</c:if>
						</c:when>
						<c:otherwise>
							<!-- 404 -->
						</c:otherwise>
					</c:choose>
				</div>
				<div id="right">
					<%@ include file="/jsp/userCard.jsp"%>
					<%@ include file="/jsp/login.jsp"%>
					<%@ include file="/jsp/signup.jsp"%>
				</div>
			</div>
		</div>

		<!-- footer -->
		<footer>
			<%@ include file="/jsp/footer.jsp"%>
		</footer>
	</div>
</body>

</html>