package com.memo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.memo.common.FileManagerService;
import com.memo.interceptor.PermissionInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	
	@Autowired
	private PermissionInterceptor interceptor;
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		registry
		.addResourceHandler("/images/**") // Web 이미지 주소 // http://localhost/images/aaaa_16205468768/sun.png
		.addResourceLocations("file:///" + FileManagerService.FILE_UPLOAD_PATH); // 실제 파일 위치 mac: file:// window:file:///
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {	
		registry.addInterceptor(interceptor) // 오토와이드 한 스프링 빈 넣얻주면 된다.
		.addPathPatterns("/**") // /** : 모든 주소는 다 검사할거다.(아래 디렉토리까지 확인한다)
		.excludePathPatterns("/favicon.ico", "/error", "/static/**", "/user/sign_out_view"); // 이 주소로 들어오면 제외할 거다 /static/**는 yml에 패턴 저장 해놨을 때만 가능하다.
		
	}
}
