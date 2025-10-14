<%@ page language="java" contentType="text/html; charset=UTF-8" 
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.PostInfo" %>
<%@ page import="model.Account" %>
<%
  Account user = (Account) request.getAttribute("user");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>メインメニュー -バズミシュラン</title>
<!-- フォントの読み込み -->
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Zen+Kurenaido&display=swap" rel="stylesheet">
<!-- CSSの読み込み -->
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/buzz.css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/post.css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/mainSlideshow.css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/hero.css">
</head>
<body>
<jsp:include page="header.jsp" />

<div class="hero-section">
    <div class="hero-background-slideshow">
        <div class="slideshow-container"> 
            <div class="slides">
                <img src="<%= request.getContextPath() %>/image/食べ物1.jpg" alt="食べ物1">
                <img src="<%= request.getContextPath() %>/image/食べ物2.jpg" alt="食べ物2">
                <img src="<%= request.getContextPath() %>/image/食べ物3.jpg" alt="食べ物3">
                <img src="<%= request.getContextPath() %>/image/食べ物4.jpg" alt="食べ物4">
                <img src="<%= request.getContextPath() %>/image/食べ物5.jpg" alt="食べ物5">
                <img src="<%= request.getContextPath() %>/image/食べ物6.jpg" alt="食べ物6">
                <img src="<%= request.getContextPath() %>/image/食べ物7.jpg" alt="食べ物7">
            </div>
            <div style="text-align:center; position: absolute; bottom: 20px; width: 100%;">
              <span class="dot" onclick="currentSlide(0)"></span>
              <span class="dot" onclick="currentSlide(1)"></span>
              <span class="dot" onclick="currentSlide(2)"></span>
              <span class="dot" onclick="currentSlide(3)"></span>
              <span class="dot" onclick="currentSlide(4)"></span>
              <span class="dot" onclick="currentSlide(5)"></span>
              <span class="dot" onclick="currentSlide(6)"></span>
            </div>
        </div>
    </div>
    <div class="hero-overlay">
        <div class="hero-header-wrapper">
            </div>
        <div class="hero-content">
            <h1 class="site-catchphrase">
                バズミシュラン
            </h1>
            <p class="site-description">
                福岡のグルメ情報 <br>
                つぶやきで繋がるSNS
            </p>
        </div>
    </div>
</div>
<div class="container">
<h2>
  <div class="welcome-message">
    ようこそ！<%= user != null ? user.name() : "ゲスト" %>  さん ✨
  </div>
</h2>

<nav class="nav-menu">
  <a href="PostServlet">つぶやく💬</a>
  <a href="MypageServlet">マイページ📚</a>
  <a href="RankingServlet">ランキング👑</a>
</nav>

<h2>新着つぶやき一覧</h2>
<%
    List<PostInfo> postList = (List<PostInfo>) request.getAttribute("newPosts");
    String sessionUserId = (String) session.getAttribute("userId");

    if (postList != null && !postList.isEmpty()) {
        for (PostInfo post : postList) {
%>
    <div class="post-card">
      <p> <strong><%=post.userName() %></strong>
        (<a href="MypageServlet?userId=<%=post.userId()%>"><%=post.userId()%></a>)
        </p>
      <p>店舗名：
      <% 
      	String shopName = post.shopName();
      	if (shopName != null && !shopName.isEmpty()) { 
      %>
      <a href="ShopInfoPageServlet?shopName=<%=post.shopName()%>"><%=post.shopName()%></a>
      <% } else { %>
        未記入
      <%} %>
      </p>

		<p style="white-space: pre-line;">
			<%=post.comment()%>
		</p>
		<%
		if (post.pic() != null) {
		%>
        <p><img src="ImageServlet?postId=<%= post.postId() %>"></p>
      <% } %>

      <%-- ログイン中のユーザー本人の投稿のみ編集・削除可能（表示される） --%>
     <% if (sessionUserId != null && sessionUserId.equals(post.userId())) {%>
      
        <a href="PostEditServlet?postId=<%= post.postId() %>&redirect=MainMenuServlet">編集</a>
        <!-- 削除リンク -->
      <a href="#" class="delete-link" data-url="PostDeleteServlet?postId=<%= post.postId() %>&redirect=MainMenuServlet">削除</a> |
      <% } %>
      <% 
  		boolean hasBuzzed = false;
  		try {
    		hasBuzzed = new dao.BuzzDAO().exists(post.postId(), sessionUserId);
  			} catch (Exception e) {
    			e.printStackTrace(); // 必要ならログ
  			}
  		int buzzCount = new dao.BuzzDAO().countBuzz(post.postId());
  		%>

<form class="buzz-form" method="post" action="BuzzServlet">
  <input type="hidden" name="postId" value="<%= post.postId() %>">
  <button type="submit" class="buzz-button <%= hasBuzzed ? "buzzed" : "" %>">
    <%= hasBuzzed ? "バズ済み✔️" : "バズる🔥" %>
  </button>
  <span class="buzz-count"><%= buzzCount %></span>
</form>
    </div>
<%
        } // end for
    } else {
%>
    <p>新着つぶやきはまだありません。</p>
<%
    } // end if
%>
</div>
<!-- モーダル読み込み -->
<jsp:include page="/WEB-INF/jsp/deleteModal.jsp" />

<jsp:include page="footer.jsp" />
<script src="<%= request.getContextPath() %>/js/delete.js"></script>
<script src="<%= request.getContextPath() %>/js/script.js"></script>
<script src="<%= request.getContextPath() %>/js/buzz.js"></script>
<script src="<%= request.getContextPath() %>/js/mainSlideshow.js"></script>

</body>
</html>