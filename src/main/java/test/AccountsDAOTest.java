package test;

import dao.AccountsDAO;
import model.Account;
import model.Login;

public class AccountsDAOTest {
    public static void main(String[] args) throws Exception {
        testFindByLoginOK(); // ユーザーが見つかる場合のテスト
        testFindByLoginNG(); // ユーザーが見つからない場合のテスト
        testCreateAccountOK(); // アカウント作成が成功する場合のテスト
        testCreateAccountNG(); // アカウント作成が失敗する場合のテスト (重複ユーザーIDなど)
        testUserIdSearchFound(); // ユーザーIDが見つかる場合のテスト
        testUserIdSearchNotFound(); // ユーザーIDが見つからない場合のテスト
    }

    public static void testFindByLoginOK() throws Exception {
        Login login = new Login("1234abcd", "123-xyz!");
        AccountsDAO dao = new AccountsDAO();
        Account result = dao.findByLogin(login);
        if (result != null &&
            result.userId().equals("1234abcd") &&
            result.pass().equals("123-xyz!") &&
            result.name().equals("湊　雄輔") && // AccountsDAOの変更に合わせて修正
            result.profile().equals("テスト用文章。この文章が正常に表示されていれば問題ないです。user_id 1234abcd　pass 123-xyz!　name 湊　雄輔") // AccountsDAOの変更に合わせて修正
        ) {
            System.out.println("testFindByLoginOK:成功しました");
        } else {
            System.out.println("testFindByLoginOK:失敗しました");
        }
    }

    public static void testFindByLoginNG() throws Exception {
        Login login = new Login("1234abcd", "123-xyz!!");
        AccountsDAO dao = new AccountsDAO();
        Account result = dao.findByLogin(login);
        if (result == null) {
            System.out.println("testFindByLoginNG:成功しました");
        } else {
            System.out.println("testFindByLoginNG:失敗しました");
        }
    }

    public static void testCreateAccountOK() throws Exception {
        // テスト用のユニークなユーザーIDを生成 (実行ごとに変えるか、テストDBをクリーンアップする必要あり)
        String testUserId = "testuser_" + System.currentTimeMillis();
        Account newAccount = new Account(testUserId, "testpass1234", "テストユーザー", "これはテストアカウントです。");
        AccountsDAO dao = new AccountsDAO();
        boolean result = dao.createAccount(newAccount);

        if (result) {
            System.out.println("testCreateAccountOK:成功しました");
            // 成功した場合は、作成したアカウントがfindByLoginで取得できるか確認するテストを追加することも可能
            // また、テスト後に作成したデータを削除する処理を追加するとより堅牢になります
        } else {
            System.out.println("testCreateAccountOK:失敗しました");
        }
    }

    public static void testCreateAccountNG() throws Exception {
        // 既に存在するユーザーIDでアカウントを作成しようとするテスト
        // 環境に依存するが、"minato"が既に存在すると仮定
        Account existingAccount = new Account("1234abcd", "newpass", "既存ユーザー", "既存のアカウント");
        AccountsDAO dao = new AccountsDAO();
        boolean result = dao.createAccount(existingAccount);

        if (!result) {
            System.out.println("testCreateAccountNG:成功しました (重複ユーザーIDで作成失敗)");
        } else {
            System.out.println("testCreateAccountNG:失敗しました (重複ユーザーIDで作成されてしまった)");
        }
    }

    public static void testUserIdSearchFound() throws Exception {
        // 存在するユーザーIDで検索するテスト
        String existingUserId = "1234abcd"; // データベースに存在するユーザーID
        AccountsDAO dao = new AccountsDAO();
        boolean result = dao.userIdSearch(existingUserId);

        if (result) {
            System.out.println("testUserIdSearchFound:成功しました (ユーザーIDが見つかった)");
        } else {
            System.out.println("testUserIdSearchFound:失敗しました (ユーザーIDが見つからなかった)");
        }
    }

    public static void testUserIdSearchNotFound() throws Exception {
        // 存在しないユーザーIDで検索するテスト
        String nonExistingUserId = "nonexistent_user_12345";
        AccountsDAO dao = new AccountsDAO();
        boolean result = dao.userIdSearch(nonExistingUserId);

        if (!result) {
            System.out.println("testUserIdSearchNotFound:成功しました (ユーザーIDが見つからなかった)");
        } else {
            System.out.println("testUserIdSearchNotFound:失敗しました (ユーザーIDが見つかってしまった)");
        }
    }
}

