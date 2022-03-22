<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css" href="${rc.contextPath}/css/style.css">
<script type="text/javascript" src="${rc.contextPath}/webjars/jquery/3.2.1/dist/jquery.min.js"></script>
<script type="text/javascript">
var contextPath = "${rc.contextPath}";
var _locale = "${rc.locale.language}";

$(document).ready(function() {
	// 아이디 찾기 페이지 
	if($("#userCount").val() == "") {
		$("#search").show();
		$("#result").hide();
	} else {
		// 아이디 찾기 결과 페이지
		$("#search").hide();
		$("#result").show();
	}
});

// 언어 변경
$(function(){
	$("#_locale").change(function(){
		location.href = contextPath + "/login/find/id?locale=" + $(this).val();
	});
});

function findPassword() {
	location.href = contextPath + "/login/find/password";
}

function cancel() {
	location.href = contextPath + "/login";
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
	<div id="search">
		<form id="findIdForm" method="get" action="${rc.contextPath}/login/find/id">
			<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }"/>
			<h2><spring:message code="find.id"/></h2>
			<div>
				<!-- 
				한글+영문 정규식: /[^ㄱ-힣a-zA-Z]/gi
				한글+영문+숫자 정규식: /[^ㄱ-힣a-zA-Z0-9]/gi
				숫자만 정규식: /[^0-9]/g
				영문+숫자+특수문자 정규식: 
				특수문자 제외 정규식: /[~!@\#$%^&*\()\-=+_']/gi
				이메일정규식: 
				 -->
				<input type="text" id="username" name="username" value="" placeholder="<spring:message code="find.id.username"/>" onKeyup="this.value=this.value.replace(/[~!@\#$%^&*\()\-=+_']/gi, '');" maxLength=20/>
				
			</div>
			<div>
				<input type="text" id="mobilePhoneNumber" name="mobilePhoneNumber" value="" placeholder="<spring:message code="find.phone"/>" onKeyup="this.value=this.value.replace(/[^0-9]/g,'');" maxLength=20/>
			</div>
			<button type="submit"><spring:message code="find.id.btn"/></button>
			<input type="button" id="findId" name="findId" value="<spring:message code="find.btn.cancel"/>" onclick="cancel()"/>
		</form>
	</div>
	<div id="result">
		<form id="showIdForm">
			<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }"/>
			<input type="hidden" id="userCount" name="userCount" value="${userCount }"/>
			
			<h2><spring:message code="show.id"/></h2>
			<div>
				<c:choose>  
					<c:when test="${userCount  == 0}"> 
						<spring:message code="show.id.no"/>
					</c:when> 
					<c:otherwise> 
						<spring:message code="show.id.list"/>
						<br></br>
						<c:forEach items="${userList }" var="user">
							${user.userId }
						</c:forEach>
					</c:otherwise>
				</c:choose>  
			</div>
			<input type="button" id="btnSearch" value="<spring:message code="find.password"/>" onclick="findPassword()"/>
			<input type="button" id="showId" name="showId" value="<spring:message code="find.btn.cancel"/>" onclick="cancel()"/>
		</form>
	</div>
</body>
</html>
