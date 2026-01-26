package gui;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import dbms.users.TableUsersDAO;
import dbms.users.TableUsersDTO;
import utils.InputLimit;

public class SignupGUI extends JFrame implements ActionListener {

	private JTextField userid, nickname, phone, email;
	private JPasswordField password;
	private JComboBox<String> yearCombo, monthCombo, dayCombo;
	private JButton btnsignup, btncancel, btnlogin, btnexit;
	
	public SignupGUI() {
		setTitle("회원가입");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 2, 10, 10));
		
		userid = new JTextField(15);
		password = new JPasswordField(15);	// UI 보안 추가
		nickname = new JTextField(15);
		phone = new JTextField(15);
		email = new JTextField(15);
		
		// 입력 길이 제한 설정 (입력값, 입력길이)
		InputLimit.checkMaxLength(userid, 15);
		InputLimit.checkMaxLength(password, 25);
		InputLimit.checkMaxLength(nickname, 10);
		InputLimit.checkMaxLength(phone, 11);
		InputLimit.checkMaxLength(email, 30);
		
		// 생년월일 콤보박스 설정
		JPanel birthPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		yearCombo = new JComboBox<>();
		monthCombo = new JComboBox<>();
		dayCombo = new JComboBox<>();
		int currentYear = Calendar.getInstance().get(Calendar.YEAR); // 년도(year)
		for(int i = currentYear; i >= 1900; i--) {
			yearCombo.addItem(String.valueOf(i));
		}
		for (int i = 1; i <= 12; i++) { // 월(month)
			monthCombo.addItem(String.format("%02d", i));
		}
		for (int i = 1; i <= 31; i++) { // 일(day)
			dayCombo.addItem(String.format("%02d", i));
		}
		birthPanel.add(yearCombo);
		birthPanel.add(new JLabel("년 "));
		birthPanel.add(monthCombo);
		birthPanel.add(new JLabel("월 "));
		birthPanel.add(dayCombo);
		birthPanel.add(new JLabel("일 "));
	
		panel.add(new JLabel("아이디", SwingConstants.LEFT));
		panel.add(userid);
		panel.add(new JLabel("비밀번호", SwingConstants.LEFT));
		panel.add(password);
		panel.add(new JLabel("닉네임", SwingConstants.LEFT));
		panel.add(nickname);
		panel.add(new JLabel("생년월일", SwingConstants.LEFT));
		panel.add(birthPanel); // 콤보박스 패널로 변경
		panel.add(new JLabel("전화번호", SwingConstants.LEFT));
		panel.add(phone);
		panel.add(new JLabel("이메일", SwingConstants.LEFT));
		panel.add(email);
		
		btnsignup = new JButton("등록");
		btnsignup.addActionListener(this);
		btncancel = new JButton("취소");
		btncancel.addActionListener(this);
		btnlogin = new JButton("돌아가기");
		btnlogin.addActionListener(this);
		btnexit = new JButton("종료");
		btnexit.addActionListener(this);
		
		panel.add(btnsignup);
		panel.add(btncancel);
		panel.add(btnlogin);
		panel.add(btnexit);
		
		setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		add(panel);
		pack();
		setLocationRelativeTo(null);
	}
	
	private void addUser() {
		// 등록 시 모든 입력칸이 비었을 경우 알림
		// 아이디, 닉네임, 이메일 중복 시 알림
		// 전화번호 숫자만 입력, 다른 글자 입력 시 알림('-'제외 입력)
		// users table 등록
		// 등록 시 등록완료 알림 추가
		// 비밀번호 보안 추가
		
		// 입력한 데이터 가져오기
		String uid = userid.getText().trim();
		String pw = new String(password.getPassword()).trim();
		String nick = nickname.getText().trim();
		String ph = phone.getText().trim();
		String mail = email.getText().trim();
		// 생년월일 조합
		String year = (String) yearCombo.getSelectedItem();
		String month = (String) monthCombo.getSelectedItem();
		String day = (String) dayCombo.getSelectedItem();
		Date birthDate = Date.valueOf(year + "-" + month + "-" + day);
		
		// 유효성 검사
		// 모든 입력칸이 비었을 경우
		if (uid.isEmpty() || pw.isEmpty() || nick.isEmpty() || ph.isEmpty() || mail.isEmpty()) {
			JOptionPane.showMessageDialog(this, "모든 정보를 입력해주세요.", "입력 오류", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		// 최소 길이 검사
		if (!InputLimit.checkMinLength(userid, 5, "아이디")) return;
		if (!InputLimit.checkMinLength(password, 10, "비밀번호")) return;
		if (!InputLimit.checkMinLength(nickname, 2, "닉네임")) return;
		if (!InputLimit.checkMinLength(phone, 10, "전화번호")) return;
		if (!InputLimit.checkMinLength(email, 5, "이메일")) return;
		
		// 전화번호 숫자 외 다른 글자를 입력하였을 경우
		if (!ph.matches("\\d+")) {
			JOptionPane.showMessageDialog(this, "전화번호는 숫자만 입력해주세요. ('-' 제외)", "입력 오류", JOptionPane.WARNING_MESSAGE);
			return;
		}
		TableUsersDAO dao = new TableUsersDAO(); // 객체 생성
		// 아이디 중복 확인
		if (dao.isIdDuplicate(uid)) {
			JOptionPane.showMessageDialog(this, "이미 존재하는 아이디입니다.", "중복 오류", JOptionPane.WARNING_MESSAGE);
			return;
		}
		// 닉네임 중복 확인
		if (dao.isNicknameDuplicate(nick)) {
			JOptionPane.showMessageDialog(this, "이미 존재하는 닉네임입니다.", "중복 오류", JOptionPane.WARNING_MESSAGE);
			return;
		}
		// 이메일 중복 확인
		if (dao.isEmailDuplicate(mail)) {
			JOptionPane.showMessageDialog(this, "이미 존재하는 이메일입니다.", "중복 오류", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		// 비밀번호 암호화 SHA-256
		String hashedPw = hashPassword(pw);
		if (hashedPw == null) {
			JOptionPane.showMessageDialog(this, "비밀번호 설정 중 오류가 발생하였습니다.", "설정 오류", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		// 데이터베이스 등록
		TableUsersDTO newUser = new TableUsersDTO(uid, hashedPw, nick, birthDate, ph, mail);
		int result = dao.insertUser(newUser);
		
		// 회원 등록 처리
		if (result > 0) {
			JOptionPane.showMessageDialog(this, "회원가입에 성공하였습니다.", "등록 성공", JOptionPane.WARNING_MESSAGE);
			setVisible(false);
			(new LoginGUI()).setVisible(true);
		} else {
			JOptionPane.showMessageDialog(this, "회원가입에 실패하였습니다.", "등록 실패", JOptionPane.WARNING_MESSAGE);
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
	
	// 입력 취소 메서드
	private void reset() {
		// id, pw, 닉네임 초기화
		userid.setText("");
		password.setText("");
		nickname.setText("");
		// 생년월일 초기화
		yearCombo.setSelectedIndex(0);
		monthCombo.setSelectedIndex(0);
		dayCombo.setSelectedIndex(0);
		// 전화번호, 이메일 초기화
		phone.setText("");
		email.setText("");
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == btnsignup) {
			// 회원 등록 완료 후 로그인 화면으로 이동
			addUser();
		} else if(event.getSource() == btncancel) {
			reset();
		} else if(event.getSource() == btnlogin) {
			setVisible(false);
			(new LoginGUI()).setVisible(true);
		} else if(event.getSource() == btnexit) {
			System.exit(0);
		}
	}

	public static void main(String[] args) {
		(new SignupGUI()).setVisible(true);
	}
}
