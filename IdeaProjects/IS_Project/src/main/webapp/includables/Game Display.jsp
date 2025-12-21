<%--
  Created by IntelliJ IDEA.
  User: cube7
  Date: 17/11/2025
  Time: 09:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body>
    <div class="game-display">
        <img class="game-image" src="${param.image}" alt="${param.game_name}">
        <div class="game-name-display">
            ${param.game_name}
        </div>
    </div>
</body>
</html>
