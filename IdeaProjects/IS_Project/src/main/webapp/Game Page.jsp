<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<link rel="stylesheet" href="CSS/style.css">

<jsp:useBean id="api" class="Model.APIControl.APIImplementation"/>
<html>



<head>
    <title>${requestScope.info.title}</title>
</head>

<jsp:include page="includables/Error%20Popup.jsp"/>

<body class="home-page">
    <jsp:include page="includables/NavBar.jsp"/>
    <div class="game-page">
        <div class="game-page-left">
            <img class="game-image" src="${requestScope.info.GetHighestResolutionBanner()}" alt="${requestScope.info.title}">
            <div class="game-prices">
                <c:if test="${requestScope.price != null and requestScope.price.getDeals() != null}">
                    <c:forEach var="deal" items="${requestScope.price.getDeals()}">
                        <jsp:include page="includables/Deal%20Display.jsp">
                            <jsp:param name="shopName" value="${deal.shopName}"/>
                            <jsp:param name="price" value="${deal.dealPrice.amount}"/>
                            <jsp:param name="cut" value="${deal.cut}"/>
                            <jsp:param name="regularPrice" value="${deal.regularPrice.amount}"/>
                            <jsp:param name="currency" value="${deal.dealPrice.currency}"/>
                        </jsp:include>
                    </c:forEach>
                </c:if>

            </div>
        </div>

        <div class="game-page-right">

        </div>
    </div>

</body>
</html>
