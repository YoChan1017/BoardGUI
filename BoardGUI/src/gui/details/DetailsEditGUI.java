package gui.details;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import dbms.users.TableUsersDAO;
import dbms.users.TableUsersDTO;
import gui.LoginGUI;
import gui.MainGUI;
import session.UserSession;

public class DetailsEditGUI extends JFrame implements ActionListener {
	// 필드, 생성자, 메서드 생성
	// DetailsGUI와 비슷하게 구성
	// 버튼 > 메인, 수정, 취소, 돌아가기, 종료
	
	private JTextField Nickname, Phone, Email; // 닉네임, 생년월일, 전화번호, 이메일만 수정할 수 있도록
	private JComboBox<String> Year, Month, Day; // 생년월일은 콤보박스로 변경
	private JButton btnmain, btnlogout, btnexit, btnedit, btncancel, btnback; // 메인이동, 로그아웃, 종료, 수정완료, 수정취소, 돌아가기

	public DetailsEditGUI() {
		setTitle("내 정보 수정");
		setDefaultCloseOperation(EXIT_ON_CLOSE);		
		setLayout(new BorderLayout());
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
		
		centerPanel.setLayout(new GridLayout(5, 2, 10, 10));
		centerPanel.setBorder(new EmptyBorder(50, 100, 50, 100));
		
		// 데이터 표시 공간
		Nickname = new JTextField();
		Phone = new JTextField();
		Email = new JTextField();
		
		JPanel birthPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
		Year = new JComboBox();
		Month = new JComboBox();
		Day = new JComboBox();
		
		birthDateComboBoxes();
		
		birthPanel.add(Year);
		birthPanel.add(new JLabel("년"));
		birthPanel.add(Month);
		birthPanel.add(new JLabel("월"));
		birthPanel.add(Day);
		birthPanel.add(new JLabel("일"));
		
		// 데이터 가져와서 채우기
		loadUserData();
		
		// UI 추가
		// 상단
		btnmain = new JButton("HOME");
		btnmain.addActionListener(this);
		btnlogout = new JButton("로그아웃");
		btnlogout.addActionListener(this);
		btnexit = new JButton("종료");
		btnexit.addActionListener(this);
		btnback = new JButton("뒤로가기");
		btnback.addActionListener(this);
		
		// 중앙
		centerPanel.add(new JLabel("닉네임", SwingConstants.LEFT));
		centerPanel.add(Nickname);
		centerPanel.add(new JLabel("생년월일", SwingConstants.LEFT));
		centerPanel.add(birthPanel);
		centerPanel.add(new JLabel("전화번호", SwingConstants.LEFT));
		centerPanel.add(Phone);
		centerPanel.add(new JLabel("이메일", SwingConstants.LEFT));
		centerPanel.add(Email);
		
		// 하단
		btnedit = new JButton("수정 완료");
		btnedit.addActionListener(this);
		btncancel = new JButton("수정 취소");
		btncancel.addActionListener(this);
		
		// 버튼 Panel에 추가
		topPanel.add(btnmain);
		topPanel.add(btnlogout);
		topPanel.add(btnexit);
		
		bottomPanel.add(btnedit);
		bottomPanel.add(btncancel);
		bottomPanel.add(btnback);
		
		// Panel 배치
		add(topPanel, BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);
		setLocationRelativeTo(null);
	}
	
	// 텍스트 필드 초기화 
	private JTextField createReadOnlyField() {
		JTextField data = new JTextField();
		data.setBackground(Color.white);	// 배경 흰색
		data.setHorizontalAlignment(JTextField.CENTER);	// 중앙 표시
		return data;
	}
	
	// 생년월일 콤보박스 초기화
	private void birthDateComboBoxes() {
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		// 년도 (1900 ~ 현재 년도)
		for (int i =currentYear; i >= 1900; i--) {
			Year.addItem(String.valueOf(i));
		}
		// 월 (1월 ~ 12월)
		for(int i = 1; i <= 12; i++) {
			Month.addItem(String.format("%02d", i));
		}
		// 일 (1일 ~ 31일)
		for(int i= 1; i <= 31; i++) {
			Day.addItem(String.format("%02d", i));
		}
	}
	
