package sig.codebind.model;

import java.util.ArrayList;
import java.util.Date;

public class InvoiceHeader {
    public int invoiceNum;
    public String customerName;
    public String invoiceDate;
    public ArrayList<InvoiceLine> invoiceLines;

    public int getTotal() {
        int result = 0;
        for (InvoiceLine line : invoiceLines) {
            result += (line.itemPrice * line.itemsCount);
        }
        return result;
    }
}
