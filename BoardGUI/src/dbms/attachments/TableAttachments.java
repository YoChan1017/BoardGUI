package dbms.attachments;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dbms.DBcon;

public class TableAttachments {
	
	// 첨부파일 테이블 생성 > 존재시 여부확인
	
	// 첨부파일 테이블 생성 메서드
	public void createAttachmentsTable() {
		// 테이블 생성 SQL
		String sql = "CREATE TABLE IF NOT EXISTS attachments ("
				+ "file_id INT AUTO_INCREMENT PRIMARY KEY, "
				+ "post_id INT NOT NULL, "
				+ "origin_name VARCHAR(255) NOT NULL, "
				+ "save_name VARCHAR(255) NOT NULL, "
				+ "save_path VARCHAR(255) NOT NULL, "
				+ "file_size BIGINT NOT NULL, "
				+ "ext VARCHAR(10), "
				+ "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
				+ "FOREIGN KEY (post_id) REFERENCES posts(post_id) ON DELETE CASCADE"
				+ ")";
		
		try (Connection conn = DBcon.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			// 쿼리 실행
			pstmt.executeUpdate();
			System.out.println("'attachments' Table Created or Already Exists.");
			if (checkTableExists(conn, "attachments")) {
				System.out.println("[SUCCESS] attachments 테이블 확인이 완료되었습니다.");
			} else {
				System.out.println("[ERROR] attachments 테이블이 확인되지 않습니다.");
			}
		} catch (SQLException e) {
			System.err.println("Created Table ERROR : " + e.getMessage());
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
		TableAttachments setup = new TableAttachments();
		setup.createAttachmentsTable();
	}
}
