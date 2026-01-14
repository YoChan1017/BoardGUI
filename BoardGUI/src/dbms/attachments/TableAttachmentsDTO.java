package dbms.attachments;

import java.sql.Timestamp;

public class TableAttachmentsDTO {
	
	private int fileId;
	private int postId;
	private String originName;
	private String saveName;
	private String savePath;
	private long fileSize;
	private String ext;
	private Timestamp createdAt;
	
	// 기본 생성자
	public TableAttachmentsDTO() {}
	
	// 첨부파일 생성용
	public TableAttachmentsDTO(int postId, String originName, String saveName, String savePath, long fileSize, String ext) {
		this.postId = postId;
		this.originName = originName;
		this.saveName = saveName;
		this.savePath = savePath;
		this.fileSize = fileSize;
		this.ext = ext;
	}
			
	// 데이터 조회용
	public TableAttachmentsDTO(int fileId, int postId, String originName, String saveName, String savePath, long fileSize, String ext, Timestamp createdAt) {
		this.fileId = fileId;
		this.postId = postId;
		this.originName = originName;
		this.saveName = saveName;
		this.savePath = savePath;
		this.fileSize = fileSize;
		this.ext = ext;
		this.createdAt = createdAt;
	}
			
	// Getters and Setters
	// Getters > 값을 가져오는 메서드 get변수()
	// Setters > 값을 설정하는 메서드 set변수(값)
	public int getFiledId() { return fileId; }
	public void setFileId(int fileId) { this.fileId = fileId; }
	
	public 
	
	@Override
	public String toString() {
		return "TableAttachmentsDTO [fileId=" + fileId + ", originName=" + originName + "]";
	}
}
