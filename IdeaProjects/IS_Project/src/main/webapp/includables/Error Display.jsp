<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%--
  Created by IntelliJ IDEA.
  User: cube7
  Date: 02/11/2025
  Time: 16:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <body>
      <c:if test="${requestScope.error_list != null and requestScope.error_list.size() gt 0}">
          <c:choose>
              <c:when test="${requestScope.error_list.size() eq 1}">
                  <span class="error-message">
                      <c:forEach items="${requestScope.error_list}" var="e">
                          ${e}
                      </c:forEach>
                  </span>
              </c:when>

              <c:otherwise>
                  <ul class="error_list">
                      <c:forEach items="${requestScope.error_list}" var="e">
                          <li class="error-message">${e}</li>
                      </c:forEach>
                  </ul>
              </c:otherwise>
          </c:choose>
      </c:if>
  </body>
</html>
