<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<link rel="stylesheet" href="CSS/style.css">

<html>
<head>
    <title>Wishlist</title>
</head>

<jsp:include page="includables/Error%20Popup.jsp"/>

<body class="home-page">

<jsp:include page="includables/NavBar.jsp"/>

<div style="font-size: 30px">La tua wishlist</div>

<c:choose>

    <!-- Wishlist vuota -->
    <c:when test="${requestScope.wishlistItems == null or requestScope.wishlistItems.size() le 0}">
        <div>Nessun gioco nella wishlist</div>
    </c:when>

    <!-- Wishlist con elementi -->
    <c:otherwise>
        <div class="collection">

            <c:forEach items="${requestScope.wishlistItems}" var="item">


                <jsp:include page="includables/Game%20Display.jsp">
                    <jsp:param name="gameID" value="${item.gameId}"/>
                    <jsp:param name="image" value="${item.gameImageURL}"/>
                    <jsp:param name="game_name" value="${item.gameTitle}"/>
                    <jsp:param name="addedDate"
                               value="${item.addedDate}">
                    </jsp:param>
                </jsp:include>

                <!-- Bottone rimozione -->
                <form action="Wishlist/Remove" method="post" style="text-align:center;">
                    <input type="hidden" name="gameItemId" value="${item.gameId}">
                    <button type="submit">Rimuovi dalla wishlist</button>
                </form>

            </c:forEach>

        </div>
    </c:otherwise>

</c:choose>

</body>
</html>
