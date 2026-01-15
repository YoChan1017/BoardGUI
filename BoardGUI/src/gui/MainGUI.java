package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import dbms.boards.TableBoardsDAO;
import dbms.boards.TableBoardsDTO;
import gui.board.boards.BoardGUI;
import session.UserSession;

public class MainGUI extends JFrame implements ActionListener{
	
	private JButton btnmain, btnuser, btnlogout, btnexit;
	
	public MainGUI() {
		setTitle("MAIN");
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
		
		JPanel topPanel = new JPanel();
		JPanel centerPanel = new JPanel();
		JPanel bottomPanel = new JPanel();
		
		// topPanel에 게시판 종류 버튼(추가될 때마다 표시해함(공지사항, 건의사항, 자유게시판, 이후 추가될때마다 표시))
		// 눌렀을시 BoardGUI화면으로 이동되며 해당 게시판의 정보 표시
		TableBoardsDAO boardDao = new TableBoardsDAO();
		List<TableBoardsDTO> boardList = boardDao.getAllBoards();
		if (boardList != null) {
			for (TableBoardsDTO board : boardList) {
				if (board.isActive()) { // 활성화된 게시판만 버튼 표시
					JButton btnBoard = new JButton(board.getName());
					btnBoard.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							setVisible(false);
							new BoardGUI(board).setVisible(true);
						}
					});
					topPanel.add(btnBoard);
				}
			}
		}
		
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
		// 생성자가 정상적으로 실행하여 띄었을 경우에만 실행
		MainGUI mainGui = new MainGUI();
		if (mainGui.isDisplayable()) {
			mainGui.setVisible(true);
		}
	}
}
