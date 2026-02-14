package gui.details;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import dbms.boards.TableBoardsDAO;
import dbms.boards.TableBoardsDTO;
import gui.LoginGUI;
import gui.MainGUI;
import session.UserSession;
import utils.InputLimit;

public class BoardManageGUI extends JFrame implements ActionListener {
	
	
	// 게시판 목록 나열 > 게시판 이름, 게시판 타입, 게시판 코드, 읽기 권한, 작성 권한, 활성화 상태 표시
	// 게시판 생성 > code는 소문자, type은 대문자로만 생성, 게시판 이름 길이 제한, 권한부여 
	// 게시판 수정 > 이름, 타입, 코드, 권한, 활성화 여부 변경
	// 게시판 검색 추가
	
	// 필드
	private JComboBox<String> cbSearchType;
	private JTextField txtSearch;
	private DefaultTableModel tableModel;
	private JTable boardTable;
	private JButton btnmain, btnuser, btnlogout, btnexit, btnBsearch, btnBcreate;
	
	
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
		
		JPanel topPanel = new JPanel(new BorderLayout());
		JPanel centerPanel = new JPanel(new BorderLayout());
		JPanel bottomPanel = new JPanel();
		
		// 상단	
		// 상단 - 중앙 - 타이틀
		JLabel lblTitle = new JLabel("게시판 목록");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		// 상단 Panel 배치
		topPanel.add(lblTitle, BorderLayout.CENTER);
		
		
		// 중앙
		JPanel functionPanel = new JPanel(new BorderLayout());
		// 중앙 - 좌측 상단 - 검색
		JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		String[] searchOptions = {"게시판 이름", "코드"};
		cbSearchType = new JComboBox<>(searchOptions);
		txtSearch = new JTextField(15);
		btnBsearch = new JButton("검색");
		btnBsearch.addActionListener(this);
		searchPanel.add(cbSearchType);
		searchPanel.add(txtSearch);
		searchPanel.add(btnBsearch);
		// 중앙 - 우측 상단 - 게시판 생성
		JPanel createPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		btnBcreate = new JButton("게시판 생성");
		btnBcreate.addActionListener(this);
		createPanel.add(btnBcreate);
		functionPanel.add(searchPanel, BorderLayout.WEST);
		functionPanel.add(createPanel, BorderLayout.EAST);
		
		centerPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
		// 테이블 컬럼
		String[] columnNames = {"No", "게시판 이름", "게시판 타입", "게시판 코드", "읽기 권한", "작성 권한", "활성화 여부"};
		tableModel = new DefaultTableModel(columnNames, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		boardTable = new JTable(tableModel);
		boardTable.setRowHeight(25);
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		// 번호, 이름, 타입, 코드, 권한, 활성화 여부
		boardTable.getColumn("No").setPreferredWidth(40);
		boardTable.getColumn("No").setCellRenderer(centerRenderer);
		boardTable.getColumn("게시판 이름").setPreferredWidth(150);
		boardTable.getColumn("게시판 이름").setCellRenderer(centerRenderer);
		boardTable.getColumn("게시판 타입").setPreferredWidth(80);
		boardTable.getColumn("게시판 타입").setCellRenderer(centerRenderer);
		boardTable.getColumn("게시판 코드").setPreferredWidth(80);
		boardTable.getColumn("게시판 코드").setCellRenderer(centerRenderer);
		boardTable.getColumn("읽기 권한").setPreferredWidth(60);
		boardTable.getColumn("읽기 권한").setCellRenderer(centerRenderer);
		boardTable.getColumn("작성 권한").setPreferredWidth(60);
		boardTable.getColumn("작성 권한").setCellRenderer(centerRenderer);
		boardTable.getColumn("활성화 여부").setPreferredWidth(70);
		boardTable.getColumn("활성화 여부").setCellRenderer(centerRenderer);
		// 이벤트 메서드 호출
		addTableSelectionListener();
		// 스크롤 추가
		JScrollPane scrollPane = new JScrollPane(boardTable);
		scrollPane.getViewport().setBackground(Color.WHITE);
		scrollPane.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
		// centerPanel에 추가
		centerPanel.add(functionPanel, BorderLayout.NORTH);
		centerPanel.add(scrollPane, BorderLayout.CENTER);
		
		// 데이터 표시
		loadBoardList();
		
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
	// 게시판 목록 불러오기
	private void loadBoardList() {
		tableModel.setRowCount(0);
		TableBoardsDAO dao = new TableBoardsDAO();
		List<TableBoardsDTO> list = dao.getAllBoards();
		
		if (list != null) {
			for (TableBoardsDTO b : list) {
				String status = b.isActive() ? "활성화" : "비활성화";
				
				Object[] rowData = {
						b.getBoardId(),
						b.getName(),
						b.getType(),
						b.getCode(),
						b.getReadRole(),
						b.getWriteRole(),
						status
				};
				tableModel.addRow(rowData);
			}
		}
	}
	
	// 테이블 클릭 이벤트
	private void addTableSelectionListener() {
		boardTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int row = boardTable.getSelectedRow();
					if (row != -1) {
						processTableSelection(row);
					}
				}
			}
		});
	}
	
	// 테이블 선택 처리
	private void processTableSelection(int row) {
		int id = (int) boardTable.getValueAt(row, 0);
		String name = (String) boardTable.getValueAt(row, 1);
		String type = (String) boardTable.getValueAt(row, 2);
		String code = (String) boardTable.getValueAt(row, 3);
		int readRole = (int) boardTable.getValueAt(row, 4);
		int writeRole = (int) boardTable.getValueAt(row, 5);
		String status = (String) boardTable.getValueAt(row, 6);
		boolean isActive = "활성화".equals(status);
		
		TableBoardsDTO board = new TableBoardsDTO(id, code, name, type, readRole, writeRole, isActive);
		showEditBoardDialog(board);
	}
	
	// 게시판 생성
	private void showCreateBoardDialog() {
		
		JDialog dialog = new JDialog(this, "게시판 생성", true);
		dialog.setSize(350, 400);
		dialog.setLayout(new BorderLayout());
		dialog.setLocationRelativeTo(this);
		
		JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));
		inputPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
		
		// 게시판 설정
		JTextField tfName = new JTextField();
		JTextField tfCode = new JTextField();
		JTextField tfType = new JTextField();
		JSpinner spinRead = new JSpinner(new SpinnerNumberModel(1, 1, 9, 1));
		JSpinner spinWrite = new JSpinner(new SpinnerNumberModel(1, 1, 9, 1));
		// 입력 길이 제한
		InputLimit.checkMaxLength(tfName, 15);
		InputLimit.checkMaxLength(tfCode, 15);
		InputLimit.checkMaxLength(tfType, 20);
		
		inputPanel.add(new JLabel("게시판 이름 : "));
		inputPanel.add(tfName);
		inputPanel.add(new JLabel("게시판 코드 : "));
		inputPanel.add(tfCode);
		inputPanel.add(new JLabel("게시판 타입 : "));
		inputPanel.add(tfType);
		inputPanel.add(new JLabel("읽기 권한 레벨 : "));
		inputPanel.add(spinRead);
		inputPanel.add(new JLabel("쓰기 권한 레벨 : "));
		inputPanel.add(spinWrite);
		
		// 설명 표시
		JLabel lblInfo = new JLabel("<html><font color='gray' size='2'><br>* 코드와 타입은 영문으로 작성바랍니다.<br>* 코드는 소문자, 타입은 대문자로 변환됩니다.<br>* 권한 레벨은 1 ~ 9로 설정해주세요.</font></html>");
		lblInfo.setHorizontalAlignment(SwingConstants.CENTER);
		lblInfo.setBorder(new EmptyBorder(0, 0, 10, 0));
		
		JPanel btnPanel = new JPanel();
		JButton btnSave = new JButton("생성");
		JButton btnCancel = new JButton("취소");
		
		// 버튼 이벤트
		btnSave.addActionListener(e -> {
			String name = tfName.getText().trim();
			String code = tfCode.getText().trim();
			String type = tfType.getText().trim();
			code = code.toLowerCase();
			type = type.toUpperCase();
			int readRole = (int) spinRead.getValue();
			int writeRole = (int) spinWrite.getValue();
			
			// 유효성 검사
			if (name.isEmpty() || code.isEmpty() || type.isEmpty()) { // 모든 칸 입력
				JOptionPane.showMessageDialog(dialog, "모든 정보를 입력해주세요.", "입력 오류", JOptionPane.WARNING_MESSAGE);
				return;
			}
			if (!code.matches("^[a-zA-Z0-9]*$") || !type.matches("^[a-zA-Z0-9]*$")) { // 영문 확인
				JOptionPane.showMessageDialog(dialog, "코드와 타입은 영문 또는 숫자만 입력 가능합니다.", "입력 오류", JOptionPane.WARNING_MESSAGE);
			}
			TableBoardsDAO dao = new TableBoardsDAO();
			if (dao.isNameDuplicate(name, 0)) { // 게시판 이름 중복 확인
				JOptionPane.showMessageDialog(dialog, "이미 존재하는 게시판 이름입니다.", "중복 오류", JOptionPane.WARNING_MESSAGE);
				return;
			}
			if (dao.isCodeDuplicate(code, 0)) { // 게시판 코드 중복 확인
				JOptionPane.showMessageDialog(dialog, "이미 존재하는 게시판 코드입니다.", "중복 오류", JOptionPane.WARNING_MESSAGE);
				return;
			}
			try { // 권한 레벨(숫자) 확인
				spinRead.commitEdit();
				spinWrite.commitEdit();
			} catch (java.text.ParseException pe) {
				JOptionPane.showMessageDialog(dialog, "권한 레벨 설정은 숫자여야 합니다.", "입력 오류", JOptionPane.WARNING_MESSAGE);
				return;
			}

			// DB 저장
			TableBoardsDTO newBoard = new TableBoardsDTO(code, name, type, readRole, writeRole, true);
			int result = dao.insertBoard(newBoard);
			if (result > 0) {
				JOptionPane.showMessageDialog(dialog, "게시판이 생성되었습니다.");
				loadBoardList();
				dialog.dispose();
			} else {
				JOptionPane.showMessageDialog(dialog, "게시판 생성에 실패하였습니다.", "생성 오류", JOptionPane.ERROR_MESSAGE);
			}
			
		});
		btnCancel.addActionListener(e -> dialog.dispose());
		
		btnPanel.add(btnSave);
		btnPanel.add(btnCancel);
		dialog.add(inputPanel, BorderLayout.CENTER);
		dialog.add(lblInfo, BorderLayout.NORTH);
		dialog.add(btnPanel, BorderLayout.SOUTH);
		
		dialog.setVisible(true);
	}
	
	// 게시판 수정
	private void showEditBoardDialog(TableBoardsDTO board) {
		
		JDialog dialog = new JDialog(this, "게시판 수정", true);
		dialog.setSize(350, 420);
		dialog.setLayout(new BorderLayout());
		dialog.setLocationRelativeTo(this);
		
		JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));
		inputPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
		JTextField tfName = new JTextField(board.getName());
		JTextField tfCode = new JTextField(board.getCode());
		JTextField tfType = new JTextField(board.getType());
		JSpinner spinRead = new JSpinner(new SpinnerNumberModel(board.getReadRole(), 1, 9, 1));
		JSpinner spinWrite = new JSpinner(new SpinnerNumberModel(board.getWriteRole(), 1, 9, 1));
		JCheckBox chkActive = new JCheckBox("활성화", board.isActive());
		
		InputLimit.checkMaxLength(tfName, 20);
		InputLimit.checkMaxLength(tfCode, 15);
		InputLimit.checkMaxLength(tfType, 10);
		
		inputPanel.add(new JLabel("게시판 이름 : "));
		inputPanel.add(tfName);
		inputPanel.add(new JLabel("게시판 코드 : "));
		inputPanel.add(tfCode);
		inputPanel.add(new JLabel("게시판 타입 : "));
		inputPanel.add(tfType);
		inputPanel.add(new JLabel("읽기 권한 레벨 : "));
		inputPanel.add(spinRead);
		inputPanel.add(new JLabel("쓰기 권한 레벨 : "));
		inputPanel.add(spinWrite);
		inputPanel.add(new JLabel("게시판 상태 : "));
		inputPanel.add(chkActive);
		
		JPanel btnPanel = new JPanel();
		JButton btnBupdate = new JButton("수정");
		JButton btnBcancel = new JButton("취소");
		
		// 버튼 이벤트 생성
		btnBupdate.addActionListener(e -> {
			String name = tfName.getText().trim();
			String code = tfCode.getText().trim();
			String type = tfType.getText().trim();
			code = code.toLowerCase();
			type = type.toUpperCase();
			int readRole = (int) spinRead.getValue();
			int writeRole = (int) spinWrite.getValue();
			boolean isActive = chkActive.isSelected();
			
			// 유효성 검사
			if (name.isEmpty() || code.isEmpty() || type.isEmpty()) { // 모든 칸 입력
				JOptionPane.showMessageDialog(dialog, "모든 정보를 입력해주세요.", "입력 오류", JOptionPane.WARNING_MESSAGE);
				return;
			}
			if (!code.matches("^[a-zA-Z0-9]*$") || !type.matches("^[a-zA-Z0-9]*$")) { // 영문 확인
				JOptionPane.showMessageDialog(dialog, "코드와 타입은 영문 또는 숫자만 입력 가능합니다.", "입력 오류", JOptionPane.WARNING_MESSAGE);
			}
			try { // 권한 레벨(숫자) 확인
				spinRead.commitEdit();
				spinWrite.commitEdit();
			} catch (java.text.ParseException pe) {
				JOptionPane.showMessageDialog(dialog, "권한 레벨 설정은 숫자여야 합니다.", "입력 오류", JOptionPane.WARNING_MESSAGE);
				return;
			}
			TableBoardsDAO dao = new TableBoardsDAO();
			if (dao.isNameDuplicate(name, board.getBoardId())) { // 게시판 이름 중복 확인
				JOptionPane.showMessageDialog(dialog, "이미 존재하는 게시판 이름입니다.", "중복 오류", JOptionPane.WARNING_MESSAGE);
				return;
			}
			if (dao.isCodeDuplicate(code, board.getBoardId())) { // 게시판 코드 중복 확인
				JOptionPane.showMessageDialog(dialog, "이미 존재하는 게시판 코드입니다.", "중복 오류", JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			
			// DB 업데이트
			TableBoardsDTO updateBoard = new TableBoardsDTO(board.getBoardId(), code, name, type, readRole, writeRole, isActive);
			int result = dao.updateBoard(updateBoard);
			if (result > 0) {
				JOptionPane.showMessageDialog(dialog, "게시판 정보가 수정되었습니다.");
				loadBoardList();
				dialog.dispose();
			} else {
				JOptionPane.showMessageDialog(dialog, "게시판 정보 수정에 실패하였습니다.", "수정 오류", JOptionPane.ERROR_MESSAGE);
			}
		});
		btnBcancel.addActionListener(e -> dialog.dispose());
		
		
		btnPanel.add(btnBupdate);
		btnPanel.add(btnBcancel);
		
		dialog.add(inputPanel, BorderLayout.CENTER);
		dialog.add(btnPanel, BorderLayout.SOUTH);
		
		dialog.setVisible(true);
	}
	
	// 게시판 검색
	private void searchBoards() {
		
	}
	
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
			
		} else if(event.getSource() == btnBsearch) {
			// 검색 기능 메서드 추가
			searchBoards();
			
		} else if(event.getSource() == btnBcreate) {
			// 게시판 생성 메서드 추가
			showCreateBoardDialog();
			
		}
	}
	
	public static void main(String[] args) {
		
	}
}
