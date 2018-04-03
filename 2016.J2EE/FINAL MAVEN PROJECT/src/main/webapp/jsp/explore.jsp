<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	pageContext.setAttribute("headerIndex", 1);
%>

<!DOCTYPE HTML>
<html>

<head>
<title>发现</title>
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
					<!-- 搜索结果 -->
					<%@ include file="/jsp/articleList.jsp"%>
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