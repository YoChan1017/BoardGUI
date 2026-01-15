package gui.board.boards;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import dbms.boards.TableBoardsDTO;
import gui.DetailsGUI;
import gui.LoginGUI;
import gui.MainGUI;
import session.UserSession;

public class BoardGUI extends JFrame implements ActionListener {

	// 다중 게시판 GUI
	// 게시판 정보에 따라 동적 표시
	// 상단에 게시판 이름 표시
	// 중단에 게시글 목록 표시, 글쓰기 버튼 생성, 검색란 생성
	// 하단에 메인화면, 내 정보, 로그아웃, 종료 버튼 생성
	// 작성된 게시글이 없을 경우 '게시글 목록이 없습니다' 표시
	
	// 필드
	private TableBoardsDTO currentBoard;						// 선댁된 게시판 정보
	private JButton btnmain, btnuser, btnlogout, btnexit;		// 메인화면, 내 정보, 로그아웃, 종료 버튼
	private JLabel lblBoardName;
	
	// 생성자
	public BoardGUI(TableBoardsDTO boardInfo) {
		this.currentBoard = boardInfo;
		
		setTitle(boardInfo.getName());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(800, 600);
		
		// 현재 GUI화면 진입 시 로그인 체크 여부
		if (!UserSession.getInstance().isLoggedIn()) {
			// 현재 생성자를 종료 후 로그인 화면으로 이동
			JOptionPane.showMessageDialog(this, "로그인을 먼저 해주세요.", "접근 제한", JOptionPane.WARNING_MESSAGE);	
			dispose();
			(new LoginGUI()).setVisible(true);
			return;
		}
		
		JPanel topPanel = new JPanel();
		JPanel centerPanel = new JPanel();
		JPanel bottomPanel = new JPanel();
		
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
	
	
	// 버튼 이벤트
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
