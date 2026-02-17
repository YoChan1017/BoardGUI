package gui.details;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
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
import utils.InputLimit;

public class UserManageGUI extends JFrame implements ActionListener {
	
	// 회원 정보 확인 및 편집, 활성화 여부, 삭제
	// 비밀번호는 재설정으로 덮어쓰기
	// 회원 검색 추가

	// 필드
	private JComboBox<String> cbSearchType;
	private JTextField txtSearch;
	private DefaultTableModel tableModel;
	private JTable userTable;
	private JButton btnmain, btnuser, btnlogout, btnexit, btnUsearch, btnUcreate;
	
	
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
		JPanel functionPanel = new JPanel(new BorderLayout());
		// 중앙 - 좌측 상단 - 회원 검색
		JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		String[] searchOptions = {"닉네임", "전화번호"};
		cbSearchType = new JComboBox<>(searchOptions);
		txtSearch = new JTextField(15);
		btnUsearch = new JButton("검색");
		btnUsearch.addActionListener(this);
		searchPanel.add(cbSearchType);
		searchPanel.add(txtSearch);
		searchPanel.add(btnUsearch);
		// 중앙 - 우측 상단 - 회원 추가
		JPanel createPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		btnUcreate = new JButton("회원 추가");
		btnUcreate.addActionListener(this);
		createPanel.add(btnUcreate);
		functionPanel.add(searchPanel, BorderLayout.WEST);
		functionPanel.add(createPanel, BorderLayout.EAST);
		
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
		// 테이블 클릭 이벤트
		addTableSelectionListener();
		// 스크롤 추가
		JScrollPane scrollPane = new JScrollPane(userTable);
		scrollPane.getViewport().setBackground(Color.WHITE);
		scrollPane.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
		// centerPanel에 추가
		centerPanel.add(functionPanel, BorderLayout.NORTH);
		centerPanel.add(scrollPane, BorderLayout.CENTER);
		// 데이터 표시
		loadUserList();
		
