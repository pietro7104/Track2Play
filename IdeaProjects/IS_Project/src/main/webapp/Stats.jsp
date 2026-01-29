<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
  <title>Statistiche</title>
  <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
  <link rel="stylesheet" href="CSS/style.css">
</head>

<body class="home-page">

<jsp:include page="includables/NavBar.jsp"/>

<h1>Le tue statistiche</h1>

<div class="stats-grid">

  <!-- BLOCCO NUMERI -->
  <div class="stat-box">
    <h3>Riepilogo</h3>
    <p>Giochi acquistati: ${stats.purchasedGames}</p>
    <p>Giochi ricevuti in regalo: ${stats.giftedGames}</p>
    <p>Totale speso: ${stats.totalSpent} ${stats.currency}</p>
  </div>

  <div class="stat-box">
    <h3>Collezione</h3>
    <p>Totali: ${stats.totalGames}</p>
    <p>Completati: ${stats.completedGames}</p>
    <p>Non completati: ${stats.notCompletedGames}</p>
    <p>Completamento: ${stats.completionPercentage}%</p>
  </div>

  <!-- GRAFICI -->
  <div class="chart-box">
    <canvas id="completionChart"></canvas>
  </div>

  <div class="chart-box">
    <canvas id="purchaseChart"></canvas>
  </div>

</div>

<!-- TABELLA ACQUISTI -->
<h2 style="text-align: center;">Storico acquisti</h2>

<table class="styled-table">
  <tr>
    <th>Data</th>
    <th>Gioco</th>
    <th>Piattaforma</th>
    <th>Prezzo</th>
    <th>Tipo</th>
  </tr>

  <c:forEach items="${purchases}" var="p">
    <tr>
      <td>${p.date}</td>
      <td>${p.gameTitle}</td>
      <td>${p.platform}</td>
      <td>${p.price} ${p.currency}</td>
      <td>
        <c:choose>
          <c:when test="${p.gift}">Regalo</c:when>
          <c:otherwise> Acquistato</c:otherwise>
        </c:choose>
      </td>
    </tr>
  </c:forEach>
</table>

<!-- DATI GRAFICI -->
<script>
  const completed = ${stats.completedGames};
  const notCompleted = ${stats.notCompletedGames};

  new Chart(document.getElementById("completionChart"), {
    type: 'doughnut',
    data: {
      labels: ["Completati", "Non completati"],
      datasets: [{
        data: [completed, notCompleted]
      }]
    }
  });

  const bought = ${stats.purchasedGames};
  const gifted = ${stats.giftedGames};

  new Chart(document.getElementById("purchaseChart"), {
    type: 'pie',
    data: {
      labels: ["Acquistati", "Regalo"],
      datasets: [{
        data: [bought, gifted]
      }]
    }
  });
</script>

</body>
</html>

