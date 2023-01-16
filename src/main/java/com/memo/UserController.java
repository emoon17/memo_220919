package com.memo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/user")
@Controller
public class UserController {
	
	/**
	 * 회원 가입 화면
	 * @return
	 */
	@GetMapping("/sign_up_view")
	public String signUpView(Model model) {
		model.addAttribute("viewName", "user/signUp"); 
		return "template/layout";
	}
	/**
	 * 로그인 화면
	 * @param model
	 * @return
	 */
	@GetMapping("/sign_in_view")
	public String signInView(Model model) {
		model.addAttribute("viewName", "user/signin");
		return "template/layout";
	}
	
	@GetMapping("/sign_out")
	public String signOut(HttpSession session) { 
		//로그아웃 => 세션에 있는 바구니를 모두 비어버려야함.
		session.removeAttribute("userId");
		session.removeAttribute("loginId");
		session.removeAttribute("name");
		
		return "redirect:/user/sign_in_view"; //로그아웃 후 로그인 페이지로 리다이렉트
	}
}
