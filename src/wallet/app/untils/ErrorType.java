package wallet.app.untils;

public enum ErrorType {
    /*
     * Zawiera ustandaryzowane informacje o najczęstrzych błędach.
     * */
    NO_ERROR(""),
    UNAUTHORIZED("Unauthorized request."),
    INVALID_DATA("Invalid data."),
    APPLICATION_ERROR("Application error."),
    NO_INTERNET_CONNECTION("No internet connection.");

    private String errorMessage;

    ErrorType(String errorMessage){
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage(){
        return this.errorMessage;
    }

}
