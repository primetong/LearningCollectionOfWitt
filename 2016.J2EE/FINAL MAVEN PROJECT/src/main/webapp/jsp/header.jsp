<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE HTML>
<html>
<head>
</head>

<body>
	<div id="view">
		<div class="logo">
			<a href=""></a>
		</div>
		<form class="searchbox" action="explore/search.html" method="GET">
			<input type="text" name="keyword" placeholder="搜索文章" />
			<input type="submit" value="搜索" />
		</form>
		<nav class="menu">
			<ul>
				<li><a href="" title="首页"
					${headerIndex == 0 ? "class=\"active\"":""}>首页</a></li>
				<li><a href="explore.html" title="发现"
					${headerIndex == 1 ? "class=\"active\"":""}>发现</a></li>
				<c:if test="${not empty sessionScope.user}">
					<li><a href="${'user/'}${sessionScope.user.username}${'.html'}" title="我的"
						${headerIndex == 2 ? "class=\"active\"":""}>我的</a></li>
				</c:if>
			</ul>
		</nav>
		<c:choose>
			<c:when test="${not empty sessionScope.user}">
				<nav class="user">
					<a href="${'user/'}${sessionScope.user.username}${'.html'}"><img src="${sessionScope.user.avatar}" /></a>
					<ul>
						<li><a href="paper/edit.html">写新文章</a></li>
						<li><a href="${'user/'}${sessionScope.user.username}${'.html'}">我的文章</a></li>
						<li><a href="${'user/'}${sessionScope.user.username}${'/settings.html'}">个人设置</a></li>
						<li><a href="console/logout.html">退出登陆</a></li>
					</ul>
				</nav>
			</c:when>
			<c:otherwise>
			</c:otherwise>
		</c:choose>
	</div>
</body>
</html>