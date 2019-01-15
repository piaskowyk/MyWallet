package wallet.CommonElements.Forms;

import wallet.CommonElements.Entity.PaymentCategory;

import java.util.ArrayList;
import java.util.Date;

public class DashboardForm {

    private boolean standardMode;
    private ArrayList<PaymentCategory> showCategoriesList;
    private Date dateStart;
    private Date dateEnd;

    public boolean isStandardMode() {
        return standardMode;
    }

    public void setStandardMode(boolean standardMode) {
        this.standardMode = standardMode;
    }

    public ArrayList<PaymentCategory> getShowCategoriesList() {
        return showCategoriesList;
    }

    public void setShowCategoriesList(ArrayList<PaymentCategory> showCategoriesList) {
        this.showCategoriesList = showCategoriesList;
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
}
