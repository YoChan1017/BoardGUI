package gui.board.posts;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import dbms.StorageSetup;
import dbms.attachments.TableAttachmentsDAO;
import dbms.attachments.TableAttachmentsDTO;
import dbms.boards.TableBoardsDTO;
import dbms.comments.TableCommentsDAO;
import dbms.comments.TableCommentsDTO;
import dbms.posts.TablePostsDAO;
import dbms.posts.TablePostsDTO;
import dbms.users.TableUsersDAO;
import dbms.users.TableUsersDTO;
import dbms.users.TableUsersRole;
import gui.LoginGUI;
import gui.MainGUI;
import gui.board.boards.BoardGUI;
import gui.details.DetailsGUI;
import session.UserSession;

public class PostViewGUI extends JFrame implements ActionListener {
	
	// 필드
	private TableBoardsDTO currentBoard;
	private TablePostsDTO currentPost;
	private TableAttachmentsDTO attachedFile;
	private int postId;
	private JLabel lblTitle, lblWriter, lblDate, lblViewCount, lblFile;
	private JTextArea txtContent, txtCommentInput;
	private JPanel commentListPanel;
	private JCheckBox chkCommentSecret;
	private JButton btnmain, btnuser, btnlogout, btnexit, btndownload, btnlist, btnupdate, btndelete, btncommentadd;
	
	// 생성자
	public PostViewGUI(TableBoardsDTO board, int postId) {
		this.currentBoard = board;
		this.postId = postId;
		
		setTitle(currentBoard.getName() + " - 글 상세보기");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		setSize(800, 600);
		
		// 현재 GUI화면 진입 시 로그인 체크 여부
		if (!UserSession.getInstance().isLoggedIn()) {
			// 현재 생성자를 종료 후 로그인 화면으로 이동
			JOptionPane.showMessageDialog(this, "로그인을 먼저 해주세요.", "접근 제한", JOptionPane.WARNING_MESSAGE);	
			dispose();
			(new LoginGUI()).setVisible(true);
			return;
		}
		
		// 데이터 load
		loadPostData();
		
		// 비밀글 체크
		if (!checkPermission()) {
			return;
		}
		
		// 글 존재 여부
		if (currentPost == null) {
			JOptionPane.showMessageDialog(this,  "삭제되거나 존재하지 않는 게시글입니다.", "열람 오류", JOptionPane.ERROR_MESSAGE);
			dispose();
			(new BoardGUI(currentBoard)).setVisible(true);
			return;
		}
		
		// UI 구성
		JPanel topPanel = new JPanel(new BorderLayout());
		JPanel centerPanel = new JPanel(new BorderLayout());
		JPanel bottomPanel = new JPanel();
		
		// topPanel
		// 게시판 이름
		JLabel lblBoardName = new JLabel(currentBoard.getName());
		lblBoardName.setHorizontalAlignment(SwingConstants.CENTER);
		lblBoardName.setBorder(new EmptyBorder(10, 0, 10, 0));
		topPanel.add(lblBoardName, BorderLayout.CENTER);
		
		// centerPanel
		// 게시글 정보/내용/첨부파일
		centerPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
		// 게시글 정보
		JPanel infoPanel = createInfoPanel();	
		// 게시글 내용
		txtContent = new JTextArea();
		txtContent.setText(currentPost.getContent());
		txtContent.setEditable(false);
		txtContent.setLineWrap(true);
		JScrollPane contentScroll = new JScrollPane(txtContent);
		contentScroll.setBorder(new EmptyBorder(10, 0, 10, 0));
		// centerPanel에서 하단 Panel > 첨부파일, 댓글, 버튼(수정/삭제/목록)
		JPanel bottomContentPanel = new JPanel(new BorderLayout());
		// 첨부파일
		JPanel filePanel = createFilePanel();
		// 댓글 Panel
		JPanel commentPanel = createCommentPanel();	
		// 목록/수정/삭제 버튼
		JPanel actionPanel = createActionPanel();
		// centerPanel 내부 배치
		bottomContentPanel.add(filePanel, BorderLayout.NORTH);
		bottomContentPanel.add(commentPanel, BorderLayout.CENTER);
		bottomContentPanel.add(actionPanel, BorderLayout.SOUTH);
		centerPanel.add(infoPanel, BorderLayout.NORTH);
		centerPanel.add(contentScroll, BorderLayout.CENTER);
		centerPanel.add(bottomContentPanel, BorderLayout.SOUTH);
		
		
		// bottomPanel
		btnmain = new JButton("HOME");
		btnmain.addActionListener(this);
		btnuser = new JButton("내 정보");
		btnuser.addActionListener(this);
		btnlogout = new JButton("로그아웃");
		btnlogout.addActionListener(this);
		btnexit = new JButton("종료");
		btnexit.addActionListener(this);
				
		bottomPanel.add(btnmain);
		bottomPanel.add(btnuser);
		bottomPanel.add(btnlogout);
		bottomPanel.add(btnexit);
				
		add(topPanel, BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);
		
		loadComments();
		
		setLocationRelativeTo(null);
		
		setVisible(true);
	}
	
