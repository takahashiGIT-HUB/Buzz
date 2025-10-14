package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import dao.PostDAO;
import model.PostInfo;


@WebServlet("/PostEditServlet")
@MultipartConfig
public class PostEditServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String loginUserId = (String) session.getAttribute("userId");
		// 検索ワードを変数に代入
		String searchWord = request.getParameter("searchWord");	
		// 検索ワードをSearchResultServletに渡すためにリクエストスコープに設定
		request.setAttribute("searchWord", searchWord);
		//check
		System.out.println("postEditServlet searchWord:"+ searchWord);
		
		//遷移元のページを確認する
		String redirect = request.getParameter("redirect");
		//check
		System.out.println("postEditServlet ridirect:"+ redirect);
		
		 //遷移元の判定
	    if(!redirect.equals("MypageServlet")) {
			if(!redirect.equals("MainMenuServlet")) {
				redirect = "SearchResultServlet";
			}else {
				redirect = "MainMenuServlet";
			}
		}else {
			redirect = "MypageServlet";
		}
		// 投稿IDを受け取る
		String postIdStr = request.getParameter("postId");
		//check
		System.out.println("postsId = " + postIdStr);
		if (postIdStr == null || postIdStr.isEmpty()) {
			response.sendRedirect(redirect); // 不正アクセス防止
			return;
		}

		int postId = Integer.parseInt(postIdStr);

		// DAOで投稿を取得
		PostDAO dao = new PostDAO();
		PostInfo post = dao.findById(postId);
		if (post == null) {
		    // 存在しない投稿ID → エラー対応
		    response.sendRedirect(redirect);
		    return;
		}

		
		// 自分の投稿でない場合はリダイレクト
		if (!loginUserId.equals(post.userId()) &&loginUserId != null ) {
			response.sendRedirect(redirect);
			return;
		}
		System.out.println("post内容 " + post);
		// 投稿情報をリクエストスコープに保存
		request.setAttribute("redirect", redirect);
		request.setAttribute("post", post);
		request.setAttribute("hasPicture", post.pic() != null);
		
		// 投稿するIDをセッションに保存
		session.setAttribute("editingPostId", postId);
		
		// 編集画面に戻るためのredirectBackTo(目印)をセッションに保存
		session.setAttribute("redirectBackTo", "PostEditServlet");
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/postEdit.jsp");
		dispatcher.forward(request, response);
	}
    

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		HttpSession session = request.getSession();
		// 検索ワードを変数に代入
		String searchWord = request.getParameter("searchWord");	
		// 検索ワードをSearchResultServletに渡すためにリクエストスコープに設定
		request.setAttribute("searchWord", searchWord);
		//check
		System.out.println("postEditServlet doPost:"+ searchWord);
		
	    // 投稿ID（hiddenから送られてくる）
		Part postIdPart = request.getPart("postId");
		String postIdStr = readFormField(postIdPart);
	    //check
	    System.out.println("doPostのpostId: " + postIdStr);
	    if (postIdStr == null || postIdStr.isEmpty()) {
	        // 適切なエラーハンドリング（リダイレクトやエラーメッセージ表示など）
	    	request.setAttribute("errorMessage", "つぶやきIDが指定されていません");
		    request.getRequestDispatcher("/WEB-INF/jsp/postEdit.jsp").forward(request, response);
		    return;
	    }
	    int postId = Integer.parseInt(postIdStr);
	   
	    // コメントと店舗名
	    Part commentPart = request.getPart("comment");
		String comment = readFormField(commentPart);
		Part shopPart = request.getPart("shop");
		String shopName = readFormField(shopPart);

	    // 画像削除のチェック
		Part deletePicPart = request.getPart("deletePicture");
		String deletePicture = readFormField(deletePicPart);
	    boolean shouldDeletePic = "true".equals(deletePicture);

	    // 画像ファイルの取得
	    Part picturePart = request.getPart("pictures");
	    byte[] pictureData = null;

	    // 画像を削除しない && 新しく画像がアップロードされた場合だけ読み込む
	    if (!shouldDeletePic && picturePart != null && picturePart.getSize() > 0) {
	        try (InputStream is = picturePart.getInputStream()) {
	            pictureData = is.readAllBytes();
	        }
	    }

	    // 削除が選択されたら画像データをnullにして上書き（PostDAOの処理に合わせる）
	    if (shouldDeletePic) {
	        pictureData = null;
	    }

	    // セッションから user_id を取得（ログイン済み前提）
	    String userId = (String) session.getAttribute("userId");

	    // PostInfo オブジェクトに詰める
	    PostInfo postInfo = new PostInfo(postId, userId, comment, pictureData, shopName, null, null); // postTime は不要

	    // DAOで更新
	    PostDAO dao = new PostDAO();
	    boolean result = dao.postEdit(postInfo, postId,shouldDeletePic);
	    //check
	    System.out.println("postInfo:" + postInfo );
	    
	    String redirect = request.getParameter("redirect");
		
	    //遷移元の判定
	    if(!redirect.equals("MypageServlet")) {
			if(!redirect.equals("MainMenuServlet")) {
				redirect = "SearchResultServlet";
			}else {
				redirect = "MainMenuServlet";
			}
		}else {
			redirect = "MypageServlet";
		}
	    if (result) {
	        // 成功 → 遷移元にリダイレクト
	        response.sendRedirect(redirect + "?searchWord=" + URLEncoder.encode(searchWord, "UTF-8"));
	    } else {
	    	// 再度DBから投稿情報を取得（失敗しても少なくともnullでなくなる）
	        PostInfo originalPost = dao.findById(postId);
	        if (originalPost  == null) {
	            // 存在しない投稿ID → エラー対応
	            response.sendRedirect(redirect + "?searchWord=" + URLEncoder.encode(searchWord, "UTF-8"));
	            return;
	        }
	        request.setAttribute("post", originalPost);
			request.setAttribute("errorMessage", "更新に失敗しました");
			request.getRequestDispatcher("/WEB-INF/jsp/postEdit.jsp").forward(request, response);
	    }
	}
	// ★ Partから文字列を読み取る共通処理
	private String readFormField(Part part) throws IOException {
		if (part == null || part.getSize() == 0) {
			return null;
		}

		// BufferedReaderを作る
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(part.getInputStream(), "UTF-8"))) {

			// StringBuilderの変数を用意
			StringBuilder content = new StringBuilder();
			String line;

			// 一行ずつ読み込み、contentに追加していく
			while ((line = reader.readLine()) != null) {
				content.append(line);
				// Builderしたものは改行コードが消えるので代入する
				content.append("\r\n");
			}

			// StringBuilderをStringに変換して返す
			// 最後の改行が不要な場合は、長さを調整
			if (content.length() > 0) {
				content.setLength(content.length() - 2); // 最後の改行(\r\n)を削除
			}
			return content.toString();
		}
	}
}