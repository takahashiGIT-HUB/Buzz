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

import model.Account;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.removeAttribute("userId");
        request.removeAttribute("name");
        request.removeAttribute("profile");
        request.removeAttribute("errorMsgs");

        RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/register.jsp");
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        if ("edit".equals(action)) {
            HttpSession session = request.getSession();
            Account account = (Account) session.getAttribute("account");

            if (account != null) {
                request.setAttribute("userId", account.userId());
                request.setAttribute("name", account.name());
                request.setAttribute("profile", account.profile());
            } else {
                List<String> errorMsgs = new ArrayList<>();
                errorMsgs.add("セッション情報が無効です。再度入力してください。");
                request.setAttribute("errorMsgs", errorMsgs);
            }

            RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/register.jsp");
            dispatcher.forward(request, response);
        } else {
            response.sendRedirect("RegisterServlet");
        }
    }
}
