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

public class LoginGUI extends JFrame implements ActionListener {
	
	private JTextField userId, userPassword;
	private JButton btnlogin, btncancel, btnsignup, btnexit; 
	
	public LoginGUI() {
		setTitle("Login");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 2, 10, 10));	// 행(0=자동), 열, 수평간격, 수직간격
		
		userId = new JTextField(15);
		userPassword = new JTextField(15);
		
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
		setLocationRelativeTo(null);					// 중앙에 띄우기
		// getConnection();
	}
	
	// 입력 취소 메서드
	private void reset() {
		userId.setText("");
		userPassword.setText("");
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == btnlogin) {
			
		} else if(event.getSource() == btncancel) {
			reset();
		} else if(event.getSource() == btnsignup) {
			setVisible(false);
			(new SignupGUI()).setVisible(true);
		} else if(event.getSource() == btnexit) {
			// disConnection();
			System.exit(0);
		}
	}

	public static void main(String[] args) {
		(new LoginGUI()).setVisible(true);
	}
}
