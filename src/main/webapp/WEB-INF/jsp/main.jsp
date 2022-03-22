<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.dhkim.prj.admin.core.support.security.user.User" %>
<%@ page import="org.springframework.security.core.context.SecurityContextHolder" %>

<%
	// 사용자 정보 조회 
	User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	
	// 사용자 언어 확인
	String language = user.getLanguage();
%>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Test Admin</title>
<!-- Bootstrap 5 -->
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.8.2/css/all.min.css">
<link rel="stylesheet" href="https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">

<!-- jQuery -->
<script type="text/javascript" src="${rc.contextPath}/webjars/jquery/3.2.1/dist/jquery.min.js"></script>

<!-- Vue -->
<script src="https://unpkg.com/vue@2.6.0"></script>

<!-- Vue 라우터 -->
<script src="https://unpkg.com/vue-router@3.0.1/dist/vue-router.js"></script>

<!-- 컴포넌트 이동 -->
<script src="https://unpkg.com/http-vue-loader"></script>

<!-- Axios -->
<script src="https://unpkg.com/axios/dist/axios.min.js"></script>

<!-- Date picker -->
<script src="//code.jquery.com/jquery-3.6.0.js"></script>
<script src="//code.jquery.com/ui/1.12.1/jquery-ui.js"></script>

<!-- 입력값 유효성 검증 -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/vee-validate/2.0.9/vee-validate.min.js"></script>

<!-- 다국어 -->
<script src="https://unpkg.com/vue-i18n@8"></script>

</head>

<body>
	<div id="admin">
		<header-view></header-view>
		<menu-view></menu-view>
		<router-view></router-view>
	</div>
	
	<script type="text/javascript">
		// 컨텍스트 경로 설정
		var contextPath = "${rc.contextPath}";
		
		// 사용자 언어 설정
		var language = "<%=language%>";
	</script>
	
	<!-- 다국어 -->
	<script src="${rc.contextPath}/js/message.js"></script>
	
	<!-- 메뉴 라우터 -->
	<script src="${rc.contextPath}/js/router.js"></script>
	
	<!-- 뷰 인스턴스 -->
	<script src="${rc.contextPath}/js/app.js"></script>
	
	<!-- 페이징 컴포넌트 -->
	<script src="${rc.contextPath}/js/function/paging.js"></script>
	
	<!-- Global Methods -->
	<script src="${rc.contextPath}/js/function/global.js"></script>
		
	<!-- Date picker 컴포넌트 -->
	<script src="${rc.contextPath}/js/function/datepicker.js"></script>
	
	<!-- Selection 컴포넌트 -->
	<script src="${rc.contextPath}/js/function/selection.js"></script>

</body>
</html>