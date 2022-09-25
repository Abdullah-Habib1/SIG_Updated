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
import javax.swing.table.DefaultTableModel;

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
			case "deleteSelectedRows":
				deleteSelectedRows();
				break;
			case "cancel":
				cancel();
				break;
		}
	}

	private void cancel() {
		salesInvoiceGenerator.dispose();
	}

	private void deleteSelectedRows() {
		System.out.println(salesInvoiceGenerator.getInvoicesDetailsTable().getSelectedRow());
		DefaultTableModel df1 = (DefaultTableModel) salesInvoiceGenerator.getInvoicesDetailsTable().getModel();
		int rs[] = salesInvoiceGenerator.getInvoicesDetailsTable().getSelectedRows();
		for (int i = rs.length-1; i >=0 ; i--) {
			df1.removeRow(rs[i]);
		}
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

		int selectedRow = salesInvoiceGenerator.getInvoicesTable().getSelectedRow();

		if (selectedRow == -1){
			JOptionPane.showMessageDialog(salesInvoiceGenerator, "Please load and select invoice!",
					"Error", JOptionPane.ERROR_MESSAGE);
		}else {
			salesInvoiceGenerator.setInvoiceItemDialog(new InvoiceItemDialog(salesInvoiceGenerator));
			salesInvoiceGenerator.getInvoiceItemDialog().setVisible(true);
		}
	}
}
