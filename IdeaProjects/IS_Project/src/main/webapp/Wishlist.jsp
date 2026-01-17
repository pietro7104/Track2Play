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
        <strong><%= item.getName() %></strong>  <!-- Nome del gioco -->
        <br>
        Aggiunto da: <%= item.getUsername() %>  <!-- Username dell'utente -->
        <br>
        Aggiunto il: <%= item.getAddedDate() %>  <!-- Data di aggiunta -->
        <form action="Wishlist/Remove" method="post" style="display:inline;">
            <input type="hidden" name="name" value="<%= item.getName() %>">
            <input type="hidden" name="username" value="<%= item.getUsername() %>">
            <button type="submit">Rimuovi</button>
        </form>
    </li>
    <% } %>
</ul>
<% } %>

</body>
</html>