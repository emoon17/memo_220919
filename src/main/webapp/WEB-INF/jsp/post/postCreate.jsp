<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div class="d-flex justify-content-center">
	<div class="w-50">
		<h1>글 쓰기</h1>
		<input type="text" id="subject" class="form-control" placeholder="제목을 입력하세요">
		<textArea class="form-control" placeholder="내용을 입력하세요" rows="15" id="content"></textArea>
		<div class="d-flex justify-content-end my-4">
			<input type="file" id="file" accept=".jpg,.jpeg,.png,.gif">
		</div>
		
		<div class="d-flex justify-content-between">
			<button type="button" id="postListBtn" class="btn btn-dark">목록</button>
			
			<div>
				<button type="button" id="clearBtn" class="btn btn-secondary">모두 지우기</button>
				<button type="button" id="postCreateBtn" class="btn btn-info">저장</button>
			</div>
		</div>
		
	</div>
</div>

<script>
	$(document).ready(function (){
		// 목록 버튼 클릭 => 글 목록
		$('#postListBtn').on('click', function(){
			location.href="/post/post_list_view";
		});
		
		// 모두 지우기 눌렀을 때 
		$('#clearBtn').on('click', function(){
			// input의 밸류를 가져와서 빈칸으로 세팅한다.
			$('#subject').val("");
			$('#content').val("");
		});
		
		// 저장 버튼 눌렀을 때(업로드 될 수 있도록)
		$('#postCreateBtn').on('click', function() {
			//alert("dd");
			
			//validation check
			let subject = $('#subject').val().trim();
			let content = $('#content').val();
			
			if (subject == '') {
				alert("제목을 입력하세요");
				return;
			}
			//console.log(content);
			
			// 파일 저장 (지정한 확장자만 가능하게 하는 것, 위에 accept는 고정해놓는 건 아니다.)
			let file = $('#file').val(); 
			//alert(file); //C:\fakepath\frozen-lake-7658478_960_720.jpg
			
			if (file != '') {
				//파일이 업로드 된 경우에만 확장자 체크
				//alert(file.split(".").pop().toLowerCase()); // pop: 젤 마지막에 있는 배열 칸을 뽑고 삭제. toLowerCase: 다 소문자로 바뀌어짐
				let ext = file.split(".").pop().toLowerCase();
				
				// $.inArray 이 어레이에 ext가 있느냐 없으면 -1로 반환한다.
				if($.inArray(ext, ['jpg', 'jpeg', 'png', 'gif']) == -1) { 
					alert("이미지 파일만 업로드 할 수 있습니다.");
					$('#file').val(''); // 파일을 비운다
					return;
				}
				
			}
			// 서버 - AJAX (파일은 폼태그로 꼭 보내야한다.)
			// 지금 방법 : 태그로 폼태그를 만들기(자바스크립트에서 만듦)
			let formData = new FormData();
			// append로 넣는 값은 폼태그의 name으로 넣는 것과 같다(request parameter)
			formData.append("subject", subject); // form데이타에 하나씩 추가하는 함수 : append(리케스트파리미터, 값);
			formData.append("content", content);
			formData.append("file", $('#file')[0].files[0]); // 여러개 올릴 때는 배열로 올려야함.
			
			// ajax 통신으로 formData에 있는 데이터 전송
			$.ajax({
				//request
				type:"post"
				, url:"/post/create"
				, data:formData // json이 아니라 form객체를 통으로 들어가야 됌
				// 여기서 부터는 이미지 넣을 때 꼭 넣어야 하는 것들 3개
				, enctype:"multipart/form-data" // 파일 업로드를 위한 필수 설정 1
				, processData:false // 파일 업로드를 위한 필수 설정 2 - RequestBody에 Json이 아니라 폼태그객체가 담겨지는 설정 
				, contentType:false //파일 업로드를 위한 필수 설정 3 - RequestBody에 Json이 아니라 폼태그객체가 담겨지는 설정 
				
				//response
				, success:function(data) {
					if (data.code == 1) {
						// 성공
						alert("메모가 저장되었습니다.");
						location.href="/post/post_list_view";
					} else {
						// 실패
						alert(data.errorMessage);
					}
				}
				, error:function(e){
					alert("메모 저장에 실패하였습니다.") // ajax가 완전히 실패했을 때
				}
			});
			
		});
	});
</script>