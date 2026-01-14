package dbms.boards;

public class TableBoardsDTO {
	
	private int boardId;
	private String code;
	private String name;
	private String type;
	private int readRole;
	private int writeRole;
	private boolean isActive;
	
	// 기본 생성자
	public TableBoardsDTO() {}
	
	// 게시판 생성용
	public TableBoardsDTO(String code, String name, String type, int readRole, int writeRole, boolean isActive) {
		this.code = code;
		this.name = name;
		this.type = type;
		this.readRole = readRole;
		this.writeRole = writeRole;
		this.isActive = isActive;
	}
	
	// 데이터 조회용
	public TableBoardsDTO(int boardId, String code, String name, String type, int readRole, int writeRole, boolean isActive) {
		this.boardId = boardId;
		this.code = code;
		this.name = name;
		this.type = type;
		this.readRole = readRole;
		this.writeRole = writeRole;
		this.isActive = isActive;
	}
	
	// Getters and Setters
	// Getters > 값을 가져오는 메서드 get변수()
	// Setters > 값을 설정하는 메서드 set변수(값)
	public int getBoardId() { return boardId; }
	public void setBoardId(int boardId) { this.boardId = boardId; }
	
	public String getCode() { return code; }
	public void setCode(String code) { this.code = code; }
	
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	
	public String getType() { return type; }
	public void setType(String type) { this.type = type; }
	
	public int getReadRole() { return readRole; }
	public void setReadRole(int readRole) { this.readRole = readRole; }
	
	public int getWriteRole() { return writeRole; }
	public void setWriteRole(int writeRole) { this.writeRole = writeRole; }
	
	public boolean isActive() { return isActive; }
	public void setActive(boolean isActive) { this.isActive = isActive; }
	
	@Override
	public String toString() {
		return "TableBoardsDTO [boardId=" + boardId + ", code=" + code + ", name=" + name + ", type=" + type + "]";
	}
}
