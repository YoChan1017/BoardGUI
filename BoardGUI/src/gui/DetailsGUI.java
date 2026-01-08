package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import session.UserSession;

public class DetailsGUI extends JFrame implements ActionListener {
	// 필드, 생성자, 메서드 생성
	// 유저 정보 확인 > nickname, birth_data, phone, email, created_at 표시
	// 버튼 > 메인화면, 작성글, 로그아웃, 종료, 회원정보 수정, 비밀번호 변경
	// 게시판 기능 추가 후 내가 작성한 글 보기 페이지 연결
	
	private JTextField data1, data2, data3, data4, data5; // 데이터 넣을 때 명칭 변경
	private JButton btnmain, btnmypost, btnlogout, btnexit, btnuseredit, btnpwedit;
	
	public DetailsGUI() {
		setTitle("로그인 정보");
		setDefaultCloseOperation(EXIT_ON_CLOSE);		
		setLayout(new BorderLayout());
		setSize(800, 600);
		
		JPanel topPanel = new JPanel();
		JPanel centerPanel = new JPanel();
		JPanel bottomPanel = new JPanel();
		
		centerPanel.setLayout(new GridLayout(5, 2, 10, 10));
		centerPanel.setBorder(new EmptyBorder(50, 100, 50, 100));
		
		// 데이터 표시 공간(임시로 지정)
		data1 = createReadOnlyField();
		data2 = createReadOnlyField();
		data3 = createReadOnlyField();
		data4 = createReadOnlyField();
		data5 = createReadOnlyField();
			
		// UI 추가
		btnmain = new JButton("HOME");
		btnmain.addActionListener(this);
		btnmypost = new JButton("작성글 보기");
		btnmypost.addActionListener(this);
		btnlogout = new JButton("로그아웃");
		btnlogout.addActionListener(this);
		btnexit = new JButton("종료");
		btnexit.addActionListener(this);
		
		centerPanel.add(new JLabel("닉네임", SwingConstants.LEFT));
		centerPanel.add(data1);
		centerPanel.add(new JLabel("생년월일", SwingConstants.LEFT));
		centerPanel.add(data2);
		centerPanel.add(new JLabel("전화번호", SwingConstants.LEFT));
		centerPanel.add(data3);
		centerPanel.add(new JLabel("이메일", SwingConstants.LEFT));
		centerPanel.add(data4);
		centerPanel.add(new JLabel("가입날짜", SwingConstants.LEFT));
		centerPanel.add(data5);
		
		btnuseredit = new JButton("회원정보 수정");
		btnuseredit.addActionListener(this);
		btnpwedit = new JButton("비밀번호 변경");
		btnpwedit.addActionListener(this);
		
		topPanel.add(btnmain);
		topPanel.add(btnmypost);
		topPanel.add(btnlogout);
		topPanel.add(btnexit);
		
		bottomPanel.add(btnuseredit);
		bottomPanel.add(btnpwedit);
		
		add(topPanel, BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);
		setLocationRelativeTo(null);
		
		// 현재 GUI화면 진입 시 로그인 체크 여부
		if (!UserSession.getInstance().isLoggedIn()) {
			// 현재 생성자를 종료 후 로그인 화면으로 이동
			JOptionPane.showMessageDialog(this, "로그인을 먼저 해주세요.", "접근 제한", JOptionPane.WARNING_MESSAGE);		
			dispose();
			(new LoginGUI()).setVisible(true);
			return;
		}
	}
	
	private JTextField createReadOnlyField() {
		JTextField data = new JTextField();
		data.setEditable(false);
		data.setBackground(Color.white);
		data.setHorizontalAlignment(JTextField.CENTER);
		return data;
	}
	
	// 버튼 이벤트 작성
	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == btnmain) {
			// 로그인 세션 보유한채로 메인화면 이동
			setVisible(false);
			(new MainGUI()).setVisible(true);
			
		} else if(event.getSource() == btnmypost) {
			// 나의 작성글 확인
			
		} else if(event.getSource() == btnlogout) {
			// 세션 제거 추가 - 로그아웃 처리
			UserSession.getInstance().logout();
			JOptionPane.showMessageDialog(this, "로그아웃 되었습니다.");
			// 로그아웃 후 로그인으로 다시 이동
			setVisible(false);						
			(new LoginGUI()).setVisible(true);
			
		} else if(event.getSource() == btnexit) {
			System.exit(0);
			
		} else if(event.getSource() == btnuseredit) {
			// 로그인 중인 회원 정보 수정
			
		} else if(event.getSource() == btnpwedit) {
			// " 비밀번호 수정
		}
	}
	
	public static void main(String[] args) {
		DetailsGUI detailsGui = new DetailsGUI();
		if (detailsGui.isDisplayable()) {
			detailsGui.setVisible(true);
		}
	}
}
 