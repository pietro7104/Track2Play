<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<div class="deal-display">
    <div class="shop-name">${param.shopName}</div>
    <div class="game-price-display">
        <c:if test="${param.cut+0 gt 0}">
            <div style="margin-right: auto">
                <span class="discount-display">-${param.cut}%</span>
                <span class="regular-price"><fmt:formatNumber value="${param.regularPrice}" type="currency" currencyCode="${param.currency}"/> </span>
            </div>
        </c:if>
        <span class="game-price-text"><fmt:formatNumber value="${param.price}" type="currency" currencyCode="${param.currency}"/></span>
    </div>
</div>

</html>
