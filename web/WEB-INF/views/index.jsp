<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
  <head>
    <title>Welcome</title>
      <%--<meta name="_csrf" content="${_csrf.token}"/>--%>
      <!-- default header name is X-CSRF-TOKEN -->
      <%--<meta name="_csrf_header" content="${_csrf.headerName}"/>--%>
      <sec:csrfMetaTags/>
      <script src="${pageContext.request.contextPath}/resources/jquery-3.1.1.min.js" type="text/javascript"></script>
      <script src="${pageContext.request.contextPath}/resources/test.js" type="text/javascript"></script>
  </head>
  <body>
  <form method="post" action="/login">
    <input type="text" id="login" name="login" placeholder="login">
    <input type="password" id="password" name="password" placeholder="pass">
    <input type="hidden" name="loginPage" value="loginPage"/>
    <%--<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>--%>
    <button id="logb" type="submit">Login</button>
  </form>
  <%--<form method="post" action="/registration">--%>
      <%--<input type="text" id="name" name="name" placeholder="name">--%>
      <%--<input type="text" id="login" name="login" placeholder="login">--%>
      <%--<input type="password" id="password" name="password" placeholder="pass">--%>
      <%--&lt;%&ndash;<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>&ndash;%&gt;--%>
      <%--<button id="regb" type="submit">Register</button>--%>
  <%--</form>--%>
  </body>
</html>
