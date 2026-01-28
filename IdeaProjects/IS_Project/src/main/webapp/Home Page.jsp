<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="CSS/style.css">
    <title>Home Page</title>
</head>
<jsp:include page="includables/Error%20Popup.jsp"/>

<body class="home-page">
<jsp:include page="includables/NavBar.jsp"/>

    <div class="top-games">
        <c:forEach items="${requestScope.topGames}" var="game">
            <c:if test="${requestScope.prices.get(game.getIsThereAnyDealID()) != null}">
                <c:set var="bestDeal" value="${requestScope.prices.get(game.getIsThereAnyDealID()).GetBestDeal()}"/>
                <jsp:include page="includables/Game%20Display.jsp">
                    <jsp:param name="gameID" value="${game.isThereAnyDealID}"/>
                    <jsp:param name="image" value="${game.GetHighestResolutionBanner()}"/>
                    <jsp:param name="game_name" value="${game.getTitle()}"/>
                    <jsp:param name="price" value="${bestDeal.dealPrice.amount}"/>
                    <jsp:param name="cut" value="${bestDeal.cut}"/>
                    <jsp:param name="regularPrice" value="${bestDeal.regularPrice.amount}"/>
                    <jsp:param name="currency" value="${bestDeal.dealPrice.currency}"/>
                    <jsp:param name="priceInfo" value="${requestScope.prices.get(game.getIsThereAnyDealID())}"/>
                </jsp:include>
            </c:if>
        </c:forEach>
    </div>
    <div class="collection">
        <c:forEach items="${requestScope.games}" var="game">
            <c:if test="${requestScope.prices.get(game.getIsThereAnyDealID()) != null}">
                <c:set var="bestDeal" value="${requestScope.prices.get(game.getIsThereAnyDealID()).GetBestDeal()}"/>
                <jsp:include page="includables/Game%20Display.jsp">
                    <jsp:param name="gameID" value="${game.isThereAnyDealID}"/>
                    <jsp:param name="image" value="${game.GetHighestResolutionBanner()}"/>
                    <jsp:param name="game_name" value="${game.getTitle()}"/>
                    <jsp:param name="price" value="${bestDeal.dealPrice.amount}"/>
                    <jsp:param name="cut" value="${bestDeal.cut}"/>
                    <jsp:param name="regularPrice" value="${bestDeal.regularPrice.amount}"/>
                    <jsp:param name="currency" value="${bestDeal.dealPrice.currency}"/>
                    <jsp:param name="priceInfo" value="${requestScope.prices.get(game.getIsThereAnyDealID())}"/>
                </jsp:include>
            </c:if>
        </c:forEach>
    </div>
</body>
</html>