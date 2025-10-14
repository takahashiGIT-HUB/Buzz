package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.BuzzDAO;
import model.BuzzInfo;


@WebServlet("/BuzzServlet")
public class BuzzServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		//check
		System.out.println("BuzzServlet に到達");
		response.setContentType("application/json; charset=UTF-8");
		
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userId");
        String postIdStr = request.getParameter("postId");
        //check
        System.out.println("userId in BuzzServlet = " + userId);
        System.out.println("postIdStr = " + postIdStr);

        if (userId == null || postIdStr == null || postIdStr.isEmpty()) {
            response.sendRedirect("LoginServlet");
            return;
        }

        int postId = Integer.parseInt(postIdStr);
        int buzzCount = 0;
        boolean liked;
        try {
            BuzzDAO dao = new BuzzDAO();

            if (dao.exists(postId, userId)) {
            	//check
        		System.out.println("Buzzdelete");
                dao.delete(postId, userId);
                liked = false;
            } else {
                dao.insert(new BuzzInfo(0, postId, userId));
              //check
        		System.out.println("Buzzinsert");
                liked = true;
            }

            buzzCount = dao.countBuzz(postId);

            // JSONで返す
            String json = String.format("{\"liked\": %b, \"buzzCount\": %d}", liked, buzzCount);
            response.getWriter().write(json);

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}