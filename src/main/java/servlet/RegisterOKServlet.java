package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.AccountsDAO;
import model.Account;

@WebServlet("/RegisterOKServlet")
public class RegisterOKServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        if ("confirm".equals(action)) {
            HttpSession session = request.getSession();
            Account account = (Account) session.getAttribute("account");

            if (account == null) {
                List<String> errorMsgs = new ArrayList<>();
                errorMsgs.add("セッション情報が無効です。再度入力してください。");

                request.setAttribute("errorMsgs", errorMsgs);
                request.setAttribute("userId", request.getParameter("userId"));
                request.setAttribute("name", request.getParameter("name"));
                request.setAttribute("profile", request.getParameter("profile"));

                RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/register.jsp");
                dispatcher.forward(request, response);
                return;
            }

            AccountsDAO dao = new AccountsDAO();
            boolean result = false;
            try {
                result = dao.createAccount(account);
            } catch (Exception e) {
                e.printStackTrace();

                List<String> errorMsgs = new ArrayList<>();
                errorMsgs.add("システムエラーが発生しました（アカウント作成）");

                request.setAttribute("errorMsgs", errorMsgs);
                request.setAttribute("userId", account.userId());
                request.setAttribute("name", account.name());
                request.setAttribute("profile", account.profile());

                RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/register.jsp");
                dispatcher.forward(request, response);
                return;
            }

            if (result) {
                session.setAttribute("userId", account.userId());
                session.removeAttribute("account");

                RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/registerOK.jsp");
                dispatcher.forward(request, response);
            } else {
                List<String> errorMsgs = new ArrayList<>();
                errorMsgs.add("登録処理中に予期せぬエラーが発生しました。");

                request.setAttribute("errorMsgs", errorMsgs);
                request.setAttribute("userId", account.userId());
                request.setAttribute("name", account.name());
                request.setAttribute("profile", account.profile());

                RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/register.jsp");
                dispatcher.forward(request, response);
            }

        } else {
            response.sendRedirect("RegisterServlet");
        }
    }
}
