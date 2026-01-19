<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<a class="unstyled deal-display" <c:if test="${param.dealURL != null}">href="${param.dealURL}"</c:if>>
    <div class="shop-name">${param.shopName}</div>
    <c:if test="${param.storeLow != null}">
            <span class="game-price-text">
                <fmt:formatNumber value="${param.storeLow}" type="currency" currencyCode="${param.currency}"/>
            </span>
    </c:if>
    <c:if test="${param.price != null}">
        <div class="game-price-display">
            <c:if test="${param.cut+0 gt 0}">
                <span class="discount-display">-${param.cut}%</span>
                <span class="regular-price"><fmt:formatNumber value="${param.regularPrice}" type="currency" currencyCode="${param.currency}"/></span>
            </c:if>
            <span class="game-price-text"><fmt:formatNumber value="${param.price}" type="currency" currencyCode="${param.currency}"/></span>
        </div>
    </c:if>
</a>


