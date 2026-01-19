package Controller;

import Model.APIControl.APIBridge;
import Model.APIControl.APIInterface;
import Model.APIControl.RefinedAPIBridge;
import jakarta.servlet.http.HttpServletRequest;

import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Utility {

    public static void addError(HttpServletRequest request, String error){
        ArrayList<String> errorlist = (ArrayList<String>)request.getAttribute("error_list");
        if (errorlist == null) errorlist = new ArrayList<>();
        errorlist.add(error);
        request.setAttribute("error_list", errorlist);
    }

    // Hashing SHA-512 con lunghezza di hash a 128 caratteri
    public static String toHash(String pass){
        String hashString = null;
        try{
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-512");
            byte[] hash = md.digest(pass.getBytes(StandardCharsets.UTF_8));
            hashString = "";
            for (int i = 0; i < hash.length; i++) {
                hashString += Integer.toHexString((hash[i] & 0xff) | 0x100).substring(1, 3);
            }
        }
        catch(java.security.NoSuchAlgorithmException e){
            System.out.println(e.getMessage());
        }
        return hashString;
    }

    public static APIInterface getAPI(){
        APIBridge bridge = new RefinedAPIBridge();
        return bridge.getAPIInterface();
    }

    public static String dateToString(Date date){
        String pattern = "dd/MM/yyyy";

        DateFormat df = new SimpleDateFormat(pattern);

        return df.format(date);
    }

    public String dateToStringJSP(Date date){
        return dateToString(date);
    }
}
