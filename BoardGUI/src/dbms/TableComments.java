package dbms;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TableComments {

	// 댓글 테이블 생성 > 존재시 여부확인
	
	// 댓글 테이블 생성 메서드
	public void createCommentsTalbe() {
		// 테이블 생성 SQL
		String sql = "CREATE TABLE IF NOT EXISTS comments ("
				
				+ ")";
		
		try (Connection conn = DBcon.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			// 쿼리 실행
			pstmt.executeUpdate();
			System.out.println("'comments' Table Created or Already Exists.");
			if (checkTableExists(conn, "comments")) {
				System.out.println("[SUCCESS] comments 테이블 확인이 완료되었습니다.");
			} else {
				System.out.println("[ERROR] comments 테이블이 확인되지 않습니다.");
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
		TableComments setup = new TableComments();
		setup.createCommentsTalbe();
	}
}
