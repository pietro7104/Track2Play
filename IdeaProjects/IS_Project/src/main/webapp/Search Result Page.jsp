<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<link rel="stylesheet" href="CSS/style.css">
<html>
<head>
    <title>Ricerca</title>
</head>
<body class="home-page">
    <div>Risultati per "${requestScope.query}"</div>
    <c:choose>
        <c:when test="${requestScope.search_results == null or requestScope.search_results.size() le 0}">
            Nessun risultato
        </c:when>
        <c:otherwise>
            <div class="collection">
                <c:forEach items="${requestScope.search_results}" var="game">
                    <jsp:include page="includables/Game%20Display.jsp">
                        <jsp:param name="image" value="${game.GetHighestResolutionBanner()}"/>
                        <jsp:param name="game_name" value="${game.getTitle()}"/>
                    </jsp:include>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>

</body>
</html>
