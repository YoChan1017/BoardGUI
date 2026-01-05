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
		panel.setLayout(new GridLayout(0, 2, 10, 5));	// 행(0=자동), 열, 수평간격, 수직간격
		userId = new JTextField(30);
		userPassword = new JTextField(30);
		
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
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
	}

	public static void main(String[] args) {
		(new LoginGUI()).setVisible(true);
	}
}
