package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import model.PostInfo;
import util.DBUtil;

public class PostDAO {
	//データベース接続に使用する情報
		
		private  Connection conn = null;
	
	public PostDAO()  {
		//JDBCドライバーを読み込む
		try {
			//データベース接続
			 conn = DBUtil.getConnection();
		}catch(Exception  e) {
			throw new IllegalStateException("JDBCドライバーを読み込めませんでした");
		}
	}
	
	//つぶやきを投稿
	public boolean postInsert(PostInfo postInfo) {
		//sql文の準備
		String sql = 
				"INSERT INTO posts (user_id,comment, pictures, shop, postTime) " +
		                 "VALUES (?, ?, ?, ?,NOW())";
		
		try (PreparedStatement stmt = conn.prepareStatement(sql);){
			
			
			stmt.setString(1, postInfo.userId());
		    stmt.setString(2, postInfo.comment());
		    stmt.setBytes(3, postInfo.pic());
		    stmt.setString(4, postInfo.shopName());
		    
		    int result = stmt.executeUpdate();
	        return result == 1;  // 成功したら true を返す
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false; // 失敗したら false を返す
	    }
	}
	
	//つぶやきを編集
	public boolean postEdit(PostInfo postInfo,int postId,boolean deletePicture) {
		String sql;
		
		if (deletePicture) {
		    // 画像を削除（pictures = null）
		    sql = "UPDATE posts SET comment = ?, pictures = NULL, shop = ?, postTime = NOW() WHERE posts_id = ?";
		} else if (postInfo.pic() != null) {
		    // 画像を変更
		    sql = "UPDATE posts SET comment = ?, pictures = ?, shop = ?, postTime = NOW() WHERE posts_id = ?";
		} else {
		    // 画像は変更なし
		    sql = "UPDATE posts SET comment = ?, shop = ?, postTime = NOW() WHERE posts_id = ?";
		}
		
		try(PreparedStatement stmt = conn.prepareStatement(sql);){
			
			
			if (deletePicture) {
		        stmt.setString(1, postInfo.comment());
		        stmt.setString(2, postInfo.shopName());
		        stmt.setInt(3, postId);
		    } else if (postInfo.pic() != null) {
		        stmt.setString(1, postInfo.comment());
		        stmt.setBytes(2, postInfo.pic());
		        stmt.setString(3, postInfo.shopName());
		        stmt.setInt(4, postId);
		    } else {
		        stmt.setString(1, postInfo.comment());
		        stmt.setString(2, postInfo.shopName());
		        stmt.setInt(3, postId);
		    }
			int result = stmt.executeUpdate();
	        return result == 1;  // 成功したら true を返す
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false; // 失敗したら false を返す
	    }
		
	}
	
