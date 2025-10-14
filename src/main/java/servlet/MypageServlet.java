package servlet;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.AccountsDAO;
import dao.PostDAO;
import model.Account;
import model.PostInfo;


@WebServlet("/MypageServlet")
public class MypageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// パラメータから、選択したユーザーのIDを取得
		String requestedUserId = request.getParameter("userId");
		// セッションからログイン中のユーザーIDを取得
		HttpSession session = request.getSession();
		String loggedInUserId = (String) session.getAttribute("userId");
		session.setAttribute("userId", loggedInUserId);
		
		//check
        System.out.println("loggedInUserId in MypageServlet = " + loggedInUserId);
        System.out.println("requestedUserId in MypageServlet = " + requestedUserId);
        
		// もしログインしていなければログイン画面へ強制送還
		if (loggedInUserId == null || loggedInUserId.isEmpty()) {
			response.sendRedirect("LoginServlet");
			return;
		}

		// 選択したユーザーIDがパラメータに指定されてない場合
		// 自分のマイページに遷移
		// 選択したIDがnull empty もしくは、ログインIDと等しい場合
		if (requestedUserId == null || requestedUserId.isEmpty() || requestedUserId.equals(loggedInUserId)) {
			// DAOで自分のユーザー情報を取得
			AccountsDAO userDao = new AccountsDAO();
			Account user = null;
			try {
				user = userDao.findByUserId(loggedInUserId);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// DAOで自分の投稿一覧を取得
			PostDAO postDao = new PostDAO();
			List<PostInfo> postList = postDao.findPostsByUserId(loggedInUserId);

			// リクエスト属性にセット
			request.setAttribute("user", user);
			request.setAttribute("postList", postList);

			request.getRequestDispatcher("WEB-INF/jsp/mypage.jsp").forward(request, response);
		} else {
			// ログインしているけど、他のユーザーのページを見ている場合
			// userpage.jspへフォワード

			// DAOで他ユーザーの情報を取得
			AccountsDAO userDao = new AccountsDAO();
			Account user = null;
			try {
				user = userDao.findByUserId(requestedUserId);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// DAOで他ユーザーの投稿一覧を取得
			PostDAO postDao = new PostDAO();
			List<PostInfo> postList = postDao.findPostsByUserId(requestedUserId);

			// リクエスト属性にセット
			request.setAttribute("user", user);
			request.setAttribute("postList", postList);
			
			// check
			System.out.println("userId = " + requestedUserId);
			System.out.println("postList size = " + (postList != null ? postList.size() : "null"));
			
			// ログイン済み、自分以外のユーザーIDなのでuserpageに遷移
			request.getRequestDispatcher("WEB-INF/jsp/userpage.jsp").forward(request, response);
		}
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

}
