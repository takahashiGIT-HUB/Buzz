<%@ page language="java" contentType="text/html; charset=UTF-8" 
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>プロフィール編集 - バズミシュラン</title>
<!-- CSSの読み込みを追加 -->
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/userEdit.css">
</head>
<body>
  <%-- ヘッダー読み込み用 --%>
  <jsp:include page="header.jsp" />

  <div class="login-wrapper">
    <div class="login-container">
      <h1>プロフィール編集</h1>

      <c:if test="${not empty requestScope.errorMsgs}">
        <div class="error-msg" style="text-align: left;">
          <ul>
            <c:forEach var="msg" items="${requestScope.errorMsgs}">
              <li><c:out value="${msg}" /></li>
            </c:forEach>
          </ul>
        </div>
      </c:if>

      <form action="UserEditConfirmServlet" method="post">
        <div class="form-group">
          <label for="userId">ユーザーID</label>
          <input type="text" name="userId" id="userId" value="${userId}" readonly>
        </div>

        <div class="form-group">
          <label for="pass">パスワード（変更する場合のみ入力）</label>
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
          <input type="text" name="name" id="name" value="${name}">
          <small>※40文字以内</small>
        </div>

        <div class="form-group">
          <label for="profile">プロフィール</label>
          <textarea name="profile" id="profile" class="profile-textarea" rows="4" cols="40">${profile}</textarea>
          <small>※200文字以内</small>
        </div>

        <input type="submit" class="button-link full-width" value="確認">
      </form>

      <a href="MypageServlet" class="back-link">戻る</a>
    </div>
  </div>

  <%-- フッター読み込み用 --%>
  <jsp:include page="footer.jsp" />
  <script src="<%= request.getContextPath() %>/js/password.js"></script>
  <script src="<%= request.getContextPath() %>/js/script.js"></script>
</body>
</html>
