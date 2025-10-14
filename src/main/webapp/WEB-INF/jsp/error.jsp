<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>エラー発生 -バズミシュラン</title>
</head>
<body>
<h1>エラー発生</h1>
<%
    String errorMessage = (String) request.getAttribute("errorMessage");
    String message = (String) request.getAttribute("message");
    
    // エラーメッセージがあれば表示
    if (errorMessage != null) {
        out.println("<p style=\"color: red;\">" + errorMessage + "</p>");
    }
    
    // 通常のメッセージがあれば表示
    if (message != null) {
        out.println("<p style=\"color: blue;\">" + message + "</p>");
    }
%>
	<br>
	<a href="javascript:history.back()">前のページに戻る</a>
</body>
</html>