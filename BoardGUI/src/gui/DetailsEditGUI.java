package gui;

import java.awt.BorderLayout;
import java.awt.Color;
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

import dbms.TableUsersDTO;
import session.UserSession;

public class DetailsEditGUI extends JFrame implements ActionListener {
	// 필드, 생성자, 메서드 생성
	// DetailsGUI와 비슷하게 구성
	// 버튼 > 메인, 수정, 취소, 돌아가기, 종료
	
	private JTextField Nickname, Birth, Phone, Email; // 닉네임, 생년월일, 전화번호, 이메일만 수정할 수 있도록
	private JButton btnmain, btnlogout, btnexit, btnedit, btncancel, btnback; // 메인이동, 로그아웃, 종료, 수정완료, 수정취소, 돌아가기

	public DetailsEditGUI() {
		setTitle("내 정보 수정");
		setDefaultCloseOperation(EXIT_ON_CLOSE);		
		setLayout(new BorderLayout());
		setSize(800, 600);
		
		// 현재 GUI화면 진입 시 로그인 체크 여부
		if (!UserSession.getInstance().isLoggedIn()) {
			// 현재 생성자를 종료 후 로그인 화면으로 이동
			JOptionPane.showMessageDialog(this, "로그인을 먼저 해주세요.", "접근 제한", JOptionPane.WARNING_MESSAGE);		
			dispose();
			(new LoginGUI()).setVisible(true);
			return;
		}
		
		JPanel topPanel = new JPanel();
		JPanel centerPanel = new JPanel();
		JPanel bottomPanel = new JPanel();
		
		centerPanel.setLayout(new GridLayout(5, 2, 10, 10));
		centerPanel.setBorder(new EmptyBorder(50, 100, 50, 100));
		
		// 데이터 표시 공간
		Nickname = createReadOnlyField();
		Birth = createReadOnlyField();
		Phone = createReadOnlyField();
		Email = createReadOnlyField();
		
		// 데이터 가져와서 채우기
		loadUserData();
		
		// UI 추가
		// 상단
		btnmain = new JButton("HOME");
		btnmain.addActionListener(this);
		btnlogout = new JButton("로그아웃");
		btnlogout.addActionListener(this);
		btnexit = new JButton("종료");
		btnexit.addActionListener(this);
		btnback = new JButton("뒤로가기");
		btnback.addActionListener(this);
		
		// 중앙
		centerPanel.add(new JLabel("닉네임", SwingConstants.LEFT));
		centerPanel.add(Nickname);
		centerPanel.add(new JLabel("생년월일", SwingConstants.LEFT));
		centerPanel.add(Birth);
		centerPanel.add(new JLabel("전화번호", SwingConstants.LEFT));
		centerPanel.add(Phone);
		centerPanel.add(new JLabel("이메일", SwingConstants.LEFT));
		centerPanel.add(Email);
		
		// 하단
		btnedit = new JButton("수정 완료");
		btnedit.addActionListener(this);
		btncancel = new JButton("수정 취소");
		btncancel.addActionListener(this);
		
		// 버튼 Panel에 추가
		topPanel.add(btnmain);
		topPanel.add(btnlogout);
		topPanel.add(btnexit);
		
		bottomPanel.add(btnedit);
		bottomPanel.add(btncancel);
		bottomPanel.add(btnback);
		
		// Panel 배치
		add(topPanel, BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);
		setLocationRelativeTo(null);
	}
	
	// 텍스트 필드 초기화 
	private JTextField createReadOnlyField() {
		JTextField data = new JTextField();
		data.setBackground(Color.white);	// 배경 흰색
		data.setHorizontalAlignment(JTextField.CENTER);	// 중앙 표시
		return data;
	}
	
	// 로그인 정보를 텍스트 필드에 표시하는 메서드
	private void loadUserData() {
		TableUsersDTO user = UserSession.getInstance().getUser();
		if (user != null) {
			// 닉네임
			Nickname.setText(user.getNickname());
			// 생년월일
			if (user.getBirthDate() != null) {
				Birth.setText(user.getBirthDate().toString());
			}
			// 전화번호
			Phone.setText(user.getPhone());
			// 이메일
			Email.setText(user.getEmail());
		}
	}
	
	// 수정된 정보 저장 메서드
	private void editUserData() {
		// 수정된 정보 저장 > 중복 및 null 확인 알림
	}
	
	// 입력 취소 메서드
	private void reset() {
		// 원래 데이터 복구
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == btnmain) {
			// 로그인 세션 보유한채로 메인화면 이동
			setVisible(false);
			(new MainGUI()).setVisible(true);
			
		} else if(event.getSource() == btnlogout) {
			// 세션 제거 추가 - 로그아웃 처리
			UserSession.getInstance().logout();
			JOptionPane.showMessageDialog(this, "로그아웃 되었습니다.");
			// 로그아웃 후 로그인으로 다시 이동
			setVisible(false);						
			(new LoginGUI()).setVisible(true);
			
		} else if(event.getSource() == btnexit) {
			// 프로그램 종료
			System.exit(0);
			
		} else if(event.getSource() == btnedit) {
			// 수정 완료 후 전화면(DetailsGUI)로 돌아가기
			editUserData();
			
		} else if(event.getSource() == btncancel) {
			// 입력(수정) 취소
			reset();
			
		} else if(event.getSource() == btnback) {
			setVisible(false);						
			(new DetailsGUI()).setVisible(true);
		}
	}

	public static void main(String[] args) {
		(new DetailsEditGUI()).setVisible(true);
	}
}
