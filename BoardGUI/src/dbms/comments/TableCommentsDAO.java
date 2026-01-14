package dbms.comments;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import dbms.DBcon;

public class TableCommentsDAO {

	// CRUD Query 작성		
	// 특정 게시글의 댓글 목록 조회
			
	// 댓글 작성(INSERT)
	public int insertComment(TableCommentsDTO comment) {
		int result = 0;
		String sql = "INSERT INT comments (post_id, user_id, content, is_secret) VALUES (?, ?, ?, ?)";
		try (Connection conn = DBcon.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, comment.getPostId());
			pstmt.setInt(2, comment.getUserId());
			pstmt.setString(3, comment.getContent());
			pstmt.setBoolean(4, comment.isSecret());
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("댓글 작성 실패 : " + e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
		
	// 댓글 상세 조회(SELECT)
	public TableCommentsDTO getCommentById(int commentId) {
		TableCommentsDTO comment = null;
		String sql = "SELECT * FROM comments WHERE comment_id = ?";
		try (Connection conn = DBcon.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, commentId);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					comment = new TableCommentsDTO(
							rs.getInt("comment_id"),
							rs.getInt("post_id"),
							rs.getInt("user_id"),
							rs.getString("content"),
							rs.getBoolean("is_secret"),
							rs.getBoolean("is_deleted"),
							rs.getTimestamp("created_at")
							);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return comment;
	}
		
	// 댓글 수정(UPDATE)
	public int updateComment(TableCommentsDTO comment) {
		int result = 0;
		String sql = "UPDATE comments SET content = ?, is_secret = ? WHERE comment_id = ?";
		try (Connection conn = DBcon.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1,  comment.getContent());
			pstmt.setBoolean(2, comment.isSecret());
			pstmt.setInt(3, comment.getCommentId());
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("댓글 실패 : " + e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
		
	// 댓글 삭제(DELETE) > 논리적 삭제
	public int deleteComment(int commentId) {
		int result = 0;
		String sql = "UPDATE comments SET is_deleted = TRUE WHERE comment_id = ?";
		try (Connection conn = DBcon.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, commentId);
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("댓글 삭제 실패 : " + e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	// 특정 게시글의 댓글 목록 조회
	public ArrayList<TableCommentsDTO> getCommentsByPostId(int postId) {
		ArrayList<TableCommentsDTO> list = new ArrayList<>();
		String sql = "SELECT * FROM comments WHERE post_id = ? ORDER BY created_at ASC";
		try (Connection conn = DBcon.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, postId);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					TableCommentsDTO comment = new TableCommentsDTO(
							rs.getInt("comment_id"),
							rs.getInt("post_id"),
							rs.getInt("user_id"),
							rs.getString("content"),
							rs.getBoolean("is_secret"),
							rs.getBoolean("is_deleted"),
							rs.getTimestamp("created_at")
							);
					list.add(comment);
				}
			}
		} catch (SQLException e) {
			System.out.println("댓글 목록 조회 실패 : " + e.getMessage());
			e.printStackTrace();
		}
		return list;
	}
	
	// 필요시 추가
	
}
