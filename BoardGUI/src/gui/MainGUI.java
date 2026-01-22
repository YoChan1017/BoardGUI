package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import dbms.boards.TableBoardsDAO;
import dbms.boards.TableBoardsDTO;
import dbms.posts.TablePostsDAO;
import dbms.posts.TablePostsDTO;
import dbms.users.TableUsersDTO;
import gui.board.boards.BoardGUI;
import gui.board.posts.PostViewGUI;
import session.UserSession;

public class MainGUI extends JFrame implements ActionListener{
	
	// 필드
	private JPanel topPanel, dashboardPanel;
	private JButton btnmain, btnuser, btnlogout, btnexit;
	
	private static final int MAX_TOP_BUTTONS = 5;
	
	// 생성자
	public MainGUI() {
		setTitle("MAIN");
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
		
		topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		JPanel centerPanel = new JPanel(new BorderLayout());
		JPanel bottomPanel = new JPanel();
		
		// topPanel에 게시판 종류 버튼(추가될 때마다 표시해함(공지사항, 건의사항, 자유게시판, 이후 추가될때마다 표시))
		// 눌렀을시 BoardGUI화면으로 이동되며 해당 게시판의 정보 표시
		updateBoardButtons();
		
		// centerPanel
		// 접속중인 계정의 닉네임, 게시판별 최신 정보(5~10개 정도의 최신 작성글) 등 표시
		centerPanel.setBorder(new EmptyBorder(30, 50, 30, 50));
		// 상단 환영문구
		TableUsersDTO user =UserSession.getInstance().getUser();
		String nickname = (user != null) ? user.getNickname() : "게스트";
		String role = (user != null) ? user.getRole() : "";
		JLabel lblWelcome = new JLabel("<html><div style='text-align:center;'><font size='6'><b>" + nickname + "</b>님 환영합니다!</font><br><font size='3' color='gray'> 등급 : " + role + "</font></div></html>", SwingConstants.CENTER);
		lblWelcome.setBorder(new EmptyBorder(0, 0, 20, 0));
		centerPanel.add(lblWelcome, BorderLayout.NORTH);
		// 게시판별 최신 정보 표시
		dashboardPanel = new JPanel(new GridLayout(0, 2, 15, 15));
		JScrollPane scrollPane = new JScrollPane(dashboardPanel);
		scrollPane.setBorder(null);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16); // 스크롤 속도
		// 게시판 최신 데이터 로드
		loadAllBoardsLatestData();
		centerPanel.add(scrollPane, BorderLayout.CENTER);
		
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
		setLocationRelativeTo(null);
	}
	
	// 메서드
	// 게시판 버튼 생성/갱신
	private void updateBoardButtons() {
		topPanel.removeAll();
		TableBoardsDAO boardDao = new TableBoardsDAO();
		List<TableBoardsDTO> boardList = boardDao.getAllBoards();
		
		if (boardList != null) {
			List<TableBoardsDTO> activeBoards = new ArrayList<>();
			for (TableBoardsDTO b : boardList) {
				if (b.isActive()) activeBoards.add(b);
			}
			
			int size = activeBoards.size();
			if (size <= MAX_TOP_BUTTONS) { // 버튼 개수가 제한 이하일 때
				for (TableBoardsDTO board : activeBoards) {
					addBoardButton(board);
				} 
			} else { // 버튼 개수가 제한 초과할 때
				for (int i = 0; i < MAX_TOP_BUTTONS - 1; i++) {
					addBoardButton(activeBoards.get(i));
				}
				
				JButton btnMore = new JButton("...");
				JPopupMenu popupMenu = new JPopupMenu();
				
				// 나머지 게시판버튼은 팝업 메뉴 아이템으로 추가
				for (int i = MAX_TOP_BUTTONS - 1; i <size; i++) {
					TableBoardsDTO hiddenBoard = activeBoards.get(i);
					JMenuItem item = new JMenuItem(hiddenBoard.getName());
					item.addActionListener(e -> {
						setVisible(false);
						new BoardGUI(hiddenBoard).setVisible(true);
					});
					popupMenu.add(item);
				}
				
				btnMore.addActionListener(e -> {
					popupMenu.show(btnMore, 0, btnMore.getHeight());
				});
				
				topPanel.add(btnMore);
			}
		}
		topPanel.revalidate();
		topPanel.repaint();
	}
	
	// 게시판 버튼 추가 헬퍼
	private void addBoardButton(TableBoardsDTO board) {
		JButton btn = new JButton(board.getName());
		btn.addActionListener(e -> {
			setVisible(false);
			new BoardGUI(board).setVisible(true);
		});
		topPanel.add(btn);
	}
	
	// 게시판의 최신 정보를 centerPanel load
	private void loadAllBoardsLatestData() {
		
	}
	
	// 게시판 미니 테이블 Panel
	private JPanel createMiniBoardPanel(TableBoardsDTO board, TablePostsDAO postDao) {
		JPanel p = new JPanel(new BorderLayout());
		p.setBorder(new TitledBorder(null, board.getName(), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12), Color.DARK_GRAY));
		p.setBackground(Color.WHITE);
		p.setPreferredSize(new Dimension(300, 150));
		
		String[] colNames = {"제목", "작성일"};
		DefaultTableModel model = new DefaultTableModel(colNames, 0) {
			@Override
			public boolean isCellEditable(int row, int column) { return false; }
		};
		JTable table = new JTable(model);
		table.setRowHeight(20);
		table.setShowGrid(false);
		
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		table.getColumn("작성일").setCellRenderer(centerRenderer);
		table.getColumn("작성일").setPreferredWidth(80);
		
		// Data Load
		ArrayList<TablePostsDTO> posts = postDao.getPostsByBoardId(board.getBoardId());
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
		if (posts != null && !posts.isEmpty()) {
			int count = 0;
			for (TablePostsDTO post : posts) {
				if (count >= 5) break;
				String title = post.getTitle();
				if(post.isNotice()) title = "[공지] " + title;
				if(post.isSecret()) title = "🔒 " + title;
				
				model.addRow(new Object[] { title, sdf.format(post.getCreatedAt()) });
				count++;
			}
		} else {
			model.addRow(new Object[] { "(게시글 없음)", "" });
		}
		
		// 클릭 시 해당 글로 이동
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int row = table.getSelectedRow();
					if (row != -1 && posts != null && row < posts.size()) {
						TablePostsDTO selectedPost = posts.get(row);
						PostViewGUI view = new PostViewGUI(board, selectedPost.getPostId());
						if (view.checkPermission()) {
							setVisible(false);
							view.setVisible(true);
						} else {
							view.dispose();
						}
					}
				}
			}
			
		});
		p.add(new JScrollPane(table), BorderLayout.CENTER);
		
		// 해당 게시판 이동 버튼 
		JButton btnMore = new JButton("더보기 > ");
		btnMore.setBorderPainted(false);
		btnMore.setContentAreaFilled(false);
		btnMore.setHorizontalAlignment(SwingConstants.RIGHT);
		btnMore.addActionListener(e -> {
			setVisible(false);
			new BoardGUI(board).setVisible(true);
		});
		p.add(btnMore, BorderLayout.SOUTH);
		
		return p;
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
		}
	}

	public static void main(String[] args) {
		// 생성자가 정상적으로 실행하여 띄었을 경우에만 실행
		MainGUI mainGui = new MainGUI();
		if (mainGui.isDisplayable()) {
			mainGui.setVisible(true);
		}
	}
}
