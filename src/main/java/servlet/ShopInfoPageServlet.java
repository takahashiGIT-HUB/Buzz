package servlet;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.PostDAO;
import dao.ShopDAO;
import model.PostInfo;
import model.ShopInfo;

@WebServlet("/ShopInfoPageServlet")
public class ShopInfoPageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		// リクエストパラメータでリンク元の店舗名を取得する。
		String shopName = request.getParameter("shopName");
		// 取得した店舗名を保管しておく変数
		ShopInfo shopInfo = null;
		ShopDAO shopDAO = new ShopDAO();
		PostDAO postDAO = new PostDAO();
		
		// 取得した店舗名から情報をDAOより取得する
		try {
			if (shopName != null && !shopName.isEmpty()) { // shopNameが空でなければDB検索
				shopInfo = shopDAO.findByShopName(shopName); // shopNameを元に情報をDAOより取得する
			} else {
				// shopNameがnullまたは空の場合の処理（エラーメッセージなど）
				request.setAttribute("errorMessage", "店舗名が指定されていません。");
			}
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "店舗情報の取得中にエラーが発生しました。");
		}

		if (shopInfo != null) {
			// 取得した店舗情報をJSPに渡す
			request.setAttribute("shopDetail", shopInfo);
		} else {
			// shopInfoがnullの場合（DBで見つからなかった場合など）
			request.setAttribute("errorMessage", "指定された店舗は見つかりませんでした。");
		}
		RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/shopInfoPage.jsp");
		
		//画像をランダム取得
		try {
	        if (shopName != null && !shopName.isEmpty()) {
	            // 店舗情報取得
	            shopInfo = shopDAO.findByShopName(shopName);

	            // 画像付き投稿取得
	            List<PostInfo> postList = postDAO.findPostsByShopWithImage(shopName);

	            PostInfo randomPost = null;
	            if (postList != null && !postList.isEmpty()) {
	                int randomIndex = new Random().nextInt(postList.size());
	                randomPost = postList.get(randomIndex);
	            }

	            request.setAttribute("shopDetail", shopInfo);
	            request.setAttribute("randomPostWithImage", randomPost);
	        } else {
	            request.setAttribute("errorMessage", "指定された店舗は見つかりませんでした。");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        request.setAttribute("errorMessage", "店舗情報の取得中にエラーが発生しました。");
	    }
		
		dispatcher.forward(request, response);
	}	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}
	
	// リクエストパラメータの取得
	/* 
	String shopName = request.getParameter("shopName");
	String addressPref = request,getParameter("addressPref");
	String addressDetail = request,getParameter("addressDetail");
	String url = request,getParameter("url");
	String tel = request,getParameter("tel");
	*/
}