package dbms.users;

// Enum(Enumeration) 정해진 값들 중에서만 선택하도록 강제하는 목록
public enum TableUsersRole {
	// 권한 정의
	ADMIN("admin", 9),
	MANAGER("manager", 5),
	USER("user", 1);
	
	private final String dbRole;	// DB에 저장되는 문자열(VARCHAR)
	private final int level;		// 권한 비교를 위한 숫자(INT)
	
	// 생성자
	TableUsersRole(String dbRole, int level) {
		this.dbRole = dbRole;
		this.level = level;
	}
	
	// Getter
	public String getDbRole() {
		return dbRole;
	}
	public int getLevel() {
		return level;
	}
	
	// DB에서 가져온 String 값을 Enum으로 변환하는 메서드
	public static TableUsersRole fromDbRole(String dbRole) {
		if (dbRole == null) {
			return USER;
		}
		for (TableUsersRole role :values()) {
			if (role.dbRole.equalsIgnoreCase(dbRole)) {
				return role;
			}
		}
		return USER;
	}
	
	public boolean isAtLeast(TableUsersRole requiredRole) {
		return this.level >= requiredRole.getLevel();
	}
}
