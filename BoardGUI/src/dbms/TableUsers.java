package dbms;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TableUsers {

	public void createUsersTable() {
		// 테이블 생성 SQL Query
		String sql = "CREATE TABLE IF NOT EXISTS users ("
				+ "user_id INT AUTO_INCREMENT PRIMARY KEY, "			// 기본키
				+ "username VARCHAR(30) NOT NULL UNIQUE, "				// 유저 ID, 중복/null 안됨
				+ "password VARCHAR(255) NOT NULL, "					// 유저 PW, 해싱 필요, null 안됨
				+ "nickname VARCHAR(30) NOT NULL, "						// 유저 닉네임, null 안됨
				+ "role VARCHAR(20) DEFAULT 'user', "					// 유저 권한, 기본값 'user'(일반유저)
				+ "is_active BOOLEAN DEFAULT TRUE, "					// 계정 활성 여부, 기본값 'true'(활성)
				+ "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP"	// 생성날짜, 유저 데이터 생성 시 현재 시간 자동 입력
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
			DatabaseMetaData metaData = conn.getMetaData();
			rs = metaData.getTables(null, null, tableName, null);
			return rs.next();
			// 결과가 있으면 테이블이 존재
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			if (rs != null) {
				try { rs.close(); } catch (SQLException e) {}
			}
		}
	}
	
	public static void main(String[] args) {
		TableUsers setup = new TableUsers();
		setup.createUsersTable();
	}
}
