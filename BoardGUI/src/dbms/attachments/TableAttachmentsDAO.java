package dbms.attachments;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import dbms.DBcon;

public class TableAttachmentsDAO {

	// CRUD Query 작성
	// 특정 게시글 첨부파일 목록 조회

	// 복사/붙여넣기용
	// (Connection conn = DBcon.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql))
	// System.out.println("게시글 작성 실패 : " + e.getMessage());	
				
	// 첨부파일 생성(INSERT)
	public int insertAttachment(TableAttachmentsDTO attachment) {
		int result = 0;
		String sql = "INSERT INTO attachments (post_id, origin_name, save_name, save_path, file_size, ext) VALUES (?, ?, ?, ?, ?, ?)";
		try (Connection conn = DBcon.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, attachment.getPostId());
			pstmt.setString(2, attachment.getOriginName());
			pstmt.setString(3, attachment.getSaveName());
			pstmt.setString(4, attachment.getSavePath());
			pstmt.setLong(5, attachment.getFileSize());
			pstmt.setString(6, attachment.getExt());
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("첨부파일 저장 실패 : " + e.getMessage());	
			e.printStackTrace();
		}
		return result;
	}
			
	// 첨부파일 상세 조회(SELECT)
	public TableAttachmentsDTO getAttachmentById(int fileId) {
		TableAttachmentsDTO file = null;
		String sql = "SELECT * FROM attachments WHERE file_id = ?";
		try (Connection conn = DBcon.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, fileId);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					file = new TableAttachmentsDTO(
							rs.getInt("file_id"),
							rs.getInt("post_id"),
							rs.getString("origin_name"),
							rs.getString("save_name"),
							rs.getString("save_path"),
							rs.getLong("file_size"),
							rs.getString("ext"),
							rs.getTimestamp("created_at")
							);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return file;
	}
			
	// 첨부파일 정보 수정(UPDATE)
	// 첨부파일 정보 수정은 일반적으로 하지 않는다하여 미작성 > 삭제 후 다시 생성	
			
	// 첨부파일 삭제(DELETE)
	public int deleteAttachment(int fileId) {
		int result = 0;
		String sql = "DELETE FROM attachments WHERE file_id = ?";
		try (Connection conn = DBcon.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, fileId);
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("첨부파일 삭제 실패 : " + e.getMessage());	
			e.printStackTrace();
		}
		return result;
	}
	
	// 특정 게시글 첨부파일 목록 조회
	public ArrayList<TableAttachmentsDTO> getAttachmentsByPostId(int postId) {
		ArrayList<TableAttachmentsDTO> list = new ArrayList<>();
		String sql = "SELECT * FROM attachments WHERE post_id = ? ORDER BY file_id ASC";
		try (Connection conn = DBcon.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, postId);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					TableAttachmentsDTO file = new TableAttachmentsDTO(
							rs.getInt("file_id"),
							rs.getInt("post_id"),
							rs.getString("origin_name"),
							rs.getString("save_name"),
							rs.getString("save_path"),
							rs.getLong("file_size"),
							rs.getString("ext"),
							rs.getTimestamp("created_at")
							);
					list.add(file);
				}
			}
		} catch (SQLException e) {
			System.out.println("첨부파일 목록 조회 실패 : " + e.getMessage());	
			e.printStackTrace();
		}
		return list;
	}
}
