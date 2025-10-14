<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ユーザー登録 - バズミシュラン</title>
<!-- CSSの読み込みを追加 -->
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/top.css">
</head>
<body>
    <%-- ログイン前ヘッダー読み込み用 --%>
    <jsp:include page="headerTop.jsp" />

    <div class="login-wrapper">
        <div class="login-container">
            <h1>ユーザー登録</h1>

            <c:if test="${not empty requestScope.errorMsgs}">
              <div class="error-msg" style="text-align: left;">
                <ul>
                  <c:forEach var="errorMsg" items="${requestScope.errorMsgs}">
                    <li><c:out value="${errorMsg}"/></li>
                  </c:forEach>
                </ul>
              </div>
            </c:if>

            <form action="RegisterConfirmServlet" method="post">
                <div class="form-group">
                    <label for="userId">ユーザーID</label>
                    <input type="text" name="userId" id="userId" value="${requestScope.userId}">
                    <small>※40文字以内、半角英数字と一部記号（例: ! - / : @ など）</small>
                </div>

                <div class="form-group">
                    <label for="pass">パスワード</label>
                    <input type="password" id="pass" name="pass">
                </div>

                <div class="form-group">
                    <label for="confirmPass">確認用パスワード</label>
                    <input type="password" id="confirmPass" name="confirmPass">
                    <label><input type="checkbox" id="togglePassword"> パスワードを表示</label>
                    <small>※8〜40文字、半角英数字と一部記号（例: ! - / : @ など）</small>
                    <small>英字・数字をそれぞれ1文字以上含む</small>
                </div>

                <div class="form-group">
                    <label for="name">ユーザー名</label>
                    <input type="text" name="name" id="name" value="${requestScope.name}">
                    <small>※40文字以内</small>
                </div>

                <div class="form-group">
                    <label for="profile">プロフィール</label>
                    <textarea name="profile" id="profile" class="profile-textarea">${requestScope.profile}</textarea>
                    <small>※200文字以内</small>
                </div>

                <div class="form-group">
                    <input type="submit" class="button-link full-width" value="確認">
                </div>
            </form>

            <a href="TopServlet" class="back-link">トップへ戻る</a>
        </div>
    </div>

    <%-- ログイン前フッター読み込み用 --%>
    <jsp:include page="footerTop.jsp" />
    <script src="<%= request.getContextPath() %>/js/password.js"></script>
</body>
</html>
