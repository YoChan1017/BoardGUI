package gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainGUI extends JFrame implements ActionListener{
	
	private JButton btnmain, btnuser, btnlogout, btnexit;
	
	public MainGUI() {
		setTitle("MAIN");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		setSize(800, 600);
		
		btnmain = new JButton("HOME");
		btnmain.addActionListener(this);
		btnuser = new JButton("내 정보");
		btnuser.addActionListener(this);
		btnlogout = new JButton("로그아웃");
		btnlogout.addActionListener(this);
		btnexit = new JButton("종료");
		btnexit.addActionListener(this);
		
		panel.add(btnmain);
		panel.add(btnuser);
		panel.add(btnlogout);
		panel.add(btnexit);
		
		setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		add(panel);
		setLocationRelativeTo(null);
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == btnmain) {
			// 로그인 세션 보유한채로
			setVisible(false);						
			(new MainGUI()).setVisible(true);			
		} else if(event.getSource() == btnuser) {
			// 로그인 세션 보유한채로
			setVisible(false);					
			(new DetailsGUI()).setVisible(true);
		} else if(event.getSource() == btnlogout) {
			// 세션 제거 추가 - 로그아웃 처리
			setVisible(false);						
			(new LoginGUI()).setVisible(true);
		} else if(event.getSource() == btnexit) {
			// 세션 제거 추가 - 로그아웃 처리
			System.exit(0);
		}
	}

	public static void main(String[] args) {
		(new MainGUI()).setVisible(true);
	}
}
