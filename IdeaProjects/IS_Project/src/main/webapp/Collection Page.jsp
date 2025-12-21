<%--
  Created by IntelliJ IDEA.
  User: cube7
  Date: 17/11/2025
  Time: 09:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<link rel="stylesheet" href="CSS/style.css">

<html>
<head>
    <title>Collezione</title>
</head>
<body class="home-page">
    <jsp:include page="includables/NavBar.jsp"/>

    <h1>La tua collezione</h1>
    <div class="collection">
        <jsp:include page="includables/Game Display.jsp">
            <jsp:param name="image" value="images/silksong_header.jpg"/>
            <jsp:param name="game_name" value="Hollow Knight: Silksong"/>
        </jsp:include>

        <jsp:include page="includables/Game Display.jsp">
            <jsp:param name="image" value="images/silksong_header.jpg"/>
            <jsp:param name="game_name" value="Hollow Knight: Silksong"/>
        </jsp:include>
    </div>
</body>
</html>
