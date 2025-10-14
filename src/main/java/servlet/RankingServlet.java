package servlet;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.BuzzDAO;
import dao.PostDAO;
import dao.ShopDAO;
import model.PostInfo;
import model.ShopInfo;

@WebServlet("/RankingServlet")
public class RankingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			PostDAO postDao = new PostDAO();
			BuzzDAO buzzDao = new BuzzDAO();
			ShopDAO shopDao = new ShopDAO();
			
			//店舗名の上位3件を取得
			List<String> top3Shops = postDao.shopRanking();

			//各店舗に対して、バズ数上位3位の投稿を取得
			Map<String, List<PostInfo>> rankingMap = new LinkedHashMap<>();
			// 追加：店舗情報を格納するマップ
            Map<String, ShopInfo> shopInfoMap = new LinkedHashMap<>();

			for (String shop : top3Shops) {
				List<PostInfo> buzzPosts = buzzDao.rankingComment(shop);
				rankingMap.put(shop, buzzPosts);

				// ShopDAOを使って店舗詳細情報を取得し、マップに格納
				ShopInfo shopInfo = shopDao.findByShopName(shop);
				if (shopInfo != null) {
					shopInfoMap.put(shop, shopInfo);
				}

				}
			// リクエストスコープにセット
			request.setAttribute("top3Shops", top3Shops);
			request.setAttribute("rankingMap", rankingMap);
			request.setAttribute("shopInfoMap", shopInfoMap);

			// 表示用JSPへフォワード
			request.getRequestDispatcher("WEB-INF/jsp/ranking.jsp").forward(request, response);
		}catch(Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "ランキングの取得に失敗しました");

		}

	}

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}