	// 메서드
	// 데이터불러오기 및 조회수 증가
	private void loadPostData() {
		TablePostsDAO dao = new TablePostsDAO();
		dao.increaseViewCount(postId); 			// 조회수 증가
		currentPost = dao.getPostById(postId);	// 게시글 정보 가져오기
	}
	
	// 비밀글 열람 확인
	public boolean checkPermission() {
		// 일단 게시글이 존재하는지
		if (currentPost == null) {
			JOptionPane.showMessageDialog(this, "삭제되거나 존재하지 않는 게시글입니다.", "열람 오류", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		// 비밀글에 대한 권한이 있는지
		if (currentPost != null && currentPost.isSecret() ) {
			int currentUserId = UserSession.getInstance().getUser().getUserId();
			String roleStr = UserSession.getInstance().getUser().getRole();
			TableUsersRole userRole = TableUsersRole.fromDbRole(roleStr);
			if (currentPost.getUserId() != currentUserId && userRole != TableUsersRole.ADMIN) {
				JOptionPane.showMessageDialog(this, "비밀글은 작성자와 관리자만 볼 수 있습니다.", "열람 불가", JOptionPane.WARNING_MESSAGE);
				this.dispose();
				(new BoardGUI(currentBoard)).setVisible(true);
				return false;
			}
		}
		return true;
	}
	
	// 게시글 헤더
	private JPanel createInfoPanel() {
		JPanel infoPanel = new JPanel(new BorderLayout());
		infoPanel.setBorder(new MatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
		lblTitle = new JLabel(" " + currentPost.getTitle());
		lblTitle.setBorder(new EmptyBorder(5, 0, 5, 0));
		JPanel subInfoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		TableUsersDAO userDAO = new TableUsersDAO();
		TableUsersDTO writer = userDAO.getUserById(currentPost.getUserId());
		String writerName = (writer != null) ? writer.getNickname() : "알수없음";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String dateStr = sdf.format(currentPost.getCreatedAt());
		lblWriter = new JLabel("작성자 : " + writerName + " | ");
		lblDate = new JLabel("작성일 : " + dateStr + " | ");
		lblViewCount = new JLabel("조회 : " + currentPost.getViewCount());
		subInfoPanel.add(lblWriter);
		subInfoPanel.add(lblDate);
		subInfoPanel.add(lblViewCount);
		infoPanel.add(lblTitle, BorderLayout.CENTER);
		infoPanel.add(subInfoPanel, BorderLayout.SOUTH);
		return infoPanel;
	}
	
	// 첨부파일 Panel
	private JPanel createFilePanel() {
		JPanel filePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		filePanel.setBorder(BorderFactory.createTitledBorder("첨부파일"));
		TableAttachmentsDAO attachDAO = new TableAttachmentsDAO();
		ArrayList<TableAttachmentsDTO> fileList = attachDAO.getAttachmentsByPostId(postId);
		if (fileList != null && !fileList.isEmpty()) {
			attachedFile = fileList.get(0);
			long fileSizeKB = attachedFile.getFileSize() / 1024;
			if (fileSizeKB == 0 && attachedFile.getFileSize() > 0) fileSizeKB = 1;
			lblFile = new JLabel(attachedFile.getOriginName() + " (" + fileSizeKB + " KB");
			btndownload = new JButton("다운로드");
			btndownload.addActionListener(this);
			filePanel.add(lblFile);
			filePanel.add(btndownload);
		} else {
			filePanel.add(new JLabel("첨부된 파일이 없습니다"));
		}
		return filePanel;
	}
	
	// 첨부파일 다운로드 
	private void downloadFile() {
		if (attachedFile == null) return;
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setSelectedFile(new File(attachedFile.getOriginName()));
		int option = fileChooser.showSaveDialog(this);
		if (option == JFileChooser.APPROVE_OPTION) {
			File saveDest = fileChooser.getSelectedFile();
			File sourceFile = new File(StorageSetup.SAVE_DIR + attachedFile.getSaveName());
			if (sourceFile.exists()) {
				try {
					Files.copy(sourceFile.toPath(), saveDest.toPath(), StandardCopyOption.REPLACE_EXISTING);
					JOptionPane.showMessageDialog(this, "파일이 성공적으로 다운로드 되었습니다.", " 다운로드 완료", JOptionPane.INFORMATION_MESSAGE);
				} catch (IOException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(this,  "파일 다운로드 중 오류가 발생하였습니다.\n" + e.getMessage(), "파일 오류", JOptionPane.ERROR_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(this, "원본 파일이 존재하지 않습니다.", "파일 없음", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	// 댓글 Panel
	private JPanel createCommentPanel() {
		JPanel commentPanel = new JPanel(new BorderLayout());
		commentPanel.setBorder(BorderFactory.createTitledBorder("댓글"));
		// 댓글 목록 영역
		commentListPanel = new JPanel();
		commentListPanel.setLayout(new BoxLayout(commentListPanel, BoxLayout.Y_AXIS)); // 수직 나열
		commentListPanel.setBackground(Color.WHITE);
		JScrollPane commentScroll = new JScrollPane(commentListPanel);
		commentScroll.setPreferredSize(new Dimension(0, 150));
		commentScroll.getVerticalScrollBar().setUnitIncrement(16); // scroll 속도
		// 댓글 입력 영역
		JPanel commentInputPanel = new JPanel(new BorderLayout());
		commentInputPanel.setBorder(new EmptyBorder(5, 0, 5, 0));
		txtCommentInput = new JTextArea(3, 50);
		txtCommentInput.setLineWrap(true);
		JScrollPane inputScroll = new JScrollPane(txtCommentInput);
		// 댓글 입력 버튼 + 비밀댓글 체크
		JPanel inputRightPanel = new JPanel(new BorderLayout());
		chkCommentSecret = new JCheckBox("비밀");
		chkCommentSecret.setHorizontalAlignment(SwingConstants.CENTER);
		btncommentadd = new JButton("등록");
		btncommentadd.addActionListener(this);
		inputRightPanel.add(chkCommentSecret, BorderLayout.NORTH);
		inputRightPanel.add(btncommentadd, BorderLayout.CENTER);
		commentInputPanel.add(inputScroll, BorderLayout.CENTER);
		commentInputPanel.add(inputRightPanel, BorderLayout.EAST);
		commentPanel.add(commentScroll, BorderLayout.CENTER);
		commentPanel.add(commentInputPanel, BorderLayout.SOUTH);
		
		return commentPanel;
	}
	
	// 댓글 목록 불러오기
	private void loadComments() {
		commentListPanel.removeAll();
		TableCommentsDAO commentDAO = new TableCommentsDAO();
		TableUsersDAO userDAO = new TableUsersDAO();
		ArrayList<TableCommentsDTO> list = commentDAO.getCommentsByPostId(postId);
		int currentUserId = UserSession.getInstance().getUser().getUserId();
		String roleStr = UserSession.getInstance().getUser().getRole();
		boolean isAdmin = "admin".equalsIgnoreCase(roleStr);
		int postWriterId = currentPost.getUserId();
		if (list != null) {
			for (TableCommentsDTO c : list) {
				// System.out.println("댓글작성자 ID : " + c.getUserId() + " / 내ID : " + currentUserId);
				// 개별 댓글 Panel
				JPanel rowPanel = new JPanel(new BorderLayout());
				rowPanel.setBorder(new MatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
				rowPanel.setBackground(Color.WHITE);
				// Header Panel
				JPanel headerPanel = new JPanel(new BorderLayout());
				headerPanel.setBackground(Color.WHITE);
				headerPanel.setBorder(new EmptyBorder(5, 5, 2, 5));
				// 닉네임 조회
				TableUsersDTO commentWriter = userDAO.getUserById(c.getUserId());
				String writerName = (commentWriter != null) ? commentWriter.getNickname() : "(알수없음)";
				SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
				// 작성자 + 날짜
				String headerText = "<html><b>" + writerName + "</b><font color='gray' size='2'>(" + sdf.format(c.getCreatedAt()) + ")</font></html>";
				JLabel lblHeader = new JLabel(headerText);
				headerPanel.add(lblHeader, BorderLayout.CENTER);
				
				JPanel headerRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
				headerRightPanel.setBackground(Color.WHITE);
				// 댓글 삭제 버튼
				if (!c.isDeleted() && (c.getUserId() == currentUserId || isAdmin)) {
					JButton btndel = new JButton("댓글 삭제");
					btndel.setMargin(new Insets(0, 2, 0, 2));
					btndel.setFocusable(false);
					btndel.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							int choice = JOptionPane.showConfirmDialog(PostViewGUI.this, "댓글을 삭제하시겠습니까?", "삭제 확인", JOptionPane.YES_NO_OPTION);
							if (choice == JOptionPane.YES_OPTION) {
								int result = commentDAO.deleteComment(c.getCommentId());
								if (result > 0) {
									loadComments();
								} else {
									JOptionPane.showMessageDialog(PostViewGUI.this, "댓글 삭제 실패");
								}
							}
						}
					});
					headerRightPanel.add(btndel);
				}
				headerPanel.add(headerRightPanel, BorderLayout.EAST);
				
				// 내용
				String contentText = c.getContent();
				JLabel lblContent = new JLabel();
				lblContent.setBorder(new EmptyBorder(0, 0, 5, 5));
				
				JPanel contentPanel = new JPanel(new BorderLayout(5, 0));
				contentPanel.setBackground(Color.WHITE);
				// 삭제된 댓글 처리
				if (c.isDeleted()) {
					lblContent.setText("<html><font color='gray'><i>삭제된 댓글입니다.</i></font></html>");
					contentPanel.add(lblContent, BorderLayout.CENTER);
				} else if (c.isSecret()) { // 비밀 댓글 처리 > 작성자와 관리자만 볼 수 있음
					if (c.getUserId() == currentUserId || currentUserId == postWriterId || isAdmin) {
						JLabel lblLock = new JLabel("🔒");
						lblLock.setForeground(Color.RED);
						lblLock.setBorder(new EmptyBorder(0, 10, 5, 0));
						lblContent.setText("<html>" + contentText + "</html>");
						contentPanel.add(lblLock, BorderLayout.WEST);
						contentPanel.add(lblContent, BorderLayout.CENTER);
					} else {
						JLabel lblLock = new JLabel("🔒");
						lblLock.setForeground(Color.RED);
						lblLock.setBorder(new EmptyBorder(0, 10, 5, 0));
						lblContent.setText("비밀 댓글입니다.");
						contentPanel.add(lblLock, BorderLayout.WEST);
						contentPanel.add(lblContent, BorderLayout.CENTER);
					}
				} else { // 일반 댓글 처리
					lblContent.setBorder(new EmptyBorder(0, 10, 5, 0));
					lblContent.setText(contentText);
					contentPanel.add(lblContent, BorderLayout.CENTER);
				}
				rowPanel.add(headerPanel, BorderLayout.NORTH);
				rowPanel.add(contentPanel, BorderLayout.CENTER);
				
				commentListPanel.add(rowPanel);
			}
		}
		// 화면 갱신
		commentListPanel.revalidate();
		commentListPanel.repaint();
	}
	
	// 댓글 등록
	private void addComment() {
		String content = txtCommentInput.getText().trim();
		if (content.isEmpty()) {
			JOptionPane.showMessageDialog(this, "댓글 내용을 입력해주세요.");
			return;
		}
		
		TableCommentsDTO newComment = new TableCommentsDTO();
		newComment.setPostId(postId);
		newComment.setUserId(UserSession.getInstance().getUser().getUserId());
		newComment.setContent(content);
		newComment.setSecret(chkCommentSecret.isSelected());
		
		TableCommentsDAO dao = new TableCommentsDAO();
		int result = dao.insertComment(newComment);
		if (result > 0) {
			txtCommentInput.setText("");
			chkCommentSecret.setSelected(false);
			loadComments(); // 등록 후 새로고침
		} else {
			JOptionPane.showMessageDialog(this, "댓글 등록 실패", "등록 오류", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	// 버튼(수정/목록/삭제) Panel
	private JPanel createActionPanel() {
		JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		btnlist = new JButton("목록");
		btnlist.addActionListener(this);
		actionPanel.add(btnlist);
		int currentUserId = UserSession.getInstance().getUser().getUserId();
		String roleStr = UserSession.getInstance().getUser().getRole();
		boolean isAdmin = "admin".equalsIgnoreCase(roleStr);
		if (currentPost.getUserId() == currentUserId || isAdmin) {
			btnupdate = new JButton("수정");
			btndelete = new JButton("삭제");
			btnupdate.addActionListener(this);
			btndelete.addActionListener(this);
			actionPanel.add(btnupdate);
			actionPanel.add(btndelete);
		}
		return actionPanel;
	}
	
	// 게시글 삭제처리
	private void deletePost() {
		int choice = JOptionPane.showConfirmDialog(this, "정말로 해당 게시글을 삭제하시겠습니까?", "삭제 확인", JOptionPane.YES_NO_OPTION);
		if (choice == JOptionPane.YES_OPTION) {
			TablePostsDAO dao = new TablePostsDAO();
			int result = dao.deletePost(postId);
			if (result > 0) {
				JOptionPane.showMessageDialog(this, "게시글이 삭제되었습니다.");
				setVisible(false);
				(new BoardGUI(currentBoard)).setVisible(true);
			} else {
				JOptionPane.showMessageDialog(this, "삭제실패");
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == btnmain) {
			// 로그인 세션 보유한채로 새로고침
			setVisible(false);
			(new MainGUI()).setVisible(true);		
			
		} else if(event.getSource() == btnuser) {
			// 로그인 세션 보유한채로 내 정보화면으로 이동
			setVisible(false);					
			(new DetailsGUI()).setVisible(true);
			
		} else if(event.getSource() == btnlogout) {
			// 세션 제거 추가 - 로그아웃 처리
			UserSession.getInstance().logout();
			JOptionPane.showMessageDialog(this, "로그아웃 되었습니다.");
			// 로그아웃 후 로그인으로 다시 이동
			setVisible(false);						
			(new LoginGUI()).setVisible(true);
			
		} else if(event.getSource() == btnexit) {
			// 세션 제거 추가 - 로그아웃 처리
			// 프로그램 종료로 세션 자동 소멸
			System.exit(0);
			
		} else if(event.getSource() == btnlist) {
			// 게시판(게시글 목록으로 이동)
			setVisible(false);
			(new BoardGUI(currentBoard)).setVisible(true);
			
		} else if(event.getSource() == btndelete) {
			// 해당 게시글 삭제
			deletePost();		
			
		} else if(event.getSource() == btnupdate) {
			// 해당 게시글 수정 화면으로 이동
			setVisible(false);
			(new PostEditGUI(currentBoard, postId)).setVisible(true);
			
		} else if(event.getSource() == btndownload) {
			// 첨부파일 다운로드
			downloadFile();
			
		} else if(event.getSource() == btncommentadd) {
			// 댓글 등록
			addComment();
		}
	}

	public static void main(String[] args) {
		
	}
}
