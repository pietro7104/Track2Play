<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<link rel="stylesheet" href="CSS/style.css">

<html>
<head>
    <title>Wishlist</title>
</head>

<jsp:include page="includables/Error Popup.jsp"/>

<body>

<jsp:include page="includables/NavBar.jsp"/>

<div style="font-size: 30px">La tua wishlist</div>

<c:choose>

    <c:when test="${requestScope.wishlistItems == null or requestScope.wishlistItems.size() le 0}">
        <div>Nessun gioco nella wishlist</div>
    </c:when>

    <c:otherwise>
        <div class="collection">

            <c:forEach items="${requestScope.wishlistItems}" var="item">


                <jsp:include page="includables/Game Display.jsp">
                    <jsp:param name="gameID" value="${item.getgameId()}"/>
                    <jsp:param name="image" value="${item.getGameImageURL()}"/>
                    <jsp:param name="game_name" value="${item.getGameTitle()}"/>
                    <jsp:param name="addedDate" value="${item.getAddedDate()}"/>
                </jsp:include>

                <!-- Bottone rimozione -->
                <form action="Wishlist/Remove" method="post" style="text-align:center;">
                    <input type="hidden" name="gameItemId" value="${item.getgameId()}">
                    <button type="submit">Rimuovi dalla wishlist</button>
                </form>

            </c:forEach>

        </div>
    </c:otherwise>

</c:choose>

</body>
</html>
