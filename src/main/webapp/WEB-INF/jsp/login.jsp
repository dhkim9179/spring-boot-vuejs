<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

<link rel="stylesheet" type="text/css" href="${rc.contextPath}/css/style.css">
<link rel="shortcut icon" href="${rc.contextPath}/favicon.ico">
<script type="text/javascript" src="${rc.contextPath}/webjars/jquery/3.2.1/dist/jquery.min.js"></script>

<script type="text/javascript">
<c:if test="${rc.queryString == 'error'}">
alert("<spring:message code="common.message.login.fail"/>");
</c:if>

$(function(){
	var $form = $("form");

	//인풋박스에서 엔터키 눌렀을때 로그인 시도
	$form.find('input').keydown(function(event){
		if(event.which == '13'){
			$form.submit();
			return false;
    }
	});
});

var contextPath = "${rc.contextPath}";
var _locale = "${rc.locale.language}";

// 언어 변경
$(function(){
	$("#_locale").change(function(){
		location.href = contextPath + "/login?locale=" + $(this).val();
	});
});

// 아이디 찾기 페이지 이동
function findId() {
	location.href = contextPath + "/login/find/id";
}

// 비밀번호 찾기 페이지 이동
function findPwd() {
	location.href = contextPath + "/login/find/password";
}
</script>
</head>

<body>
	<div>
		<select id="_locale">
        	<option><spring:message code="common.language"/></option>
        	<option value="ko"><spring:message code="common.korean"/></option>
        	<option value="en"><spring:message code="common.english"/></option>
        	<option value="jp"><spring:message code="common.japanese"/></option>
    	</select>
	</div>
	<div>
		<form id="form" name="form" method="post" class="loginForm" action="${rc.contextPath}/login/process">
			<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }"/>
			<h2><spring:message code="login.title"/></h2>
			<div class="idForm">
				<input type="text" class="id" id="id" name="username" value="" placeholder="<spring:message code="login.id"/>"/>
			</div>
			<div class="passForm">
				<input type="password" class="pw" id="password" name="password" value="" placeholder="<spring:message code="login.pw"/>"/>
			</div>
			<button type="submit" class="btn" id="btnLogin"><spring:message code="login.btn"/></button>
			
			<div></div>
			<input type="button" id="btnFindId" value="<spring:message code="find.id"/>" onclick="findId()"/>
			<input type="button" id="btnFindPwd" value="<spring:message code="find.password"/>" onclick="findPwd()"/>
		</form>
	</div>
</body>
</html>
