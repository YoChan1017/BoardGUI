package dbms;

import java.sql.Date;
import java.sql.Timestamp;

public class TableUsersDTO {
	
	private int userId;
	private String username;
	private String password;
	private String nickname;
	private Date birthDate;
	private String phone;
	private String email;
	private String role;
	private boolean isActive;
	private Timestamp createdAt;
	
	// 기본
	public TableUsersDTO() {}
	
	// 회원가입용
	public TableUsersDTO(String username, String password, String nickname, Date birthDate, String phone, String email) {
		this.username = username;
		this.password = password;
		this.nickname = nickname;
		this.birthDate = birthDate;
		this.phone = phone;
		this.email = email;
	}
	
	// 데이터 조회용
	public TableUsersDTO(int userId, String username, String password, String nickname,
			Date birthDate, String phone, String email, String role, boolean isActive, Timestamp createdAt) {
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.nickname = nickname;
		this.birthDate = birthDate;
		this.phone = phone;
		this.email = email;
		this.role = role;
		this.isActive = isActive;
		this.createdAt = createdAt;
	}
	
	// Getters and Setters
	public int getUserId() { return userId; }
	public void setUserId(int userId) { this.userId = userId; }
	public String getUsername() { return username; }
	public void setUsername(String username) { this.username = username; }
	// 마저 작성..
	
	@Override
	public String toString() {
		return "UserDTO [userId=" + userId + ", username=" + username + ", nickname=" + nickname + "]";
	}
}
