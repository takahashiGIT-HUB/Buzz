<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ログアウト - バズミシュラン</title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/top.css">
</head>
<body>
    <%-- ログイン前ヘッダー --%>
    <jsp:include page="headerTop.jsp" />

    <div class="login-wrapper">
        <div class="login-container center-text">
            <h1>ログアウトしました</h1>
            <p>ご利用ありがとうございました。</p>
            <a href="TopServlet" class="button-link">トップへ戻る</a>
        </div>
    </div>

    <%-- ログイン前フッター --%>
    <jsp:include page="footerTop.jsp" />
</body>
</html>
