package dbms.posts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import dbms.DBcon;

public class TablePostsDAO {

	// CRUD Query 작성
	// 조회수 증가, 특정 게시판의 게시글 목록 조회
		
	// 게시글 생성(INSERT)
	public int insertPost(TablePostsDTO post) {
		int result = 0;
		String sql = "INSERT INTO posts (board_id, user_id, title, content, is_notice, is_secret) VALUES (?, ?, ?, ?, ?, ?)";
		try (Connection conn = DBcon.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1,  post.getBoardId());
			pstmt.setInt(2, post.getUserId());
			pstmt.setString(3, post.getTitle());
			pstmt.setString(4, post.getContent());
			pstmt.setBoolean(5, post.getisNotice());
			pstmt.setBoolean(6, post.getisSecret());
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("게시글 작성 실패 : " + e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	// 게시글 상세 조회(SELECT)
	public TablePostsDTO getPostById(int postId) {
		TablePostsDTO post = null;
		String sql = "SELECT * FROM posts WHERE post_id = ?";
		try (Connection conn = DBcon.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, postId);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					post = new TablePostsDTO(
							rs.getInt("post_id"),
							rs.getInt("board_id"),
							rs.getInt("user_id"),
							rs.getString("title"),
							rs.getString("content"),
							rs.getInt("view_count"),
							rs.getTimestamp("created_at"),
							rs.getTimestamp("updated_at"),
							rs.getBoolean("is_notice"),
							rs.getBoolean("is_secret"),
							rs.getString("status")
							);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return post;
	}
	
	// 게시글 정보 수정(UPDATE)
	public int updatePost(TablePostsDTO post) {
		int result = 0;
		String sql = "UPDATE posts SET title = ?, content = ?, is_notice = ?, is_secret = ?, updated_at = CURRENT_TIMESTAMP WHERE post_id = ?";
		try (Connection conn = DBcon.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1,  post.getTitle());
			pstmt.setString(2, post.getContent());
			pstmt.setBoolean(3, post.getisNotice());
			pstmt.setBoolean(4, post.getisSecret());
			pstmt.setInt(5, post.getPostId());
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("게시글 수정 실패 : " + e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	// 게시글 삭제(DELETE)
	public int deletePost(int postId) {
		int result = 0;
		String sql = "DELETE FROM posts WHERE post_id = ?";
		try (Connection conn = DBcon.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, postId);
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("게시글 삭제 실패 : " + e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	// 조회수 증가
	public void increaseViewCount(int postId) {
		String sql = "UPDATE posts SET view_count = view_count + 1 WHERE post_id = ?";
		try (Connection conn = DBcon.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, postId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// 특정 게시판의 게시글 목록 조회
	public ArrayList<TablePostsDTO> getPostsByBoardId(int boardId) {
		ArrayList<TablePostsDTO> list = new ArrayList<>();
		String sql = "SELECT * FROM posts WHERE board_id = ? ORDER BY is_notice DESC, created_at DESC";
		try (Connection conn = DBcon.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, boardId);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					TablePostsDTO post = new TablePostsDTO(
							rs.getInt("post_id"),
							rs.getInt("board_id"),
							rs.getInt("user_id"),
							rs.getString("title"),
							rs.getString("content"),
							rs.getInt("view_count"),
							rs.getTimestamp("created_at"),
							rs.getTimestamp("updated_at"),
							rs.getBoolean("is_notice"),
							rs.getBoolean("is_secret"),
							rs.getString("status")
							);
					list.add(post);
				}
			}
		} catch (SQLException e) {
			System.out.println("게시글 목록 조회 실패 : " + e.getMessage());
			e.printStackTrace();
		}
		return list;
	}
	
	// 필요시 추가작성
}
