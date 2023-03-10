package com.memo.post.bo;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.memo.common.FileManagerService;
import com.memo.post.dao.PostDAO;
import com.memo.post.model.Post;

@Service
public class PostBO {
	
//	private Logger logger = LoggerFactory.getLogger(PostBO.class); // slf4j 임포트
	private Logger logger = LoggerFactory.getLogger(this.getClass()); // slf4j 임포트 - 로그를 찍고 싶은 부분에서 사용하면 됌.
	
	private static final int POST_MAX_SIZE = 3;  // 한 페이지에 몇개씩 있게 할 것인지 상수로 저장.
	
	@Autowired
	private PostDAO postDAO;
	
	@Autowired
	private FileManagerService fileManagerService;
	
	// 글 추가
	public int addPost(int userId, String loginId, String subject,
			String content, MultipartFile file) {
		
		// 파일 업로드 => 내컴퓨터 서버에 올린다 =>경로로 보여지기
		String imagePath = null;
		if (file != null) {
			// 파일이 있을 때만 업로드 => 이미지 경로를 얻어냄
			imagePath = fileManagerService.saveFile(loginId, file);
		}
		
		// dao insert
		return postDAO.insertPost(userId, subject, content, imagePath);
	}
	
	// 글 , 사진 수정 
	public void updatePost(
			int userId,
			String loginId,
			int postId,
			String subdject,
			String content,
			MultipartFile file) { 
		// 행을 가져 온 담에 이미지 패스가 있는지 확인해야된다.
		
		// 기존 글을 가져온다. (이미지가 교체될 때 기존 이미지 제거를 위해)
		Post post = getPostByPostIdUserId(postId, userId); // 글 단건을 하나 가져온다.
		if (post == null) {
			logger.warn("[update post] 수정할 메모가 존재하지 않습니다. postId:{}, userId:{}", postId, userId);
			return;
		}
		
		// 파일이 비어있지 않다면 업로드 후 imagePath -> 업로드 성공 : 기존이미지 제거 / 업로드 실패 : 기존 이미지 유지
		String imagePath = null;
		if (file != null) {
			// 업로드 
		
			imagePath = fileManagerService.saveFile(loginId, file);
			// 업로드 성공하면 기존 이미지 제거 = 업로드가 실패 할 수 있으므로 업로드가 성공한 후 제거
			// imagePath가 널이 아니고, 기존 글에 imagePath가 널이 아닐 경우
			if (imagePath != null && post.getImagePath() != null) {
				// 이미지 제거
				fileManagerService.deleteFile(post.getImagePath());
			}
		}
		
		
		// db update
		postDAO.updatePostByPostIdUserId(postId, userId, subdject, content, imagePath);
		
	}
	
	// 글 삭제
	public int deletePostByPostIdUserId(int postId, int userId) {
		// 그냥 글 삭제하면 이미지가 남아있기 떄문에 원래 글을 가져온 후 삭제.
		
		// 1. 기존글 가져오기
		Post post = getPostByPostIdUserId(postId, userId);
		if (post == null) { // post null 체크 꼭 해야한다.
			logger.warn("[글 삭제] post is null. postId:{}, userId{}", postId, userId);
			return 0;
		}
		// 2. 업로드 되었던 이미지가 있으면 파일 삭제
		if (post.getImagePath() != null) {
			fileManagerService.deleteFile(post.getImagePath());
		}
		// 3. DB delete
		return postDAO.deletePostByPostIdUserId(postId, userId);
	}
	
	public List<Post> getPostListByUserId(int userId, Integer prevId, Integer nextId){//서비스를 만들 때는 객체로 해야된다.
		// 게시글 번호 :  10 9 8 | 7 6 5| 4 3 2| 1
		// 만약 4 3 2 페이지에 있을 때 
		// 1) 이전 : 정방향ASC 4보다 큰 3개 (5 6 7) = > 뿌려질 땐 7 6 5 => List reverse
		// 2) 다음 :  2보다 작은 3개 DESC 
		// 3) 첫 페이지 (이전, 다음 없음) : DESC 3개
		String direction = null; // 방향
		Integer standardId = null; // 기준 postId
		
		if (prevId != null) { // 여기에 오면 이전
			direction = "prev";
			standardId = prevId;
			
			List<Post> postList = postDAO.selectPostListByUserId(userId, direction, standardId, POST_MAX_SIZE);
			Collections.reverse(postList); // 리스트를 뒤집는 메소드 (바꿔놓고 저장까지 해줌:void)
			return postList;
		} else if (nextId != null) { // 다음
			direction = "next";
			standardId = nextId;
		} 
		// 첫 페이지 일 때 ( 페이징 안 함)  standardId, direction == null
		// 다음일 때 standardId, directionId 채워져서 넘어감
		return postDAO.selectPostListByUserId(userId, direction, standardId, POST_MAX_SIZE);
		
	}
	
	public boolean isPrevLastPage(int prevId, int userId) {
		int maxPostId = postDAO.selectPostIdByUserIdSort(userId, "DESC");
		return maxPostId == prevId ? true : false;
	}
	
	public boolean isNextLastPage(int nextId, int userId) {
		int minPostId = postDAO.selectPostIdByUserIdSort(userId, "ASC");
		return minPostId == nextId ? true : false;
	}
	
	public Post getPostByPostIdUserId(int postId, int userId) {
		return postDAO.selectPostByPostIdUserId(postId, userId);
	}
}
