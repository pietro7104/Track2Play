<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: cube7
  Date: 25/01/2026
  Time: 14:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<c:choose>
  <c:when test="${param.score+0 le 30}">
    <c:set var="color" value="#ff6874"/>
  </c:when>
  <c:when test="${param.score+0 le 60}">
    <c:set var="color" value="#f0e13c"/>
  </c:when>
  <c:otherwise>
    <c:set var="color" value="#00ce7a"/>
  </c:otherwise>
</c:choose>

<span class="review-score" style="background-color: ${color}">${param.score}</span>