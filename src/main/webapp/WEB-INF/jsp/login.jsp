<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ログイン - バズミシュラン</title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/top.css">
</head>
<body>
    <%-- ログイン前ヘッダー読み込み用 --%>
    <jsp:include page="headerTop.jsp" />

    <div class="login-wrapper">
        <div class="login-container">
            <h1>ログイン</h1>

            <c:if test="${not empty errorMsg}">
              <div class="error-msg" style="text-align: left;">
                <ul>
                  <li>${errorMsg}</li>
                </ul>
              </div>
            </c:if>

            <form action="LoginServlet" method="post">
                <div class="form-group">
                    <label for="userId">ユーザーID</label>
                    <input type="text" name="userId" id="userId">
                </div>

                <div class="form-group">
                    <label for="pass">パスワード</label>
                    <input type="password" name="pass" id="pass">
                </div>

                <div class="form-group">
                    <input type="submit" class="button-link full-width" value="ログイン">
                </div>
            </form>

            <a href="TopServlet" class="back-link">トップへ戻る</a>
        </div>
    </div>

    <%-- ログイン前フッター読み込み用 --%>
    <jsp:include page="footerTop.jsp" />
</body>
</html>
