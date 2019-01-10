package wallet.CommonElements.Untils;

public class Validator {

    public static boolean isValidUrlControllersPath(String item){
        return item.length() < 225 && item.matches("[a-zA-Z_]+");
    }

    public static boolean isValidAplhaString(String item){
        return item.length() < 225 && item.matches("[a-zA-ZóąśłżźćńÓĄŚŁŻŹĆŃ]+");
    }

    public static boolean isvalidPassword(String item){
        return item.length() > 5 && item.length() <= 40;
    }

    public static boolean isValidFloatNum(String item){
        return item.length() < 9 && item.matches("[0-9]+.[0-9]+|[0-9]+");
    }

    public static boolean isValidEmail(String item){
        return item.matches("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$") && item.length() < 225;
    }

    public static boolean isValidAlphaStringPersonInfo(String item, int maxLen){
        return item.length() <= maxLen && item.matches("[A-ZÓĄŚÐŁŻŹĆŃ][a-zóąśłżźćń]+");
    }

    public static boolean isValidAlphaStringPersonInfo(String item){
        return item.matches("[A-ZÓĄŚÐŁŻŹĆŃ][a-zóąśłżźćń]+");
    }

    public static boolean isDataPicker(String item){
        return item.matches("[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}");
    }

}
