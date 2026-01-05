package dbms;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBcon {

	public static void main(String[] args) {
		String driver = "org.mariadb.jdbc.Driver";
		String serverUrl = "jdbc:mariadb://localhost:3307/";
		String dbName = "BoardDB";
		String username = "root";
		String password = "1234";
		
		Connection con = null;
		Statement stmt = null;
		
		try {
			// Driver Load
			Class.forName(driver);
			System.out.println("Driver Load Success!");
			
			// Server Connection
			con = DriverManager.getConnection(serverUrl, username, password);
			System.out.println("Server Connection Complete.");
			
			// Database 생성/확인
			stmt = con.createStatement();
			String sql = "CREATE DATABASE IF NOT EXISTS " + dbName;	// DB가 존재하지 않을 시 생성, 존재하는 경우 경고 메세지 (EXISTS 뒤에 띄어쓰기 꼭 할 것)
			stmt.executeUpdate(sql);
			System.out.println("Database '" + dbName + "' created or already exists.");
			
			// 기존 연결 해제
			stmt.close();
			con.close();
			
			// 생성된 BoardDB로 재연결
			String targetUrl = serverUrl + dbName;
			con = DriverManager.getConnection(targetUrl, username, password);
			System.out.println("Successfully connected to '" + dbName + "'");
			
		} catch (ClassNotFoundException e) { // Driver Load에 문제가 있을 시
			System.out.println("Driver Load Failed : " + e.getMessage());
		} catch (SQLException e) { // DB 생성/확인에 문제가 있을 시
			System.out.println("DB Connection Failed : " + e.getMessage());
			e.printStackTrace();
		} finally { // 연결 해제
			try {
				if (stmt != null) stmt.close();
				if (con != null) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
