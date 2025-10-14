package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList; // 検索結果管理用
import java.util.List; // 検索結果管理用

import model.ShopInfo;
import util.DBUtil;

public class ShopDAO {

	// 全件取得用メソッド
	public List<ShopInfo> findAll() throws Exception {
		List<ShopInfo> list = new ArrayList<>();
		String sql = "SELECT * FROM shop ORDER BY NAME";

		try (Connection conn = DBUtil.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				ShopInfo shop = new ShopInfo(
						rs.getString("name"),
						rs.getString("URL"),
						rs.getString("address"),
						rs.getString("TEL")
						);
				list.add(shop);
			}
		}
		return list;
	}

	// 店舗名から店舗情報を出すメソッド
	public ShopInfo findByShopName(String name) throws Exception {
		ShopInfo shop = null;
		String sql = "SELECT name, URL, address, TEL FROM SHOP WHERE name = ?";

		try (Connection conn = DBUtil.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, name); // 1件のみ店舗名をセット
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					shop = new ShopInfo(
							rs.getString("name"),
							rs.getString("URL"),
							rs.getString("address"),
							rs.getString("TEL")
							);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		return shop;
	}

	// 店舗情報検索用メソッド
	
	/* 使い方
	 * shopDAO.searchShopsByName(keywords);
	 * keywordsはList<String>に代入するので
	 * keywords.add(検索キーワード);
	 * このように入力する。
	 */
	
	public List<ShopInfo> searchShopsByName(List<String> keywords) throws Exception {
		List<ShopInfo> list = new ArrayList<>();

		// キーワードが空の場合は空で返す
		if (keywords == null || keywords.isEmpty()) {
			return list;
		}

		StringBuilder sqlBuilder = new StringBuilder("SELECT name, URL, address, TEL FROM SHOP WHERE ");
		for (int i = 0; i < keywords.size(); i++) {
			if (i > 0) {
				sqlBuilder.append(" OR "); // 2つ以上のキーワードの場合『OR』を追加
			}
			sqlBuilder.append("LOWER(name) LIKE ?");
		}

		String sql = sqlBuilder.toString(); // SQL文を保管する場所
		
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {

			// (?)に検索ワードを入力
			for (int i = 0; i < keywords.size(); i++) { // Listのsize文ループ
				ps.setString(i + 1, "%" + keywords.get(i).toLowerCase() + "%"); // 部分一致検索
			}

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					ShopInfo shop = new ShopInfo(
							rs.getString("name"),
							rs.getString("URL"),
							rs.getString("address"),
							rs.getString("TEL")
							);
					list.add(shop);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		return list;
	}

	// 店舗登録用メソッド
	public boolean shopRegister(ShopInfo shop) throws Exception {
		String sql = "INSERT INTO shop (name, url, address, tel) VALUES (?, ?, ?, ?)";
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			
			ps.setString(1, shop.shopName());
			ps.setString(2, shop.shopURL());
			ps.setString(3, shop.shopAddress());
			ps.setString(4, shop.shopTEL());

			int result = ps.executeUpdate();
			return result == 1;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// 店舗更新用メソッド
	public boolean shopEdit(ShopInfo shopInfo, String originalShopName) throws Exception {
		String sql = "UPDATE shop SET name = ?, url = ?, address = ?, tel = ? WHERE name = ?";
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, shopInfo.shopName());
			ps.setString(2, shopInfo.shopURL());
			ps.setString(3, shopInfo.shopAddress());
			ps.setString(4, shopInfo.shopTEL());
			ps.setString(5, originalShopName);

			int result = ps.executeUpdate();
			return result == 1;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// 店舗削除用メソッド
	public void shopDelete(ShopInfo shopInfo) throws Exception {
		String sql = "DELETE FROM shop WHERE NAME = ?";
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, shopInfo.shopName());
			ps.executeUpdate();
		}
	}
	
	// 検索から店舗検索用メソッド
	
	public List<ShopInfo> findByKeyword(String keyword)throws Exception {
		List<ShopInfo> shopList = new ArrayList<>();
		// LIKE句で部分検索
		String sql = "SELECT shopName, shopURL, shopAddress, shopTEL FROM shops WHERE shopName LIKE ? ORDER BY shopName";

		try(Connection conn = DBUtil.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, "%" + keyword + "%");
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					String shopName = rs.getString("shopName");
					String shopURL = rs.getString("shopURL");
					String shopAddress = rs.getString("shopAddress");
					String shopTEL = rs.getString("shopTEL");
					// recordのコンストラクタでインスタンスを生成
					ShopInfo shopInfo = new ShopInfo(shopName, shopURL, shopAddress, shopTEL);
					shopList.add(shopInfo);
				}
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return shopList;
	}
}
