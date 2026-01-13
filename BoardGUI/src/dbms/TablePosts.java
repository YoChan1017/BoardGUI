package dbms;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TablePosts {
	
	// 게시글 테이블 생성 > 존재시 여부확인
	
	// 게시글 테이블 생성 메서드
	public void createPostsTable() {
		// 테이블 생성 SQL
		String sql = "CREATE TABLE IF NOT EXISTS posts ("
				+ "post_id INT AUTO_INCREMENT PRIMARY KEY, "
				+ "board_id INT NOT NULL, "
				+ "user_id INT NOT NULL, "
				+ "title VARCHAR(200) NOT NULL, "
				+ "content MEDIUMTEXT NOT NULL, "
				+ "view_count INT DEFAULT 0, "
				+ "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
				+ "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, "
				+ "is_notice BOOLEAN DEFAULT FALSE, "
				+ "is_secret BOOLEAN DEFAULT FALSE, "
				+ "status VARCHAR(20) DEFAULT 'waiting', "
				+ "FOREIGN KEY (board_id) REFERENCES boards(board_id) ON DELETE CASCADE, "
				+ "FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE"
				+ ")";
		
		try (Connection conn = DBcon.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			// 쿼리 실행
			pstmt.executeUpdate();
			System.out.println("'posts' Table Created or Already Exists.");
			if (checkTableExists(conn, "posts")) {
				System.out.println("[SUCCESS] posts 테이블 확인이 완료되었습니다.");
			} else {
				System.out.println("[ERROR] posts 테이블이 확인되지 않습니다.");
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
		TablePosts setupt = new TablePosts();
		setupt.createPostsTable();
	}
}
