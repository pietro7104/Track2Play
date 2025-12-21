package Controller.APITest;

import Model.APIControl.APIImplementation;
import Model.APIControl.APIInterface;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.ArrayList;

@WebServlet("/APITest")
public class APITestServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        APIInterface api = new APIImplementation();
        ArrayList<String> list = new ArrayList<>();
        /*list.add("497f6eca-6276-4993-bfeb-53cbbbba6f08e");
        //list.add("01849782-1017-7389-8de4-c97c587fd7e3");
        api.GetGamesIDsOnShop(list, 65);*/

        api.SearchByTitle("Sonic", 5);
    }
}
