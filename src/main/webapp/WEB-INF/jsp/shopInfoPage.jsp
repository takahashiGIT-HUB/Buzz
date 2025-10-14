<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="model.ShopInfo"%>
<%@ page import="model.PostInfo"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>店舗情報 -バズミシュラン</title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/shop.css">
</head>
<body>
<jsp:include page="header.jsp" />
	<%
	ShopInfo shopDetail = (ShopInfo)request.getAttribute("shopDetail");
	%>
	<div class="shop-detail-container">
		<h1>店舗情報</h1>
		
		<div class="shop-content-wrapper">
			<div class="shop-image-section">
<%
    model.PostInfo post = (model.PostInfo) request.getAttribute("randomPostWithImage");
    if (post != null) {
%>
    <!-- 投稿画像表示 -->
    <img src="<%= request.getContextPath() %>/ImageServlet?postId=<%= post.postId() %>" alt="ランダム画像">
<%
    } else {
%>
    <%-- <p class="no-image-text">登録画像無し</p> --%>
    <img src="<%=request.getContextPath()%>/image/noImage.jpg" alt="画像無し" >
<%
    }
%>
			</div>
		<div class="shop-info-section">
			<div class="info-item">
			<span class="info-label">店舗名：</span><span class="info-value"><%
				if (shopDetail != null) {
					out.print(shopDetail.shopName());
				} else {
					out.print("店舗情報なし");
				}
				%></span>
			</div>
			
			<div class="info-item">
				<span class="info-label">住所：</span><span class="info-value"><%
				if (shopDetail != null && shopDetail.shopAddress() != null && !shopDetail.shopAddress().isEmpty()) {
					out.print(shopDetail.shopAddress());
				} else {
					out.print("住所情報なし");
				}
				%></span>
			</div>
			 
			<div class="info-item">
				<span class="info-label">URL：</span> <span class="info-value"><%
				if (shopDetail != null) {
					String url = shopDetail.shopURL();
					if (url != null && !url.isEmpty()) {
						if (!url.startsWith("http://") && !url.startsWith("https://")) {
							url = "http://" + url;
						}
						out.print("<a href=\"" + url + "\" target=\"_blank\">");
						out.print(shopDetail.shopURL());
						out.print("</a>");
					} else {
						out.print("URL情報なし");
					}
				} else {
					out.print("URL情報なし");
				}
				%></span>
			</div>
			
			<div class="info-item">
				<span class="info-label">電話番号：</span><span class="info-value"><%
				if (shopDetail != null && shopDetail.shopTEL() != null && !shopDetail.shopTEL().isEmpty()) {
					out.print(shopDetail.shopTEL());
			    } else {
					out.print("電話番号情報なし");
			    }
			    %></span>
			</div>
			<% if(shopDetail != null) { %>
			<% } %>
		<form action="ShopEditServlet" method="get">
			<% if(shopDetail != null) { %>
		<input type="hidden" name="shopNameForEdit" value="<%= shopDetail.shopName() %>">
			<% } %>
				<button type="submit" class="edit-button">編集</button>
			</form>
		</div>
	</div>
</div>
<jsp:include page="footer.jsp" />
<script src="<%= request.getContextPath() %>/js/script.js"></script>
</body>
</html>