package gui;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class SignupGUI extends JFrame implements ActionListener {

	private JTextField userid, password, nickname, birthday, phone, email;
	private JButton btnsignup, btncancel, btnlogin;
	
	public SignupGUI() {
		setTitle("SignUp");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 2, 10, 10));
		
		userid = new JTextField(15);
		password = new JTextField(15);
		nickname = new JTextField(15);
		birthday = new JTextField(15);
		phone = new JTextField(15);
		email = new JTextField(15);
		
		panel.add(new JLabel("아이디", SwingConstants.LEFT));
		panel.add(userid);
		panel.add(new JLabel("비밀번호", SwingConstants.LEFT));
		panel.add(password);
		panel.add(new JLabel("닉네임", SwingConstants.LEFT));
		panel.add(nickname);
		panel.add(new JLabel("생년월일", SwingConstants.LEFT));
		panel.add(birthday);
		panel.add(new JLabel("전화번호", SwingConstants.LEFT));
		panel.add(phone);
		panel.add(new JLabel("이메일", SwingConstants.LEFT));
		panel.add(email);
		
		btnsignup = new JButton("등록");
		btnsignup.addActionListener(this);
		btncancel = new JButton("취소");
		btncancel.addActionListener(this);
		btnlogin = new JButton("로그인화면");
		btnlogin.addActionListener(this);
		
		panel.add(btnsignup);
		panel.add(btncancel);
		panel.add(btnlogin);
		
		setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));	// 수평간격 / 수직간격
		add(panel);
		pack();
		setLocationRelativeTo(null);
	}
	
	// 입력 취소 메서드
	private void reset() {
		userid.setText("");
		password.setText("");
		nickname.setText("");
		birthday.setText("");
		phone.setText("");
		email.setText("");
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == btnsignup) {
			
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
