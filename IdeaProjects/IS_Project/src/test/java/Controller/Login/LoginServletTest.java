package Controller.Login;

import Controller.AccountManagement.LoginServlet;
import Model.User;
import Model.UserManagement;
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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginServletTest {

    @Test
    void testLoginUsernameNotRegistered() throws ServletException, IOException, SQLException {

        HttpSession session = mock(HttpSession.class);
        RequestDispatcher rd = mock(RequestDispatcher.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        UserManagement userManagement = mock(UserManagement.class);


        when(request.getParameter("username")).thenReturn("user_not_registered");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getSession()).thenReturn(session);
        when(request.getRequestDispatcher(Mockito.anyString())).thenReturn(rd);


        when(userManagement.checkCredentialsAndGetUser("user_not_registered", "password"))
                .thenThrow(new RuntimeException("Nessun utente trovato con l'username inserito."));

        HashMap<String, Object> attributes = new HashMap<>();
        doAnswer(new Answer<Void>() {
            public Void answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                attributes.put(args[0].toString(), args[1]);
                return null;
            }
        }).when(request).setAttribute(Mockito.anyString(), Mockito.any(Object.class));


        LoginServlet loginServlet = new LoginServlet();
        loginServlet.doPost(request, response);


        ArrayList<String> errors = (ArrayList<String>) attributes.get("error_list");
        assert(errors != null && !errors.isEmpty() && errors.get(0).equals("Nessun utente trovato con l'username inserito."));

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(request).getRequestDispatcher("index.jsp");
        verify(rd).forward(request, response);
    }

    @Test
    void testLoginCorrect() throws ServletException, IOException, SQLException {

        HttpSession session = mock(HttpSession.class);
        RequestDispatcher rd = mock(RequestDispatcher.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        UserManagement userManagement = mock(UserManagement.class);


        when(request.getParameter("username")).thenReturn("user_registered");
        when(request.getParameter("password")).thenReturn("correct_passw");
        when(request.getSession()).thenReturn(session);
        when(request.getRequestDispatcher(Mockito.anyString())).thenReturn(rd);


        User user = new User();
        user.setUsername("user_registered");
        user.setPassword("correct_pass");

        when(userManagement.checkCredentialsAndGetUser("ema", "ema"))
                .thenReturn(user);


        LoginServlet loginServlet = new LoginServlet();
        loginServlet.doPost(request, response);


        // verify(session).setAttribute("user", user);
        verify(session).getAttribute("user");
        verify(request).getRequestDispatcher("Home Page.jsp");
        verify(rd).forward(request, response);
    }
}

