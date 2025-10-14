<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>プロフィール編集完了 - バズミシュラン</title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/userEdit.css">
</head>
<body>
    <%-- ヘッダー読み込み --%>
    <jsp:include page="header.jsp" />

    <div class="login-wrapper">
        <div class="login-container">
            <h1>プロフィール変更が完了しました</h1>

            <div class="button-group">
                <a href="MainMenuServlet" class="button-link full-width">メインメニュー</a>
                <a href="MypageServlet" class="button-link full-width">マイページ</a>
                <a href="LogoutServlet" class="button-link full-width">ログアウト</a>
            </div>
        </div>
    </div>

    <%-- フッター読み込み --%>
    <jsp:include page="footer.jsp" />
    <script src="<%= request.getContextPath() %>/js/script.js"></script>
</body>
</html>
