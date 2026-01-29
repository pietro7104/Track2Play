<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="userManagement" class="Model.UserManagement" scope="page"/>


<link rel="stylesheet" href="CSS/style.css">

<meta name="viewport" content="width=device-width, initial-scale=1">

<html>



<head>
  <title>${requestScope.info.title}</title>
</head>

<jsp:include page="includables/Error%20Popup.jsp"/>
<jsp:include page="includables/Buy%20Popup.jsp">
  <jsp:param name="gameID" value="${requestScope.info.isThereAnyDealID}"/>
  <jsp:param name="userID" value="${sessionScope.user.ID}"/>
</jsp:include>

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

    <c:if test="${sessionScope.user != null}">
      <div class="buttons" style="display: flex; flex-direction: row; margin-top: 10px; margin-bottom: 10px; gap: 5px">
        <c:catch var="wishlistError">
          <c:forEach var="item" items="${userManagement.getWishlistedGamesByUserId(sessionScope.user.ID)}">
            <c:if test="${item.gameId == requestScope.info.isThereAnyDealID}">
              <c:set var="gameInWishlist" value="true"/>
            </c:if>
          </c:forEach>
        </c:catch>

        <c:catch var="collectionError">
          <c:forEach var="item" items="${userManagement.getUserCollection(sessionScope.user.ID)}">
            <c:if test="${item.gameId == requestScope.info.isThereAnyDealID}">
              <c:set var="gameInCollection" value="true"/>
            </c:if>
          </c:forEach>
        </c:catch>


        <c:choose>
          <c:when test="${wishlistError != null}">
            <span class="error-message">Errore nel controllo della wishlist </span>
          </c:when>

          <c:when test="${gameInWishlist == null}">
            <form action="WishlistAdd" method="post">
              <input type="hidden" name="gameId" value="${requestScope.info.isThereAnyDealID}">
              <input type="hidden" name="gameTitle" value="${requestScope.info.title}">
              <input type="hidden" name="gameBanner" value="${requestScope.info.GetHighestResolutionBanner()}">
              <button class="add-to-wishlist-button" type="submit">
                Aggiungi alla wishlist
              </button>
            </form>
          </c:when>

          <c:when test="${gameInWishlist != null}">
            <form action="Wishlist/Remove" method="post">
              <input type="hidden" name="gameId" value="${requestScope.info.isThereAnyDealID}">
              <button class="remove-from-wishlist-button" type="submit">
                Rimuovi dalla wishlist
              </button>
            </form>
          </c:when>
        </c:choose>

        <c:choose>
          <c:when test="${collectionError != null}">
            <span class="error-message">Errore nel controllo della collezione</span>
          </c:when>

          <c:when test="${gameInCollection == null}">
              <button class="add-to-collection-button" type="submit" onclick="TogglePopup('buy-popup')">
                Aggiungi alla collezione
              </button>
          </c:when>

          <c:when test="${gameInCollection != null}">
            <form action="Wishlist/Remove" method="post">
              <input type="hidden" name="gameId" value="${requestScope.info.isThereAnyDealID}">
              <button class="remove-from-collection-button" type="submit">
                Rimuovi dalla collezione
              </button>
            </form>
          </c:when>
        </c:choose>
      </div>
    </c:if>
      <c:choose>
        <c:when test="${requestScope.info.reviews != null and requestScope.info.reviews.size() gt 0}">
          <span style="font-size: 30px; font-weight: bold; margin-top: 20px">Reviews:</span>
        </c:when>
        <c:otherwise>
          <span style="font-size: 30px; font-weight: bold; margin-top: 20px">Nessuna recensione</span>
        </c:otherwise>
      </c:choose>


    <div class="review-display">
      <c:forEach var="review" items="${requestScope.info.reviews}">
        <a class="unstyled review" <c:if test="${review.url != null}">href="${review.url}"</c:if>>
          <jsp:include page="includables/Review%20Score.jsp">
            <jsp:param name="score" value="${review.score}"/>
          </jsp:include>
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