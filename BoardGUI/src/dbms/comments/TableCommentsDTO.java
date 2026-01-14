package dbms.comments;

import java.sql.Timestamp;

public class TableCommentsDTO {
	
	private int commentId;
	private int postId;
	private int userId;
	private String content;
	private boolean isSecret;
	private boolean isDeleted;
	private Timestamp createdAt;
	
	// 기본 생성자
	public TableCommentsDTO() {}
	
	// 댓글 생성용
	public TableCommentsDTO(int postId, int userId, String content, boolean isSecret) {
		this.postId = postId;
		this.userId = userId;
		this.content = content;
		this.isSecret = isSecret;
	}
		
	// 데이터 조회용
	public TableCommentsDTO(int commentId, int postId, int userId, String content, boolean isSecret, boolean isDeleted, Timestamp createdAt) {
		this.commentId = commentId;
		this.postId = postId;
		this.userId = userId;
		this.content = content;
		this.isSecret = isSecret;
		this.isDeleted = isDeleted;
		this.createdAt = createdAt;
	}
		
	// Getters and Setters
	// Getters > 값을 가져오는 메서드 get변수()
	// Setters > 값을 설정하는 메서드 set변수(값)
	public int getCommentId() { return commentId; }
	public void setCommentId(int commentId) { this.commentId = commentId; }
	
	public int getPostId() { return postId; }
	public void setPostId(int postId) { this.postId = postId; }
	
	public int getUserId() { return userId; }
	public void setUserId(int userId) { this.userId = userId; }
	
	public String getContent() { return content; }
	public void setContent(String content) { this.content = content; }
	
	public boolean isSecret() { return isSecret; }
	public void setSecret(boolean isSecret) { this.isSecret = isSecret; }
	
	public boolean isDeleted() { return isDeleted; }
	public void setDeleted(boolean isDeleted) { this.isDeleted = isDeleted; }
	
	public Timestamp getCreatedAt() { return createdAt; }
	public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
	
	@Override
	public String toString() {
		return "TableCommentsDTO [commentId=" + commentId + ", postId=" + postId + ", userId=" + userId + "]";
	}
}
