package sig.codebind.listners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Vector;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;

import sig.codebind.SalesInvoiceGenerator;
import sig.codebind.model.InvoiceHeader;
import sig.codebind.model.InvoiceLine;

public class LeftPanelBtnListner implements ActionListener, ListSelectionListener,MouseListener{

	private SalesInvoiceGenerator salesInvoiceGenerator;

	public LeftPanelBtnListner(SalesInvoiceGenerator salesInvoiceGenerator) {
		this.salesInvoiceGenerator = salesInvoiceGenerator;
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		
		if(!e.getValueIsAdjusting()) {
		Action action = (Action) new AbstractAction()
		{
		    public void actionPerformed(ActionEvent e)
		    {
		        TableCellListener tcl = (TableCellListener)e.getSource();
		        System.out.println("Row   : " + tcl.getRow());
		        System.out.println("Column: " + tcl.getColumn());
		        System.out.println("Old   : " + tcl.getOldValue());
		        System.out.println("New   : " + tcl.getNewValue());
		        
		        viewDetails(tcl.getRow());
		    }
		};
		
		TableCellListener tcl = new TableCellListener(salesInvoiceGenerator.getInvoicesTable(), action);
		
	    
		}
		
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == salesInvoiceGenerator.getCreateNewInvoiceBtn()) {
			salesInvoiceGenerator.getInvoicesTableModel().insertRow(salesInvoiceGenerator.getInvoicesTableModel().getRowCount(), new Object[] {});
		}else if(e.getSource() == salesInvoiceGenerator.getDeleteInvoiceBtn()) {
			int numRows = salesInvoiceGenerator.getInvoicesTable().getSelectedRow();
			
			TableModel model = salesInvoiceGenerator.getInvoicesTable().getModel();
			
			Object numType = model.getValueAt(numRows, 0);
			
			salesInvoiceGenerator.getInvoicesTableModel().removeRow(numRows);
			
			Integer numValue = null;

			if (numType instanceof Integer) {
				numValue = (Integer) numType;
			} else if (numType instanceof String) {
				numValue = Integer.valueOf((String) numType);
			}

			Integer invoiceNum = numValue;
			
//			Predicate<String> matchInvoiceNum = i -> i.startsWith(String.valueOf(invoiceNum));
			
			salesInvoiceGenerator.getInvoiceItems().removeIf(x -> x.invoiceNum == invoiceNum);
			salesInvoiceGenerator.updateInvoiceItemsModel(salesInvoiceGenerator.getInvoiceItems());
		}
	}
	
	private void viewDetails(Integer row) {

		ArrayList<InvoiceHeader> invoiceHeaders = salesInvoiceGenerator.getInvoiceItems();
		InvoiceHeader invoiceHeader = invoiceHeaders.get(salesInvoiceGenerator.getInvoicesTable().getSelectedRow());
		resetOnNewSelection();

		salesInvoiceGenerator.getInvoiceTotalValueLabel().setText(String.valueOf(invoiceHeader.getTotal()));
		salesInvoiceGenerator.getInvoiceNumValueLabel().setText(String.valueOf(invoiceHeader.invoiceNum));
		salesInvoiceGenerator.getInvoiceDateTextField().setText(invoiceHeader.invoiceDate);
		salesInvoiceGenerator.getCustomerNametextField().setText(invoiceHeader.customerName);

		salesInvoiceGenerator.getInvoicesDetailsTableModel().setRowCount(0);
		for (int index = 0; index < invoiceHeader.invoiceLines.size(); index++) {
			InvoiceLine invoiceLine = invoiceHeader.invoiceLines.get(index);
				Object [] model = new Object[] {
						invoiceLine.invoiceNum, invoiceLine.itemName, invoiceLine.itemPrice, invoiceLine.itemsCount, invoiceLine.getTotalForItem()
				};
			salesInvoiceGenerator.getInvoicesDetailsTableModel().insertRow(index, model);
		}
	}

	private void resetOnNewSelection() {
		salesInvoiceGenerator.getInvoiceNumValueLabel().setText("");
		salesInvoiceGenerator.getInvoiceDateTextField().setText("");
		salesInvoiceGenerator.getInvoiceTotalValueLabel().setText("");
		salesInvoiceGenerator.getCustomerNametextField().setText("");
	}
	
	private boolean isEmptyOrNull(String value) {
		return ("null").equals(value.trim()) || value == null || ("").equals(value.trim());
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		viewDetails(null);	
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
