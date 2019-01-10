package wallet.CommonElements.Forms;

public class PaymentsHistoryForm {

    public enum FilterDateSort{
        NO(""),
        ASC("ASC"),
        DESC("DESC");

        private String name;

        FilterDateSort(String name){
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public enum FilterAmountSort {
        NO(""),
        ASC("ASC"),
        DESC("DESC");

        private String name;

        FilterAmountSort(String name){
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

}
