package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

public class DetailsEditGUI extends JFrame implements ActionListener {
	// 필드, 생성자, 메서드 생성
	// DetailsGUI와 비슷하게 구성
	// 버튼 > 메인, 수정, 취소, 돌아가기, 종료

	public DetailsEditGUI() {
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
	}

	public static void main(String[] args) {
		(new DetailsEditGUI()).setVisible(true);
	}
}
