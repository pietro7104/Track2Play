
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
