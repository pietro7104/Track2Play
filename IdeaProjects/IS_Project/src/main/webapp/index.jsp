<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<link rel="stylesheet" href="CSS/style.css">

<html>
<head>
    <title>Login</title>
</head>
<body style="background-color: #070c21; display: flex; flex-direction:column; align-items: center; justify-content: center">
<form action="APITest" method="get">
    <input class="button" type="submit" value="Test API">
</form>

<fieldset class="login_fieldset">
    <div class="title">Login</div>
    <form action="Login" method="post">
        <div>
            Username: <br/>
            <label>
                <input type="text" name="username">
            </label> <br/>
        </div>

        <div>
            Password: <br/>
            <label>
                <input type="password" name="password">
            </label> <br/>
        </div>


        <input class="button" type="submit" value="Login">
    </form>

    <jsp:include page="includables/Error%20Display.jsp"/>

    <div>
        Non hai un account? <a href="Register.jsp">Registrati</a>
        <br/>
        oppure <a class="link" onclick="document.getElementById('open-home-page').submit()">naviga come ospite</a>
    </div>
    <form id="open-home-page" action="OpenHomePage" method="get">
    </form>
</fieldset>



</body>
</html>