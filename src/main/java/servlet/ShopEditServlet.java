package servlet;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.ShopDAO;
import model.ShopInfo;

@WebServlet("/ShopEditServlet")
public class ShopEditServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String shopName = request.getParameter("shopNameForEdit");
		String redirect = request.getParameter("redirect");
		System.out.println("ShopEditServlet#doGet: redirect param = " + redirect);
		
		ShopInfo shopInfo = null;
		if (shopName != null && !shopName.isEmpty()) {
			ShopDAO shopDAO = new ShopDAO();
			try {
				shopInfo = shopDAO.findByShopName(shopName);
			} catch (Exception e) {
				e.printStackTrace();
				request.setAttribute("errorMessage", "編集対象の店舗情報の取得中にエラーが発生しました。");
			}
		} else {
			request.setAttribute("errorMessage", "編集する店舗名が指定されていません。");
		}
		if (shopInfo != null) {
			request.setAttribute("shopDetail", shopInfo);
		} else {
		}
		request.setAttribute("redirect", redirect);
		RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/shopEdit.jsp"); // 編集画面のJSPにフォワード
		dispatcher.forward(request, response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");

		// フォームから送信された値を取得
		String originalShopName = request.getParameter("originalShopName"); // 元の店舗名（hiddenで受け取る）
		String newShopName = request.getParameter("shopName"); // 編集された店舗名
		String newShopAddress = request.getParameter("shopAddress"); // 編集された住所
		String newShopURL = request.getParameter("shopURL"); // 編集されたURL
		String newShopTEL = request.getParameter("shopTEL"); // 編集された電話番号
		String redirect = request.getParameter("redirect"); // どこから来たかを受け取る
	    System.out.println("ShopEditServlet#doPost: redirect param = " + redirect);
		
		// 各入力値が空文字の場合にnullに変換する処理
		// trim()で前後の空白を除去してからisEmpty()でチェックする
		if (newShopAddress != null && newShopAddress.trim().isEmpty()) {
			newShopAddress = null;
		}
		if (newShopURL != null && newShopURL.trim().isEmpty()) {
			newShopURL = null;
		}
		if (newShopTEL != null && newShopTEL.trim().isEmpty()) {
			newShopTEL = null;
		}
		// 取得した値をセット
		ShopInfo shopInfo = new ShopInfo(newShopName, newShopURL, newShopAddress, newShopTEL);

		ShopDAO shopDAO = new ShopDAO();
		boolean isSuccess = false;
		String forwardPath = null;

		try { //変更前店舗名(プライマリーキー)が後ろ
			isSuccess = shopDAO.shopEdit(shopInfo, originalShopName);

			//編集テスト用
			System.out.println("ShopEditServlet: 編集結果 isSuccess = " + isSuccess);
			System.out.println("ShopEditServlet: originalShopName = " + originalShopName);
			System.out.println("ShopEditServlet: newShopName = " + newShopName);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "店舗情報の更新中にエラーが発生しました。");
		}

		if (isSuccess) {
			request.setAttribute("message", "店舗情報が正常に更新されました。");
			String encodedShopName = URLEncoder.encode(newShopName, StandardCharsets.UTF_8.toString());
			if ("shopSelect".equals(redirect)) {
				// ShopSelectServletから来た場合、ShopSelectServletに戻る
				response.sendRedirect(request.getContextPath() + "/ShopSelectServlet");
			} else {
				// それ以外の場合はShopInfoPageServletに戻る
				response.sendRedirect(request.getContextPath() + "/ShopInfoPageServlet?shopName=" + encodedShopName);
			}
			return;
		} else {
			request.setAttribute("errorMessage", "店舗情報の更新に失敗しました。");
			request.setAttribute("shopDetail", shopInfo);
			request.setAttribute("redirect", redirect); 
			forwardPath = "WEB-INF/jsp/shopEdit.jsp";
		}

		if(forwardPath != null && !response.isCommitted()){
			RequestDispatcher dispatcher = request.getRequestDispatcher(forwardPath);
			dispatcher.forward(request, response);
		}
	}
}