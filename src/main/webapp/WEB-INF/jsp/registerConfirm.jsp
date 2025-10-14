<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ユーザー登録 - バズミシュラン</title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/top.css">
</head>
<body>
    <%-- ログイン前ヘッダー読み込み用 --%>
    <jsp:include page="headerTop.jsp" />

    <div class="login-wrapper">
        <div class="login-container">
            <h1>入力内容の確認</h1>

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

            <form action="RegisterOKServlet" method="post">
                <input type="hidden" name="action" value="confirm">
                <input type="submit" class="button-link full-width" value="登録">
            </form>

            <form action="RegisterServlet" method="post" style="margin-top: 10px;">
                <input type="hidden" name="action" value="edit">
                <input type="submit" class="button-link full-width" value="修正">
            </form>

            <a href="TopServlet" class="back-link">トップへ戻る</a>
        </div>
    </div>

    <%-- ログイン前フッター読み込み用 --%>
    <jsp:include page="footerTop.jsp" />
</body>
</html>
