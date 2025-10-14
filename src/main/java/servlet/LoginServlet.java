package servlet;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.Login;
import model.LoginLogic;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/login.jsp");
		dispatcher.forward(request, response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//リクエストパラメータの取得
		request.setCharacterEncoding("UTF-8");
		String userId = request.getParameter("userId");
		String pass = request.getParameter("pass");
		
		//ログイン処理の実行
		Login login = new Login(userId, pass);
		LoginLogic bo = new LoginLogic();
		boolean result = false;
		try {
			result = bo.execute(login);
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		
		//ログイン処理の成否によって処理を分岐
		if(result) { //ログイン成功時
			//セッションスコープにユーザーIDを保存
			HttpSession session = request.getSession();
			//session.invalidate();  // 古いセッションを破棄
	        //session = request.getSession();  // 新しいセッションを生成
			session.setAttribute("userId", userId);
			//リダイレクト
			response.sendRedirect("MainMenuServlet");
		}else { //ログイン失敗時
			// エラーメッセージをリクエストスコープに保存
			request.setAttribute("errorMsg", "ユーザーIDまたはパスワードが間違っています。");
			// login.jsp にフォワード
			RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/login.jsp");
			dispatcher.forward(request, response);
		}
	}
}