<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<link rel="stylesheet" href="../CSS/style.css">
<jsp:useBean id="Utility" class="Controller.Utility"/>


<div class="popup-block" id="buy-popup">
    <div class="popup" style="flex-direction: column;">
        <form id="buy-form" action="CollectionAdd" method="post">
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
</script>
