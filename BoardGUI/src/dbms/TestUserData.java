package dbms;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import dbms.users.TableUsersDTO;

public class TestUserData {

	public int insertUserTest(TableUsersDTO user) {
		
		int result = 0;
		String sql = "INSERT INTO users (username, password, nickname, birth_date, phone, email, role)" + "VALUES (?, ?, ?, ?, ?, ?, ?)";
		
		try (Connection conn = DBcon.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			
			pstmt.setString(1, user.getUsername());
			
			String rawPassword = user.getPassword();
			String encryptedPassword = hashPassword(rawPassword);
			pstmt.setString(2, encryptedPassword);
			
			pstmt.setString(3, user.getNickname());
			pstmt.setDate(4, user.getBirthDate());
			pstmt.setString(5, user.getPhone());
			pstmt.setString(6, user.getEmail());
			pstmt.setString(7, user.getRole());
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("회원추가 실패 : " + e.getMessage());
			e.printStackTrace();
			
		}
		return result;
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

	public static void main(String[] args) {
		
		TestUserData dao = new TestUserData();
		
		// ADMIN DATA
		TableUsersDTO admin = new TableUsersDTO();
		admin.setUsername("ADMIN");
		admin.setPassword("1q2w3e");
		admin.setNickname("관리자");
		admin.setBirthDate(Date.valueOf("2000-01-01"));
		admin.setPhone("01000000000");
		admin.setEmail("admin@test.com");
		admin.setRole("admin");
		
		// MANAGER DATA
		TableUsersDTO manager = new TableUsersDTO();
		manager.setUsername("MANAGER01");
		manager.setPassword("qwer123");
		manager.setNickname("매니저_01");
		manager.setBirthDate(Date.valueOf("2000-01-01"));
		manager.setPhone("01000000000");
		manager.setEmail("manager01@test.com");
		manager.setRole("manager");
		
		// USER DATA
		TableUsersDTO user = new TableUsersDTO();
		user.setUsername("USER01");
		user.setPassword("12345678");
		user.setNickname("일반회원_01");
		user.setBirthDate(Date.valueOf("2000-01-01"));
		user.setPhone("01000000000");
		user.setEmail("user01@test.com");
		user.setRole("user");
		
		int resultA = dao.insertUserTest(admin);
		System.out.println("A 결과 : " + (resultA > 0 ? "성공" : "실패"));
		
		int resultM = dao.insertUserTest(manager);
		System.out.println("M 결과 : " + (resultM > 0 ? "성공" : "실패"));
		
		int resultU = dao.insertUserTest(user);
		System.out.println("U 결과 : " + (resultU > 0 ? "성공" : "실패"));
	}
}
