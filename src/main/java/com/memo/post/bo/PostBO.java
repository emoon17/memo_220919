package com.memo.post.bo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.memo.common.FileManagerService;
import com.memo.post.dao.PostDAO;

@Service
public class PostBO {
	
	@Autowired
	private PostDAO postDAO;
	
	@Autowired
	private FileManagerService fileManagerService;
	
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
}