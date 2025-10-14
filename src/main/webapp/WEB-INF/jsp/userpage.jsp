<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.PostInfo" %>
<%@ page import="model.Account" %>
<% 
  Account user = (Account) request.getAttribute("user");
  List<PostInfo> postList = (List<PostInfo>) request.getAttribute("postList");
  String sessionUserId = (String) session.getAttribute("userId");
  String errorMessage = (String) request.getAttribute("errorMessage");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ユーザー情報 -バズミシュラン</title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/post.css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/buzz.css">
</head>
<body>
<jsp:include page="header.jsp" />
<div class="container">
<h2>ユーザー情報</h2>
<div class="profile-card">
  <table class="profile-table">
    <tr>
      <th>ユーザーID：</th>
      <td><%= user.userId() %></td>
    </tr>
    <tr>
      <th>ユーザー名：</th>
      <td><%= user.name() %></td>
    </tr>
    <tr>
      <th>プロフィール：</th>
      <td><div class="profile-multiline"><%= user.profile() %></div></td>
    </tr>
  </table>
  </div>


	<h2>投稿一覧</h2>
<%--投稿削除が失敗した場合のエラーメッセージ --%>
<% if (errorMessage != null) { %>
  <p style="color:red;"><%= errorMessage %></p>
<% } %>
<%
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
<% if (post.pic() != null) { %>
    <p><img src="ImageServlet?postId=<%= post.postId() %>" ></p>
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
	}
  } else {
%>
    <p>まだ投稿がありません。</p>
<%}%>

</div>
</div>

	<jsp:include page="footer.jsp" />
<script src="<%= request.getContextPath() %>/js/script.js"></script>
<script src="<%= request.getContextPath() %>/js/buzz.js"></script>
</body>
</html>