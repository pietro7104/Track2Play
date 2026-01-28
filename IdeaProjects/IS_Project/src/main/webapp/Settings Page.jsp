<%--
  Created by IntelliJ IDEA.
  User: dekub
  Date: 1/28/2026
  Time: 1:41 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:useBean id="Utility" class="Controller.Utility"/>

<html>
<head>
    <link rel="stylesheet" href="CSS/style.css">
    <link rel="stylesheet" href="CSS/Settings_Style/settings_style.css">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Impostazioni Account</title>
</head>
<body class="home-page">
    <jsp:include page="includables/NavBar.jsp"/>

    <div class="center_div">
        <div class="form_div">
            <div class="input">
                <label for="username">Username</label><br>
                <input type="text" id="username" maxlength="30" value="${sessionScope.user.username}" disabled> <br>
            </div>

            <div class="input">
                <label for="password1">Nuova Password</label> <br>
                <input type="password" id="password1" maxlength="15" oninput="changePasswordEvent()"> <br>
            </div>

            <div class="input" id="passwordConfirm" style="display: none">
                <form action="ModifyPassword" method="post">
                    <label for="password2">Conferma Password</label> <br>
                    <input type="password" name="newPassword" id="password2" oninput="checkSamePassword()" maxlength="15"> <br>

                    <button class="button" id="savePasswordButton" disabled>Salva Modifiche</button>
                    <p id="alertPassword" style="display: block; color: red;">Password nuova e password di conferma non coincidono</p>
                </form>
            </div>

            <div class="input">
                <form action="ModifyISOCode" method="post">
                    <label for="isoCode">Codice ISO</label> <br>
                    <p>Il tuo codice ISO: <b>${sessionScope.user.countryISO}</b></p>
                    <p>Cambia in: </p>
                    <select name="isoCode" id="isoCode" onchange="changeISOEvent()">
                        <c:forEach items="${Utility.getISOCountries()}" var="item">
                            <option> <c:out value="${item}"/></option>
                        </c:forEach>
                    </select> <br>
                    <button class="button" id="saveISOButton" style="display: none">Salva Modifiche</button>
                </form>
            </div>

            <jsp:include page="includables/Error%20Display.jsp"/>
        </div>
        <div class="buttons_div">
            <form action="DeleteAccount" method="post">
                <button class="button">Cancella Account</button>
            </form>
            <form action="Logout" method="post">
                <button class="button">Logout</button>
            </form>
        </div>
    </div>

</body>

<script type="text/javascript">

    function changeISOEvent() {
        saveButton = document.getElementById("saveISOButton")

        if(saveButton.style.display == "none") {
            saveButton.style.display = "block"
        }
    }

    function checkSamePassword() {
        saveButton = document.getElementById("savePasswordButton")

        password1 = document.getElementById("password1").value
        password2 = document.getElementById("password2").value

        if(password1 == password2 && (password2 != "")) {
            saveButton.disabled = false
            document.getElementById("alertPassword").style.display = "none"
        } else {
            saveButton.disabled = true
            document.getElementById("alertPassword").style.display = "block"
        }

    }

    function changePasswordEvent() {
        passwordConfirm = document.getElementById("passwordConfirm")

        // Mostra il pulsante di salvataggio se è ancora nascosto
        if(passwordConfirm.style.display === "none")
            passwordConfirm.style.display = "block"

        checkSamePassword()
    }
</script>

</html>
