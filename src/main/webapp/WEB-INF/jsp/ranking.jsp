<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*, model.PostInfo, model.ShopInfo" %>
<% 
Map<String, List<PostInfo>> rankingMap = (Map<String, List<PostInfo>>) request.getAttribute("rankingMap");
Map<String, ShopInfo> shopInfoMap = (Map<String, ShopInfo>) request.getAttribute("shopInfoMap");

String[] ranks = { "1位", "2位", "3位","4位", "5位" };
int index = 0;
%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="header.jsp" />
<meta charset="UTF-8">
<title>バズ飯ランキング -バズミシュラン</title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/post.css">
</head>
<body>
<div class="container"> <%-- 全体を囲むコンテナを追加 --%>
    <h2>👑バズミシュランのバズ飯ランキング👑</h2>

    <%
    	if (rankingMap != null && !rankingMap.isEmpty()) {
    		for (Map.Entry<String, List<PostInfo>> entry : rankingMap.entrySet()) {
    			String shopName = entry.getKey();
    			List<PostInfo> comments = entry.getValue();
    			String rankDisplay = (index < ranks.length) ? ranks[index] : (index + 1) + "位";
    			// 色はCSSクラスで指定する
    			String rankClass = "";
    			if (index == 0) {
    				rankClass = "rank-gold";
    			} else if (index == 1) {
    				rankClass = "rank-silver";
    			} else if (index == 2) {
    				rankClass = "rank-bronze";
    			} else {
    				rankClass = "rank-normal"; // 4位以下のスタイル
    			}
    			
    			String crownFileName;
    		    if (index < 3) {
    		        crownFileName = (index + 1) + "crown.png"; // 1crown.png, 2crown.png, 3crown.png
    		    } else {
    		        crownFileName = "normalcrown.png"; // normalcrown.png
    		    }
    %>
    <div class="ranking-item <%= rankClass %>"> <%-- 各ランキング項目をdivで囲む --%>
        <div class="rank-info">
            <div class="rank-with-crown">
			<img src="${pageContext.request.contextPath}/image/<%= crownFileName %>" alt="${rankDisplay}の王冠" class="crown-image ${(index < 3) ? 'crown-top3' : ''}">
			<span class="rank-number"><%= rankDisplay.replace("位", "") %></span>
			</div>
            <a href="ShopInfoPageServlet?shopName=<%=shopName%>" class="shop-name"><%=shopName%></a>
            <%
            ShopInfo shopDetails = shopInfoMap != null ? shopInfoMap.get(shopName) : null;
            if (shopDetails != null) {
            %>
            <p class="shop-address"><%=(shopDetails.shopAddress() != null) ? shopDetails.shopAddress() : "情報なし"%></p>
            <p class="shop-tel"><%=(shopDetails.shopTEL() != null) ? shopDetails.shopTEL() : "情報なし"%></p>
            <%
            } else {
            %>
            <p class="shop-no-info">店舗情報なし</p> 
            <% } %>
        </div>
        <div class="buzz-comments"> 
            <% for (PostInfo post : comments) { %>
                <p class="comment-text"><%=post.comment()%></p>
            <% } %>
        </div>
    </div> <%-- .ranking-item 終わり --%>
    <%
    		index++; 
    	}
    } else {
    %>
    <p class="no-ranking-data">現在、ランキングデータはありません。</p>
    <%
    }
    %>
</div>
<jsp:include page="footer.jsp" />
<script src="<%= request.getContextPath() %>/js/script.js"></script>
</body>
</html>