<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ユーザー登録完了 - バズミシュラン</title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/top.css">
</head>
<body>
    <%-- ログイン前ヘッダー読み込み用 --%>
    <jsp:include page="headerTop.jsp" />

    <div class="login-wrapper">
        <div class="login-container">
            <h1>登録が完了しました</h1>

            <p>ユーザーID：<strong><c:out value="${sessionScope.userId}" /></strong> を登録しました。</p>
            <p>ご登録ありがとうございます。</p>
            <p>引き続きバズミシュランをお楽しみください。</p>

            <a href="MainMenuServlet" class="button-link full-width">メインメニューへ</a>
            <a href="TopServlet" class="back-link">トップへ戻る</a>
        </div>
    </div>

    <%-- ログイン前フッター読み込み用 --%>
    <jsp:include page="footerTop.jsp" />
</body>
</html>
