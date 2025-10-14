<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="model.PostInfo"%>
<%@ page import="model.ShopInfo"%>
<%@ page import="dao.BuzzDAO" %>
<%@ page import="java.net.URLEncoder" %>
<%String searchWord = (String)request.getAttribute("searchWord");%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>検索結果 -バズミシュラン</title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/post.css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/buzz.css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/shop.css">

</head>
<body>
	<jsp:include page="header.jsp" />
	<div class="container">
	<h2>
	  『<%= searchWord%>』の検索結果
	</h2>

	<%-- 投稿の検索結果を表示 --%>
	<h2>つぶやきの検索結果</h2>
	<%
		List<PostInfo> postList = (List<PostInfo>) request.getAttribute("postResults");
		String sessionUserId = (String) session.getAttribute("userId");
		
		if (postList != null && !postList.isEmpty()) {
			for (PostInfo post : postList) {
	%>

	<div class="post-card">
		<p> <strong><%=post.userName()%></strong>
		(<a href="MypageServlet?userId=<%=post.userId()%>"><%=post.userId()%></a>)
		</p>
		
		<p> 店舗名：
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
		if (post.pic() != null && post.pic().length > 0) {
		%>
		<p>
			<img src="ImageServlet?postId=<%=post.postId()%>" >
		</p>
		<%}%>
		<%
		if (sessionUserId != null && sessionUserId.equals(post.userId())) {
		%>
		<a href="PostEditServlet?postId=<%= post.postId() %>
			&redirect=SearchResultServlet
			&searchWord=<%= URLEncoder.encode(searchWord, "UTF-8") %>">編集
		</a>
         <!-- 削除リンク -->
        <a href="#" class="delete-link" data-url="PostDeleteServlet?postId=<%=post.postId()%>&redirect=SearchResultServlet
        	&searchWord=<%= URLEncoder.encode(searchWord, "UTF-8") %>">削除
        </a> |
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
			<input type="hidden" name="postId" value="<%=post.postId()%>">
			<button type="submit"
				class="buzz-button <%=hasBuzzed ? "buzzed" : ""%>">
				<%=hasBuzzed ? "バズ済み✔️" : "バズる🔥"%>
			</button>
			<span class="buzz-count"><%=buzzCount%></span>
		</form>
		
	</div>
	<%
	}
	} else {
	%>
	<p>該当する投稿は見つかりませんでした。</p>
	<%
	}
	%>
	
	
	<br>
		
	<%-- 店舗の検索結果を表示 --%>
	<div style="margin: 40px 0; border-top: 2px solid #333;"></div>
	<%
		List<ShopInfo> shopList = (List<ShopInfo>) request.getAttribute("shopResults");
	%>
	<h2>店舗の検索結果</h2>
	<% if (shopList != null && !shopList.isEmpty()) { %>
		<% for (ShopInfo shop : shopList) { %>
		<div class="shop-card"> 
			<p><strong>店舗名：</strong><a href="ShopInfoPageServlet?shopName=<%=shop.shopName()%>"><%=shop.shopName()%></a></p>
			<p><strong>住所：</strong><span><%= (shop.shopAddress() != null) ? shop.shopAddress() : "情報なし" %></span></p>
			<p><strong>URL：</strong>
				<%
					String url = shop.shopURL();
					if (url != null && !url.isEmpty()) {
						if (!url.startsWith("http://") && !url.startsWith("https://")) {
							url = "http://" + url;
						}
						out.print("<a href=\"" + url + "\" target=\"_blank\">");
						out.print(shop.shopURL()); // ここで表示されるURL
						out.print("</a>");
					} else {
						out.print("情報なし");
					}
				%>
			</p>
			<p><strong>電話番号：</strong><span><%= (shop.shopTEL() != null) ? shop.shopTEL() : "情報なし" %></span></p>
		</div>
		<% } %>
	<% } else { %>
		<p>該当する店舗は見つかりませんでした。</p>
	<% } %>
	</div>
	<jsp:include page="footer.jsp" />
	<!-- モーダル読み込み -->
	<jsp:include page="/WEB-INF/jsp/deleteModal.jsp" />
	
	<script src="<%= request.getContextPath() %>/js/delete.js"></script>
	<script src="<%= request.getContextPath() %>/js/script.js"></script>
	<script src="<%= request.getContextPath() %>/js/buzz.js"></script>
</body>
</html>