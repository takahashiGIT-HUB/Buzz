package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {
						//自宅環境でテストする場合ここを  ↓『localhost』
	                    //172.31.98.112
	private static final String URL = "jdbc:postgresql://localhost:5432/buzz?characterEncoding=UTF-8";
    private static final String USER = "postgres";
    private static final String PASS = "root";

public static Connection getConnection() throws Exception {
    Class.forName("org.postgresql.Driver");
    return DriverManager.getConnection(URL, USER, PASS);
	}
}