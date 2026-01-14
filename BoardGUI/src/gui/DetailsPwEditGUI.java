package gui;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;

import dbms.users.TableUsersDAO;
import dbms.users.TableUsersDTO;
import session.UserSession;

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
		
		// 현재 GUI화면 진입 시 로그인 체크 여부
		if (!UserSession.getInstance().isLoggedIn()) {
			// 현재 생성자를 종료 후 로그인 화면으로 이동
			JOptionPane.showMessageDialog(this, "로그인을 먼저 해주세요.", "접근 제한", JOptionPane.WARNING_MESSAGE);		
			dispose();
			(new LoginGUI()).setVisible(true);
			return;
		}
		
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
	
	private void pwEdit() {
		// 변경된 비밀번호 저장
		// 비밀번호 암호화
		// 기존 비밀번호(MyPw) 맞는지 확인 > 틀렸을시 알림
		// 신규 비밀번호(EditPw)와 비밀번호 확인(ReEditPw)이 서로 맞는지 확인 > 틀렸을시 알림
		// 모든 입력이 올바를 경우 변경 알림 > 확인 누르면 변경, 취소 누르면 변경 내용 취소
		// 올바르게 변경완료시 DetailsGUI로 이동
		
		String currentPw = new String(MyPw.getPassword()).trim();
		String newPw = new String(EditPw.getPassword()).trim();
		String confirmPw = new String(ReEditPw.getPassword()).trim();
		// 입력칸이 전부 비어있을시
		if(currentPw.isEmpty() || newPw.isEmpty() || confirmPw.isEmpty()) {
			JOptionPane.showMessageDialog(this, "모든 정보를 입력해주세요.", "입력 오류", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		// 현재 비밀번호 일치 여부 확인
		TableUsersDTO currentUser = UserSession.getInstance().getUser();
		String hashedCurrentPw = hashPassword(currentPw); // 입력받은 현재 비밀번호 암호화
		// 현재 비밀번호가 일치하지 않을시
		if(!hashedCurrentPw.equals(currentUser.getPassword())) {
			JOptionPane.showMessageDialog(this, "현재 비밀번호가 일치하지 않습니다.", "입력 오류", JOptionPane.ERROR_MESSAGE);
			return;
		}
		// 신규 비밀번호와 확인 비밀번호 일치 여부
		if(!newPw.equals(confirmPw)) {
			JOptionPane.showMessageDialog(this, "신규 비밀번호가 서로 일치하지 않습니다.", "입력 오류", JOptionPane.WARNING_MESSAGE);
			return;
		}	
		// 변경 의사 확인
		int choice = JOptionPane.showConfirmDialog(this, "비밀번호를 변경하시겠습니까?", "입력 확인", JOptionPane.YES_NO_OPTION);
		if(choice != JOptionPane.YES_OPTION) {
			return; // 취소 시 중단
		}
		
		// 데이터베이스(테이블) 업데이트
		String hashedNewPw = hashPassword(newPw); // 신규 비밀번호 암호화
		// 기존 정보는 그래도 두고 비밀번호만 변경
		TableUsersDTO updatedUser = new TableUsersDTO(
				currentUser.getUserId(),
				currentUser.getUsername(),
				hashedNewPw, // 변경된 비밀번호
				currentUser.getNickname(),
				currentUser.getBirthDate(),
				currentUser.getPhone(),
				currentUser.getEmail(),
				currentUser.getRole(),
				currentUser.isActive(),
				currentUser.getCreatedAt()
				);
		TableUsersDAO dao = new TableUsersDAO();
		int result = dao.updateUser(updatedUser);
		if(result > 0) {
			// 세션 갱신
			UserSession.getInstance().login(updatedUser);
			JOptionPane.showMessageDialog(this, "비밀번호가 성공적으로 변경되었습니다.", "변경 성공", JOptionPane.INFORMATION_MESSAGE);
			// 완료 후 DetailsGUI 화면으로 이동
			setVisible(false);
			(new DetailsGUI()).setVisible(true);
		} else {
			JOptionPane.showMessageDialog(this, "비밀번호 변경에 실패했습니다.", "변경 오류", JOptionPane.ERROR_MESSAGE);
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
	
	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == btnpwedit) {
			// 비밀번호 변경 저장 후 DetailsGUI 이동
			pwEdit();
			
		} else if(event.getSource() == btncancel) {
			// 입력 취소
			reset();
			
		} else if(event.getSource() == btnback) {
			// DetailsGUI 이동
			setVisible(false);						
			(new DetailsGUI()).setVisible(true);
			
		} else if(event.getSource() == btnexit) {
			// 프로그램 종료
			System.exit(0);
		}
	}

	public static void main(String[] args) {
		DetailsPwEditGUI detailsPwGui = new DetailsPwEditGUI();
		if (detailsPwGui.isDisplayable()) {
			detailsPwGui.setVisible(true);
		}
	}
}
