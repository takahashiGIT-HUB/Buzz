package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.BuzzInfo;
import model.PostInfo;
import util.DBUtil;

public class BuzzDAO {
	//バズボタンを押したユーザーを登録する
	public boolean insert(BuzzInfo buzz) {
		//sql文の準備
		String sql = 
				"INSERT INTO buzzbutton (posts_id,user_id) " +
						"VALUES (?, ?)";
		Connection connection = null;

		try {
			connection = DBUtil.getConnection();
			connection.setAutoCommit(false);

			try (PreparedStatement stmt = connection.prepareStatement(sql)) {
				stmt.setInt(1, buzz.postId());
				stmt.setString(2, buzz.userId());
				int result = stmt.executeUpdate();
				connection.commit(); // 成功したらコミット
				return result == 1;
			}
		} catch (Exception e) {
			try {
				if (connection != null) {
					connection.rollback(); // 失敗したらロールバック
				}
			} catch (SQLException rollbackEx) {
				rollbackEx.printStackTrace();
			}
			e.printStackTrace();
			return false;
		} finally {
			//  最終的にコネクションを閉じる
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException closeEx) {
					closeEx.printStackTrace();
				}
			}
		}
	}
	// バズを削除
	public boolean delete(int postId , String userId)  {
		String sql = "DELETE FROM buzzbutton WHERE posts_id = ? AND user_id = ?";
		Connection connection = null;
		try {
			connection = DBUtil.getConnection();
			connection.setAutoCommit(false);
			// 自動でのコミットをオフにする
			try (PreparedStatement stmt = connection.prepareStatement(sql)) {
				stmt.setInt(1, postId);
				stmt.setString(2, userId);
				int result = stmt.executeUpdate();
				connection.commit(); // 成功したらコミット
				return result == 1;
			}
		} catch (Exception e) {
			try {
				if (connection != null) {
					connection.rollback(); // 失敗したらロールバック
				}
			} catch (SQLException rollbackEx) {
				rollbackEx.printStackTrace();
			}
			e.printStackTrace();
			return false;
		} finally { //  最終的にコネクションを閉じる
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException closeEx) {
					closeEx.printStackTrace();
				}
			}
		}
	}
	//バズ数を表示する
	public int countBuzz(int postId) {
		String sql = "SELECT COUNT(*) FROM buzzbutton WHERE posts_id = ?";
		int count = 0;
		try (Connection connection = DBUtil.getConnection();
				 PreparedStatement stmt = connection.prepareStatement(sql)) {
				stmt.setInt(1, postId);
				
				ResultSet rs = stmt.executeQuery();
				if (rs.next()) {
					count = rs.getInt(1);
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
			return count;
		}
	
	//バズボタンを押しているかの確認
	public boolean exists(int postId, String userId) {
	    String sql = "SELECT 1 FROM buzzbutton WHERE posts_id = ? AND user_id = ?";

	    try (Connection connection = DBUtil.getConnection();
				 PreparedStatement stmt = connection.prepareStatement(sql)) {
				stmt.setInt(1, postId);
				stmt.setString(2, userId);
				ResultSet rs = stmt.executeQuery();
				return rs.next();
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
	//バズした投稿一覧を表示するメソッド
	public List<BuzzInfo> findBuzzPost(String userId) {
	    List<BuzzInfo> buzzList = new ArrayList<>();

	    String sql = "SELECT buzz_id, posts_id, user_id FROM buzzbutton WHERE user_id = ?";

	    try (Connection connection = DBUtil.getConnection();
		         PreparedStatement stmt = connection.prepareStatement(sql)) {
		        stmt.setString(1, userId);
		        ResultSet rs = stmt.executeQuery();
		        while (rs.next()) {
		            int buzzId = rs.getInt("buzz_id");
		            int postId = rs.getInt("posts_id");
		            String uId = rs.getString("user_id");
		            BuzzInfo buzz = new BuzzInfo(buzzId, postId, uId);
		            buzzList.add(buzz);
		        }
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		    return buzzList;
		}
	//店舗毎にバズ数が多いコメント上位3位を取得
	public List<PostInfo> rankingComment(String shop) {
	    List<PostInfo> result = new ArrayList<>();

	    String sql = """
	        SELECT p.posts_id, p.user_id, p.comment, p.pictures, p.shop, p.postTime, u.userName, COUNT(b.buzz_id) AS buzz_count
	        FROM posts p
	        LEFT JOIN buzzbutton b ON p.posts_id = b.posts_id
	        LEFT JOIN users u ON p.user_id = u.user_id
	        WHERE p.shop = ?
	        GROUP BY p.posts_id, p.user_id, p.comment, p.pictures, p.shop, p.postTime, u.userName
	        ORDER BY buzz_count DESC
	        LIMIT 3
	    """;

	    try (Connection connection = DBUtil.getConnection();
	    		PreparedStatement stmt = connection.prepareStatement(sql)) {
	    	stmt.setString(1, shop);

	    	try (ResultSet rs = stmt.executeQuery()) {
	    		while (rs.next()) {
	    			PostInfo post = new PostInfo(
	    					rs.getInt("posts_id"),
	            			rs.getString("user_id"),
	            			rs.getString("comment"),
	            			rs.getBytes("pictures"),
	            			rs.getString("shop"),
	            			rs.getTimestamp("postTime"),
	            			rs.getString("userName")
	            			);
	    			result.add(post);
	    		}
	    	}
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	    return result;
	}
	
	//つぶやきを消すときにバズ情報を消す
	public void deleteByPostId(int postId) throws Exception {
	    String sql = "DELETE FROM buzzbutton WHERE posts_id = ?";
	    try (Connection conn = DBUtil.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, postId);
	        pstmt.executeUpdate();
	    }
	}
	
	
	
	
	
	
}