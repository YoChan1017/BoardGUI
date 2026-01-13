package dbms;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TableComments {

	// 댓글 테이블 생성 > 존재시 여부확인
	
	// 댓글 테이블 생성 메서드
	public void createCommentsTalbe() {
		
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
