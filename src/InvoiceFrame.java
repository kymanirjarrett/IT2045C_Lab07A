import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.util.ArrayList;

/**
 * Swing GUI for Invoice.
 */
public class InvoiceFrame extends JFrame {

    private static final String INT_PATTERN = "^[0-9]*$";
    private static final String DOUBLE_PATTERN = "^[0-9]*\\.?[0-9]{0,2}$";

    private final JTextField titleField = new JTextField(20);
    private final JTextField customerNameField = new JTextField(20);
    private final JTextField streetField = new JTextField(20);
    private final JTextField cityField = new JTextField(15);
    private final JTextField stateField = new JTextField(5);
    private final JTextField zipField = new JTextField(10);

    private final JTextField productNameField = new JTextField(15);
    private final JTextField unitPriceField = new JTextField(10);
    private final JTextField quantityField = new JTextField(10);

    private final JTextArea receiptArea = new JTextArea(18, 50);

    private final ArrayList<LineItem> pendingItems = new ArrayList<>();

    public InvoiceFrame() {
        setTitle("Lab 07A Invoice");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        add(buildInputPanel(), BorderLayout.NORTH);
        add(buildReceiptPanel(), BorderLayout.CENTER);
        add(buildButtonPanel(), BorderLayout.SOUTH);

        applyRegexFilter(quantityField, INT_PATTERN);
        applyRegexFilter(unitPriceField, DOUBLE_PATTERN);

        receiptArea.setEditable(false);
        receiptArea.setFont(new Font("Monospaced", Font.PLAIN, 13));

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel buildInputPanel() {
        JPanel mainPanel = new JPanel(new GridLayout(2, 1, 10, 10));

        JPanel customerPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        customerPanel.setBorder(new TitledBorder("Invoice / Customer Info"));

        customerPanel.add(new JLabel("Invoice Title:"));
        customerPanel.add(titleField);
        customerPanel.add(new JLabel("Customer Name:"));
        customerPanel.add(customerNameField);
        customerPanel.add(new JLabel("Street:"));
        customerPanel.add(streetField);
        customerPanel.add(new JLabel("City:"));
        customerPanel.add(cityField);
        customerPanel.add(new JLabel("State:"));
        customerPanel.add(stateField);
        customerPanel.add(new JLabel("ZIP:"));
        customerPanel.add(zipField);

        JPanel itemPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        itemPanel.setBorder(new TitledBorder("Add Line Item"));

        itemPanel.add(new JLabel("Product Name:"));
        itemPanel.add(productNameField);
        itemPanel.add(new JLabel("Unit Price:"));
        itemPanel.add(unitPriceField);
        itemPanel.add(new JLabel("Quantity:"));
        itemPanel.add(quantityField);

        mainPanel.add(customerPanel);
        mainPanel.add(itemPanel);

        return mainPanel;
    }

    private JPanel buildReceiptPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new TitledBorder("Invoice Display"));

        JScrollPane scrollPane = new JScrollPane(receiptArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel buildButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());

        JButton addItemButton = new JButton("Add Line Item");
        JButton generateButton = new JButton("Generate Invoice");
        JButton clearButton = new JButton("Clear");
        JButton quitButton = new JButton("Quit");

        addItemButton.addActionListener(e -> addLineItem());
        generateButton.addActionListener(e -> generateInvoice());
        clearButton.addActionListener(e -> clearForm());
        quitButton.addActionListener(e -> System.exit(0));

        panel.add(addItemButton);
        panel.add(generateButton);
        panel.add(clearButton);
        panel.add(quitButton);

        return panel;
    }

    private void addLineItem() {
        String productName = productNameField.getText().trim();
        String unitPriceText = unitPriceField.getText().trim();
        String quantityText = quantityField.getText().trim();

        if (productName.isEmpty() || unitPriceText.isEmpty() || quantityText.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please complete all line item fields.",
                    "Missing Data",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        double unitPrice = Double.parseDouble(unitPriceText);
        int quantity = Integer.parseInt(quantityText);

        Product product = new Product(productName, unitPrice);
        LineItem item = new LineItem(product, quantity);
        pendingItems.add(item);

        JOptionPane.showMessageDialog(this,
                "Line item added successfully.");

        productNameField.setText("");
        unitPriceField.setText("");
        quantityField.setText("");
    }

    private void generateInvoice() {
        String title = titleField.getText().trim();
        String customerName = customerNameField.getText().trim();
        String street = streetField.getText().trim();
        String city = cityField.getText().trim();
        String state = stateField.getText().trim();
        String zip = zipField.getText().trim();

        if (title.isEmpty() || customerName.isEmpty() || street.isEmpty()
                || city.isEmpty() || state.isEmpty() || zip.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please complete all customer and invoice fields.",
                    "Missing Data",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (pendingItems.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please add at least one line item.",
                    "No Line Items",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Customer customer = new Customer(customerName, street, city, state, zip);
        Invoice invoice = new Invoice(title, customer);

        for (LineItem item : pendingItems) {
            invoice.addLineItem(item);
        }

        receiptArea.setText(invoice.generateInvoiceText());
    }

    private void clearForm() {
        titleField.setText("");
        customerNameField.setText("");
        streetField.setText("");
        cityField.setText("");
        stateField.setText("");
        zipField.setText("");

        productNameField.setText("");
        unitPriceField.setText("");
        quantityField.setText("");

        receiptArea.setText("");
        pendingItems.clear();
    }

    private void applyRegexFilter(JTextField field, String regex) {
        ((AbstractDocument) field.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {

                String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                String nextText = currentText.substring(0, offset) + text + currentText.substring(offset + length);

                if (nextText.isEmpty() || nextText.matches(regex)) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });
    }
}