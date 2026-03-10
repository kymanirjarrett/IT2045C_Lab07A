import javax.swing.SwingUtilities;

/**
 * Launches the Invoice GUI.
 */
public class InvoiceRunner {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(InvoiceFrame::new);
    }
}