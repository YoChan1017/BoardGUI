package dbms;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// TableUsersDTO, DBcon 메서드 사용
// > import dbms.DBcon;
// > import dbms.TableUsersDTO;

public class TableUsersDAO {

	// CRUD Query 작성
	// 회원 등록 		INSERT
	// 회원 조회 		Read
	// 회원 수정		Update
	// 회원 탈퇴 		DELETE
	// 회원 비활성화
	// 로그인 인증
	// 중복 확인 (ID, Nickname, Email)
	
	// 회원 등록 (INSERT)
	public int insertUser(TableUsersDTO user) {
		int result = 0;
		String sql = "INSERT INTO users (username, password, nickname, birth_date, phone, email)" + "VALUES (?, ?, ?, ?, ?, ?)";
		try (Connection conn = DBcon.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, user.getUsername());
			pstmt.setString(2, user.getPassword());
			pstmt.setString(3, user.getNickname());
			pstmt.setDate(4, user.getBirthDate());
			pstmt.setString(5, user.getPhone());
			pstmt.setString(6, user.getEmail());
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("회원가입 실패 : " + e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	// 회원 조회 (READ)
	public TableUsersDTO getUserByUsername(String username) {
		TableUsersDTO user = null;
		String sql = "SELECT * FROM users WHERE username = ?";
		try (Connection conn = DBcon.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, username);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					user = new TableUsersDTO(
							rs.getInt("user_id"),
							rs.getString("username"),
							rs.getString("password"),
							rs.getString("nickname"),
							rs.getDate("birth_date"),
							rs.getString("phone"),
							rs.getString("email"),
							rs.getString("role"),
							rs.getBoolean("is_active"),
							rs.getTimestamp("created_at")
							);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}
	
	// 회원 수정 (UPDATE)
	public int updateUser(TableUsersDTO user) {
		int result = 0;
		String sql = "UPDATE users SET password = ?, nickname = ?, phone = ?, email = ? WHERE username = ?";
		try (Connection conn = DBcon.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, user.getPassword());
			pstmt.setString(2, user.getNickname());
			pstmt.setString(3, user.getPhone());
			pstmt.setString(4, user.getEmail());
			pstmt.setString(5, user.getUsername());
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("회원정보 수정 실패 : " + e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	// 회원 탈퇴 (DELETE)
	public int deleteUser() {
		
	}
	
	
	
	// 로그인 인증
	public boolean login(String username, String inputPassword) {
		boolean isValid = false;
		String sql = "SELECT password FROM users WHERE username = ? AND is_active = TRUE";
		try (Connection conn = DBcon.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, username);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					String dbHashedPassword = rs.getString("password");
					String inputHashedPassword = hashPassword(inputPassword);
					if (inputHashedPassword != null && inputHashedPassword.equals(dbHashedPassword)) {
						isValid = true;
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isValid;
	}
	
	// 회원 비활성화
	public int deactivateUser( ) {
		
	}
	
	// ID 중복 확인
	public boolean isIdDuplicate(String username) {
		String sql = "SELECT 1 FROM users WHERE LOWER(username) = LOWER(?)";
		try (Connection conn = DBcon.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, username);
			try (ResultSet rs = pstmt.executeQuery()) {
				return rs.next();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	// Nickname 중복 확인
	public boolean isNicknameDuplicate(String nickname) {
		String sql = "SELECT 1 FROM users WHERE LOWER(nickname) = LOWER(?)";
		try (Connection conn = DBcon.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, nickname);
			try (ResultSet rs = pstmt.executeQuery()) {
				return rs.next();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	// Email 중복 확인
	public boolean isEmailDuplicate(String email) {
		String sql = "SELECT 1 FROM users WHERE LOWER(email) = LOWER(?)";
		try (Connection conn = DBcon.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, email);
			try (ResultSet rs = pstmt.executeQuery()) {
				return rs.next();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	// 비밀번호 암호화 메서드
	private String hashPassword(String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(password.getBytes());
			byte[] byteData = md.digest();
			StringBuilder sb = new StringBuilder();
			for (byte b : byteData) {
				sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// 필요 시 추가
}
