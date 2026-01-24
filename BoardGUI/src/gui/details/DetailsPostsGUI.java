package gui.details;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import dbms.boards.TableBoardsDAO;
import dbms.boards.TableBoardsDTO;
import dbms.posts.TablePostsDAO;
import dbms.posts.TablePostsDTO;
import gui.LoginGUI;
import gui.MainGUI;
import gui.board.posts.PostViewGUI;
import session.UserSession;

public class DetailsPostsGUI extends JFrame implements ActionListener {

	// 필드
	private JButton btnmain, btnuser, btnlogout, btnexit, btnsearch, btndelete;
	private JLabel lblBoardName;
	private JComboBox<String> cbSearchType;
	private JTextField txtSearch;
	private DefaultTableModel tableModel;
	private JTable postTable;
	
	
	// 생성자
	public DetailsPostsGUI() {
		
		setTitle("내가 작성한 글");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
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
		JPanel centerPanel = new JPanel(new BorderLayout());
		JPanel bottomPanel = new JPanel();
		
		// 상단(topPanel)
		lblBoardName = new JLabel("내가 작성한 글 목록");
		lblBoardName.setHorizontalAlignment(SwingConstants.CENTER);
		topPanel.add(lblBoardName);
		
		// 중앙(centerPanel)
		JPanel functionPanel = new JPanel(new BorderLayout());
		// 검색
		JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		String[] searchTypes = {"제목", "내용", "작성자"};
		cbSearchType = new JComboBox<>(searchTypes);
		txtSearch = new JTextField(20);
		btnsearch = new JButton("검색");
		searchPanel.add(cbSearchType);
		searchPanel.add(txtSearch);
		searchPanel.add(btnsearch);
		// 삭제 버튼
		JPanel deletePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		btndelete = new JButton("글 삭제");
		btndelete.addActionListener(this);
		deletePanel.add(btndelete);
		// functionPanel > 글 삭제, 글작성 버튼
		functionPanel.add(searchPanel, BorderLayout.WEST);
		functionPanel.add(deletePanel, BorderLayout.EAST);
		
		// 중앙(centerPanel) - 게시글 목록 Panel
		JPanel listContainerPanel = new JPanel(new BorderLayout());
		listContainerPanel.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
		// 목록 Header
		String[] columnNames = {"번호", "게시판", "제목", "작성일", "조회수"};
		tableModel = new DefaultTableModel(columnNames, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		// 목록 List
		postTable = new JTable(tableModel);
		postTable.setRowHeight(25);
		postTable.getColumn("번호").setPreferredWidth(50);
		postTable.getColumn("게시판").setPreferredWidth(300);
		postTable.getColumn("제목").setPreferredWidth(100);
		postTable.getColumn("작성일").setPreferredWidth(150);
		postTable.getColumn("조회수").setPreferredWidth(50);
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		postTable.getColumn("번호").setCellRenderer(centerRenderer);
		postTable.getColumn("게시판").setCellRenderer(centerRenderer);
		postTable.getColumn("작성일").setCellRenderer(centerRenderer);
		postTable.getColumn("조회수").setCellRenderer(centerRenderer);
		
		postTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) { // 클릭 횟수
					int row = postTable.getSelectedRow();
					if (row != -1) {
						int postId = (int) postTable.getValueAt(row, 0);
						openPostView(postId);
					}
				}
			}
		});
		
		JScrollPane scrollPane = new JScrollPane(postTable);
		scrollPane.getViewport().setBackground(Color.WHITE);
		listContainerPanel.add(scrollPane, BorderLayout.CENTER);
		
		centerPanel.add(functionPanel, BorderLayout.NORTH); 		// centerPanel에서 상단(북쪽)에 functionPanel 배치
		centerPanel.add(new JPanel(), BorderLayout.CENTER);
		centerPanel.add(listContainerPanel, BorderLayout.CENTER); 	// centerPanel에서 중앙에 listContainerPanel 배치
		
		loadPostList();	// 데이터 불러오기		
		
		
		// 하단(bottomPanel)
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
				
		// 상단, 중단, 하단 Panel 배치
		add(topPanel, BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);
		
		setLocationRelativeTo(null);
	}
	
	
	// 메서드
	// 게시글 목록 불러오기
	private void loadPostList() {
		// 내가 작성한 게시글 목록만 표시
		tableModel.setRowCount(0);
		int myUserId = UserSession.getInstance().getUser().getUserId();
		
		TablePostsDAO postDao = new TablePostsDAO();
		TableBoardsDAO boardDao = new TableBoardsDAO();
		
		ArrayList<TablePostsDTO> myPosts = postDao.getPostsByUserId(myUserId);
		
		if (myPosts != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			for (TablePostsDTO post : myPosts) {
				String title = post.getTitle();
				if (post.isSecret()) title = "🔒 " + title;
				
				TableBoardsDTO board = boardDao.getBoardById(post.getBoardId());
				String boardName = (board != null) ? board.getName() : "알수없음";
				Object[] rowData = {
						post.getPostId(),
						boardName,
						title,
						sdf.format(post.getCreatedAt()),
						post.getViewCount()
				};
				tableModel.addRow(rowData);
			}
		}
	}
	
	// 게시글 삭제
	private void deletePost() {
		// 게시글 목록 중 선택하여 삭제
		int row = postTable.getSelectedRow();
		if (row == -1) {
			JOptionPane.showMessageDialog(this, "삭제할 글을 선택해주세요.");
			return;
		}
		
		int postId = (int) postTable.getValueAt(row, 0);
		int choice = JOptionPane.showConfirmDialog(this, "정말로 삭제하시겠습니까?", "삭제 확인", JOptionPane.YES_NO_OPTION);
		if (choice == JOptionPane.YES_OPTION) {
			TablePostsDAO dao = new TablePostsDAO();
			int result = dao.deletePost(postId);
			if (result > 0) {
				JOptionPane.showMessageDialog(this, "게시글이 삭제되었습니다.");
				loadPostList();
			} else {
				JOptionPane.showMessageDialog(this, "삭제 실패", "삭제 오류", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	// 게시글 상세 보기
	private void openPostView(int postId) {
		TablePostsDAO postDao = new TablePostsDAO();
		TablePostsDTO post = postDao.getPostById(postId);
		if (post != null) {
			TableBoardsDAO boardDao = new TableBoardsDAO();
			TableBoardsDTO board = boardDao.getBoardById(post.getBoardId());
			if (board != null) {
				DetailsPostsViewGUI view = new DetailsPostsViewGUI(board, postId); 
				if (view.checkPermission()) {
					setVisible(false);
					view.setVisible(true);
				} else {
					view.dispose();
				}
			} else {
				JOptionPane.showMessageDialog(this, "게시글 정보를 찾을 수 없습니다.");
			}
		}
	}
	
	// 검색 기능
	private void searchPosts() {
		
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
			
		} else if(event.getSource() == btnsearch) {
			// 검색 버튼 기능 추가
			searchPosts();
			
		} else if(event.getSource() == btndelete) {
			// 글 삭제 기능 추가
			deletePost();
		}
	}

	public static void main(String[] args) {
		
	}
}
