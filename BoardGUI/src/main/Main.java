package main;

import javax.swing.SwingUtilities;

import dbms.StorageSetup;
import dbms.attachments.TableAttachments;
import dbms.boards.TableBoards;
import dbms.comments.TableComments;
import dbms.posts.TablePosts;
import dbms.users.TableUsers;
import gui.LoginGUI;

public class Main {

	// 메인 프로그램 실행
	
	// > 실행 시 데이터베이스 확인
	// DB(boarddb) - TABLE(users, boards, posts, comments, attachments)
	// TableUsers, TableBoards, TablePosts, TableComments, TableAttachments 클래스 파일 실행
	
	// > 첨부파일 저장 경로 확인 
	// StorageSetup 클래스 파일 실행
	
	// > GUI 첫 화면 실행
	// LoginGUI 클래스 파일부터 시작
	
	// 메서드
	// 테이블 생성 및 확인
	private static void checkAndCreateTables() {
		System.out.println("테이블 생성 및 확인 시작...");
		
		try {
			System.out.println(" - users 테이블 확인 중... ");
			new TableUsers().createUsersTable();
			System.out.println(" - boards 테이블 확인 중... ");
			new TableBoards().createBoardsTable();
			System.out.println(" - posts 테이블 확인 중... ");
			new TablePosts().createPostsTable();
			System.out.println(" - comments 테이블 확인 중... ");
			new TableComments().createCommentsTalbe();
			System.out.println(" - attachments 테이블 확인 중... ");
			new TableAttachments().createAttachmentsTable();
		} catch (Exception e) {
			System.err.println("\n[ERROR] 테이블 초기화 중 오류 발생.");
			System.err.println("DB가 생성되어 있는지 확인해주세요.");
			e.printStackTrace();
		}
	}
	
	// 첨부파일 저장소 생성 및 확인
	private static void checkAndCreateStorage() {
		System.out.println("첨부 파일 저장소 확인...");
		new StorageSetup().createStorageDirectory();
	}
	
	public static void main(String[] args) {
		System.out.println("====== [프로그램 초기 설정 중...] ======");
		checkAndCreateTables();
		checkAndCreateStorage();
		System.out.println("====== [프로그램 실행] ======");
		SwingUtilities.invokeLater(() -> {
			new LoginGUI().setVisible(true);
		});
	}
}
