<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="model.PostInfo"%>
<%
String errorMessage = (String) request.getAttribute("errorMessage");
PostInfo post = (PostInfo) request.getAttribute("post");
Boolean hasPicture = (Boolean) request.getAttribute("hasPicture");
String redirect = (String) request.getAttribute("redirect");
//セッションから新しい店舗名を取得する
//ShopSelectActionServletがこのセッションに値を設定している
String selectedShopFromSession = (String) session.getAttribute("selectedShopForPost");
String shopNameValue = (selectedShopFromSession != null && !selectedShopFromSession.isEmpty()) ? selectedShopFromSession
		: "";
if (selectedShopFromSession != null) {
	session.removeAttribute("selectedShopForPost");
}

%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>つぶやき編集 -バズミシュラン</title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/post.css">
</head>
<body>
	<jsp:include page="header.jsp" />
	<div class="container">
	<h2>つぶやき編集</h2>

	<%--投稿編集が失敗した場合のエラーメッセージ --%>
	<% if (errorMessage != null) { %>
	<p style="color: red;"><%= errorMessage %></p>
	<% } %>

	<form class="form-group" action="<%= request.getContextPath() %>/PostEditServlet"
		method="post" enctype="multipart/form-data" >
		<!-- リダイレクト先 -->
		<input type="hidden" name="redirect" value="<%= redirect %>">
		<!-- 検索キーワード -->
		<input type="hidden" name="searchWord" value="<%= request.getParameter("searchWord") %>">
		<!-- つぶやきID -->
		<input type="hidden" name="postId" value="<%= post.postId() %>"><br>

		<!-- 店舗名 -->
<%
	//表示する店舗名を決定するための変数
	String shopToDisplay = null;
		if (selectedShopFromSession != null && !selectedShopFromSession.isEmpty()) {
			// セッションに新しい店舗名があれば、それを表示する店舗名とする
			shopToDisplay = selectedShopFromSession;
			session.removeAttribute("selectedShopForPost"); // 使ったので消す
			// セッションになければ、元の投稿の店舗名を使う
		} else if (post.shopName() != null && !post.shopName().isEmpty()) {
	shopToDisplay = post.shopName();
}
%>
		<%
		     // どちらにも当てはまらない場合
		if (shopToDisplay != null && !shopToDisplay.isEmpty()) {
		%>
		<p>
			選択店舗: <strong><%=shopToDisplay%></strong> <a
				href="<%=request.getContextPath()%>
				/ShopSelectServlet?clearShop=true&postId=
				<%=post.postId()%>&redirect=<%=redirect %>">店舗名を変更する</a>
		</p>
		<input type="hidden" name="shop" value="<%=shopToDisplay%>">
		<%
		} else {
		%>
		<p>
			店名<br> <input type="text" name="shop" value="">
			<button type="submit"
				formaction="<%=request.getContextPath()%>/ShopSelectServlet">検索</button>
			<input type="hidden" name="postId" value="<%=post.postId()%>">
			<!-- リダイレクト先 -->
			<input type="hidden" name="redirect" value="<%= redirect %>">
		</p>
		<%
		}
		%>
		<%-- 
<p><select name="category">
	<option value="ramen">ラーメン</option>
	<option value="italy">イタリアン</option>
	<option value="yakiniku">焼肉</option>
</select></p>
--%>
		コメント<br>
		<textarea name="comment" rows="4" cols="40"><%= post.comment() %></textarea><br> 
		<label for="pictures" class="custom-file-btn" >画像を添付する</label>
		<input id="pictures" type="file" name="pictures" style="display: none;">
		<!-- ファイル名の表示領域を追加 -->
		<span id="file-name" style="margin-left: 10px;"></span><br>

		<% if (hasPicture != null && hasPicture) { %>
		<input type="checkbox" name="deletePicture" value="true">
		画像を削除する<br>
		<% } %>
		
		
		<button type="submit">更新</button>
	</form>
	<a href=<%= redirect %>>キャンセル</a>
	</div>
	<jsp:include page="footer.jsp" />
	
<script>
  const fileInput = document.getElementById('pictures');
  const fileNameSpan = document.getElementById('file-name');

  fileInput.addEventListener('change', function() {
    if (fileInput.files.length > 0) {
      fileNameSpan.textContent = "選択されたファイル: " + fileInput.files[0].name;
    } else {
      fileNameSpan.textContent = "";
    }
  });
</script>
<script src="<%= request.getContextPath() %>/js/script.js"></script>
</body>
</html>