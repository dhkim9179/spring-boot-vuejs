<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css" href="${rc.contextPath}/css/style.css">
<script type="text/javascript" src="${rc.contextPath}/webjars/jquery/3.2.1/dist/jquery.min.js"></script>
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/i18next/17.0.18/i18next.min.js"></script>
<script type="text/javascript">
var contextPath = "${rc.contextPath}";
var _locale = "${rc.locale.language}";

// 언어 변경
$(function(){
	$("#_locale").change(function(){
		location.href = contextPath + "/login/find/password?locale=" + $(this).val();
	});
});

function sendPassword() {
	var userId = $("#userId").val();
	var mobilePhoneNumber = $("#mobilePhoneNumber").val();

	if(userId == "") {
		alert("<spring:message code="common.message.input.id"/>");
		return;
	}
	
	if(mobilePhoneNumber == "") {
		alert("<spring:message code="common.message.input.phone"/>");
		return;
	}

	var data = {
        "userId" 			: userId,
        "mobilePhoneNumber" : mobilePhoneNumber
	}
	
	$.ajax({
		   url : contextPath + "/login/find/password",
		   type : 'post',
		   data : JSON.stringify(data),
		   dataType: "json",
		   contentType : "application/json",
		   success : function( data, status, xhr) {			
			if(data.result.code == "0000") {
				alert("<spring:message code="common.message.send.password"/>");
				location.href = contextPath + "/login";
			} else {
				alert("<spring:message code="common.message.send.password.failure"/>");
			}
		}, error: function(data, status, xhr) {
			alert(xhr);
		}		
	});
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
	<div>
		<form id="form" name="form" method="post" action="${rc.contextPath}/login/find/password">
			<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }"/>
			<h2><spring:message code="find.password"/></h2>
			<div>
				<!-- <label><spring:message code="find.password.id"/></label> -->
				<!-- 추후 이메일 정규식으로 변경해야함 -->
				<input type="text" id="userId" name="userId" value="" placeholder="<spring:message code="find.password.id"/>" maxLength=50 onKeyup="/^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i"/>
			</div>
			<div>
				<input type="text" id="mobilePhoneNumber" name="mobilePhoneNumber" value="" placeholder="<spring:message code="find.phone"/>" onKeyup="this.value=this.value.replace(/[^0-9]/g,'');" maxLength=20/>
			</div>
			<input type="button" id="btnSearch" value="<spring:message code="find.password.btn"/>" onclick="sendPassword()"/>
			<input type="button" name="findPwd" value="<spring:message code="find.btn.cancel"/>" onclick="cancel()"/>
		</form>
	</div>
</body>
</html>
