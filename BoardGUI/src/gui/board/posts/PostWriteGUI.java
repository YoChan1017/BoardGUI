package gui.board.posts;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import dbms.boards.TableBoardsDTO;
import dbms.users.TableUsersRole;
import gui.DetailsGUI;
import gui.LoginGUI;
import gui.MainGUI;
import gui.board.boards.BoardGUI;
import session.UserSession;

public class PostWriteGUI extends JFrame implements ActionListener {
	
	// 게시글 작성 GUI
	
	// 필드
	private TableBoardsDTO currentBoard;
	private JTextField txtTitle;
	private JTextArea txtContent;
	private JCheckBox chkSecret, chkNotice;
	private JLabel lblFileName;
	private JButton btnmain, btnuser, btnlogout, btnexit, btnupload, btncancel, btnback, btnFile;
	
	// 생성자
	public PostWriteGUI(TableBoardsDTO boardInfo) {
		this.currentBoard = boardInfo;
		
		setTitle(currentBoard.getName() + " - 게시판 글 작성");
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
		
		// Panel은 상단, 중단, 하단으로 생성
		// 상단 : 게시판 이름 + 글작성 표시
		// 중앙 : 제목작성 + 글작성 + 첨부파일 추가 + 올리기/지우기/취소 버튼, 공지글 체크(관리자), 
		// 하단 : 메인화면 + 내정보 + 로그아웃 + 종료 버튼
		
		// 상단(topPanel) > 게시판 이름 + 현재상태(글작성) 표시
		JLabel lblTitle = new JLabel("[" + currentBoard.getName() + "] 글 작성");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		topPanel.add(lblTitle);
		
		// 중앙(centerPanel) > 제목작성 + 내용작성 + 옵션체크 + 버튼생성
		// 제목작성 Panel
		JPanel titlePanel = new JPanel(new BorderLayout());
		titlePanel.add(new JLabel("제 목 : "), BorderLayout.WEST);
		txtTitle = new JTextField();
		titlePanel.add(txtTitle, BorderLayout.CENTER);
		// 내용작성 Panel
		txtContent = new JTextArea();
		txtContent.setLineWrap(true); // 자동 줄바꿈
		JScrollPane scrollPane = new JScrollPane(txtContent); // 스크롤
		// 옵션체크 + 버튼생성 Panel
		JPanel optionsPanel = new JPanel(new BorderLayout());
		JPanel leftOptionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		chkSecret = new JCheckBox("비밀글");	// 게시판 코드가 qna(건의사항)일 때만 활성화
		if (!"qna".equalsIgnoreCase(currentBoard.getCode())) {
			chkSecret.setEnabled(false);
		}
		chkNotice = new JCheckBox("공지글"); // 관리자가 작성할 때만 활성화
		String roleStr = UserSession.getInstance().getUser().getRole();
		TableUsersRole userRole = TableUsersRole.fromDbRole(roleStr);
		if (userRole != TableUsersRole.ADMIN) {
			chkNotice.setEnabled(false);
		}
		btnFile = new JButton("파일 첨부");
		btnFile.addActionListener(this);
		lblFileName = new JLabel("선택된 파일 없음");
		leftOptionPanel.add(chkSecret);
		leftOptionPanel.add(chkNotice);
		leftOptionPanel.add(btnFile);
		leftOptionPanel.add(lblFileName);
		JPanel rightButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		btnupload = new JButton("글 등록");
		btnupload.addActionListener(this);
		btncancel = new JButton("작성취소");
		btncancel.addActionListener(this);
		btnback = new JButton("뒤로가기");
		btnback.addActionListener(this);
		rightButtonPanel.add(btnupload);
		rightButtonPanel.add(btncancel);
		rightButtonPanel.add(btnback);
		optionsPanel.add(leftOptionPanel, BorderLayout.WEST);
		optionsPanel.add(rightButtonPanel, BorderLayout.EAST);
		// centerPanel 내부 배치
		centerPanel.add(titlePanel, BorderLayout.NORTH); 	// 제목작성
		centerPanel.add(scrollPane, BorderLayout.CENTER); 	// 내용작성
		centerPanel.add(optionsPanel, BorderLayout.SOUTH);	// 옵션체크 + 버튼
		
		
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
	// DB 저장
	private void uploadPost() {
		
	}
	
	// 입력 필드 초기화
	private void reset() {
		txtTitle.setText("");					// 제목
		txtContent.setText("");					// 내용
		chkSecret.setSelected(false);			// 비밀글 여부
		chkNotice.setSelected(false);			// 공지글 여부
		lblFileName.setText("선택된 파일 없음");	// 첨부파일
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
			
		} else if(event.getSource() == btnupload) {
			// 작성한 게시글 업로드
			
			
		} else if(event.getSource() == btncancel) {
			// 작성한 글 취소(지우기)
			reset();
			
		} else if(event.getSource() == btnback) {
			// 작성중인 글 취소하고 전 화면(BoardGUI)로 돌아가기
			setVisible(false);
			(new BoardGUI(currentBoard)).setVisible(true);
			
		} else if(event.getSource() == btnFile) {
			// 파일 첨부
			JFileChooser fileChooser = new JFileChooser();
			int option = fileChooser.showOpenDialog(this);
			if (option == JFileChooser.APPROVE_OPTION) {
				String fileName = fileChooser.getSelectedFile().getName();
				lblFileName.setText(fileName);
				JOptionPane.showMessageDialog(this,  "파일이 선택되었습니다 : " + fileName);
				// DB 저장 추가
			}
		}
	}

	public static void main(String[] args) {
		
	}
}
