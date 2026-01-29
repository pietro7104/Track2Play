<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<link rel="stylesheet" href="CSS/style.css">

<html>
<head>
    <title>Wishlist</title>
</head>

<jsp:include page="includables/Error Popup.jsp"/>

<body class="home-page">

<jsp:include page="includables/NavBar.jsp"/>

<h1>La tua wishlist</h1>

<c:choose>

    <c:when test="${requestScope.wishlistItems == null or requestScope.wishlistItems.size() le 0}">
        <div style="margin-left: 20px">Nessun gioco nella wishlist</div>
    </c:when>

    <c:otherwise>
        <div class="collection">

            <c:forEach items="${requestScope.wishlistItems}" var="item">

                <c:set var="bestDeal" value="${requestScope.prices.get(item.gameId).GetBestDeal()}"/>
                <jsp:include page="includables/Game Display.jsp">
                    <jsp:param name="gameID" value="${item.gameId}"/>
                    <jsp:param name="image" value="${item.gameImageURL}"/>
                    <jsp:param name="game_name" value="${item.gameTitle}"/>
                    <jsp:param name="addedDate" value="${item.addedDate}"/>
                    <jsp:param name="price" value="${bestDeal.dealPrice.amount}"/>
                    <jsp:param name="cut" value="${bestDeal.cut}"/>
                    <jsp:param name="regularPrice" value="${bestDeal.regularPrice.amount}"/>
                    <jsp:param name="currency" value="${bestDeal.dealPrice.currency}"/>
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
