package Model.APIControl;

import Model.APIControl.APIExceptions.APIException;
import Model.Game;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class APIImplementationTest {

    @Test
    void SearchByTitleResultIsEmptyWhenTitleIsEmpty() {
        APIImplementation imp = new APIImplementation();
        try{
            ArrayList<Game> result = imp.SearchByTitle("", 100);
            assert(result.isEmpty());
        }
        catch (APIException _){
            System.out.println("Error");
        }
    }

    @Test
    void SearchByTitleResultIsNotEmptyWhenTitleIsARealGame() throws APIException {
        APIImplementation imp = new APIImplementation();
        ArrayList<Game> result = imp.SearchByTitle("The Last of Us parte 2", 100);
        assert(!result.isEmpty());
    }
}