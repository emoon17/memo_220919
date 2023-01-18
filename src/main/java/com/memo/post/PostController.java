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
	public String postListView(Model model,
			HttpSession session) {
		Integer userId = (Integer)session.getAttribute("userId"); // Integer로 하면 일단 로그인 안 된 사람도 들어올 수 있음 null로 들어옴
		if (userId == null) {
			//
			return "redirect:/user/sign_in_view";
		} 
		// 로그인 된 사람은 게시글 뿌리기
		List<Post> postList = postBO.getPostListByUserId(userId);
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
		
		//화면이동
		model.addAttribute("viewName", "/post/postDetail" );
		return "template/layout";
		
		
	}
	
}
