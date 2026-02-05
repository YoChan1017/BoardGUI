package gui.details;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import gui.LoginGUI;
import gui.MainGUI;
import session.UserSession;

public class BoardManageGUI extends JFrame implements ActionListener {
	
	
	// 게시판 목록 나열 > 게시판 이름, 게시판 타입, 게시판 코드, 읽기 권한, 작성 권한, 활성화 상태 표시
	
	// 게시판을 선택하여 수정, 활성화 여부 변경, 삭제
	// 게시판 생성
	
	// 필드
	private JButton btnmain, btnuser, btnlogout, btnexit;
	
	
	// 생성자
	public BoardManageGUI() {
		setTitle("게시판 관리");
		setDefaultCloseOperation(EXIT_ON_CLOSE);		
		setLayout(new BorderLayout());
		setSize(800, 600);
		
		// 현재 GUI화면 진입 시 로그인 체크 여부 + 관리자만 접속 가능하게 추가
		if (!UserSession.getInstance().isLoggedIn()) {
			// 현재 생성자를 종료 후 로그인 화면으로 이동
			JOptionPane.showMessageDialog(this, "로그인을 먼저 해주세요.", "접근 제한", JOptionPane.WARNING_MESSAGE);		
			dispose();
			(new LoginGUI()).setVisible(true);
			return;
		}
		String role = UserSession.getInstance().getUser().getRole();
		if (!"admin".equalsIgnoreCase(role)) {
			JOptionPane.showMessageDialog(this, "관리자만 접근할 수 있습니다.", "접근 권한 없음", JOptionPane.ERROR_MESSAGE);
			dispose();
			(new MainGUI()).setVisible(true);
			return;
		}
		
		JPanel topPanel = new JPanel();
		JPanel centerPanel = new JPanel(new BorderLayout());
		JPanel bottomPanel = new JPanel();
		
		// 상단
		JLabel lblTitle = new JLabel("게시판 목록");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		topPanel.add(lblTitle);
		
		// 중앙
		
		
		// 하단
		btnmain = new JButton("HOME");
		btnmain.addActionListener(this);
		btnuser = new JButton("작성글 보기");
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
	
	
	
	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == btnmain) {
			// 로그인 세션 보유한채로 메인화면 이동
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
