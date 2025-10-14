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

import model.Account;

@WebServlet("/UserEditConfirmServlet")
public class UserEditConfirmServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
        	response.sendRedirect("LoginServlet");
            return;
        }

        String userId = (String) session.getAttribute("userId");
        String pass = request.getParameter("pass");
        String confirmPass = request.getParameter("confirmPass");
        String name = request.getParameter("name");
        String profile = request.getParameter("profile");
        String passPattern = "^[A-Za-z0-9!-/:-@\\[-`{-~]+$";
        String regex = "^(?=.*[A-Za-z])(?=.*\\d).*$";

        List<String> errorMsgs = new ArrayList<>();

        // パスワードのチェック（空欄は「変更なし」として許容）
        if (pass != null && !pass.isEmpty()) {
        	if (pass.length() < 8 || pass.length() > 40) {
                errorMsgs.add("パスワードは8文字以上40文字以下で入力してください。");
            }
            if (!Pattern.matches(passPattern, pass)) {
                errorMsgs.add("パスワードには半角英数字と一部の記号（!-/:-@[-`{-~など）のみ使用できます。");
            }
            if (!Pattern.matches(regex, pass)) {
                errorMsgs.add("パスワードには半角英字および半角数字を最低一つ含めてください。");
            }
            if (confirmPass == null || !pass.equals(confirmPass)) {
                errorMsgs.add("パスワードと確認用パスワードが一致しません。");
            }
        }
        
        // ユーザー名のチェック
        if (name == null || name.isEmpty()) {
            errorMsgs.add("ユーザー名が入力されていません。");
        } else if (name.length() > 40) {
            errorMsgs.add("ユーザー名は40文字以内で入力してください。");
        }
        
        // プロフィールのチェック
        if (profile != null && profile.length() > 200) {
            errorMsgs.add("プロフィールは200文字以内で入力してください。");
        }
        
        // エラーがある場合、入力画面に戻す
        if (!errorMsgs.isEmpty()) {
        	request.setAttribute("errorMsgs", errorMsgs);
        	request.setAttribute("userId", userId);
        	request.setAttribute("pass", pass);
        	request.setAttribute("name", name);
        	request.setAttribute("profile", profile);
            RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/userEdit.jsp");
            dispatcher.forward(request, response);
            return;
        }

        // Accountオブジェクトにまとめてセッションへ保存（確認・登録用）
        Account account = new Account(userId, pass, name, profile);
        session.setAttribute("editAccount", account);
        request.setAttribute("userId", account.userId());
        request.setAttribute("name", account.name());
        request.setAttribute("profile", account.profile());

        // 確認画面にフォワード
        RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/userEditConfirm.jsp");
        dispatcher.forward(request, response);
    }
}
