package com.memo.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.memo.common.EncryptUtils;
import com.memo.user.bo.UserBO;
import com.memo.user.model.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
@RequestMapping("/user")
@RestController
public class UserRestController { //API 만 쓰는 곳
	
	@Autowired
	private UserBO userBO;
	/**
	 * 아이디 중복확인 API
	 * @param loginId
	 * @return
	 */
	@RequestMapping("/is_duplicated_id")
	public Map<String, Object> isDuplicatiedId(
			@RequestParam("loginId") String loginId){
		
		Map<String, Object> result = new HashMap<>();
		boolean isDuplicated = userBO.existLoginId(loginId);
		if (isDuplicated) { // 중복일 떄
			result.put("code", 1);
			result.put("result", true);
		} else {
			//사용 가능
			result.put("result", false);
		}
		return result;
	}
	
	/**
	 *  회원가입 API
	 * @param loginId
	 * @param password
	 * @param name
	 * @param email
	 * @return
	 */
	@PostMapping("/sign_up")
	public Map<String, Object> signUp(
			@RequestParam("loginId") String loginId,
			@RequestParam("password") String password,
			@RequestParam("name") String name,
			@RequestParam("email") String email){
		
		// 비밀번호 hashing( 암호화) -md5 (보안적으로 제일 취약) 단방향이라 복구가 다시 안 됌.
		String HashedPassword = EncryptUtils.md5(password);
		
		
		
		//db insert
		userBO.addUser(loginId, HashedPassword, name, email);
		// 맵 구성
		Map<String, Object> result = new HashMap<>();	
		result.put("code", 1);
		result.put("result", "성공");
		
		
		//응답 값
		return result;
		
	}
	/**
	 *  로그인하기
	 * @param loginId
	 * @param password
	 * @param request
	 * @return
	 */
	@PostMapping("/sign_in")
	public Map<String, Object> signIn(
			@RequestParam("loginId") String loginId,
			@RequestParam("password") String password,
			HttpServletRequest request){
		
		// 비밀번호 해싱
		String HashedPassword = EncryptUtils.md5(password);
		
		// db select
		User user = userBO.getUSerByLoginIdPassword(loginId, HashedPassword);
		
		Map<String, Object> result = new HashMap<>();
		if (user != null) {//행이 있으면 로그인
			result.put("code", 1);
			result.put("result", "성공");
			// 세션에 유저 정보를 담는다.(로그인 상태 유지)
			HttpSession session = request.getSession();
			session.setAttribute("userId", user.getId());
			session.setAttribute("loginId", user.getLoginId());
			session.setAttribute("name", user.getName());
		} else {//행이 없으면 로그인 실패
			result.put("code", 500);
			result.put("errorMessage", "존재하지 않는 사용자 입니다.");
		}
		//return map
		return result;
	}
	
	
}
