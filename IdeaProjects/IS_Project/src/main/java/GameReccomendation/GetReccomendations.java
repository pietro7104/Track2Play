package GameReccomendation;

import Model.Database;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class GetReccomendations {

    static final String pythonFolder = "../../../Users/cube7/Desktop/Game_Reccomandation/getreccomendations.py";

    public static ArrayList<String> get(int userID){

        try{
            String tag_appearences = "";
            float averagePrice = 0;
            Connection conn = Database.getConnection();
            PreparedStatement ps = conn.prepareStatement("set session group_concat_max_len = 999999999999");
            ps.executeUpdate();
            ps = conn.prepareStatement("SELECT U.IdUtente, AVG(S.prezzo) as prezzoMedio, " +
                    "GROUP_CONCAT(DISTINCT Concat(UtenteTag.tag_name, ':', UtenteTag.appearences)) as tag_appearences " +
                    "FROM Utente U " +
                    "LEFT JOIN UtenteTag ON (U.IdUtente = UtenteTag.IdUtente) " +
                    "    LEFT JOIN PrezzoGiocoAggiunto S ON (U.IdUtente = S.IdUtente) " +
                    "    WHERE U.IdUtente=? " +
                    "GROUP BY U.IdUtente");
            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                tag_appearences = rs.getString("tag_appearences");
                averagePrice = rs.getFloat("prezzoMedio");
            }
            else return null;

            ProcessBuilder builder = new ProcessBuilder("python", pythonFolder, ""+averagePrice, tag_appearences);
            Process process = builder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            String result;
            ArrayList<String> reccomendations = new ArrayList<>();
            while((result=reader.readLine())!=null){
                reccomendations.add(result);
                //System.out.println(result);
                //System.out.println("reading");
            }
            String error;
            while((error=errorReader.readLine())!=null){
                //System.out.println(error);
            }
            return reccomendations;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
