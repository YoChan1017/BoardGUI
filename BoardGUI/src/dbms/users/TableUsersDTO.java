package dbms.users;

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
	
	// 기본 생성자
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
	// Getters > 값을 가져오는 메서드 get변수()
	// Setters > 값을 설정하는 메서드 set변수(값)
	public int getUserId() { return userId; }
	public void setUserId(int userId) { this.userId = userId; }
	
	public String getUsername() { return username; }
	public void setUsername(String username) { this.username = username; }
	
	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }
	
	public String getNickname() { return nickname; }
	public void setNickname(String nickname) { this.nickname = nickname; }
	
	public Date getBirthDate() { return birthDate; }
	public void setBirthDate(Date birthDate) { this.birthDate = birthDate; }
	
	public String getPhone() { return phone; }
	public void setPhone(String phone) { this.phone = phone; }
	
	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }
	
	public String getRole() { return role; }
	public void setRole(String role) { this.role = role; }
	
	public boolean isActive() { return isActive; }
	public void setActive(boolean isActive) { this.isActive = isActive; }
	
	public Timestamp getCreatedAt() { return createdAt; }
	public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
	
	@Override
	public String toString() {
		return "UserDTO [userId=" + userId + ", username=" + username + ", nickname=" + nickname + "]";
	}
}
