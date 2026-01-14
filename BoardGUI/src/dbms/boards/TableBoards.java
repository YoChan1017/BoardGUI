package dbms.boards;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.mariadb.jdbc.client.result.Result;

import dbms.DBcon;

public class TableBoards {
	
	// 게시판 테이블 생성 > 존재시 여부확인
	// 게시판 기본정보 생성 > 공지사항, 건의사항, 자유게시판(notice, qna, normal)
	
	// 게시판 테이블 생성 메서드
	public void createBoardsTable() {
		// 테이블 생성 SQL
		String sql = "CREATE TABLE IF NOT EXISTS BOARDS ("
				+ "board_id INT AUTO_INCREMENT PRIMARY KEY, "	// 고유 id
				+ "code VARCHAR(30) NOT NULL UNIQUE, "			// 게시판 코드 (식별용)
				+ "name VARCHAR(50) NOT NULL, "					// 게시판 이름 (표시용)
				+ "type VARCHAR(20) DEFAULT 'NORMAL', "			// 게시판 타입 (구분용)
				+ "read_role INT DEFAULT 1, "					// 읽기 권한 레벨(기본값:1)
				+ "write_role INT DEFAULT 1, "					// 쓰기 권한 레벨(기본값:1)
				+ "is_actives BOOLEAN DEFAULT TRUE"				// 활성 여부(기본값:true)
				+ ")";
		
		try (Connection conn = DBcon.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			// 쿼리 실행
			pstmt.executeUpdate();
			System.out.println("'boards' Table Created or Already Exists.");
			if (checkTableExists(conn, "boards")) {
				System.out.println("[SUCCESS] boards 테이블 확인이 완료되었습니다.");
				insertDefaultBoards(conn); // 테이블이 확인되면 기본 데이터 삽입
			} else {
				System.out.println("[ERROR] boards 테이블이 확인되지 않습니다.");
			}
		} catch (SQLException e) { // 생성 오류 시
			System.err.println("Created Table ERROR : " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	// 기본 게시판 데이터 생성 메서드 (공지사항, 자유게시판, 건의사항)
	private void insertDefaultBoards(Connection conn) {
		// INSERT IGNORE > 중복 데이터가 있으면 에러 없이 무시
		String sql = "INSERT IGNORE INTO boards (code, name, type, read_role, write_role) VALUES (?, ?, ?, ?, ?)";
		
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			// 공지사항(NOTICE) > 쓰기 권한은 관리자만 가능
			pstmt.setString(1, "notice");
			pstmt.setString(2, "공지사항");
			pstmt.setString(3, "NOTICE");
			pstmt.setInt(4, 1);
			pstmt.setInt(5, 9);
			pstmt.addBatch();
			
			// 자유게시판(NORMAL) > 누구나 읽고 쓰기 가능
			pstmt.setString(1, "normal");
			pstmt.setString(2, "자유게시판");
			pstmt.setString(3, "NORMAL");
			pstmt.setInt(4, 1);
			pstmt.setInt(5, 1);
			pstmt.addBatch();
			
			// 건의사항(QNA) > 누구나 읽고 쓰기 가능
			pstmt.setString(1, "qna");
			pstmt.setString(2, "건의사항");
			pstmt.setString(3, "QNA");
			pstmt.setInt(4, 1);
			pstmt.setInt(5, 1);
			pstmt.addBatch();
			
			int[] result = pstmt.executeBatch();
			System.out.println("기본 게시판 데이터 적용 완료. (적용된 데이터 수 : " + result.length + ")");
		} catch (SQLException e) {
			System.err.println("기본 데이터 삽입 실패 : " + e.getMessage());
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

	// 실행
	public static void main(String[] args) {
		TableBoards setup = new TableBoards();
		setup.createBoardsTable();
	}
}
