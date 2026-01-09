package gui;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;

public class DetailsPwEditGUI extends JFrame implements ActionListener {
	// 필드, 생성자, 메서드 생성
	// 비밀번호 재입력 후 원하는 비밀번호로 변경
	// 버튼 > 변경, 취소, 돌아가기, 종료
	// 비밀번호 암호화 추가
	
	private JPasswordField MyPw, EditPw, ReEditPw; // 현재 비밀번호, 신규 비밀번호, 비밀번호 재확인
	private JButton btnpwedit, btncancel, btnback, btnexit;
	
	public DetailsPwEditGUI() {
		setTitle("비밀번호 변경");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 2, 10, 10));	// 행(0=자동), 열, 수평간격, 수직간격
		
		MyPw = new JPasswordField(15);
		EditPw = new JPasswordField(15);
		ReEditPw = new JPasswordField(15);
		
		btnpwedit = new JButton("비밀번호 변경");
		btnpwedit.addActionListener(this);
		btncancel = new JButton("입력취소");
		btncancel.addActionListener(this);
		btnback = new JButton("돌아가기");
		btnback.addActionListener(this);
		btnexit = new JButton("종료");
		btnexit.addActionListener(this);
		
		panel.add(new JLabel("현재 비밀번호", SwingConstants.LEFT));
		panel.add(MyPw);
		panel.add(new JLabel("신규 비밀번호", SwingConstants.LEFT));
		panel.add(EditPw);
		panel.add(new JLabel("비밀번호 확인", SwingConstants.LEFT));
		panel.add(ReEditPw);
		
		panel.add(btnpwedit);
		panel.add(btncancel);
		panel.add(btnback);
		panel.add(btnexit);
		
		setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		add(panel);
		pack();
		setLocationRelativeTo(null);
	}
	
	// 입력 취소 메서드
	private void reset() {
		MyPw.setText("");
		EditPw.setText("");
		ReEditPw.setText("");
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == btnpwedit) {
			// 비밀번호 변경 저장 후 DetailsGUI 이동
			
		} else if(event.getSource() == btncancel) {
			reset();
			
		} else if(event.getSource() == btnback) {
			// DetailsGUI 이동
			
		} else if(event.getSource() == btnexit) {
			System.exit(0);
		}
	}

	public static void main(String[] args) {
		(new DetailsPwEditGUI()).setVisible(true);
	}
}
