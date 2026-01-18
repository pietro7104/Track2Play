package Model.APIControl.APIExceptions;

public class APIException extends Exception{
    @Override
    public String getMessage() {
        return message;
    }

    String message;

    public APIException(String message){
        this.message = message;
    }
}
