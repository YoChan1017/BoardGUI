package gui.details;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import dbms.users.TableUsersDAO;
import dbms.users.TableUsersDTO;
import gui.LoginGUI;
import gui.MainGUI;
import session.UserSession;

public class DetailsGUI extends JFrame implements ActionListener {
	// 필드, 생성자, 메서드 생성
	// 유저 정보 확인 > nickname, birth_data, phone, email, created_at 표시
	// 버튼 > 메인화면, 작성글, 로그아웃, 종료, 회원정보 수정, 비밀번호 변경, 회원탈퇴
	// 게시판 기능 추가 후 내가 작성한 글 보기 페이지 연결
	
	private JTextField Nickname, Birth, Phone, Email, Createat; // 데이터 넣을 때 명칭 변경 (닉네임, 생년월일, 전화번호, 이메일, 가입날짜)
	private JButton btnmain, btnmypost, btnlogout, btnexit, btnuseredit, btnpwedit, btndeleteuser;
	
	public DetailsGUI() {
		setTitle("내 정보");
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
		Createat = createReadOnlyField();
		
		// 데이터 가져와서 채우기
		loadUserData();
			
		// UI 추가
		// 상단
		btnmain = new JButton("HOME");
		btnmain.addActionListener(this);
		btnmypost = new JButton("작성글 보기");
		btnmypost.addActionListener(this);
		btnlogout = new JButton("로그아웃");
		btnlogout.addActionListener(this);
		btnexit = new JButton("종료");
		btnexit.addActionListener(this);
		
		// 중앙
		centerPanel.add(new JLabel("닉네임", SwingConstants.LEFT));
		centerPanel.add(Nickname);
		centerPanel.add(new JLabel("생년월일", SwingConstants.LEFT));
		centerPanel.add(Birth);
		centerPanel.add(new JLabel("전화번호", SwingConstants.LEFT));
		centerPanel.add(Phone);
		centerPanel.add(new JLabel("이메일", SwingConstants.LEFT));
		centerPanel.add(Email);
		centerPanel.add(new JLabel("가입날짜", SwingConstants.LEFT));
		centerPanel.add(Createat);
		
		// 하단
		btnuseredit = new JButton("회원정보 수정");
		btnuseredit.addActionListener(this);
		btnpwedit = new JButton("비밀번호 변경");
		btnpwedit.addActionListener(this);
		btndeleteuser = new JButton("회원 탈퇴");
		btndeleteuser.addActionListener(this);
		
		topPanel.add(btnmain);
		topPanel.add(btnmypost);
		topPanel.add(btnlogout);
		topPanel.add(btnexit);
		
		bottomPanel.add(btnuseredit);
		bottomPanel.add(btnpwedit);
		bottomPanel.add(btndeleteuser);
		
		add(topPanel, BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);
		setLocationRelativeTo(null);
	}
	
