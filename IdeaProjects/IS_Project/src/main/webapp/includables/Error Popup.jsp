<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<link rel="stylesheet" href="../CSS/style.css">

<c:choose>
    <c:when test="${requestScope.error_list != null and requestScope.error_list.size() gt 0}">
        <c:set var="popupClass" value="popup-block show"/>
    </c:when>
    <c:otherwise>
        <c:set var="popupClass" value="popup-block"/>
    </c:otherwise>
</c:choose>

<div class="${popupClass}" id="error-popup">
    <div class="popup" style="flex-direction: column;">
        <div id="errors">
            <jsp:include page="Error Display.jsp"/>
        </div>
        <button tabindex="0" onkeydown="TogglePopup('error-popup')" onclick="TogglePopup('error-popup')">Chiudi</button>
    </div>
</div>

<script>
    function TogglePopup(popupID)
    {
        const popup = document.getElementById(popupID);
        popup.classList.toggle("show");
    }

    function SetError(error){
        document.getElementById("errors").innerHTML = error;
    }
</script>
