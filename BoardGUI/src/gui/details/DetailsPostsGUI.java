package gui.details;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import dbms.boards.TableBoardsDTO;
import dbms.users.TableUsersRole;
import gui.LoginGUI;
import gui.MainGUI;
import gui.board.posts.PostViewGUI;
import session.UserSession;

public class DetailsPostsGUI extends JFrame implements ActionListener {

	// 필드
	private TableBoardsDTO currentBoard;
	private JButton btnmain, btnuser, btnlogout, btnexit, btnsearch, btndelete;
	private JLabel lblBoardName;
	private JComboBox<String> cbSearchType;
	private JTextField txtSearch;
	private DefaultTableModel tableModel;
	private JTable postTable;
	
	
	// 생성자
	public void DetailsPostGUI(TableBoardsDTO boardInfo) {
		this.currentBoard = boardInfo;
		
		setTitle("내가 작성한 글");
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
		
		// 상단(topPanel)
		lblBoardName = new JLabel(boardInfo.getName());
		lblBoardName.setHorizontalAlignment(SwingConstants.CENTER);
		topPanel.add(lblBoardName);
		
		// 중앙(centerPanel)
		JPanel functionPanel = new JPanel(new BorderLayout());
		JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		String[] searchTypes = {"제목", "내용", "작성자"};
		cbSearchType = new JComboBox<>(searchTypes);
		txtSearch = new JTextField(20);
		btnsearch = new JButton("검색");
		searchPanel.add(cbSearchType);
		searchPanel.add(txtSearch);
		searchPanel.add(btnsearch);
		// 삭제 버튼
		JPanel deletePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		btndelete = new JButton("글 삭제");
		btndelete.addActionListener(this);
		// 글 삭제 버튼 권한 확인
		if (UserSession.getInstance().isLoggedIn()) {
			String roleStr = UserSession.getInstance().getUser().getRole();
			TableUsersRole userRole = TableUsersRole.fromDbRole(roleStr);
			if (userRole.getLevel() < boardInfo.getWriteRole()) { // 게시판, 유저 권한 비교
				btndelete.setEnabled(false);
				btndelete.setToolTipText("글 삭제 권한이 없습니다.");
			}
		}
		deletePanel.add(btndelete);
		// functionPanel > 글 삭제, 글작성 버튼
		functionPanel.add(searchPanel, BorderLayout.WEST);
		functionPanel.add(deletePanel, BorderLayout.EAST);
		
		// 중앙(centerPanel) - 게시글 목록 Panel
		JPanel listContainerPanel = new JPanel(new BorderLayout());
		listContainerPanel.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
		// 목록 Header
		String[] columnNames = {"번호", "제목", "작성자", "작성일", "조회수"};
		tableModel = new DefaultTableModel(columnNames, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		// 목록 List
		postTable = new JTable(tableModel);
		postTable.setRowHeight(25);
		postTable.getColumn("번호").setPreferredWidth(50);
		postTable.getColumn("제목").setPreferredWidth(300);
		postTable.getColumn("작성자").setPreferredWidth(100);
		postTable.getColumn("작성일").setPreferredWidth(150);
		postTable.getColumn("조회수").setPreferredWidth(50);
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		postTable.getColumn("번호").setCellRenderer(centerRenderer);
		postTable.getColumn("작성자").setCellRenderer(centerRenderer);
		postTable.getColumn("작성일").setCellRenderer(centerRenderer);
		postTable.getColumn("조회수").setCellRenderer(centerRenderer);
		
		postTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1) { // 클릭 횟수
					int row = postTable.getSelectedRow();
					if (row != -1) {
						int postId = (int) postTable.getValueAt(row, 0);
						
						setVisible(false);
						new PostViewGUI(currentBoard, postId);
					}
				}
			}
		});
		
		JScrollPane scrollPane = new JScrollPane(postTable);
		scrollPane.getViewport().setBackground(Color.WHITE);
		listContainerPanel.add(scrollPane, BorderLayout.CENTER);
		
		centerPanel.add(functionPanel, BorderLayout.NORTH); 		// centerPanel에서 상단(북쪽)에 functionPanel 배치
		centerPanel.add(new JPanel(), BorderLayout.CENTER);
		centerPanel.add(listContainerPanel, BorderLayout.CENTER); 	// centerPanel에서 중앙에 listContainerPanel 배치
		loadPostList();	// 데이터 불러오기
		
		
		// 하단(bottomPanel)
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
				
		// 상단, 중단, 하단 Panel 배치
		add(topPanel, BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);
		
		setLocationRelativeTo(null);
	}
	
	
	// 메서드
	// 게시글 목록 불러오기
	private void loadPostList() {
		// 내가 작성한 게시글 목록만 표시
		
	}
	
	// 게시글 삭제
	private void deletePost() {
		// 게시글 목록 중 선택하여 삭제
		
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
			
		} else if(event.getSource() == btnsearch) {
			// 검색 버튼 기능 추가
			
		} else if(event.getSource() == btndelete) {
			// 글 삭제 기능 추가
			
		}
	}

	public static void main(String[] args) {
		
	}
}
