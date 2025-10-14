<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>トップページ - バズミシュラン</title>
<%-- CSS読み込み用 --%>
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/top.css">
</head>
<body class="top-page">
    <%-- ログイン前ヘッダー読み込み用 --%>
    <jsp:include page="headerTop.jsp" />
    <div class="main-content">
      <div class="slideshow">
      <img src="<%= request.getContextPath() %>/image/slide1.png" class="slide active">
      <img src="<%= request.getContextPath() %>/image/slide2.png" class="slide">
      <img src="<%= request.getContextPath() %>/image/slide3.png" class="slide">
      <img src="<%= request.getContextPath() %>/image/slide4.png" class="slide">
      </div>
      <!-- メインコンテンツ -->
      <div class="main-container">
        <h1>
  <span>ようこそ</span>
  <span class="buzz-title">バズミシュラン</span>
  <span>へ！</span>
</h1>
        <h2>みんなのおいしいがここに</h2>
        <p>今すぐあなたのおいしいを投稿しましょう</p>
        
        <a href="LoginServlet" class="button-link">ログイン</a>
        <br>
        <div class="register-inline">
          <span class="register-label">新規の方はこちら</span>
          <a href="RegisterServlet" class="button-link">アカウントを作成</a>
        </div>
      </div>
    </div>
    <%-- ログイン前フッター読み込み用 --%>
    <jsp:include page="footerTop.jsp" />
    <%-- スライドショー用 --%>
    <script src="<%= request.getContextPath() %>/js/slideshow.js"></script>
</body>
</html>
