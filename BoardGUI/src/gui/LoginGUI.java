package gui;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import dbms.TableUsersDAO;
import dbms.TableUsersDTO;

public class LoginGUI extends JFrame implements ActionListener {
	
	private JTextField userId;
	private JPasswordField userPassword;
	private JButton btnlogin, btncancel, btnsignup, btnexit; 
	
	public LoginGUI() {
		setTitle("로그인");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 2, 10, 10));	// 행(0=자동), 열, 수평간격, 수직간격
		
		userId = new JTextField(15);
		userPassword = new JPasswordField(15);
		
		btnlogin = new JButton("로그인");
		btnlogin.addActionListener(this);
		btncancel = new JButton("취소");
		btncancel.addActionListener(this);
		btnsignup = new JButton("회원가입");
		btnsignup.addActionListener(this);
		btnexit = new JButton("종료");
		btnexit.addActionListener(this);
		
		panel.add(new JLabel("ID", SwingConstants.LEFT));
		panel.add(userId);
		panel.add(new JLabel("PW", SwingConstants.LEFT));
		panel.add(userPassword);
		
		panel.add(btnlogin);
		panel.add(btncancel);
		panel.add(btnsignup);
		panel.add(btnexit);	
		
		setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));	// 수평간격 / 수직간격
		add(panel);
		pack();
		setLocationRelativeTo(null);							// 화면 중앙에 띄우기
	}
	
	private void loginCheck() {
		String uid = userId.getText().trim();
		String pw = new String(userPassword.getPassword()).trim();
		// 아이디와 비밀번호 미입력 시
		if(uid.isEmpty() || pw.isEmpty()) {
			JOptionPane.showMessageDialog(this, "아이디와 비밀번호를 입력해주세요.", "로그인 오류", JOptionPane.WARNING_MESSAGE);
			return;
		}
		// 로그인 인증 시도
		TableUsersDAO dao = new TableUsersDAO();
		boolean isLoginSuccess = dao.login(uid, pw);
		if (isLoginSuccess) {
			TableUsersDTO userInfo = dao.getUserByUsername(uid);
			String nickname = (userInfo != null) ? userInfo.getNickname() : uid;
			JOptionPane.showMessageDialog(this, nickname + "님 환영합니다.", "로그인 성공", JOptionPane.INFORMATION_MESSAGE);
			// 세션 유지 추가 - 로그인 처리
			setVisible(false);							
			(new MainGUI()).setVisible(true);
		} else {
			JOptionPane.showMessageDialog(this, "아이디 또는 비밀번호가 일치하지 않습니다.", "로그인 실패", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	// 입력 취소 메서드
	private void reset() {
		userId.setText("");
		userPassword.setText("");
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == btnlogin) {
			loginCheck();
		} else if(event.getSource() == btncancel) {
			reset();
		} else if(event.getSource() == btnsignup) {
			setVisible(false);							// 현재 화면 숨기고
			(new SignupGUI()).setVisible(true);			// SinupGUI 화면 표시
		} else if(event.getSource() == btnexit) {
			System.exit(0);
		}
	}

	public static void main(String[] args) {
		(new LoginGUI()).setVisible(true);
	}
}
