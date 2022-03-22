<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="shortcut icon" href="${rc.contextPath}/favicon.ico">
<script type="text/javascript" src="${rc.contextPath}/webjars/jquery/3.2.1/dist/jquery.min.js"></script>
<script type="text/javascript">
  var status = <%=request.getParameter("code")%>;
</script>
</head>
<body>
  <div>
    <c:choose>
      <c:when test="${status == '400'}"><c:set var="myMessage" value="잘못된 요청입니다."/></c:when>
      <c:when test="${status == '401'}"><c:set var="myMessage" value="인증되지 않은 사용자입니다."/></c:when>
      <c:when test="${status == '403'}"><c:set var="myMessage" value="접근 권한이 없습니다."/></c:when>
      <c:when test="${status == '404'}"><c:set var="myMessage" value="페이지가 존재하지 않습니다."/></c:when>
      <c:otherwise><c:set var="myMessage" value="처리중 오류가 발생하였습니다."/></c:otherwise>
    </c:choose>
    
    <h1>${myMessage}</h1>
  </div>
</body>
</html>
