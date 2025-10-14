package servlet;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.PostDAO;
import model.PostInfo;

/**
 * Servlet implementation class PostServlet
 */
@MultipartConfig
@WebServlet("/PostServlet")
public class PostServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// セッションを取得(選択店舗取得)
		HttpSession session = request.getSession(); 
		session.removeAttribute("editingPostId");
	    session.removeAttribute("redirectBackTo");
	    System.out.println("PostServlet#doGet: cleared editingPostId and redirectBackTo session attributes.");
		
		//どこから来たかを確認する
		String fromShopSelectParam = request.getParameter("fromShopSelect");
		System.out.println("PostServlet#doGet: fromShopSelectParam = " + fromShopSelectParam);
		// 店舗選択画面から戻ってきた時以外はクリア
		if (!"true".equals(fromShopSelectParam)) {
			session.removeAttribute("selectedShopForPost");
			System.out.println("PostServlet#doGet: cleared selectedShopForPost (not from shop select).");
		}else {
	        System.out.println("PostServlet#doGet: selectedShopForPost retained (from shop select).");
	    }
		
		System.out.println("PostServlet#doGet: selectedShopForPost (after clear logic) = " + session.getAttribute("selectedShopForPost"));
		RequestDispatcher dispatcher =
				request.getRequestDispatcher("WEB-INF/jsp/post.jsp");
		dispatcher.forward(request, response);
		System.out.println("--- PostServlet#doGet 終了 ---");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		HttpSession session = request.getSession();

		// 投稿処理（フォームからの入力を処理）
		String shopName = (String) session.getAttribute("selectedShopForPost");
		String userId = (String) session.getAttribute("userId"); 

		String comment = request.getParameter("comment");
		byte[] pictureBytes = null;

		var part = request.getPart("pictures");
		if (part != null && part.getSize() > 0) {
			pictureBytes = part.getInputStream().readAllBytes();
		}

		PostInfo post = new PostInfo(0, userId, comment, pictureBytes, shopName, null, null);
		PostDAO dao = new PostDAO();
		boolean result = dao.postInsert(post);

		if (result) {
			session.removeAttribute("selectedShopForPost"); // 投稿成功後、セッションから店舗情報を削除
			response.sendRedirect("MainMenuServlet");
		} else {
			request.setAttribute("error", "投稿に失敗しました。");
			RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/post.jsp");
			dispatcher.forward(request, response);
		}
	}
}
