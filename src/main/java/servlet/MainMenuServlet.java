package servlet;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
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

@WebServlet("/MainMenuServlet")
public class MainMenuServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// セッションからログイン中のユーザーIDを取得
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userId");

        if (userId == null || userId.isEmpty()) {
            response.sendRedirect("LoginServlet"); // 未ログインならログインページへ
            return;
        }

        // DAOでユーザー情報を取得
        AccountsDAO userDao = new AccountsDAO();
        Account user = null;
		try {
			user = userDao.findByUserId(userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// PostDAOのインスタンス生成
		PostDAO postDAO = new PostDAO();
			
		// Listの置き場を作っておく
		List<PostInfo> newPosts = null;
			
		newPosts = postDAO.postFindNew();
		
		// 新着ポストをリクエストスコープに設定
		request.setAttribute("user", user);
		request.setAttribute("newPosts", newPosts);
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/mainMenu.jsp");
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}

}