	// 로그인 정보를 텍스트 필드에 표시하는 메서드
	private void loadUserData() {
		TableUsersDTO user = UserSession.getInstance().getUser();
		if (user != null) {
			// 닉네임
			Nickname.setText(user.getNickname());
			// 전화번호
			Phone.setText(user.getPhone());
			// 이메일
			Email.setText(user.getEmail());
			// 생년월일
			if (user.getBirthDate() != null) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(user.getBirthDate());
				
				String y = String.valueOf(cal.get(Calendar.YEAR));
				String m = String.format("%02d", cal.get(Calendar.MONTH) + 1);
				String d = String.format("%02d", cal.get(Calendar.DAY_OF_MONTH));
				
				Year.setSelectedItem(y);
				Month.setSelectedItem(m);
				Day.setSelectedItem(d);
			}
		}
	}
	
	// 수정된 정보 저장 메서드
	private void editUserData() {
		// 수정된 정보 저장 > 중복 및 null 확인 알림
		// 닉네임 > 중복 및 null 확인
		// 생년월일 > 콤보박스 변경 후 수정 가능하게
		// 전화번호 > 숫자만 입력('-'제외, null 확인)
		// 이메일 > 중복 확인
		
		// 입력값 가져오기
		String inputN = Nickname.getText().trim();
		String inputP = Phone.getText().trim();
		String inputE = Email.getText().trim();
		String year = (String) Year.getSelectedItem();
		String month = (String) Month.getSelectedItem();
		String day = (String) Day.getSelectedItem();
		Date inputB = Date.valueOf(year + "-" + month + "-" + day);
		
		// 유효성 검사
		if (inputN.isEmpty() || inputP.isEmpty() || inputE.isEmpty()) { // 입력란이 빈 칸일시
			JOptionPane.showMessageDialog(this, "모든 정보를 입력해주세요", "입력 오류", JOptionPane.WARNING_MESSAGE);
			return;
		}
		if (!inputP.matches("\\d+")) { // 전화번호 입력에 숫자 외 다른 문자가 포함되었을시
			JOptionPane.showMessageDialog(this, "전화번호는 숫자만 입력해주세요. ('-' 제외)", "입력 오류", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		// 중복 검사
		TableUsersDAO dao = new TableUsersDAO();
		TableUsersDTO currentUser = UserSession.getInstance().getUser();
		if (!inputN.equals(currentUser.getNickname()) && dao.isNicknameDuplicate(inputN)) { // Nickname(닉네임) 중복일시
			JOptionPane.showMessageDialog(this, "이미 존재하는 닉네임입니다.", "중복 오류", JOptionPane.WARNING_MESSAGE);
			return;
		}
		if (!inputE.equals(currentUser.getEmail()) && dao.isEmailDuplicate(inputE)) { // Email(이메일) 중복일시
			JOptionPane.showMessageDialog(this, "이미 존재하는 이메일입니다.", "중복 오류", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		// 기존 정보 유지하면서 닉네임, 생년월일, 전화번호, 이메일 업데이트
		TableUsersDTO updatedUser = new TableUsersDTO(
				currentUser.getUserId(),
				currentUser.getUsername(),
				currentUser.getPassword(),
				inputN,
				inputB,
				inputP,
				inputE,
				currentUser.getRole(),
				currentUser.isActive(),
				currentUser.getCreatedAt()
				);
		
		// 데이터베이스 업데이트
		int result = dao.updateUser(updatedUser);
		if (result > 0) { // 업데이트 성공시 DetailsGUI(내 정보)화면으로 이동
			UserSession.getInstance().login(updatedUser);
			JOptionPane.showMessageDialog(this, "회원 정보가 수정되었습니다.", "수정 완료", JOptionPane.INFORMATION_MESSAGE);
			setVisible(false);
			(new DetailsGUI()).setVisible(true);			
		} else {
			JOptionPane.showMessageDialog(this, "정보 수정에 실패했습니다.", "수정 오류", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	// 입력 취소 메서드
	private void reset() {
		loadUserData(); // 원래 데이터로 다시 채움
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == btnmain) {
			// 로그인 세션 보유한채로 메인화면 이동
			setVisible(false);
			(new MainGUI()).setVisible(true);
			
		} else if(event.getSource() == btnlogout) {
			// 세션 제거 추가 - 로그아웃 처리
			UserSession.getInstance().logout();
			JOptionPane.showMessageDialog(this, "로그아웃 되었습니다.");
			// 로그아웃 후 로그인으로 다시 이동
			setVisible(false);						
			(new LoginGUI()).setVisible(true);
			
		} else if(event.getSource() == btnexit) {
			// 프로그램 종료
			System.exit(0);
			
		} else if(event.getSource() == btnedit) {
			// 수정 완료 후 전화면(DetailsGUI)로 돌아가기
			editUserData();
			
		} else if(event.getSource() == btncancel) {
			// 입력(수정) 취소
			reset();
			
		} else if(event.getSource() == btnback) {
			setVisible(false);						
			(new DetailsGUI()).setVisible(true);
		}
	}

	public static void main(String[] args) {
		// 생성자가 정상적으로 실행하여 띄었을 경우에만 실행
		DetailsEditGUI detailsEditGui = new DetailsEditGUI();
		if (detailsEditGui.isDisplayable()) {
			detailsEditGui.setVisible(true);
		}
	}
}
