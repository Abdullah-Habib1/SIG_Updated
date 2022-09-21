package sig.codebind.model;

public class InvoiceLine {
    public int invoiceNum;
    public String itemName;
    public double itemPrice;
    public int itemsCount;

    public InvoiceLine(int invoiceNum, String itemName, double itemPrice, int itemsCount) {
        this.invoiceNum = invoiceNum;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemsCount = itemsCount;
    }

    public double getTotalForItem() {
        return (itemPrice * itemsCount);
    }
}
