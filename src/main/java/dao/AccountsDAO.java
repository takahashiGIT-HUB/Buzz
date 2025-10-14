package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Account;
import model.Login;
import util.DBUtil;

public class AccountsDAO {
	//ログイン状態の確認
    public Account findByLogin(Login login) throws Exception {
        Account account = null;
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "SELECT user_id, pass, username, profile FROM users WHERE user_id = ? AND pass = ?";
            PreparedStatement pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, login.userId());
            pStmt.setString(2, login.pass());
            
            ResultSet rs = pStmt.executeQuery();

            if (rs.next()) {
                String userId = rs.getString("user_id");
                String pass = rs.getString("pass");
                String name = rs.getString("username");
                String profile = rs.getString("profile");
                account = new Account(userId, pass, name, profile);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return account;
    }
   
    //現在のユーザーIDの確認
    public Account findByUserId(String userId) throws Exception {
        Account account = null;
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "SELECT user_id, pass, username, profile FROM users WHERE user_id = ?";
            PreparedStatement pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, userId);

            ResultSet rs = pStmt.executeQuery();

            if (rs.next()) {
                String pass = rs.getString("pass");
                String name = rs.getString("username");
                String profile = rs.getString("profile");
                account = new Account(userId, pass, name, profile);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return account;
    }
    
    //ユーザー登録
    public boolean createAccount(Account account) throws Exception {
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "INSERT INTO users (user_id, pass, username, profile) VALUES (?, ?, ?, ?)";
            PreparedStatement pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, account.userId());
            pStmt.setString(2, account.pass());
            pStmt.setString(3, account.name());
            pStmt.setString(4, account.profile());

            int result = pStmt.executeUpdate();
            return result == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    //ユーザーIDの重複確認
    public boolean userIdSearch(String userId) throws Exception {
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "SELECT COUNT(*) FROM users WHERE user_id = ?";
            PreparedStatement pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, userId);

            ResultSet rs = pStmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    //ユーザー情報編集
    public boolean editProfile(Account account) throws Exception {
        try (Connection conn = DBUtil.getConnection()) {
            String sql;
            PreparedStatement p;

            if (account.pass() == null || account.pass().isEmpty()) {
                sql = "UPDATE users SET username = ?, profile = ? WHERE user_id = ?";
                p = conn.prepareStatement(sql);
                p.setString(1, account.name());
                p.setString(2, account.profile());
                p.setString(3, account.userId());
            } else {
                sql = "UPDATE users SET pass = ?, username = ?, profile = ? WHERE user_id = ?";
                p = conn.prepareStatement(sql);
                p.setString(1, account.pass());
                p.setString(2, account.name());
                p.setString(3, account.profile());
                p.setString(4, account.userId());
            }

            return p.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
