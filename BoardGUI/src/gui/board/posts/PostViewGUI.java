package gui.board.posts;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import dbms.boards.TableBoardsDTO;
import dbms.posts.TablePostsDAO;
import dbms.posts.TablePostsDTO;
import dbms.users.TableUsersRole;
import gui.DetailsGUI;
import gui.LoginGUI;
import gui.MainGUI;
import gui.board.boards.BoardGUI;
import session.UserSession;

public class PostViewGUI extends JFrame implements ActionListener {
	
	// 필드
	private TableBoardsDTO currentBoard;
	private TablePostsDTO currentPost;
	private int postId;
	private JButton btnmain, btnuser, btnlogout, btnexit;
	
	// 생성자
	public PostViewGUI(TableBoardsDTO board, int postId) {
		this.currentBoard = board;
		this.postId = postId;
		
		setTitle(currentBoard.getName() + " - 글 상세보기");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		setSize(800, 600);
		
		// 현재 GUI화면 진입 시 로그인 체크 여부
		if (!UserSession.getInstance().isLoggedIn()) {
			// 현재 생성자를 종료 후 로그인 화면으로 이동
			JOptionPane.showMessageDialog(this, "로그인을 먼저 해주세요.", "접근 제한", JOptionPane.WARNING_MESSAGE);	
			dispose();
			(new LoginGUI()).setVisible(true);
			return;
		}
		
		// 데이터 load
		loadPostData();
		
		// 비밀글 체크
		if (currentPost != null && currentPost.isSecret() ) {
			int currentUserId = UserSession.getInstance().getUser().getUserId();
			String roleStr = UserSession.getInstance().getUser().getRole();
			TableUsersRole userRole = TableUsersRole.fromDbRole(roleStr);
			if (currentPost.getUserId() != currentUserId && userRole != TableUsersRole.ADMIN) {
				JOptionPane.showMessageDialog(this, "비밀글은 작성자와 관리자만 볼 수 있습니다.", "열람 불가", JOptionPane.WARNING_MESSAGE);
				dispose();
				(new BoardGUI(currentBoard)).setVisible(true);
				return;
			}
		}
		
		// 글 존재 여부
		if (currentPost == null) {
			JOptionPane.showMessageDialog(this,  "삭제되거나 존재하지 않는 게시글입니다.", "열람 오류", JOptionPane.ERROR_MESSAGE);
			dispose();
			(new BoardGUI(currentBoard)).setVisible(true);
			return;
		}
				
		JPanel topPanel = new JPanel();
		JPanel centerPanel = new JPanel();
		JPanel bottomPanel = new JPanel();
		
		// centerPanel
		// 선택한 글 세부내용 표시
		// 댓글 기능 추가
		
				
		// bottomPanel
		btnmain = new JButton("HOME");
		btnmain.addActionListener(this);
		btnuser = new JButton("내 정보");
		btnuser.addActionListener(this);
		btnlogout = new JButton("로그아웃");
		btnlogout.addActionListener(this);
		btnexit = new JButton("종료");
		btnexit.addActionListener(this);
				
		bottomPanel.add(btnmain);
		bottomPanel.add(btnuser);
		bottomPanel.add(btnlogout);
		bottomPanel.add(btnexit);
				
		add(topPanel, BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);
		setLocationRelativeTo(null);
	}
	
	// 메서드
	private void loadPostData() {
		TablePostsDAO dao = new TablePostsDAO();
		dao.increaseViewCount(postId); 			// 조회수 증가
		currentPost = dao.getPostById(postId);	// 게시글 정보 가져오기
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == btnmain) {
			// 로그인 세션 보유한채로 새로고침
			setVisible(false);
			(new MainGUI()).setVisible(true);		
			
		} else if(event.getSource() == btnuser) {
			// 로그인 세션 보유한채로 내 정보화면으로 이동
			setVisible(false);					
			(new DetailsGUI()).setVisible(true);
			
		} else if(event.getSource() == btnlogout) {
			// 세션 제거 추가 - 로그아웃 처리
			UserSession.getInstance().logout();
			JOptionPane.showMessageDialog(this, "로그아웃 되었습니다.");
			// 로그아웃 후 로그인으로 다시 이동
			setVisible(false);						
			(new LoginGUI()).setVisible(true);
			
		} else if(event.getSource() == btnexit) {
			// 세션 제거 추가 - 로그아웃 처리
			// 프로그램 종료로 세션 자동 소멸
			System.exit(0);
		}
	}

	public static void main(String[] args) {
		
	}
}
