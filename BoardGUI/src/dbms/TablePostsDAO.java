package dbms;

import java.util.ArrayList;

public class TablePostsDAO {

	// CRUD Query 작성
	// 조회수 증가, 특정 게시판의 게시글 목록 조회
	
		
	// 게시글 생성(INSERT)
	public int insertPost(TablePostsDTO post) {
		
	}
	
	// 게시글 목록 조회(SELECT)
	public TablePostsDTO getPostById(int postId) {
		
	}
	
	// 게시글 정보 수정(UPDATE)
	public int updatePost(TablePostsDTO post) {
		
	}
	
	// 게시판 삭제(DELETE)
	public int deletePost(int postId) {
		
	}
	
	// 조회수 증가
	public void increaseViewCount(int postId) {
		
	}
	
	// 특정 게시판의 게시글 목록 조회
	public ArrayList<TablePostsDTO> getPostsByBoardId(int boardId) {
		
	}
	
	// 필요시 추가작성
}
