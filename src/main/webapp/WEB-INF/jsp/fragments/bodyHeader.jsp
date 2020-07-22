<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<head>
    <base href="${pageContext.request.contextPath}/">
</head>

<header>
    <a href="meals"><spring:message code="app.title"/></a> | <a href="users"><spring:message code="user.title"/></a> | <a href=""><spring:message code="app.home"/></a>
</header>