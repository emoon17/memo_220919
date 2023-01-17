package com.memo.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component // 일반적인 스트링 빈 - Autowired로 가져와서 쓰면 됌
public class FileManagerService {
	
	// 실제 이미지가 저장될 경로(서버)
	public static final String FILE_UPLOAD_PATH = "D:\\jungeunhye\\6.Spring_project\\memo\\workspace\\imges/";
	
	
	// 실제 업로드하는 메소드
	// input : MultipartFile, userLoginId
	// output : image path
	public String saveFile(String loginId, MultipartFile file) {
		// loginId를 가져온 이유 : 사람마다 파일을 만드려고.
		// 파일 디렉토리 예) aaaa_16205468768/sun.png (중복이 생기면 안되기 때문에)
		String directoryName = loginId + "_" + System.currentTimeMillis() + "/"; //aaaa_16205468768/
		String filePath = FILE_UPLOAD_PATH + directoryName;  //D:\\jungeunhye\\6.Spring_project\\memo\\workspace\\imges/aaaa_16205468768/
		
		File directory = new File(filePath); // 파일을 만들어 낼 준비(명시)
		if (directory.mkdir() == false) {
			return null; // 폴더 만드는데에 실패했으면 이미지패스는 null이 된다.
		} 
		
		// 파일 업로드 : byte 단위로 업로드 된다.
		try {
			byte[] bytes = file.getBytes();
			Path path = Paths.get(filePath + file.getOriginalFilename()); //OriginalFilename:사용자가 올린 파일명 
			//한글은 안 올라감. 한글 올리고 싶을 땐 확장자를 분해하고 이름은""스트링으로감싸서 내가 만들어야한다.
			Files.write(path, bytes); // 진짜로 업로드 하는 순간
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		// 파일 업로드 성공했으면 이미지 url path를 리턴한다. (아직 매핑 안한상태(경로는 안 만듬)- 1번까지 한 상태)
		// http://localhost/images/aaaa_16205468768/sun.png
		return "/images/" + directoryName + file.getOriginalFilename();
	}
}
