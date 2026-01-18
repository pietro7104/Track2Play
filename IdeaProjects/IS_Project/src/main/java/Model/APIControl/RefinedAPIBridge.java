package Model.APIControl;

public class RefinedAPIBridge extends APIBridge {

    public APIInterface getAPIInterface() {
        apiInterface = new APIImplementation();
        return apiInterface;
    }
}
