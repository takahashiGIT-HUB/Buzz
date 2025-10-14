package test;

import model.Login;
import model.LoginLogic;

public class LoginLogicTest{
	public static void main(String[] args) throws Exception {
		testExecuteOK(); //ログイン成功のテスト
		testExecuteNG(); //ログイン失敗のテスト
	}
	public static void testExecuteOK() throws Exception {
		Login login = new Login("1234abcd", "123-xyz!");
		LoginLogic bo = new LoginLogic();
		boolean result = bo.execute(login);
		if(result) {
			System.out.println("testExecuteOK:成功しました");
		}else {
			System.out.println("testExecuteOK:失敗しました");
		}
	}
	public static void testExecuteNG() throws Exception {
		Login login = new Login("minato", "12345");
		LoginLogic bo = new LoginLogic();
		boolean result = bo.execute(login);
		if(!result) {
			System.out.println("testExecuteNG:成功しました");
		}else {
			System.out.println("testExecuteNG:失敗しました");
		}
	}
}