	// つぶやきを削除
	public boolean postDelete(int postId)  {
		String sql = "DELETE FROM posts WHERE posts_id = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql);) {
			stmt.setInt(1, postId);
			
			//バズ情報の削除
			BuzzDAO buzzDao = new BuzzDAO();
			buzzDao.deleteByPostId(postId);
			
			int result = stmt.executeUpdate();
	        return result ==1;  // 成功したら true を返す
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false; // 失敗したら false を返す
	    }
	}
	
	//指定されたIDのつぶやきを1件だけ取得する
	public PostInfo findById(int postId) {
	    PostInfo postInfo = null;
	    String sql = "SELECT p.posts_id, p.user_id, p.comment, p.pictures, p.shop, p.postTime, u.userName " +
                "FROM posts p JOIN users u ON p.user_id = u.user_id " +
                "WHERE p.posts_id = ?";

	    try (PreparedStatement stmt = conn.prepareStatement(sql)) {

	        stmt.setInt(1, postId);

	        try (ResultSet rs = stmt.executeQuery()) {
	            if (rs.next()) {
	                int id = rs.getInt("posts_id");
	                String userId = rs.getString("user_id");
	                String comment = rs.getString("comment");
	                byte[] pic = rs.getBytes("pictures");
	                String shop = rs.getString("shop");
	                Timestamp postTime = rs.getTimestamp("postTime");
	                String userName = rs.getString("userName");

	                postInfo = new PostInfo(id, userId, comment, pic, shop, postTime, userName);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return postInfo;
	}
	
	//つぶやきを全件表示
	public List<PostInfo> postFindAll(){
		List<PostInfo> postList = new ArrayList<>();
		
		
			//select文の準備
		String sql = "SELECT p.posts_id, p.user_id, p.comment, p.pictures, p.shop, p.postTime, u.userName " +
				"FROM posts p JOIN users u ON p.user_id = u.user_id " + 
				"ORDER BY p.postTime DESC";
			try(PreparedStatement stmt = conn.prepareStatement(sql);){
			
			//select文を実行
			ResultSet rs = stmt.executeQuery();
			
			//select文の結果をArrayListに格納
			while(rs.next()) {
				int postId = rs.getInt("posts_id");
				String userId = rs.getString("user_id");
				String comment = rs.getString("comment");
				byte[] pic = rs.getBytes("pictures");
				String shop = rs.getString("shop");
				Timestamp postTime = rs.getTimestamp("postTime");
				String userName = rs.getString("userName");
				
				PostInfo post = new PostInfo(postId, userId, comment, pic, shop, postTime, userName);
				postList.add(post);
			}
		
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return postList;
	}
	//ログインユーザーのつぶやきを全件表示
	public List<PostInfo> findPostsByUserId(String userId) {
	    List<PostInfo> postList = new ArrayList<>();
	    String sql = "SELECT p.posts_id, p.user_id, p.comment, p.pictures, p.shop, p.postTime, u.userName " +
                "FROM posts p JOIN users u ON p.user_id = u.user_id " +
                "WHERE p.user_id = ? ORDER BY p.posts_id DESC";
	    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setString(1, userId);
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
	                postList.add(post);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return postList;
	}
	
	public byte[] getPictureByPostId(int postId) {
	    String sql = "SELECT pictures FROM posts WHERE posts_id = ?";
	    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setInt(1, postId);
	        try (ResultSet rs = stmt.executeQuery()) {
	            if (rs.next()) {
	                return rs.getBytes("pictures");
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null;
	}
	
	// 検索からポストを引用するメソッド
	public List<PostInfo> findByKeyword(String keyword) {
		List<PostInfo> postList = new ArrayList<>();
		// SQLはコメントと店舗名の両方を部分一致検索する
		// ORでどちらかの条件に合致すれば検索一致扱い
		String sql = "SELECT p.posts_id, p.user_id, p.comment, p.pictures, p.shop, p.postTime, u.userName FROM posts p JOIN users u ON p.user_id = u.user_id WHERE p.comment LIKE ? OR p.shop LIKE ? ORDER BY p.postTime DESC";

		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			// 部分一致検索にする
			stmt.setString(1, "%" + keyword + "%");
			stmt.setString(2, "%" + keyword + "%");

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					int postId = rs.getInt("posts_id");
					String userId = rs.getString("user_id");
					String comment = rs.getString("comment");
					byte[] pic = rs.getBytes("pictures");
					String shop = rs.getString("shop");
					Timestamp postTime = rs.getTimestamp("postTime");
					
					String userName = rs.getString("userName");
					
					PostInfo post = new PostInfo(postId, userId, comment, pic, shop, postTime, userName);
					
					postList.add(post);
					
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return postList;
	}
	
	//新着のつぶやきを一部表示
	public List<PostInfo> postFindNew(){
		List<PostInfo> postList = new ArrayList<>();
			//select文の準備
			String sql = "SELECT p.posts_id, p.user_id, p.comment, p.pictures, p.shop, p.postTime, u.userName " +
	                 "FROM posts p JOIN users u ON p.user_id = u.user_id " +
	                 "ORDER BY p.posts_id DESC LIMIT 10";
			try(PreparedStatement stmt = conn.prepareStatement(sql);){
			
			//select文を実行
			ResultSet rs = stmt.executeQuery();
				
			//select文の結果をArrayListに格納
			while(rs.next()) {
				int postId = rs.getInt("posts_id");
				String userId = rs.getString("user_id");
				String comment = rs.getString("comment");
				byte[] pic = rs.getBytes("pictures");
				String shop = rs.getString("shop");
				Timestamp postTime = rs.getTimestamp("postTime");
				String userName = rs.getString("userName");
				
				PostInfo post = new PostInfo(postId, userId, comment, pic, shop, postTime, userName);
				postList.add(post);
			}
		
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return postList;
	}
	//投稿数上位3店舗を表示
	public List<String> shopRanking() {
	    List<String> shopList = new ArrayList<>();
	    
	    //投稿数の多い店舗名をtop3までを表示
	    String sql = """
	        SELECT shop
	        FROM posts
	        WHERE shop IS NOT NULL AND TRIM(shop) <> ''
	        GROUP BY shop
	        ORDER BY COUNT(*) DESC
	        LIMIT 5
	    """;

	    try (PreparedStatement stmt = conn.prepareStatement(sql);
	         ResultSet rs = stmt.executeQuery()) {

	        while (rs.next()) {
	            shopList.add(rs.getString("shop"));
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return shopList;
	}
	
	//ある店舗についての画像付きつぶやきの取得(ShopInfoServlet用)
	public List<PostInfo> findPostsByShopWithImage(String shopName) {
	    List<PostInfo> postList = new ArrayList<>();
	    String sql = "SELECT p.posts_id, p.user_id, p.comment, p.pictures, p.shop, p.postTime, u.userName " +
	                 "FROM posts p JOIN users u ON p.user_id = u.user_id " +
	                 "WHERE p.shop = ? AND p.pictures IS NOT NULL " +
	                 "ORDER BY p.postTime DESC";

	    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setString(1, shopName);
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
	                postList.add(post);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return postList;
	}
	
}
