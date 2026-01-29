<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<link rel="stylesheet" href="../CSS/style.css">
<jsp:useBean id="Utility" class="Controller.Utility"/>
<jsp:useBean id="now" class="java.util.Date" />

<fmt:formatDate value="${now}" pattern="yyyy-MM-dd" var="time"/>
<fmt:formatDate value="${now}" pattern="HH:mm" var="timeM"/>

<div class="popup-block" id="buy-popup">
    <div class="buy-popup" style="flex-direction: column;">
        <form id="buy-form" action="CollectionAdd" method="post"
              style="display: flex; flex-direction: column">
            <input type="hidden" name="gameId" value="${param.gameID}">
            <span style="display: flex; flex-direction: row; justify-content: space-between">Data di acquisto: <label>
                    <input type="date" name="buy-date" value="${time}">
                </label>
                <label>
                    <input type="time" name="buy-time" value="${timeM}">
                </label>

            </span>
            <div style="display: flex; flex-direction: row; justify-content: space-between">
                <span>Prezzo: </span>
                <div>
                <label>
                    <input type="number" name="price" value="0" style="width: 40px">
                </label>
                <label>
                    <select name="currency_code">
                        <c:forEach items="${Utility.getAllCurrencies()}" var="item">
                            <option><c:out value="${item.getCurrencyCode()}"/></option>
                        </c:forEach>
                    </select>
                </label>
                </div>
            </div>
            <span> Piattaforma: <label>
                <input name="platform" type="text" value="Steam">
            </label></span>

            <div style="display: flex; flex-direction: row; justify-content: space-between">
                <span>E' un regalo? </span>
                <label>
                    <input type="checkbox" name="gift" value="false">
                </label>
            </div>
        </form>
        <div style="display: flex; flex-direction: row; justify-content: center; gap: 2px">
            <button tabindex="0" onkeydown="TogglePopup('buy-popup')" onclick="TogglePopup('buy-popup')">Annulla</button>
            <button tabindex="0" onkeydown="document.getElementById('buy-form').submit()" onclick="document.getElementById('buy-form').submit()">Aggiungi</button>
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
