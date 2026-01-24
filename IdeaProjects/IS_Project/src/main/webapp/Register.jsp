<%--
  Created by IntelliJ IDEA.
  User: cube7
  Date: 02/11/2025
  Time: 09:12
  To change this template use File | Settings | File Templates.
--%>

<link rel="stylesheet" href="CSS/style.css">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Registrazione</title>
</head>
<body style="background-color: #070c21; display: flex; flex-direction:column; align-items: center; justify-content: center">
<fieldset class="login_fieldset">
    <div class="title">Registrati</div>

    <form action="Register" method="post">
        Username:
        <label>
            <input type="text" name="username">
        </label> <br/>

        Password:
        <label>
            <input type="password" name="password">
        </label> <br/>

        <input class="button" type="submit" value="Registrati">
    </form>

    <jsp:include page="includables/Error%20Display.jsp"/>

    <div>
        Hai già un account? <a href="index.jsp">Accedi</a>
        <br/>
        oppure <a class="link" onclick="document.getElementById('open-home-page').submit()">naviga come ospite</a>
    </div>
    <form id="open-home-page" action="OpenHomePage" method="get">
    </form>
</fieldset>
</body>
</html>
