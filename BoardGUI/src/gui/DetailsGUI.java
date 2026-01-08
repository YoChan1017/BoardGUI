package gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class DetailsGUI extends JFrame implements ActionListener {
	// 필드, 생성자, 메서드
	// 유저 정보 확인/수정 등 
	
	public DetailsGUI() {
		setTitle("로그인 정보");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		setSize(800, 600);
		
		// UI 추가
		
		setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		add(panel);
		setLocationRelativeTo(null);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
	}
	
	public static void main(String[] args) {
		(new DetailsGUI()).setVisible(true);
	}
}
