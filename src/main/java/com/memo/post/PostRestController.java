package com.memo.post;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.memo.post.bo.PostBO;

import jakarta.servlet.http.HttpSession;
@RequestMapping("/post")
@RestController
public class PostRestController {

	@Autowired
	private PostBO postBO;
	
	/**
	 * 글쓰기 API
	 * @param subject
	 * @param content
	 * @param file
	 * @param session
	 * @return
	 */
	@PostMapping("/create")
	public Map<String, Object> create( // userId가 필요하지만 session에 있기 때문에 괜찮음.
			@RequestParam("subject") String subject,
			@RequestParam(value="content", required=false) String content,
			@RequestParam(value="file", required=false) MultipartFile file,
			HttpSession session){
		
		// 필요한 session 가져오기
		int userId = (int)session.getAttribute("userId"); // int를 하면 오류체크 바로 됌(null은 int가 될 수 없음)
		String loginId= (String)session.getAttribute("loginId");
		
		// db insert
		int rowCount = postBO.addPost(userId, loginId, subject, content, file);
		// code 구분
		Map<String, Object> result = new HashMap<>();
		if (rowCount > 0 ) {
			result.put("code", 1);
			result.put("result", "성공");
		} else {
			result.put("code", 500);
			result.put("errorMessage", "메모 저장에 실패했습니다. 관리자에게 문의해주세요.");
		}
		
		// 응답 내리기
		return result;
	}

	
	@PutMapping("/update")
	public Map<String, Object> update(
			@RequestParam("postId") int postId,
			@RequestParam("subject") String subject,
			@RequestParam(value="content", required=false) String content,
			@RequestParam(value="file", required=false) MultipartFile file,
			HttpSession session){
		
		int userId = (int)session.getAttribute("userId");
		String loginId = (String)session.getAttribute("loginId");
		
		
		//update db (기존에 있던 건 삭제하고 올린다)
		postBO.updatePost(userId, loginId, postId, loginId, content, file);
		
		Map<String, Object> result = new HashMap<>();
		result.put("code", 1);
		
		// 응답하기
		return result;
	}
	
}




















