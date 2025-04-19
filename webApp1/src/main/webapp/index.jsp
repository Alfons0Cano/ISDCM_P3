<!-- filepath: /Users/aleixpg/Documents/GitHub/ISDCM_P1/webApp1/src/main/webapp/index.jsp -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/favicon.ico">
</head>
<body>
<%
    response.sendRedirect(request.getContextPath() + "/usuarios");
%>
</body>
</html>