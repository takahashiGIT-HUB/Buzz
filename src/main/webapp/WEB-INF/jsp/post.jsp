<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String selectedShopFromSession = (String) session.getAttribute("selectedShopForPost");
String shopNameValue = (selectedShopFromSession != null && !selectedShopFromSession.isEmpty()) ? selectedShopFromSession
		: "";

%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>投稿ページ -バズミシュラン</title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/post.css">
</head>
<body>
<jsp:include page="header.jsp" />
<div class="container">
<!-- ユーザーID -->
  ${user_id}<br>
	<%
	if (shopNameValue != null && !shopNameValue.isEmpty()) {
	%>
	<p>選択店舗:<strong>
		 <%=shopNameValue%></strong><a
			href="<%=request.getContextPath()%>/PostServlet?clearShop=true">店舗名を変更する</a>
	</p>
	<p></p>
	<%
	} else {
	%>
	<form class="form-group" action="<%=request.getContextPath()%>/ShopSelectServlet"
		method="get">
		店名<br> <input type="text" name="searchName" value="">
		<button type="submit" class="btn-pop">検索</button>
	</form>
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
<form class="form-group" action="<%=request.getContextPath()%>/PostServlet" method="post" enctype="multipart/form-data">
<input type="hidden"
			name="selectedShopForPost" value="<%=shopNameValue%>">
コメント<br>
<textarea name="comment" rows="4" cols="40"></textarea><br>
<label for="pictures" class="custom-file-btn" >画像を添付する</label>
<input id="pictures" type="file" name="pictures" style="display: none;">
<!-- ファイル名の表示領域を追加 -->
<span id="file-name" style="margin-left: 10px;"></span><br>
<button type="submit" class="btn-pop">つぶやく</button>
</form>
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