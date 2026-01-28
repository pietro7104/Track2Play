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
    <form id="open-home-page" action="OpenHomePage" method="get">
        <button class="navbar_button" onclick="document.getElementById('open-home-page').submit()">Home</button>
    </form>

    <div>
        <form action="Search" method="get">
            <label>
                <input name="query" class="search-bar" type="text" placeholder="Cerca..">
            </label>
            <button type="submit" class="search-button">Cerca</button>
        </form>

    </div>

    <div style="display: flex; flex-direction: row">
        <c:choose>
            <c:when test="${sessionScope.user != null}">
                <a href="Settings Page.jsp">
                    <button class="navbar_button">Impostazioni Account</button>
                </a>

                <a href="Collection Page.jsp">
                    <button class="navbar_button">Collezione</button>
                </a>
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

