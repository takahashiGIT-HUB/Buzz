package servlet;

import java.io.IOException;
import java.util.List;

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

@WebServlet("/SearchResultServlet")
public class SearchResultServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// 検索ワードを変数に代入
		String searchWord = request.getParameter("searchWord");
		
		// 検索ワードをJSPに渡すためにリクエストスコープに設定
		request.setAttribute("searchWord", searchWord);
		
		// PostDAOとShopDAOのインスタンス生成
		PostDAO postDAO = new PostDAO();
		ShopDAO shopDAO = new ShopDAO();
		
		// Listの置き場を作っておく
		List<PostInfo> postResults = null;
		List<ShopInfo> shopResults = null;

		// 検索ワードがある場合はそれで検索
		if (searchWord != null && !searchWord.isEmpty()) {
			postResults = postDAO.findByKeyword(searchWord);
			try { 
				shopResults = shopDAO.searchShopsByName(java.util.Arrays.asList(searchWord));
			} catch (Exception e) {
				e.printStackTrace();
			}
			 // 検索ワードがnullの場合は全件検索
		} else { 
			postResults = postDAO.postFindAll();
			try {
				shopResults = shopDAO.findAll();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// 検索結果をリクエストスコープに設定
		request.setAttribute("postResults", postResults);
		request.setAttribute("shopResults", shopResults);

		RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/searchResult.jsp");
		dispatcher.forward(request, response);
	}
}