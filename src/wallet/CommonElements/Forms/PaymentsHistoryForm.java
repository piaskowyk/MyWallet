package wallet.CommonElements.Forms;

import wallet.CommonElements.Entity.PaymentCategory;

import java.util.Date;

public class PaymentsHistoryForm {

    private FilterDateSort filterDateSort;
    private FilterAmountSort filterAmountSort;
    private PaymentCategory paymentCategory;
    private Date dateStart;
    private Date dateEnd;

    public FilterDateSort getFilterDateSort() {
        return filterDateSort;
    }

    public void setFilterDateSort(FilterDateSort filterDateSort) {
        this.filterDateSort = filterDateSort;
    }

    public FilterAmountSort getFilterAmountSort() {
        return filterAmountSort;
    }

    public void setFilterAmountSort(FilterAmountSort filterAmountSort) {
        this.filterAmountSort = filterAmountSort;
    }

    public PaymentCategory getPaymentCategory() {
        return paymentCategory;
    }

    public void setPaymentCategory(PaymentCategory paymentCategory) {
        this.paymentCategory = paymentCategory;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public enum FilterDateSort{
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
