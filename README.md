# 📋 BoardGUI — Java Swing 기반 게시판 시스템

> Java Swing과 MariaDB를 활용하여 제작한 데스크탑 게시판 애플리케이션입니다.  
> 회원 관리, 다중 게시판, 게시글 · 댓글 · 첨부파일 기능을 포함하며,  
> 역할 기반 권한(admin / manager / user) 시스템을 직접 설계하고 구현하였습니다.

---

## 🛠️ 기술 스택

| 구분 | 사용 기술 |
|------|-----------|
| Language | Java |
| UI | Java Swing |
| Database | MariaDB 10.6 |
| JDBC Driver | mariadb-java-client 3.5.7 |
| IDE | Eclipse |
| 패턴 | DAO / DTO |

---

## 📁 프로젝트 구조

```
BoardGUI/
├── src/
│   ├── dbms/                    # DB 연결 및 테이블별 DAO/DTO
│   │   ├── DBcon.java           # DB 커넥션 관리
│   │   ├── users/               # 유저 테이블 (DAO, DTO)
│   │   ├── boards/              # 게시판 테이블 (DAO, DTO)
│   │   ├── posts/               # 게시글 테이블 (DAO, DTO)
│   │   ├── comments/            # 댓글 테이블 (DAO, DTO)
│   │   └── attachments/         # 첨부파일 테이블 (DAO, DTO)
│   ├── gui/                     # 화면(View) 구성
│   │   ├── LoginGUI.java        # 로그인 화면
│   │   ├── RegisterGUI.java     # 회원가입 화면
│   │   ├── MainGUI.java         # 메인 화면
│   │   ├── board/               # 게시판 · 게시글 화면
│   │   │   ├── BoardGUI.java
│   │   │   └── posts/
│   │   │       ├── PostViewGUI.java
│   │   │       ├── PostWriteGUI.java
│   │   │       └── PostEditGUI.java
│   │   └── details/             # 회원 정보 · 관리 화면
│   │       ├── DetailsGUI.java
│   │       ├── DetailsEditGUI.java
│   │       ├── DetailsPostsViewGUI.java
│   │       └── UserManageGUI.java
│   ├── session/
│   │   └── UserSession.java     # 로그인 세션 싱글톤 관리
│   └── utils/
│       ├── InputLimit.java      # 입력값 길이 제어 유틸
│       └── StorageSetup.java    # 첨부파일 저장 경로 설정
└── misc/
    └── memo.txt                 # 설계 메모
```

---

## 💾 데이터베이스 설계

MariaDB를 사용하며, 프로그램 시작 시 DB와 테이블을 자동으로 확인하고 없을 경우 생성합니다.

**테이블 생성 순서:** `users → boards → posts → comments → attachments`

### 테이블 목록

| 테이블 | 설명 |
|--------|------|
| `users` | 회원 정보 (아이디, 비밀번호 해싱, 닉네임, 생년월일, 전화번호, 이메일, 권한, 활성 여부) |
| `boards` | 게시판 설정 (코드, 이름, 타입, 읽기/쓰기 권한) |
| `posts` | 게시글 (제목, 내용, 조회수, 공지 고정, 비밀글, 처리 상태) |
| `comments` | 댓글 (게시글 종속, 비밀댓글, 논리 삭제 지원) |
| `attachments` | 첨부파일 (원본명/저장명 분리, UUID로 중복 방지, 경로 관리) |

### 권한 체계

```
admin   (9) — 최고 관리자 : 모든 기능 + 회원 관리 + 게시판 관리
manager (5) — 부운영자   : 게시판 관리 기능 일부
user    (1) — 일반 회원  : 기본 읽기/쓰기
```

---

## ✨ 주요 기능

### 👤 회원 관리
- 회원가입 · 로그인 · 로그아웃
- 비밀번호 SHA 해싱 처리
- 회원 정보 수정 (닉네임, 생년월일, 전화번호, 이메일)
- 비밀번호 변경 (현재 비밀번호 확인 후 변경)
- 회원 탈퇴 (2단계 확인 → 비활성화 처리)
- 싱글톤 패턴으로 로그인 세션 관리

### 📌 게시판
- 다중 게시판 지원 (공지사항 / 자유게시판 / QnA 등)
- 게시판별 읽기 · 쓰기 권한 설정
- 게시판 활성 / 비활성 전환
- 공지 고정 · 비밀글 기능

### 📝 게시글
- 게시글 작성 · 수정 · 삭제
- 조회수 자동 증가
- 파일 첨부 (최대 30MB, UUID로 저장명 중복 방지)
- 첨부파일 다운로드

### 💬 댓글
- 댓글 작성 · 삭제
- 비밀댓글 기능 (작성자 · 게시글 작성자 · 관리자만 열람)
- 논리 삭제 (`is_deleted`) 처리

### 🔧 관리자 기능 (admin 전용)
- 회원 목록 조회 및 검색
- 회원 정보 수정 · 비밀번호 초기화 · 권한 변경
- 계정 활성 / 비활성 전환
- 게시판 추가 · 수정 · 관리

---

## 🚀 실행 방법

### 사전 요구사항
- Java JDK 8 이상
- MariaDB 10.6 이상
- mariadb-java-client-3.5.7.jar (ClassPath에 추가 필요)

### DB 설정
1. MariaDB 서버를 실행합니다. (`services.msc` → MariaDB 서비스 시작)
2. `boarddb` 전용 계정을 생성합니다.
3. `StorageSetup.java`에서 첨부파일 저장 경로를 환경에 맞게 수정합니다.
   - Windows: 로컬 경로
   - Linux 서버 배포 시: 서버 경로로 변경

### 실행
Eclipse 또는 IDE에서 `main` 클래스를 실행하면  
DB 연결 확인 → 테이블 자동 생성 → 로그인 화면 진입 순으로 시작됩니다.

---

## 📐 설계 포인트

- **DAO / DTO 패턴 적용** — DB 접근 로직(DAO)과 데이터 전달 객체(DTO)를 명확히 분리하여 유지보수성 향상
- **싱글톤 세션 관리** — `UserSession.getInstance()`로 어느 화면에서든 로그인 상태 일관성 유지
- **입력값 유효성 검사** — `InputLimit` 유틸 클래스로 최소 · 최대 길이 제어를 공통화
- **파일명 UUID 처리** — 동일한 파일명 업로드 시 서버 충돌 방지
- **논리 삭제** — 댓글 삭제 시 DB에서 즉시 제거하지 않고 `is_deleted` 플래그로 화면 표시 제어

---

## 📌 개선 예정 사항

- [ ] 게시글 검색 기능 추가
- [ ] 댓글 수정 기능 추가
- [ ] 내가 작성한 글 목록 페이지 연결
- [ ] 관리자 회원 추가 기능 구현
- [ ] 페이지네이션 적용
- [ ] 리눅스 서버 배포 및 네트워크 다중 접속 지원

---

> 본 프로젝트는 Java 데스크탑 애플리케이션 개발 학습을 목적으로 제작되었습니다.
