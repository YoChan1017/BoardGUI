package gui.details;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import dbms.boards.TableBoardsDAO;
import dbms.boards.TableBoardsDTO;
import dbms.users.TableUsersDAO;
import dbms.users.TableUsersDTO;
import gui.LoginGUI;
import gui.MainGUI;
import session.UserSession;

public class UserManageGUI extends JFrame implements ActionListener {
	
	// 회원 정보 확인 및 편집, 활성화 여부, 삭제
	// 비밀번호는 재설정으로 덮어쓰기
	// 회원 검색 추가

	// 필드
	private DefaultTableModel tableModel;
	private JTable userTable;
	private JButton btnmain, btnuser, btnlogout, btnexit;
	
	
	// 생성자
	public UserManageGUI() {
		setTitle("회원 관리");
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
		JLabel lblTitle = new JLabel("회원 목록");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		topPanel.add(lblTitle);
		
		// 중앙
		centerPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
		// 테이블 컬럼
		String[] columnNames = {"No", "회원 ID", "회원 PW", "닉네임", "생년월일", "전화번호", "이메일", "회원 권한", "활성화 여부", "가입 날짜"};
		tableModel = new DefaultTableModel(columnNames, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		userTable = new JTable(tableModel);
		userTable.setRowHeight(25);
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		// 번호, ID, PW, 닉네임, 생일, 전화번호, 이메일, 권한, 활성화 여부, 가입날짜
		userTable.getColumn("No").setPreferredWidth(50);
		userTable.getColumn("No").setCellRenderer(centerRenderer);
		userTable.getColumn("회원 ID").setPreferredWidth(100);
		userTable.getColumn("회원 ID").setCellRenderer(centerRenderer);
		userTable.getColumn("회원 PW").setPreferredWidth(80);
		userTable.getColumn("회원 PW").setCellRenderer(centerRenderer);
		userTable.getColumn("닉네임").setPreferredWidth(100);
		userTable.getColumn("닉네임").setCellRenderer(centerRenderer);
		userTable.getColumn("생년월일").setPreferredWidth(90);
		userTable.getColumn("생년월일").setCellRenderer(centerRenderer);
		userTable.getColumn("전화번호").setPreferredWidth(120);
		userTable.getColumn("전화번호").setCellRenderer(centerRenderer);
		userTable.getColumn("이메일").setPreferredWidth(180);
		userTable.getColumn("이메일").setCellRenderer(centerRenderer);
		userTable.getColumn("회원 권한").setPreferredWidth(60);
		userTable.getColumn("회원 권한").setCellRenderer(centerRenderer);
		userTable.getColumn("활성화 여부").setPreferredWidth(80);
		userTable.getColumn("활성화 여부").setCellRenderer(centerRenderer);
		userTable.getColumn("가입 날짜").setPreferredWidth(150);
		userTable.getColumn("가입 날짜").setCellRenderer(centerRenderer);
		// 스크롤 추가
		JScrollPane scrollPane = new JScrollPane(userTable);
		scrollPane.getViewport().setBackground(Color.WHITE);
		scrollPane.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
		// centerPanel에 추가
		centerPanel.add(scrollPane, BorderLayout.CENTER);
		// 데이터 표시
		loadUserList();
		
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
	// 회원 목록 불러오기
	private void loadUserList() {
		tableModel.setRowCount(0);
		TableUsersDAO dao = new TableUsersDAO();
		List<TableUsersDTO> list = dao.getAllUsers();
		
		if (list != null) {
			for (TableUsersDTO u : list) {
				
				String maskedPw = "*********";
				String roleRaw = u.getRole();
				String roleDisplay = roleRaw;
				if (roleRaw != null) {
					switch (roleRaw.toLowerCase()) {
					case "admin": roleDisplay = "관리자"; break;
					case "manager": roleDisplay = "매니저"; break;
					case "user": roleDisplay = "일반"; break;
					}
				}
				String status = u.isActive() ? "활성화" : "비활성화";
				
				Object[] rowData = {
						u.getUserId(),
						u.getUsername(),
						maskedPw,
						u.getNickname(),
						u.getBirthDate(),
						u.getPhone(),
						u.getEmail(),
						roleDisplay,
						u.getCreatedAt(),
						status
				};
				tableModel.addRow(rowData);
			}
		}
	}
	
	
	@Override
	public void actionPerformed(ActionEvent event ) {
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
