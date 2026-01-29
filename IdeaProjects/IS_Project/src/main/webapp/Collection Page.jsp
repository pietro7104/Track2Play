<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:useBean id="ourDate" class="java.util.Date"/>
<jsp:useBean id="Utility" class="Controller.Utility"/>
<jsp:setProperty name="ourDate" property="time" value="${ourDate.time}"/>

<link rel="stylesheet" href="CSS/style.css">

<html>

<head>
    <title>Collezione</title>
</head>

<jsp:include page="includables/Error%20Popup.jsp"/>
<body class="home-page">
    <jsp:include page="includables/NavBar.jsp"/>


    <h1>La tua collezione</h1>
    <c:choose>

        <c:when test="${requestScope.collection == null or requestScope.collection.size() le 0}">
            <div style="margin-left: 20px">Nessun gioco nella collezione</div>
        </c:when>

        <c:otherwise>
            <div class="collection">

                <c:forEach items="${requestScope.collection}" var="item">
                    <div style="display: flex; flex-direction: column; align-items: center">
                    <jsp:include page="includables/Game Display.jsp">
                        <jsp:param name="gameID" value="${item.gameId}"/>
                        <jsp:param name="image" value="${item.cover}"/>
                        <jsp:param name="game_name" value="${item.gameTitle}"/>
                        <jsp:param name="addedDate"
                                   value="${Utility.dateToStringJSP(item.dateAdded)}"/>
                        <jsp:param name="completed" value="${item.completed}"/>
                    </jsp:include>

                    <div style="display: flex; flex-direction: row; gap: 15px; align-items: center">
                        <!-- Pulsante di rimozione -->
                        <form action="CollectionRemove" method="post" style="text-align:center;">
                            <input type="hidden" name="gameId" value="${item.gameId}">
                            <button class="remove-game-button" type="submit">Rimuovi dalla collezione</button>
                        </form>
                        <!-- Stato di completamento gioco -->
                        <form action="CollectionUpdateCompletion" method="post">

                            <input type="hidden" name="gameId" value="${item.gameId}" />

                            <label>
                                <input type="checkbox"
                                       name="completed"
                                       value="true"
                                       onchange="this.form.submit()"
                                       <c:if test="${item.completed}">checked</c:if> />
                                Completato
                            </label>

                        </form>
                    </div>

                    </div>
                </c:forEach>

            </div>
        </c:otherwise>

    </c:choose>

</body>
</html>
