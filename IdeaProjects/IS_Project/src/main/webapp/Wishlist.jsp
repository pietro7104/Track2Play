<%@ page import="java.util.List" %>
<%@ page import="Model.WishlistItem" %>
<!DOCTYPE html>

<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>La tua Wishlist</title>
</head>
<body>

<h2>Ciao <%= username %>, ecco la tua Wishlist</h2>

<% if (wishlistItems == null || wishlistItems.isEmpty()) { %>
<p>La tua wishlist è vuota.</p>
<% } else { %>
<ul>
    <% for (WishlistItem item : wishlistItems) { %>
    <li>
        <strong><%= item.getGameTitle() %></strong>  <!-- Titolo del gioco -->
        <br>
        Aggiunto il: <%= item.getAddedDate() %>  <!-- Data di aggiunta -->
        <form action="Wishlist/Remove" method="post" style="display:inline;">
            <input type="hidden" name="gameName" value="<%= item.getGameTitle() %>">
            <input type="hidden" name="username" value="<%= item.getUsername() %>">
            <button type="submit">Rimuovi</button>
        </form>
    </li>
    <% } %>
</ul>
<% } %>

</body>
</html>