	// 텍스트 필드 초기화 및 수정불가
	private JTextField createReadOnlyField() {
		JTextField data = new JTextField();
		data.setEditable(false);			// 수정 불가
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
			// 가입날짜
			if (user.getCreatedAt() != null) {
				String dateStr = user.getCreatedAt().toString();
				if (dateStr.length() > 19) {
					dateStr = dateStr.substring(0, 19);
				}
				Createat.setText(dateStr);
			}
		}
	}
	
	// 회원 탈퇴를 처리하는 메서드
	private void deleteUserData() {
		// 현재 회원 탈퇴(비활성화) 처리 > 나중에 관리자가 직접 삭제하거나 일정기간 뒤에 삭제
		// "YES_NO_OPTION"알림으로 선택
		// 확인 눌렀을시 안내 메시지와 함께 비밀번호 재입력 및 안내메시지를 읽었다는 확인에 체크
		// 완료하였을시 탈퇴 처리 후 LoginGUI로 이동
		
		// 1차 알림 - 안내메세지(확인/취소)
		int choice = JOptionPane.showConfirmDialog(this, "정말로 탈퇴하시겠습니까?\n탈퇴 시 모든 정보가 삭제됩니다.", "회원 탈퇴 확인", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		if (choice != JOptionPane.YES_OPTION) {
			return;
		}
		
		// 2차 알림 - 경고메세지(체크박스 + 비밀번호 입력)
		JPanel panel = new JPanel(new GridLayout(4, 1, 5, 5));
		JLabel warnLabel = new JLabel("<html><font color='red'><b>[경고] 탈퇴 후에는 계정이 비활성화됩니다.</b><font><br>복구를 원하시면 3일내로 관리자한테 문의바랍니다.</html>");
		JCheckBox confirmCheck = new JCheckBox("위 안내사항을 모두 확인하였으며, 탈퇴에 동의합니다.");
		JPasswordField pwField = new JPasswordField();
		
		panel.add(warnLabel);
		panel.add(confirmCheck);
		panel.add(new JLabel("비밀번호 입력 : "));
		panel.add(pwField);
		
		int result = JOptionPane.showConfirmDialog(this, panel, "회원 탈퇴 최종 확인", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			// 유효성 검사 - 체크박스 및 비밀번호 입력 빈칸일시, 비밀번호 일치 확인
			if (!confirmCheck.isSelected()) {
				JOptionPane.showMessageDialog(this, "탈퇴 동의에 체크해주세요.", "확인 오류", JOptionPane.WARNING_MESSAGE);
				return;
			}
			String inputPw = new String(pwField.getPassword()).trim();
			if (inputPw.isEmpty()) {
				JOptionPane.showMessageDialog(this, "비밀번호를 입력해주세요.", "입력 오류", JOptionPane.WARNING_MESSAGE);
				return;
			}
			TableUsersDTO user = UserSession.getInstance().getUser();
			String hashedInputPw = hashPassword(inputPw);
			if (!hashedInputPw.equals(user.getPassword())) {
				JOptionPane.showMessageDialog(this, "비밀번호가 일치하지 않습니다.", "인증 오류", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			// 탈퇴 처리
			TableUsersDAO dao = new TableUsersDAO();
			int deleteResult = dao.deactivateUser(user.getUsername());
			if (deleteResult > 0) {
				JOptionPane.showMessageDialog(this, "회원 탈퇴 처리가 완료되었습니다.\n이용해 주셔서 감사합니다.", "탈퇴 성공", JOptionPane.INFORMATION_MESSAGE);
				// 로그아웃 처리 후 로그인 화면으로 이동
				UserSession.getInstance().logout();
				setVisible(false);
				(new LoginGUI()).setVisible(true);
			} else {
				JOptionPane.showMessageDialog(this, "회원 탈퇴 처리에 실패하였습니다.\n관리자에게 문의해주세요.", "탈퇴 오류", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	// 비밀번호 암호화 메서드
	private String hashPassword(String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(password.getBytes());
			byte[] byteData = md.digest();
			StringBuilder sb = new StringBuilder();
			for (byte b : byteData) {
				sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
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
			setVisible(false);
			(new DetailsPostsGUI()).setVisible(true);
			
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
			
		} else if(event.getSource() == btnuseredit) {
			// 로그인 중인 회원 정보 수정
			setVisible(false);
			(new DetailsEditGUI()).setVisible(true);
			
		} else if(event.getSource() == btnpwedit) {
			// 비밀번호 수정
			setVisible(false);
			(new DetailsPwEditGUI()).setVisible(true);
		} else if(event.getSource() == btndeleteuser) {
			// 회원탈퇴
			deleteUserData();
		}
	}
	
	public static void main(String[] args) {
		DetailsGUI detailsGui = new DetailsGUI();
		if (detailsGui.isDisplayable()) {
			detailsGui.setVisible(true);
		}
	}
}
 