		// 하단
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
						status,
						u.getCreatedAt()
				};
				tableModel.addRow(rowData);
			}
		}
	}
	
	// 테이블 클릭 이벤트
	private void addTableSelectionListener() {
		userTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int row = userTable.getSelectedRow();
					if (row != -1) {
						int userId = (int) userTable.getValueAt(row, 0);
						processTableSelection(userId);
					}
				}
			}
		});
	}
	
	// 테이블 선택 처리
	private void processTableSelection(int userId) {
		TableUsersDAO dao = new TableUsersDAO();
		TableUsersDTO user = dao.getUserById(userId);
		if (user != null) {
			showEditUserDialog(user);
		} else {
			JOptionPane.showMessageDialog(this, "회원 정보를 불러올 수 없습니다.", "정보 오류", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	// 회원 추가
	private void showCreateUserDialog() {
		
	}
	
	// 회원 수정
	private void showEditUserDialog(TableUsersDTO user) {
		
		JDialog dialog = new JDialog(this, "회원정보 수정 - 관리자", true);
		dialog.setSize(450, 520);
		dialog.setLayout(new BorderLayout());
		dialog.setLocationRelativeTo(this);
		
		JPanel inputPanel = new JPanel(new GridLayout(9, 2, 10, 10));
		inputPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
		
		// 아이디 - 수정불가
		JTextField tfId = new JTextField(user.getUsername());
		tfId.setEditable(false);
		// 비밀번호
		JButton btnResetPw = new JButton("비밀번호 초기화");
		btnResetPw.addActionListener(e -> resetPassword(user.getUserId())); // 비밀번호 초기화 버튼 이벤트 메서드로 분리
		// 회원 정보
		JTextField tfNick = new JTextField(user.getNickname());
		JTextField tfPhone = new JTextField(user.getPhone());
		JTextField tfEmail = new JTextField(user.getEmail());
		// 입력길이 제한
		InputLimit.checkMaxLength(tfNick, 10);
		InputLimit.checkMaxLength(tfPhone, 11);
		InputLimit.checkMaxLength(tfEmail, 30);
		// 생년월일
		JPanel birthPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		JComboBox<String> cbYear = new JComboBox<>();
		JComboBox<String> cbMonth = new JComboBox<>();
		JComboBox<String> cbDay = new JComboBox<>();
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		for(int i = currentYear; i >= 1900; i--) cbYear.addItem(String.valueOf(i));
		for(int i = 1; i <= 12; i++) cbMonth.addItem(String.format("%02d", i));
		for(int i = 1; i <= 31; i++) cbDay.addItem(String.format("%02d", i));
		if (user.getBirthDate() != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(user.getBirthDate());
			cbYear.setSelectedItem(String.valueOf(cal.get(Calendar.YEAR)));
			cbMonth.setSelectedItem(String.format("%02d", cal.get(Calendar.MONTH) + 1));
			cbDay.setSelectedItem(String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)));
		}
		birthPanel.add(cbYear);
		birthPanel.add(new JLabel("년 "));
		birthPanel.add(cbMonth);
		birthPanel.add(new JLabel("월 "));
		birthPanel.add(cbDay);
		birthPanel.add(new JLabel("일"));
		// 권한
		String[] roles = {"일반", "매니저", "관리자"};
		JComboBox<String> cbRole = new JComboBox<>(roles);
		String currentRole = user.getRole();
		if ("admin".equalsIgnoreCase(currentRole)) cbRole.setSelectedItem("관리자");
		else if ("manager".equalsIgnoreCase(currentRole)) cbRole.setSelectedItem("매니저");
		else cbRole.setSelectedItem("일반");
		// 활성화 여부
		JCheckBox chkActive = new JCheckBox("계정 활성화", user.isActive());
		
		inputPanel.add(new JLabel("아이디 : ")); inputPanel.add(tfId);
		inputPanel.add(new JLabel("비밀번호 : ")); inputPanel.add(btnResetPw);
		inputPanel.add(new JLabel("닉네임 : ")); inputPanel.add(tfNick);
		inputPanel.add(new JLabel("생년월일 : ")); inputPanel.add(birthPanel);
		inputPanel.add(new JLabel("전화번호 : ")); inputPanel.add(tfPhone);
		inputPanel.add(new JLabel("이메일 : ")); inputPanel.add(tfEmail);
		inputPanel.add(new JLabel("회원 권한 : ")); inputPanel.add(cbRole);
		inputPanel.add(new JLabel("활성화 여부 : ")); inputPanel.add(chkActive);
		inputPanel.add(new JLabel("가입날짜 : ")); inputPanel.add(new JLabel(user.getCreatedAt().toString()));
		
		JPanel btnPanel = new JPanel();
		JButton btnBupdate = new JButton("수정");
		JButton btnBcancel = new JButton("취소");
		
		// 수정 버튼 이벤트
		btnBupdate.addActionListener(e -> {
			
		});
		btnBcancel.addActionListener(e -> dialog.dispose());
		
		btnPanel.add(btnBupdate);
		btnPanel.add(btnBcancel);
		
		dialog.add(inputPanel, BorderLayout.CENTER);
		dialog.add(btnPanel, BorderLayout.SOUTH);
		
		dialog.setVisible(true);
	}
	
	// 비밀번호 초기화
	private void resetPassword(int userId) {
		String newPw = JOptionPane.showInputDialog(this, "새로운 비밀번호를 입력하세요 : ", "비밀번호 초기화", JOptionPane.QUESTION_MESSAGE);
		if (newPw != null && !newPw.trim().isEmpty()) {
			if (newPw.length() < 10) {
				JOptionPane.showMessageDialog(this, "비밀번호는 10자리 이상이어야 합니다.");
				return;
			}
			
			TableUsersDAO dao = new TableUsersDAO();
			String hashedPw = hashPassword(newPw);
			int result = dao.updatePassword(userId, hashedPw);
			
			if (result > 0) {
				JOptionPane.showMessageDialog(this, "비밀번호가 변경되었습니다.");
				loadUserList();
			} else {
				JOptionPane.showMessageDialog(this, "비밀번호 변경에 실패하였습니다.");
			}
		}
	}
	
	// 비밀번호 암호화 메서드
	private String hashPassword(String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(password.getBytes());
			byte[] byteData = md.digest();
			StringBuilder sb = new StringBuilder();
			for (byte b : byteData) {
				sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// 회원 검색
	private void searchUseres() {
		
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
			
		} else if(event.getSource() == btnUsearch) {
			// 검색 기능 메서드 추가
			searchUseres();
			
		} else if(event.getSource() == btnUcreate) {
			// 회원 추가 메서드 추가
			showCreateUserDialog();
			
		}
	}
	
	public static void main(String[] args) {
		
	}
}
