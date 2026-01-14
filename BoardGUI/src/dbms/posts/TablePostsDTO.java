package dbms.posts;

import java.sql.Timestamp;

public class TablePostsDTO {

	private int postId;
	private int boardId;
	private int userId;
	private String title;
	private String content;
	private int viewCount;
	private Timestamp createdAt;
	private Timestamp updatedAt;
	private boolean isNotice;
	private boolean isSecret;
	private String status;
	
	// 기본 생성자
	public TablePostsDTO() {}
	
	// 게시글 생성용
	public TablePostsDTO(int boardId, int userId, String title, String content, boolean isNotice, boolean isSecret) {
		this.boardId = boardId;
		this.userId = userId;
		this.title = title;
		this.content = content;
		this.isNotice = isNotice;
		this.isSecret = isSecret;
	}
	
	// 데이터 조회용
	public TablePostsDTO(int postId, int boardId, int userId, String title, String content, int viewCount, Timestamp createdAt,
			Timestamp updatedAt, boolean isNotice, boolean isSecret, String status) {
		this.postId = postId;
		this.boardId = boardId;
		this.userId = userId;
		this.title = title;
		this.content = content;
		this.viewCount = viewCount;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.isNotice = isNotice;
		this.isSecret = isSecret;
		this.status = status;
	}
	
	// Getters and Setters
	// Getters > 값을 가져오는 메서드 get변수()
	// Setters > 값을 설정하는 메서드 set변수(값)
	public int getPostId() { return postId; }
	public void setPostId(int postId) { this.postId = postId; }
	
	public int getBoardId() { return boardId; }
	public void getBoardId(int boardId) { this.boardId = boardId; }
	
	public int getUserId() { return userId; }
	public void getUserId(int userId) { this.userId = userId; }
	
	public String getTitle() { return title; }
	public void getTitle(String title) { this.title = title; }
	
	public String getContent() { return content; }
	public void getContent(String content) { this.content = content; }
	
	public int getViewCount() { return viewCount; }
	public void getViewCount(int viewCount) { this.viewCount = viewCount; }
	
	public Timestamp getCreatedAt() { return createdAt; }
	public void getCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
	
	public Timestamp getUpdatedAt() { return updatedAt; }
	public void getUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
	
	public boolean getisNotice() { return isNotice; }
	public void getisNotice(boolean isNotice) { this.isNotice = isNotice; }
	
	public boolean getisSecret() { return isSecret; }
	public void getisSecret(boolean isSecret) { this.isSecret = isSecret; }
	
	public String getStatus() { return status; }
	public void getStatus(String status) { this.status = status; }
	
	@Override
	public String toString() {
		return "TablePostsDTO [postId=" + postId + ", title=" + title + ", user=" +userId + "]";
	}
}
