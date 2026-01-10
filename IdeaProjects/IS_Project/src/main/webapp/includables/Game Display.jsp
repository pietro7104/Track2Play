<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body>
    <div class="game-display">
        <img class="game-image" src="${param.image}" alt="${param.game_name}">
        <div class="game-name-display">
            ${param.game_name}
        </div>
        <c:if test="${param.price != null}">
            <div class="game-price-display">
                <c:if test="${param.cut+0 gt 0}">
                    <span class="discount-display" style="margin-right: auto">-${param.cut}%</span>
                    <span class="regular-price" style="margin-left: auto"><fmt:formatNumber value="${param.regularPrice}" type="currency" currencyCode="${param.currency}"/> </span>
                </c:if>
                <span class="game-price-text" style="margin-left: auto"><fmt:formatNumber value="${param.price}" type="currency" currencyCode="${param.currency}"/></span>
            </div>
        </c:if>
    </div>
</body>
</html>
