package Service;

import Controller.Utility;
import Model.APIControl.APIExceptions.APIException;
import Model.APIControl.APIInterface;
import Model.APIControl.Cost;
import Model.APIControl.Deal;
import Model.APIControl.Price;
import Model.ExpenseDAO;
import Model.Game;
import Model.TagDAO;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class AddGameInfoService {

    public void addGameInfo(int userId, Game game){
        TagDAO tagDAO = new TagDAO();
        for (String tag : game.getTags()) {
            tagDAO.addTagAppearence(userId, tag, 1);
        }
        ExpenseDAO expenseDAO = new ExpenseDAO();
        APIInterface api = Utility.getAPI();
        ArrayList<String> id = new ArrayList<>();
        id.add(game.getIsThereAnyDealID());
        ArrayList<Integer> shopid = new ArrayList<>();
        shopid.add(61);
        try{
            LinkedHashMap<String, Price> p = api.GetGamesPrices(id, shopid, "IT", false, 100, false);
            Price price = p.get(game.getIsThereAnyDealID());
            for (Deal d : price.getDeals()){
                Cost c = d.getRegularPrice();
                expenseDAO.addExpense(userId, game.getIsThereAnyDealID(), c.getAmount());
            }
        }catch (APIException e){
            e.printStackTrace();
        }
    }

    public void removeGameInfo(int userId, Game game){
        TagDAO tagDAO = new TagDAO();
        for (String tag : game.getTags()) {
            tagDAO.removeTagAppearence(userId, tag, 1);
        }
        ExpenseDAO expenseDAO = new ExpenseDAO();
        expenseDAO.deleteExpense(userId, game.getIsThereAnyDealID());
    }
}
