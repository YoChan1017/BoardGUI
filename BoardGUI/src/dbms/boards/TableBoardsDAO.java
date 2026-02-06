package dbms.boards;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dbms.DBcon;

public class TableBoardsDAO {

	// CRUD Query 작성
	// 유틸 : 중복 확인 (필요한 쿼리문 있을시 추가 작성)
	
	// 게시판 생성(INSERT)
	public int insertBoard(TableBoardsDTO board) {
		int result = 0;
		String sql = "INSERT INTO boards (code, name, type, read_role, write_role, is_active) VALUES (?, ?, ?, ?, ?, ?)";
		try (Connection conn = DBcon.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1,  board.getCode());
			pstmt.setString(2, board.getName());
			pstmt.setString(3, board.getType());
			pstmt.setInt(4, board.getReadRole());
			pstmt.setInt(5, board.getWriteRole());
			pstmt.setBoolean(6, board.isActive());
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("게시판 생성 실패 : " + e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	// 게시판 목록 조회(SELECT)
	public List<TableBoardsDTO> getAllBoards() {
		ArrayList<TableBoardsDTO> list = new ArrayList<>();
		String sql = "SELECT * FROM boards ORDER BY board_id ASC";
		try (Connection conn = DBcon.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
			while (rs.next()) {
				TableBoardsDTO board = new TableBoardsDTO(
						rs.getInt("board_id"),
						rs.getString("code"),
						rs.getString("name"),
						rs.getString("type"),
						rs.getInt("read_role"),
						rs.getInt("write_role"),
						rs.getBoolean("is_active")
						);
				list.add(board);
			}
		} catch (SQLException e) {
			System.out.println("게시판 목록 조회 실패");
			e.printStackTrace();
		}
		return list;
	}
	
	// 게시판 정보 수정(UPDATE)
	public int updateBoard(TableBoardsDTO board) {
		int result = 0;
		String sql = "UPDATE boards SET name = ?, type = ?, read_role = ?, write_role = ?, is_active = ? WHERE code = ?";
		try (Connection conn = DBcon.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, board.getName());
			pstmt.setString(2, board.getType());
			pstmt.setInt(3, board.getReadRole());
			pstmt.setInt(4, board.getWriteRole());
			pstmt.setBoolean(5, board.isActive());
			pstmt.setString(6, board.getCode());
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("게시판 수정 실패 : " + e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	// 게시판 삭제(DELETE)
	public int deleteBoard(String code) {
		int result = 0;
		String sql = "DELETE FROM boards WHERE code = ?";
		try (Connection conn = DBcon.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1,  code);
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("게시판 삭제 실패 : " + e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	// 게시판 중복확인 - 코드(code)
	public boolean isCodeDuplicate(String code) {
		String sql = "SELECT 1 FROM boards WHERE code = ?";
		try (Connection conn = DBcon.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, code);
			try (ResultSet rs = pstmt.executeQuery()) {
				return rs.next();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	// 게시판 중복확인 - 이름(name)
	public boolean isNameDuplicate(String name) {
		String sql = "SELECT 1 FROM boards WHERE name = ?";
		try (Connection conn = DBcon.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, name);
			try (ResultSet rs = pstmt.executeQuery()) {
				return rs.next();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	// board_id로 게시판 조회
	public TableBoardsDTO getBoardById(int boardId) {
		TableBoardsDTO board = null;
		String sql = "SELECT * FROM boards WHERE board_id = ?";
		try (Connection conn = DBcon.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, boardId);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					board = new TableBoardsDTO(
							rs.getInt("board_id"),
							rs.getString("code"),
							rs.getString("name"),
							rs.getString("type"),
							rs.getInt("read_role"),
							rs.getInt("write_role"),
							rs.getBoolean("is_active")
							);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return board;
	}
	
	// 필요시 추가
}
