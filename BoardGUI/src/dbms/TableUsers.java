package dbms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TableUsers {

	public void createUsersTable() {
		// 테이블 생성 SQL Query
		String sql = "CREATE TABLE IF NOT EXISTS users ("
				+ ")";
		// DBcon 클래스 호출로 SQL server connection
		try (Connection conn = DBcon.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			// 쿼리 실행
			pstmt.executeUpdate();
			System.out.println("'users' Table Created Query Complete");
			// 테이블 존재 확인
			if (checkTableExists(conn, "users")) {
				System.out.println("[SUCCESS]");
			} else {
				System.out.println("[ERROR]");
			}
		} catch (SQLException e) { // 생성 오류 시
			System.out.println("Created Table ERROR : " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	// 테이블 존재 여부 확인 메서드
	private boolean checkTableExists(Connection conn, String tableName) {
		ResultSet rs = null;
		try {
			
		} catch (SQLException e) {
			
		} finally {
			
		}
	}
	
	public static void main(String[] args) {
		
	}
}
