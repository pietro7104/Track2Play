<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: cube7
  Date: 03/11/2025
  Time: 09:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>



<html>
<header class="navbar" style="align-items: center">
    <a href="Home Page.jsp">
        <button class="navbar_button">Home</button>
    </a>

    <div>
        <form action="Search" method="get">
            <label>
                <input name="query" class="search-bar" type="text" placeholder="Cerca..">
            </label>
            <button type="submit" class="search-button">Cerca</button>
        </form>

    </div>

    <div style="display: flex; flex-direction: row">
        <a href="Collection Page.jsp">
            <button class="navbar_button">Collezione</button>
        </a>
        <c:choose>
            <c:when test="${sessionScope.user != null}">
                <a href="Collection Page.jsp">
                    <button class="navbar_button">Collezione</button>
                </a>

                <form action="Logout" method="post">
                    <input class="navbar_button" type="submit" value="Logout">
                </form>

                <form action="DeleteAccount" method="post">
                    <input class="navbar_button" type="submit" value="Cancella account">
                </form>
            </c:when>

            <c:otherwise>
                <a href="index.jsp">
                    <button class="navbar_button">Login</button>
                </a>
            </c:otherwise>
        </c:choose>
    </div>

</header>
</html>

