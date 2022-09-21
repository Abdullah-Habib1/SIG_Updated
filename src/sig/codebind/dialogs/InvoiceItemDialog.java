package sig.codebind.dialogs;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import sig.codebind.SalesInvoiceGenerator;

public class InvoiceItemDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5459085903298396288L;
	private JTextField invoiceNoField;
	private JTextField itemNameField;
	private JTextField itemCountField;
	private JTextField itemPriceField;
	private JLabel invoiceNoLbl;
	private JLabel itemNameLbl;
	private JLabel itemCountLbl;
	private JLabel itemPriceLbl;
	private JButton okBtn;
	private JButton cancelBtn;

	public InvoiceItemDialog(SalesInvoiceGenerator salesInvoiceGenerator) {
		invoiceNoField = new JTextField(20);
		invoiceNoLbl = new JLabel("No.");
		
		itemNameField = new JTextField(20);
		itemNameLbl = new JLabel("Item Name");

		itemCountField = new JTextField(20);
		itemCountLbl = new JLabel("Item Count");

		itemPriceField = new JTextField(20);
		itemPriceLbl = new JLabel("Item Price");

		okBtn = new JButton("OK");
		cancelBtn = new JButton("Cancel");

		okBtn.setActionCommand("createItemOK");
		cancelBtn.setActionCommand("createItemCancel");

		okBtn.addActionListener(salesInvoiceGenerator.getRightPanelBtnListner());
		cancelBtn.addActionListener(salesInvoiceGenerator.getRightPanelBtnListner());
		setLayout(new GridLayout(5, 2));

		add(invoiceNoLbl);
		add(invoiceNoField);
		add(itemNameLbl);
		add(itemNameField);
		add(itemCountLbl);
		add(itemCountField);
		add(itemPriceLbl);
		add(itemPriceField);
		add(okBtn);
		add(cancelBtn);

		pack();
	}

	public JTextField getItemNameField() {
		return itemNameField;
	}

	public JTextField getItemCountField() {
		return itemCountField;
	}

	public JTextField getItemPriceField() {
		return itemPriceField;
	}

	public JTextField getInvoiceNoField() {
		return invoiceNoField;
	}
}
