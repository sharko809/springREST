<%--
  Created by IntelliJ IDEA.
  User: dsharko
  Date: 10/4/2016
  Time: 5:35 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>Welcome</title>
  </head>
  <body>
  <form method="post" action="/login">
    <input type="text" name="login" placeholder="login">
    <input type="password" name="password" placeholder="pass">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <button type="submit">Login</button>
  </form>
  </body>
</html>
