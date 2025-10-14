package servlet;

import java.io.IOException;
import java.io.OutputStream;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.PostDAO;

/**
 * Servlet implementation class ImageServlet
 */
@WebServlet("/ImageServlet")
public class ImageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// パラメータから投稿IDを取得
        String postIdParam = request.getParameter("postId");
        if (postIdParam == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        int postId = Integer.parseInt(postIdParam);

        // DAOから画像データを取得
        PostDAO dao = new PostDAO();
        byte[] imageBytes = dao.getPictureByPostId(postId);

        if (imageBytes == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // Content-Typeを画像バイトの先頭で判定（PNG or JPEG）
        if (imageBytes.length >= 8 &&
            imageBytes[0] == (byte) 0x89 && imageBytes[1] == (byte) 0x50 &&
            imageBytes[2] == (byte) 0x4E && imageBytes[3] == (byte) 0x47) {
            response.setContentType("image/png");
        } else {
            response.setContentType("image/jpeg");
        }
        response.setContentLength(imageBytes.length);

        // 画像データを出力
        try (OutputStream out = response.getOutputStream()) {
            out.write(imageBytes);
        }
    }
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
