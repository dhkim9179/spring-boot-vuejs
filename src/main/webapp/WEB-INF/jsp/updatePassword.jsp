<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script type="text/javascript" src="${rc.contextPath}/webjars/jquery/3.2.1/dist/jquery.min.js"></script>
<script type="text/javascript">
var contextPath = "${rc.contextPath}";

// 언어 변경
$(function(){
	$("#_locale").change(function(){
		location.href = contextPath + "/login/updatePassword?locale=" + $(this).val();
	});
});

function updatePassword() {
	var newPwd = $("#newPwd").val();
	var cfmPwd = $("#cfmPwd").val();
	
	if(newPwd == "" || cfmPwd == "") {
		alert("신규비밀번호를 입력해주세요");
		return;
	}
	
	if(newPwd != cfmPwd) {
		alert("비밀번호가 일치하지 않습니다");
		return;
	}
	
	// 비밀번호 검증
	// - 8~20자 
	if(newPwd.length < 8) {
		alert("비밀번호 길이는 최소 8자입니다");
		return;
	}
	
	// 영문+숫자+특수문자 최소 1자리 이상
	if(!validatePassword(newPwd)) {
		alert("비밀번호는 영문+숫자+특수문자 조합으로 입력해야합니다");
		return;
	}
	
	var data = {
		"userId"	: $("#userId").val(),
        "password" 	: newPwd
	}
	
	$.ajax({
		   url : contextPath + "/login/updatePassword",
		   type : 'post',
		   data : JSON.stringify(data),
		   dataType: "json",
		   contentType : "application/json",
		   success : function( data, status, xhr) {			
			if(data.result.code == "0000") {
				alert("정상적으로 변경되었습니다.");
				location.href = contextPath + "/";
			} else {
				alert("비밀번호변경에 실패했습니다.");
			}
		}, error: function(data, status, xhr) {
			alert(xhr);
		}		
	});
}

function validatePassword(character) {
    return /^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{6,}$/.test(character)
}
</script>
</head>

<body>
	<div>
		<form id="passowrdForm" name="passowrdForm" method="post" action="${rc.contextPath}/login/update/password">
			<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }"/>
			<input type="hidden" id="userId" name="userId" value="${userId }"/> 
			<h2>비밀번호변경</h2>
			<div>
				<label>신규비밀번호</label>
				<input type="password" id="newPwd" name="newPwd" value="" maxlength=20 placeholder="신규비밀번호"/>
			</div>
			<div>
				<label>신규비밀번호확인</label>
				<input type="password" id="cfmPwd" name="cfmPwd" value="" maxlength=20 placeholder="신규비밀번호확인"/>
			</div>
			<input type="button" id="btnSearch" value="변경하기" onclick="updatePassword()"/>
		</form>
	</div>
</body>
</html>
