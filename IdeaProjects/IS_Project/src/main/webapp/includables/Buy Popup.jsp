<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<link rel="stylesheet" href="../CSS/style.css">
<jsp:useBean id="Utility" class="Controller.Utility"/>

<c:choose>
    <c:when test="${requestScope.error_list != null and requestScope.error_list.size() gt 0}">
        <c:set var="popupClass" value="popup-block show"/>
    </c:when>
    <c:otherwise>
        <c:set var="popupClass" value="popup-block"/>
    </c:otherwise>
</c:choose>

<div class="${popupClass}" id="buy-popup">
    <div class="popup" style="flex-direction: column;">
        <form id="buy-form" action="Collection/Add" method="post">
            <input type="hidden" name="gameId" value="${param.gameID}">
            <input type="hidden" name="userId" value="${param.userID}">
            Data di acquisto: <input type="date" name="buy-date" value="<%= new java.util.Date()%>">
            Prezzo: <div style="display: flex; flex-direction: row">
                <input type="number" name="price" value="0">
                <label>
                    <select name="currency_code">
                        <c:forEach items="${Utility.getAllCurrencies()}" var="item">
                            <option><c:out value="${item.getCurrencyCode()}"/></option>
                        </c:forEach>
                    </select>
            </label>
            </div>
            Piattaforma: <input name="platform" type="text" value="Steam">
        </form>
        <div style="display: flex; flex-direction: row">
            <button tabindex="0" onkeydown="TogglePopup('buy-popup')" onclick="TogglePopup('buy-popup')">Annulla</button>
            <button tabindex="0" onkeydown="document.getElementById('buy-form').submit()" onclick="TogglePopup('buy-popup')">Aggiungi</button>
        </div>
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
