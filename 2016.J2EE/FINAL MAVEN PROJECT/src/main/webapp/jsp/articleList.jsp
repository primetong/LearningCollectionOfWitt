<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE HTML>
<html>
<head>
</head>

<body>
	<div class="intro">
		<hr />
		<h1>${listHelper.title}</h1>
		<h2>${listHelper.intro}</h2>
	</div>
	<c:forEach items="${listHelper.data}" var="item">
		<div class="articleAbstract">
			<div class="left">
				<a href="${'paper/'}${item.aid}${'.html'}" target="_blank"> 
					<img src="${item.preview}" />
				</a>
				<!--  -->
			</div>
			<div class="right">
				<div class="title">
					<a href="${'paper/'}${item.aid}${'.html'}" target="_blank">${item.title}</a>
				</div>
				<div class="date">
					<fmt:formatDate value="${item.creationdate}"
						pattern="yyyy MM dd HH:mm" />
				</div>
				<div class="author">
					<c:choose>
						<c:when test="${not empty sessionScope.user and sessionScope.user.uid == item.uid}">
							<div class="edit">
								<a href="${'paper/edit/'}${item.aid}${'.html'}">修改</a>
							</div>
							<div class="delete">
								<a href="${'paper/delete/'}${item.aid}${'.html'}">删除</a>
							</div>
						</c:when>
						<c:otherwise>
							<a href="${'user/'}${item.author}${'.html'}">${item.author}</a>
						</c:otherwise>
					</c:choose>
				</div>
				<div class="content">${item.content}</div>
			</div>
		</div>
	</c:forEach>
	<div class="pageList">
		<ol>
			<c:forEach items="${listHelper.pageList}" var="pageLink">
				<li> ${pageLink}</li>
			</c:forEach>
		</ol>
	</div>
</body>
</html>
