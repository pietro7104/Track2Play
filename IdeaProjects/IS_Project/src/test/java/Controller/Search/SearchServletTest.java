package Controller.Search;

import Model.APIControl.Price;
import Model.Game;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SearchServletTest {

    @Test
    void SearchServletGivesErrorWhenQueryIsEmpty() throws ServletException, IOException {

        HttpSession session = mock(HttpSession.class);
        RequestDispatcher rd = mock(RequestDispatcher.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameter("query")).thenReturn("");
        when(request.getSession()).thenReturn(session);
        when(request.getRequestDispatcher("Home Page.jsp")).thenReturn(rd);
        when(request.getRequestDispatcher("Search Result Page.jsp")).thenReturn(rd);

        ArrayList<String> errors = new ArrayList();
        when(request.getAttribute("error_list")).thenReturn(errors);

        SearchServlet searchServlet = new SearchServlet();
        searchServlet.doGet(request, response);

        assert(errors != null && !errors.isEmpty() && errors.getFirst().equals("Nessun termine di ricerca"));
    }

    @Test
    void SearchResultIsNotEmptyWhenTitleIsARealGame() throws ServletException, IOException {
        HttpSession session = mock(HttpSession.class);
        RequestDispatcher rd = mock(RequestDispatcher.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameter("query")).thenReturn("The Last of Us parte 2");
        when(request.getSession()).thenReturn(session);
        when(request.getRequestDispatcher("Home Page.jsp")).thenReturn(rd);
        when(request.getRequestDispatcher("Search Result Page.jsp")).thenReturn(rd);


        SearchServlet searchServlet = new SearchServlet();
        searchServlet.doGet(request, response);

        ArrayList<Price> prices = (ArrayList<Price>)request.getAttribute("prices");
        ArrayList<Game> searchResults = (ArrayList<Game>)request.getAttribute("search_results");

        assert(prices != null);
        assert(searchResults != null);
        assert(!prices.isEmpty());
        assert(!searchResults.isEmpty());

    }
}