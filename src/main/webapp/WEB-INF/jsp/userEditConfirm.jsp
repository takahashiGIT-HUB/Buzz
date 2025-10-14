<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>プロフィール確認 - バズミシュラン</title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/userEdit.css">
</head>
<body>
    <%-- ヘッダー読み込み --%>
    <jsp:include page="header.jsp" />

    <div class="login-wrapper">
        <div class="login-container">
            <h1>プロフィール編集確認</h1>

            <table class="confirm-table">
                <tr>
                    <th>ユーザーID:</th>
                    <td><c:out value="${userId}" /></td>
                </tr>
                <tr>
                    <th>パスワード:</th>
                    <td>********</td>
                </tr>
                <tr>
                    <th>ユーザー名:</th>
                    <td><c:out value="${name}" /></td>
                </tr>
                <tr>
                    <th>プロフィール:</th>
                    <td><div class="profile-box"><c:out value="${profile}" /></div></td>
                </tr>
            </table>

            <form action="UserEditOKServlet" method="post">
                <input type="hidden" name="action" value="confirm">
                <input type="submit" class="button-link full-width" value="変更">
            </form>

            <form action="UserEditServlet" method="get">
                <input type="submit" class="button-link full-width" value="修正">
            </form>

            <a href="MypageServlet" class="back-link">マイページへ戻る</a>
        </div>
    </div>

    <%-- フッター読み込み --%>
    <jsp:include page="footer.jsp" />
    <script src="<%= request.getContextPath() %>/js/script.js"></script>
</body>
</html>
