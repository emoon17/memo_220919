package com.memo.post;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.memo.post.bo.PostBO;
import com.memo.post.model.Post;

import jakarta.servlet.http.HttpSession;
@RequestMapping("/post")
@Controller
public class PostController {

	
	@Autowired
	private PostBO postBO;
	
	// 글 목록화면- 로그인 된 사람만 뜨게 해야 됌
	@GetMapping("/post_list_view")
	public String postListView(
			@RequestParam(value="prevId", required=false) Integer preveIdParam,
			@RequestParam(value="nextId", required=false) Integer nextIdParam,
			Model model,
			HttpSession session) {
		Integer userId = (Integer)session.getAttribute("userId"); // Integer로 하면 일단 로그인 안 된 사람도 들어올 수 있음 null로 들어옴
		if (userId == null) {
			//
			return "redirect:/user/sign_in_view";
		} 
		// 로그인 된 사람은 게시글 뿌리기
		int prevId = 0;
		int nextId = 0;
		List<Post> postList = postBO.getPostListByUserId(userId, preveIdParam, nextIdParam);
		if (postList.isEmpty() == false) { // 리스트가 비어 있을 때 에러 방지
			prevId = postList.get(0).getId(); // 가져온 리스트 중 가장 앞 쪽 (큰 id)
			nextId = postList.get(postList.size() - 1).getId(); // 가져온 리스트 중 가장 뒤 쪽( 작은 id)
			
			// 이전 방향의 끝인가? 끝이면 0으로 세팅
			// postList의 0인덱스 값(prevId)과 post 테이블의 가장 큰 값이 같으면 마지막 페이지
			if (postBO.isPrevLastPage(prevId, userId)) { // 마지막 페이지 일 때
				prevId = 0;
			}
			
			// 다음 방향의 끝인가? 끝이면 0으로 세팅
			// postList의 마지막 인덱스 값(nextId)과 post테이블의 가장 작은 값이 같으면 마지막 페이지
			if (postBO.isNextLastPage(nextId, userId)) {
				nextId = 0;
			}
		} // -- 처음 페이지에서 이전 못 누르고 마지막 페이지에서 다음 못 누르고
		
		model.addAttribute("prevId", prevId); // 가져온 리스트 중 가장 앞 쪽 (큰 id)
		model.addAttribute("nextId", nextId); // 가져온 리스트 중 가장 뒤 쪽( 작은 id)
		
		model.addAttribute("postList", postList);
		
		model.addAttribute("viewName", "post/postList");
		return "template/layout";
	}
	/**
	 * 글쓰기 화면
	 * @param model
	 * @return
	 */
	// 글쓰기 화면
	@GetMapping("/post_create_view")
	public String postCreateView(Model model) {
		model.addAttribute("viewName", "post/postCreate");
		return "template/layout";
	}
	
	//글 상세화면
	@GetMapping("post_detail_view")
	public String postDetailView(
			@RequestParam("postId") int postId,
			HttpSession session,
			Model model) {
		
		// 세션에서 userId 가져오기
		Integer userId = (Integer)session.getAttribute("userId");
		if (userId == null) {
			return "redirect:/user/sign_in_view";
		}
		// 로그인 되어있는 사람 - 1. DB select By- userId, postId
		Post post = postBO.getPostByPostIdUserId(postId, userId);
		model.addAttribute("post", post);
		
		
		//화면이동
		model.addAttribute("viewName", "/post/postDetail" );
		return "template/layout";
		
		
	}
	
}
