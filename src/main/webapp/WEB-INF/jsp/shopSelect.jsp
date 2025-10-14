<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="model.ShopInfo"%>
<%String redirect = (String) request.getAttribute("redirect"); %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>店舗検索結果 -バズミシュラン</title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/shop.css">
</head>
<body>
<jsp:include page="header.jsp" />

<div class="shop-form-section shop-search-form">
	<ul>
		<form action="<%=request.getContextPath()%>/ShopSelectServlet"
			method="get">
			<!-- リダイレクト先 -->
			<input type="hidden" name="redirect" 
			value="<%= redirect %>">
			<div class="shop-form-group search-form-group">
			<label for="searchNameInput">店名<br>
			<input type="text" id="searchNameInput" name="searchName"
				value="<%=request.getAttribute("searchKeyword") != null ? request.getAttribute("searchKeyword") : ""%>"
				placeholder="選択したい店舗名を入力">
                </div>
			<button type="submit">検索</button>
		</form>
	</ul>
</div>

<div class="shop-list-table-container">
	<h2>
		<%
		String displayKeyword = (String) request.getAttribute("searchKeyword");
				if (displayKeyword != null && !displayKeyword.isEmpty()) {
			out.print("「" + displayKeyword + "」の検索結果");
				} else {
			out.print("全店舗一覧");
				}
		%>
	</h2>

<table class="shop-list-table">
	<thead>
		<tr>
			<th>店舗名</th>
			<th>URL</th>
			<th>住所</th>
			<th>電話番号</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<%
		List<ShopInfo> shopList = (List<ShopInfo>) request.getAttribute("shopList");
				if (shopList != null && !shopList.isEmpty()) {
			for (ShopInfo shop : shopList) {
		%>
		<tr>
			<td><a href="/Buzz/ShopInfoPageServlet?shopName=<%=shop.shopName()%>">
					<%=shop.shopName()%>
			</a></td>
			<td><% if (shop.shopURL() != null) { %>
				<a href="<%=shop.shopURL()%>" target="_blank">
				<%=shop.shopURL()%>
				</a>
				<% } else { %>
					登録なし
				<% } %>
			</td>
			<td><%= shop.shopAddress() != null ? shop.shopAddress() : "登録なし" %></td>
			<td><%= shop.shopTEL() != null ? shop.shopTEL() : "登録なし" %></td>
				<td>
				<form action="<%=request.getContextPath()%>
					/ShopSelectActionServlet" method="post">
					<!-- リダイレクト先 -->
					<input type="hidden" name="redirect" 
					value="<%= redirect %>">
					<input type="hidden" name="shopName"
						value="<%= shop.shopName() %>">
					<button type="submit">選択</button>
					</form>
				<form action= "<%=request.getContextPath() %>/ShopEditServlet" method="get">
					<input type="hidden" name="shopNameForEdit"
					value="<%= shop.shopName() %>">
					<input type="hidden" name="redirect" value="shopSelect">
					<button type="submit">編集</button>
				</form>
			</td>
		</tr>
		<%
		}
		} else {
		%>
		<tr>
			<td colspan="5">該当する店舗情報はありません。</td>
		</tr>
		<%
		}
		%>
		</tbody>
	</table>
</div>

<div class="shop-form-section">
	<h2>新規店舗登録</h2>
	<form action="<%=request.getContextPath()%>/ShopSelectServlet"
		method="post">
		<div class="shop-form-group new-shop-form-group">
		<label for="newShopNameInput">店舗の名前</label> <input type="text"
			id="newShopNameInput" name="newShopName" placeholder="店舗の名前を入力"
			required>
		</div>
		<div class="shop-form-group new-shop-form-group">
			<label for="newShopAddressInput">店舗の住所</label>
		<input type="text" id="newShopAddressInput" name="newShopAddress"
			placeholder="店舗の住所を入力">
		</div>
		<div class="shop-form-group new-shop-form-group">
			<label for="newShopURLInput">店舗のURL</label>
			<input type="text" id="newShopURLInput" 
			name="newShopURL" placeholder="店舗のURLを入力">
		</div>
		<div class="shop-form-group new-shop-form-group">
			<label for="newShopTelInput">店舗の電話番号</label>
		<input type="text" id="newShopTelInput" name="newShopTEL"
			placeholder="店舗の電話番号を入力">
		</div>
		<button type="submit">登録</button>
	</form>
</div>
<div class="shop-message-container">
	<% String errorMessage = (String) request.getAttribute("errorMessage"); %>
	<% if (errorMessage != null && !errorMessage.isEmpty()) { %>
	<p class="error-message"><%=errorMessage%></p>
	<% } %>
	<% String message = (String) request.getAttribute("message"); %>
	<% if (message != null && !message.isEmpty()) { %>
	<p class="success-message"><%=message%></p>
	<% } %>
</div>
	<jsp:include page="footer.jsp" />
<script src="<%= request.getContextPath() %>/js/script.js"></script>
</body>
</html>