/**
 * @file 	editor.js
 * @version testing
 * @time	2016.02.25
 *
 * @section DESCRIPTION
 *
 * 控制 editor 页面的初始化
 * 
 */

var editor = new Simditor({
	textarea : $('#mysimditor'),
	toolbar : [
		'title',
		'bold',
		'italic',
		'underline',
		'strikethrough',
		'fontScale',
		'color',
		'ol',
		'ul',
		'blockquote',
		'code',
		'table',
		'link',
		'image',
		'hr',
		'indent',
		'outdent',
		'alignment'],
	toolbarFloatOffset: 58,
	upload: {
		url: 'upload/images.do',
		fileKey: 'file',
		connectionCount: 1,
		leaveConfirm: '正在上传文件，您要退出吗？'
	},
	pasteImage: true
});

