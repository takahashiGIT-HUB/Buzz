<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="model.ShopInfo"%>
<%@ page import="java.net.URLEncoder" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>店舗情報編集 -バズミシュラン</title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/shop.css">
</head>
<body>
<jsp:include page="header.jsp" />
<%
ShopInfo shopDetail = (ShopInfo)request.getAttribute("shopDetail");
// Servletから受け取ったredirectパラメータを取得
String redirect = (String)request.getAttribute("redirect");
// redirectがnullの場合は空文字列を設定
if (redirect == null) {
    redirect = "";
}
%>
<div class="shop-info-container">
	<h1>店舗情報編集</h1>
	<form action="ShopEditServlet" method="post">
	
		<div class="form-group">
		<label for="shopName">店舗名</label> <input type="text" id="shopName"
				name="shopName" placeholder="店舗名入力"
				value="<% 
				if(shopDetail != null) {
					out.print(shopDetail.shopName()); 
				} %>">
		</div>

		<div class="form-group">
			<label for="shopAddress">住所</label> <input type="text"
				name="shopAddress" placeholder="店舗の住所を入力"
				value="<%=(shopDetail != null && shopDetail.shopAddress() != null)
				? shopDetail.shopAddress() : ""%>">
		</div>

		<div class="form-group">
			<label for="shopURL">URL</label> <input type="text" id="url" name="shopURL"
			placeholder="店舗のURLを入力"
			value="<%=(shopDetail != null && shopDetail.shopURL() != null)
				? shopDetail.shopURL() : ""%>">
		</div>
		<div class="form-group">
			<label for="shopTEL">電話番号</label> <input type="text" id="tel" name="shopTEL"
				placeholder="店舗の電話番号を入力"
				value="<%=(shopDetail != null && shopDetail.shopTEL() != null)
				? shopDetail.shopTEL() : ""%>">
		</div>

		<% if (shopDetail != null) { %>
		<input type="hidden" name="originalShopName"
			value="<%=shopDetail.shopName()%>">
		<% } %>
		<input type="hidden" name="redirect" value="<%= redirect %>">
		<div class="button-group">
			<button type="submit">変更する</button>
				<%
				String cancelUrl;
					if ("shopSelect".equals(redirect)) {
						// ShopSelectServletから来たらShopSelectServletに戻る
						cancelUrl = request.getContextPath() + "/ShopSelectServlet";
					} else if (shopDetail != null && shopDetail.shopName() != null && !shopDetail.shopName().isEmpty()) {
						// それ以外で店舗名があれば、ShopInfoPageServletに戻る
						cancelUrl = request.getContextPath() + "/ShopInfoPageServlet?shopName="
								+ URLEncoder.encode(shopDetail.shopName(), "UTF-8");
					} else {
						// どちらでもない場合MainMenuServletに戻す
						cancelUrl = request.getContextPath() + "/MainMenuServlet";
					}
				%>
				<a href="<%= cancelUrl %>" class="cancel-button">キャンセル</a>
		</div>
	</form>
</div>
<jsp:include page="footer.jsp" />
<script src="<%= request.getContextPath() %>/js/script.js"></script>
</body>
</html>