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
}
