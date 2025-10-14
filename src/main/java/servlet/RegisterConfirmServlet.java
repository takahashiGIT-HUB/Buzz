package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.AccountsDAO;
import model.Account;

@WebServlet("/RegisterConfirmServlet")
public class RegisterConfirmServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String userId = request.getParameter("userId");
        String pass = request.getParameter("pass");
        String confirmPass = request.getParameter("confirmPass");
        String name = request.getParameter("name");
        String profile = request.getParameter("profile");

        request.setAttribute("userId", userId);
        request.setAttribute("name", name);
        request.setAttribute("profile", profile);

        List<String> errorMsgs = new ArrayList<>();
        String userIdPattern = "^[A-Za-z0-9!-/:-@\\[-`{-~]+$";
        String passPattern = "^[A-Za-z0-9!-/:-@\\[-`{-~]+$";
        String regex = "^(?=.*[A-Za-z])(?=.*\\d).*$";

        AccountsDAO dao = new AccountsDAO();

        if (userId == null || userId.isEmpty()) {
            errorMsgs.add("ユーザーIDが入力されていません。");
        } else {
            if (userId.length() > 40) {
                errorMsgs.add("ユーザーIDは40文字以内で入力してください。");
            }
            if (!Pattern.matches(userIdPattern, userId)) {
                errorMsgs.add("ユーザーIDには半角英数字と一部の記号のみ使用できます。");
            }
            try {
				if (dao.userIdSearch(userId)) {
				    errorMsgs.add("そのユーザーIDは既に使用されています。");
				}
			} catch (Exception e) {
			    e.printStackTrace();
			    errorMsgs.add("システムエラーが発生しました（ユーザーID確認）");
			}
        }

        if (pass == null || pass.isEmpty()) {
            errorMsgs.add("パスワードが入力されていません。");
        } else {
            if (pass.length() < 8 || pass.length() > 40) {
                errorMsgs.add("パスワードは8文字以上40文字以下で入力してください。");
            }
            if (!Pattern.matches(passPattern, pass)) {
                errorMsgs.add("パスワードには半角英数字と一部の記号のみ使用できます。");
            }
            if (!Pattern.matches(regex, pass)) {
                errorMsgs.add("パスワードには半角英字および半角数字を最低一つ入力してください。");
            }
            if (confirmPass == null || !pass.equals(confirmPass)) {
                errorMsgs.add("パスワードと確認用パスワードが一致しません。");
            }
        }

        if (name == null || name.isEmpty()) {
            errorMsgs.add("ユーザー名が入力されていません。");
        } else if (name.length() > 40) {
            errorMsgs.add("ユーザー名は40文字以内で入力してください。");
        }
        
        if (profile != null && profile.length() > 200) {
            errorMsgs.add("プロフィールは200文字以内で入力してください。");
        }
        if (!errorMsgs.isEmpty()) {
            request.setAttribute("errorMsgs", errorMsgs);
            RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/register.jsp");
            dispatcher.forward(request, response);
            return;
        }

        Account account = new Account(userId, pass, name, profile);
        HttpSession session = request.getSession();
        session.setAttribute("account", account);

        RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/registerConfirm.jsp");
        dispatcher.forward(request, response);
    }
}
