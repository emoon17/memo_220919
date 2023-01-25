package com.memo.post.bo;

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
	
	public List<Post> getPostListByUserId(int userId){//서비스를 만들 때는 객체로 해야된다.
		return postDAO.selectPostListByUserId(userId);
		
	}
	
	public Post getPostByPostIdUserId(int postId, int userId) {
		return postDAO.selectPostByPostIdUserId(postId, userId);
	}
}
