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
    <div class="tag-display">
      <c:forEach var="tag" items="${requestScope.info.tags}">
        <span class="tag">${tag}</span>
      </c:forEach>
    </div>
    <div id="developer-and-publisher">
      <span style="font-weight: bold">Developer(s): </span><span class="list-display">
      <c:set var="first" value="no"/>
        <c:forEach var="developer" items="${requestScope.info.developers}">
          <c:if test="${first == 'done'}">, </c:if>
          ${developer.name}
          <c:set var="first" value="done"/>
        </c:forEach>
          <br>
        <c:set var="first" value="no"/>
        <span style="font-weight: bold">Publisher(s): </span><span class="list-display">
        <c:forEach var="publisher" items="${requestScope.info.publishers}">
          <c:if test="${first == 'done'}">, </c:if>
          ${publisher.name}
          <c:set var="first" value="done"/>
        </c:forEach>
      </span>
    </div>
    <span style="font-size: 30px; font-weight: bold; margin-top: 20px">Reviews:</span>
    <div class="review-display">
      <c:forEach var="review" items="${requestScope.info.reviews}">
        <a class="unstyled review" <c:if test="${review.url != null}">href="${review.url}"</c:if>>
          <span class="review-score">${review.score}</span>
          <div style="display: flex; flex-direction: column; align-items: flex-end">
            <span class="review-source">${review.source}</span>
            <span class="review-count">${review.count} reviews</span>
          </div>
        </a>
      </c:forEach>
    </div>
  </div>

  <div class="game-page-right">
    <div class="price-history" style="margin-bottom: 20px">
      <c:if test="${requestScope.price.allTimeLow != null}">
        <jsp:include page="includables/Deal%20Display.jsp">
          <jsp:param name="shopName" value="All time low"/>
          <jsp:param name="price" value="${requestScope.price.allTimeLow.amount}"/>
          <jsp:param name="currency" value="${requestScope.price.allTimeLow.currency}"/>
        </jsp:include>
      </c:if>

      <c:if test="${requestScope.price.oneYearLow != null}">
        <jsp:include page="includables/Deal%20Display.jsp">
          <jsp:param name="shopName" value="1 year low"/>
          <jsp:param name="price" value="${requestScope.price.oneYearLow.amount}"/>
          <jsp:param name="currency" value="${requestScope.price.oneYearLow.currency}"/>
        </jsp:include>
      </c:if>

      <c:if test="${requestScope.price.threeMonthsLow != null}">
        <jsp:include page="includables/Deal%20Display.jsp">
          <jsp:param name="shopName" value="Three months low"/>
          <jsp:param name="price" value="${requestScope.price.threeMonthsLow.amount}"/>
          <jsp:param name="currency" value="${requestScope.price.threeMonthsLow.currency}"/>
        </jsp:include>
      </c:if>

    </div>
    <div class="game-prices">
      <c:if test="${requestScope.price != null and requestScope.price.getDeals() != null and requestScope.price.getDeals().size() > 0}">
        <a id="deal-table-title" class="unstyled deal-display">
          <span>Shop</span>
          <span>Store Low</span>
          <span>Price</span>
        </a>
        <c:forEach var="deal" items="${requestScope.price.getDeals()}">
          <jsp:include page="includables/Deal%20Display.jsp">
            <jsp:param name="shopName" value="${deal.shopName}"/>
            <jsp:param name="price" value="${deal.dealPrice.amount}"/>
            <jsp:param name="cut" value="${deal.cut}"/>
            <jsp:param name="regularPrice" value="${deal.regularPrice.amount}"/>
            <jsp:param name="storeLow" value="${deal.storeLow.amount}"/>
            <jsp:param name="currency" value="${deal.dealPrice.currency}"/>

            <jsp:param name="dealURL" value="${deal.url}"/>
          </jsp:include>
        </c:forEach>
      </c:if>
    </div>
  </div>
  </div>



</body>
</html>