package com.memo.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptUtils {
	
	// static이 붙어 있는 이유: Memory에 올라와야 메인메소드를 수행 할 수 있기 떄문에.
	//						static이 있으면 Memory 에 일단 올라감. => 객체를 new 해서 부르지 않아도 쓸 수 있다.
	public static String md5(String message) {
		String encData = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");

			byte[] bytes = message.getBytes();
	        md.update(bytes);
	        byte[] digest = md.digest();

	        for(int i = 0; i < digest.length; i++ ) {
	            encData += Integer.toHexString(digest[i]&0xff); // 16진수로 변환하는 과정
	        }
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return encData;
	}
}
