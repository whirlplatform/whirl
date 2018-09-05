<%@page import="org.whirlplatform.editor.server.i18n.EditorI18NFilter"%>
<%@page import="org.whirlplatform.editor.server.i18n.EditorI18NMessage"%>
<%@page import="java.util.Locale"%>

<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	Locale l = EditorI18NMessage.getRequestLocale();
	String locale = request.getParameter("locale");
	if (locale == null) {
		//берем только Language
		locale = l.getLanguage();
	} else {
		if (locale.contains("_")) {
			locale = locale.substring(0, locale.indexOf("_"));
		}
	}
	l = new Locale(locale);
	EditorI18NMessage.setRequestLocale(l);
	EditorI18NFilter.setLocale(l);

	String title = "Application Editor";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">

<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta name="gwt:property" content="locale=<%=locale%>">
<meta http-equiv="X-UA-Compatible" content="IE=9">
<title><%=title%></title>
<link type="text/css" rel="stylesheet" href="editor.css">
<script type="text/javascript" src="cke/ckeditor.js"></script>

<script type="text/javascript" src="editor/codemirror/lib/codemirror.js"></script>
<script type="text/javascript" src="editor/codemirror/mode/xml/xml.js"></script>
<script type="text/javascript" src="editor/codemirror/mode/css/css.js"></script>
<script type="text/javascript"
	src="editor/codemirror/mode/htmlmixed/htmlmixed.js"></script>
<script type="text/javascript"
	src="editor/codemirror/mode/clike/clike.js"></script>
<script type="text/javascript"
	src="editor/codemirror/mode/javascript/javascript.js"></script>
<script type="text/javascript" src="editor/codemirror/mode/sql/sql.js"></script>
<script type="text/javascript"
	src="editor/codemirror/addon/edit/closetag.js"></script>
<script type="text/javascript"
	src="editor/codemirror/addon/hint/show-hint.js"></script>

</head>

<body>
	<noscript>
		<H3><%=EditorI18NMessage.getMessage(l).page_noJavaScript()%></H3>
	</noscript>

	<iframe src="javascript:''" id="__gwt_historyFrame" tabIndex='-1'
		style="position: absolute; width: 0; height: 0; border: 0"></iframe>


	<script type="text/javascript" language="javascript"
		src="editor/editor.nocache.js"></script>
</body>
</html>
