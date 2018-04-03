<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	pageContext.setAttribute("headerIndex", 0);
%>

<!DOCTYPE HTML>
<html>

<head>
<title>Hello World!</title>
<base href="<%=basePath%>" />
<link rel="stylesheet" type="text/css" href="css/basic.css" />
<link rel="stylesheet" type="text/css" href="css/index.css" />
</head>

<body>
	<div id="wrapper">
		<header>
			<%@ include file="/jsp/header.jsp"%>
		</header>
		<div id="container">
			<div id="content">
				<div id="left">
					<%@ include file="/jsp/articleList.jsp"%>
				</div>
				<div id="right">
					<%@ include file="/jsp/login.jsp"%>
					<%@ include file="/jsp/signup.jsp"%>
					<div class="intro">
						<hr />
						<h2>最赞</h2>
						<ul>
							<li><a href="" title=""></a></li>
							<li><a href="" title=""></a></li>
						</ul>
					</div>

					<div class="intro">
						<hr />
						<h2>最热</h2>
						<ul>
							<li><a href="" title=""></a></li>
							<li><a href="" title=""></a></li>
						</ul>
					</div>
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