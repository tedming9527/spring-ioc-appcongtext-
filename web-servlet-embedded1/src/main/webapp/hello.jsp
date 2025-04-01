<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.LocalTime" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
    <title>Hello World - JSP</title>
</head>
<body>
    <%-- JSP Comment --%>
    <h1>Hello World!</h1>
    <p>
    <%
        out.println("Your IP address is");
    %>
    <span style="color:red">
        <%= request.getRemoteAddr() %>
    </span>
    </p>
    <p>当前日期1：<% out.println(LocalDate.now()); %></p>
    <p>当前时间2：<%=LocalTime.now().withNano(0) %></p>
</body>
</html>
