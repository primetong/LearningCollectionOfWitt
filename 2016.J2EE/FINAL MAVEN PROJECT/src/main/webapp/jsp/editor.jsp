<%@ page language="java" pageEncoding="UTF-8"%>

<!DOCTYPE HTML>
<html>
<head>
<link rel="stylesheet" type="text/css" href="css/simditor.css" />

<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript" src="js/module.js"></script>
<script type="text/javascript" src="js/hotkeys.js"></script>
<script type="text/javascript" src="js/uploader.js"></script>
<script type="text/javascript" src="js/simditor.js"></script>

</head>

<body>
	<form class="editor" action="paper/save.html" method="post">
		<input type="hidden" name="aid" value="${article.aid}"/>
		<input type="hidden" name="uid" value="${article.uid}"/>
		<input type="text" id="title" name="title" placeholder="标题" value="${article.title}"/>
		<textarea name="content" id="mysimditor" placeholder="文字" autofocus>${article.content}</textarea>
		<script type="text/javascript" src="js/editor.js"></script>
		<ul>
			<li>
				<label>
					<span>仅自己可见</span>
					<input type="checkbox" name="isprivate" value="true" ${article.isprivate==true?"checked":""}/>
				</label>
			</li>
		</ul>
		<div id="message">${edit_feedback_message}</div>
		<input type="submit" id="submit" value="提交" />
	</form>
</body>
</html>
