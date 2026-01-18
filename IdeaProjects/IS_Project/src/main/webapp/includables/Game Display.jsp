<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body>
    <form id="open-page-form-${param.gameID}" action="OpenGamePage" method="get">
        <input type="hidden" name="itadid" value="${param.gameID}">
        <c:if test="${param.priceInfo != null}">
            <input type="hidden" name="price" value="${param.priceInfo}">
        </c:if>


    <div class="game-display" onclick="document.getElementById('open-page-form-${param.gameID}').submit()">
        <img src="${param.image}" alt="${param.game_name}">
        <div class="game-name-display">
            ${param.game_name}
        </div>
        <c:if test="${param.price != null}">
            <div class="game-price-display">
                <c:if test="${param.cut+0 gt 0}">
                    <span class="discount-display">-${param.cut}%</span>
                    <span class="regular-price"><fmt:formatNumber value="${param.regularPrice}" type="currency" currencyCode="${param.currency}"/></span>
                </c:if>
                <span class="game-price-text"><fmt:formatNumber value="${param.price}" type="currency" currencyCode="${param.currency}"/></span>
            </div>
        </c:if>
    </div>
    </form>

</body>
</html>
