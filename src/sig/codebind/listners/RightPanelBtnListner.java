package sig.codebind.listners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import sig.codebind.SalesInvoiceGenerator;
import sig.codebind.dialogs.InvoiceItemDialog;
import sig.codebind.model.InvoiceHeader;
import sig.codebind.model.InvoiceLine;

import javax.swing.*;

public class RightPanelBtnListner implements ActionListener{

	private SalesInvoiceGenerator salesInvoiceGenerator;
	
	public RightPanelBtnListner(SalesInvoiceGenerator salesInvoiceGenerator) {
		this.salesInvoiceGenerator = salesInvoiceGenerator;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "createNewInvoiceItem":
			displayNewItemDialog();
			break;
		case "createItemOK":
			  createItemOK();
			break;
		case "createItemCancel":
			createItemCancel();
			break;
		case "saveChange":
			saveChange();
			break;
		case "cancel":
			cancel();
			break;
		}
	}
	
	private void cancel() {
		salesInvoiceGenerator.dispose();
	}

	private void saveChange() {
		salesInvoiceGenerator.toCsv();
	}

	private void createItemOK() {
		String invoiceNum = salesInvoiceGenerator.getInvoiceItemDialog().getInvoiceNoField().getText();
		String itemName = salesInvoiceGenerator.getInvoiceItemDialog().getItemNameField().getText();
		String itemPriceStr = salesInvoiceGenerator.getInvoiceItemDialog().getItemPriceField().getText();
		String itemCountStr = salesInvoiceGenerator.getInvoiceItemDialog().getItemCountField().getText();
		salesInvoiceGenerator.getInvoiceItemDialog().setVisible(false);
		salesInvoiceGenerator.getInvoiceItemDialog().dispose();
		salesInvoiceGenerator.setInvoiceItemDialog(null);
		double itemPrice = Double.parseDouble(itemPriceStr);
		int itemCount = Integer.parseInt(itemCountStr);
		long total = (long) (itemPrice * itemCount);

		InvoiceLine invoiceLine = new InvoiceLine(Integer.parseInt(invoiceNum), itemName, itemPrice, itemCount);

		ArrayList<InvoiceHeader> filteredHeaders = (ArrayList<InvoiceHeader>) salesInvoiceGenerator.getInvoiceItems()
				.stream()
				.filter(it -> it.invoiceNum == invoiceLine.invoiceNum)
				.collect(Collectors.toList());
		if (filteredHeaders.size() > 0) {
			InvoiceHeader targetHeader = filteredHeaders.get(0);
			targetHeader.invoiceLines.add(invoiceLine);

			salesInvoiceGenerator.getInvoiceItems().add(targetHeader);
			salesInvoiceGenerator.updateInvoiceItemsModel(salesInvoiceGenerator.getInvoiceItems());
			if (invoiceNum.equals(salesInvoiceGenerator.getInvoiceNumValueLabel().getText())) {
				int rowCount = salesInvoiceGenerator.getInvoicesDetailsTableModel().getRowCount();
				salesInvoiceGenerator.getInvoicesDetailsTableModel().insertRow(rowCount,
						new Object[] {invoiceNum,itemName,itemPrice,itemCount,total});
			}

			salesInvoiceGenerator.getInvoicesDetailsTableModel().fireTableDataChanged();

			int totalAmount = targetHeader.getTotal();
			salesInvoiceGenerator.getInvoiceTotalValueLabel().setText(String.valueOf(totalAmount));

			int selectedRow = salesInvoiceGenerator.getInvoicesTable().getSelectedRow();

			salesInvoiceGenerator.getInvoicesTableModel().setValueAt(totalAmount, selectedRow, 3);

		}
	}

	private void createItemCancel() {
		salesInvoiceGenerator.getInvoiceItemDialog().setVisible(false);
		salesInvoiceGenerator.getInvoiceItemDialog().dispose();
		salesInvoiceGenerator.setInvoiceItemDialog(null);
	}

	private void displayNewItemDialog() {
		salesInvoiceGenerator.setInvoiceItemDialog(new InvoiceItemDialog(salesInvoiceGenerator));
		salesInvoiceGenerator.getInvoiceItemDialog().setVisible(true);
	}
}