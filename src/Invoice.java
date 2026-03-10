import java.util.ArrayList;

/**
 * Represents an invoice containing customer info and line items.
 */
public class Invoice {
    private String title;
    private Customer customer;
    private ArrayList<LineItem> lineItems;

    public Invoice(String title, Customer customer) {
        this.title = title;
        this.customer = customer;
        this.lineItems = new ArrayList<>();
    }

    public void addLineItem(LineItem item) {
        lineItems.add(item);
    }

    public double getTotalAmountDue() {
        double total = 0.0;
        for (LineItem item : lineItems) {
            total += item.getLineTotal();
        }
        return total;
    }

    public String generateInvoiceText() {
        StringBuilder sb = new StringBuilder();

        sb.append("============================================\n");
        sb.append(title).append("\n");
        sb.append("============================================\n\n");

        sb.append("Customer Address:\n");
        sb.append(customer.getFormattedAddress()).append("\n\n");

        sb.append("--------------------------------------------\n");
        sb.append(String.format("%-20s %-10s %-10s %-10s%n",
                "Product", "Unit Price", "Qty", "Total"));
        sb.append("--------------------------------------------\n");

        for (LineItem item : lineItems) {
            sb.append(String.format("%-20s $%-9.2f %-10d $%-9.2f%n",
                    item.getProduct().getName(),
                    item.getProduct().getUnitPrice(),
                    item.getQuantity(),
                    item.getLineTotal()));
        }

        sb.append("--------------------------------------------\n");
        sb.append(String.format("Total Amount Due: $%.2f%n", getTotalAmountDue()));
        sb.append("============================================\n");

        return sb.toString();
    }
}