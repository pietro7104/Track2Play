package Controller.Login;

import Controller.AccountManagement.LoginServlet;
import Controller.HomePageManagement.OpenHomePageServlet;
import Model.User;
import Model.UserManagement;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoginServletTest {

    private LoginServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private RequestDispatcher dispatcher;

    @BeforeEach
    void setUp() {
        servlet = new LoginServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        dispatcher = mock(RequestDispatcher.class);

        when(request.getSession()).thenReturn(session);
        when(request.getRequestDispatcher("index.jsp")).thenReturn(dispatcher);
    }

    // LOGIN OK
    @Test
    void testLoginSuccess() throws Exception {
        when(request.getParameter("username")).thenReturn("mario");
        when(request.getParameter("password")).thenReturn("1234");

        User fakeUser = new User();
        fakeUser.setUsername("mario");

        try (MockedConstruction<UserManagement> mocked = Mockito.mockConstruction(UserManagement.class,
                (mock, context) -> {
                    when(mock.checkCredentialsAndGetUser("mario", "1234"))
                            .thenReturn(fakeUser);
                });
             MockedConstruction<OpenHomePageServlet> mockedHome =
                     Mockito.mockConstruction(OpenHomePageServlet.class)) {

            servlet.doPost(request, response);

            verify(session).setAttribute("user", fakeUser);
            verify(response).setStatus(HttpServletResponse.SC_OK);
        }
    }

    // Password errata
    @Test
    void testLoginWrongCredentials() throws Exception {
        when(request.getParameter("username")).thenReturn("mario");
        when(request.getParameter("password")).thenReturn("wrong");

        try (MockedConstruction<UserManagement> mocked = Mockito.mockConstruction(UserManagement.class,
                (mock, context) -> {
                    when(mock.checkCredentialsAndGetUser("mario", "wrong"))
                            .thenThrow(new RuntimeException("Credenziali errate"));
                })) {

            servlet.doPost(request, response);

            verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
            verify(dispatcher).forward(request, response);
        }
    }

    // Nome utente errato
    @Test
    void testLoginUsernameNotFound() throws Exception {
        when(request.getParameter("username")).thenReturn("utenteinesistente");
        when(request.getParameter("password")).thenReturn("1234");

        try (MockedConstruction<UserManagement> mocked = Mockito.mockConstruction(UserManagement.class,
                (mock, context) -> {
                    when(mock.checkCredentialsAndGetUser("utenteinesistente", "1234"))
                            .thenThrow(new RuntimeException("Utente non trovato"));
                })) {

            servlet.doPost(request, response);

            verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);

            verify(dispatcher).forward(request, response);

            verify(session, never()).setAttribute(eq("user"), any());
        }
    }
}
