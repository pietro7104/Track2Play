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
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

        HashMap<String, Object> attributes = new HashMap<>();

        doAnswer(
                new Answer<Void>() {
                    public Void answer(InvocationOnMock invocation) throws Throwable {
                        Object[] args = invocation.getArguments();
                        attributes.put(args[0].toString(), args[1]);
                        return null;
                    }
                }
        ).when(request).setAttribute(Mockito.anyString(), Mockito.any(Object.class));

        SearchServlet searchServlet = new SearchServlet();
        searchServlet.doGet(request, response);

        ArrayList<String> errors = null;
        try{
            errors = (ArrayList<String>) attributes.get("error_list");
        }catch (ClassCastException _){}

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



        HashMap<String, Object> map = new HashMap<>();

        doAnswer(
                new Answer<Void>() {
                    public Void answer(InvocationOnMock invocation) throws Throwable {
                        Object[] args = invocation.getArguments();
                        map.put(args[0].toString(), args[1]);
                        return null;
                    }
                }
        ).when(request).setAttribute(Mockito.anyString(), Mockito.any(Object.class));


        SearchServlet searchServlet = new SearchServlet();
        searchServlet.doGet(request, response);

        ArrayList<Game> searchResults = null;
        LinkedHashMap<String, Price> prices = null;

        try{
            searchResults = (ArrayList<Game>) map.get("search_results");
        }catch (ClassCastException _){}
        try {
            prices = (LinkedHashMap<String, Price>) map.getOrDefault("prices", null);
        }catch (ClassCastException _){}

        System.out.println(prices);
        System.out.println(searchResults);

        assert(prices != null);
        assert(searchResults != null);
        assert(!prices.isEmpty());
        assert(!searchResults.isEmpty());

    }

    public void setArray(ArrayList array1, ArrayList array2) {
        array1 = new ArrayList<>();
        for (Object o : array2) {
            array1.add(o);
        }
    }

    public void setHashMap(LinkedHashMap hashmap1, LinkedHashMap hashmap2) {
        hashmap1 = new LinkedHashMap<>();
        for (Object o : hashmap2.keySet()) {
            hashmap1.put(o, hashmap2.get(o));
        }
    }
}