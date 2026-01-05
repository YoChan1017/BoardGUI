package gui;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class SignupGUI extends JFrame implements ActionListener {

	private JTextField userid, password, nickname, phone, email;
	private JComboBox<String> yearCombo, monthCombo, dayCombo;
	private JButton btnsignup, btncancel, btnlogin;
	
	public SignupGUI() {
		setTitle("회원가입");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 2, 10, 10));
		
		userid = new JTextField(15);
		password = new JTextField(15);
		nickname = new JTextField(15);
		phone = new JTextField(15);
		email = new JTextField(15);
		
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
		
		panel.add(btnsignup);
		panel.add(btncancel);
		panel.add(btnlogin);
		
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
			addUser();
			setVisible(false);
			(new LoginGUI()).setVisible(true);
		} else if(event.getSource() == btncancel) {
			reset();
		} else if(event.getSource() == btnlogin) {
			setVisible(false);
			(new LoginGUI()).setVisible(true);
		}
	}

	public static void main(String[] args) {
		(new SignupGUI()).setVisible(true);
	}
}
