package test;

import java.util.ArrayList;
import java.util.List;

import dao.ShopDAO;
import model.ShopInfo;

public class ShopDAOTest {

	private static final ShopDAO shopDAO = new ShopDAO();

	public static void main(String[] args) {
		System.out.println("--- findAll()テスト ---");
		testFindAll();
		System.out.println("\n--- findByShopName()テスト ---");
		testFindByShopName_Found();
		testFindByShopName_NotFound();
		System.out.println("\n--- shopRegister()テスト ---");
		testShopRegister_Success();
		testShopRegister_DuplicateName();
		System.out.println("\n--- shopEdit()テスト ---");
		testShopEdit();
		System.out.println("\n--- shopDelete()テスト ---");
		testShopDelete();
		System.out.println("\n--- searchShopsByName()テスト ---");
		testSearchShopsByName_Found();
		testSearchShopsByName_NotFound();
	}

	public static void testFindAll() {
		try {
			List<ShopInfo> list = shopDAO.findAll();
			if (list != null && !list.isEmpty()) {
				System.out.println("findAllメソッドテスト: OK (データが見つかりました)");
			} else {
				System.out.println("findAllメソッドテスト: NG (データが見つかりませんでした)");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("findAllメソッドテスト: 例外が発生しました");
		}
	}

	public static void testFindByShopName_Found() {
		try {
			String testName = "CoCo壱　博多駅南店";
			ShopInfo shop = shopDAO.findByShopName(testName);
			if (shop != null && shop.shopName().equals(testName)) {
				System.out.println("findByShopName_Foundテスト: OK");
			} else {
				System.out.println("findByShopName_Foundテスト: NG");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("findByShopName_Foundテスト: 例外が発生しました");
		}
	}

	public static void testFindByShopName_NotFound() {
		try {
			String testName = "存在しない店舗名"; //見つからない場合のテスト
			ShopInfo shop = shopDAO.findByShopName(testName);
			if (shop == null) {
				System.out.println("findByShopName_NotFoundテスト: OK");
			} else {
				System.out.println("findByShopName_NotFoundテスト: NG");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("findByShopName_NotFoundテスト: 例外が発生しました");
		}
	}

	public static void testShopRegister_Success() {
		try {
			ShopInfo newShop = new ShopInfo("テスト店舗", "http://test.com", "テスト住所", "000-0000-0000");
			boolean isSuccess = shopDAO.shopRegister(newShop);
			if (isSuccess) {
				System.out.println("shopRegister_Successテスト: OK (登録成功)");
			} else {
				System.out.println("shopRegister_Successテスト: NG (登録失敗)");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("shopRegister_Successテスト: 例外が発生しました");
		}
	}
	
	public static void testShopRegister_DuplicateName() {
		try {
			ShopInfo newShop = new ShopInfo("テスト店舗", "http://test.com", "テスト住所", "000-0000-0000");
			// 2回登録することで重複をテスト
			shopDAO.shopRegister(newShop); // 1回目
			boolean isSuccess = shopDAO.shopRegister(newShop); // 2回目
			if (!isSuccess) {
				System.out.println("shopRegister_DuplicateNameテスト: OK (重複登録失敗)");
			} else {
				System.out.println("shopRegister_DuplicateNameテスト: NG (重複登録成功)");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("shopRegister_DuplicateNameテスト: 例外が発生しました");
		}
	}


	public static void testShopEdit() {
		try {
			String originalName = "テスト店舗";
			ShopInfo editedShop = new ShopInfo("テスト店舗(編集後)", "http://edited.com", "編集後住所", "111-1111-1111");
			boolean isSuccess = shopDAO.shopEdit(editedShop, originalName);
			if (isSuccess) {
				System.out.println("shopEditテスト: OK (編集成功)");
			} else {
				System.out.println("shopEditテスト: NG (編集失敗)");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("shopEditテスト: 例外が発生しました");
		}
	}

	public static void testShopDelete() {
		try {
			// 編集した店舗を削除
			ShopInfo deleteShop = new ShopInfo("テスト店舗(編集後)", null, null, null);
			shopDAO.shopDelete(deleteShop);
			// 削除されたか確認
			ShopInfo deletedShop = shopDAO.findByShopName("テスト店舗(編集後)");
			if (deletedShop == null) {
				System.out.println("shopDeleteテスト: OK (削除成功)");
			} else {
				System.out.println("shopDeleteテスト: NG (削除失敗)");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("shopDeleteテスト: 例外が発生しました");
		}
	}

	public static void testSearchShopsByName_Found() {
		try {
			List<String> keywords = new ArrayList<>();
			keywords.add("CoCo");
			List<ShopInfo> result = shopDAO.searchShopsByName(keywords);
			if (result != null && !result.isEmpty()) {
				System.out.println("searchShopsByName_Foundテスト: OK");
			} else {
				System.out.println("searchShopsByName_Foundテスト: NG");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("searchShopsByName_Foundテスト: 例外が発生しました");
		}
	}

	public static void testSearchShopsByName_NotFound() {
		try {
			List<String> keywords = new ArrayList<>();
			keywords.add("存在しないキーワード");
			List<ShopInfo> result = shopDAO.searchShopsByName(keywords);
			if (result != null && result.isEmpty()) {
				System.out.println("searchShopsByName_NotFoundテスト: OK");
			} else {
				System.out.println("searchShopsByName_NotFoundテスト: NG");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("searchShopsByName_NotFoundテスト: 例外が発生しました");
		}
	}
}