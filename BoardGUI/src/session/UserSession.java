package session;

import dbms.TableUsersDTO;

public class UserSession {
	
	private static UserSession instance;
	private TableUsersDTO user;
	private UserSession() {}
	
	// 인스턴스 접근용
	public static UserSession getInstance() {
		if (instance == null) {
			instance = new UserSession();
		}
		return instance;
	}
	
	// 로그인 처리
	public void login(TableUsersDTO user) {
		this.user = user;
	}
	// 로그아웃 처리
	public void logout() {
		this.user = null;
	}
	// 현재상태 확인
	public boolean isLoggedIn() {
		return user != null;
	}
	// 저장된 사용자 정보 가져오기
	public TableUsersDTO getUser() {
		return user;
	}
}
