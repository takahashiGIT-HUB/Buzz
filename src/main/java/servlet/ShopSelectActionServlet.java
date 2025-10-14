package servlet;

import java.io.IOException;
import java.net.URLEncoder;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/ShopSelectActionServlet")
public class ShopSelectActionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String shopName = request.getParameter("shopName");
		HttpSession session = request.getSession();
		//遷移元のページを確認する
		String redirect = request.getParameter("redirect");
		//check
		System.out.println("redirect = " + redirect);
		
		session.setAttribute("selectedShopForPost", shopName);

		// 選択した店舗名をセッションに保存
		String redirectBackTo = (String) session.getAttribute("redirectBackTo");
		// セッションから取得して文字列に変換
		Object editingPostIdObj = session.getAttribute("editingPostId");
	    String editingPostId = null;
	    if (editingPostIdObj != null) {
	        editingPostId = String.valueOf(editingPostIdObj);
	    }
		if ("PostEditServlet".equals(redirectBackTo)) {
			// 編集画面から来た場合、編集画面にリダイレクト
			// リダイレクトする前にセッションをクリア
			session.removeAttribute("editingPostId");
			session.removeAttribute("redirectBackTo");
			response.sendRedirect(request.getContextPath() + "/PostEditServlet?postId=" + editingPostId + 
					(redirect != null ? "&redirect=" + URLEncoder.encode(redirect, "UTF-8") : ""));
		} else {
			// 通常の投稿画面から来た場合、投稿画面にリダイレクト
			session.removeAttribute("editingPostId"); // nullが入ってる可能性があるので念のため
			session.removeAttribute("redirectBackTo"); // nullが入ってる可能性があるので念のため
			// PostServletにリダイレクトする時にパラメータを付ける
			response.sendRedirect(request.getContextPath() + "/PostServlet?fromShopSelect=true");
		}
	}
}