package sig.codebind.listners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.ArrayList;

import sig.codebind.SalesInvoiceGenerator;
import sig.codebind.model.FileOperations;
import sig.codebind.model.InvoiceHeader;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MenuBarListner implements ActionListener{

	private SalesInvoiceGenerator salesInvoiceGenerator;

	public MenuBarListner(SalesInvoiceGenerator salesInvoiceGenerator) {
		this.salesInvoiceGenerator = salesInvoiceGenerator;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == salesInvoiceGenerator.getSaveFile()) {
			salesInvoiceGenerator.toCsv();
		}

		else if (e.getSource() == salesInvoiceGenerator.getLoadFile()) {
			try {
				ArrayList<File> selectedFiles = openFileBrowser();
				FileOperations fileOperations = new FileOperations();
				File invoiceHeaderFile = selectedFiles.get(0);
				File invoiceLineFile = selectedFiles.get(1);
				ArrayList<InvoiceHeader> invoiceHeaders = fileOperations.readFile(invoiceHeaderFile, invoiceLineFile);
				salesInvoiceGenerator.updateView(invoiceHeaders);
			} catch(Exception ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage(),"Wrong Format", JOptionPane.ERROR_MESSAGE);
			}
		}

	}

	private ArrayList<File> openFileBrowser() throws Exception {
		ArrayList<File> files = new ArrayList<File>();
		JFileChooser openFileChooser = new JFileChooser();
     	openFileChooser.setFileFilter(new FileNameExtensionFilter("CSV files (*csv)", "csv"));
		openFileChooser.setDialogTitle("Select Invoice Header File");
		int invoiceHeaderSelection = openFileChooser.showOpenDialog(salesInvoiceGenerator);

		if (invoiceHeaderSelection == JFileChooser.APPROVE_OPTION) {
			files.add(openFileChooser.getSelectedFile());

			openFileChooser.setDialogTitle("Select Invoice Line File");
			int invoiceLineSelection = openFileChooser.showOpenDialog(salesInvoiceGenerator);
			if (invoiceLineSelection == JFileChooser.APPROVE_OPTION) {
				files.add(openFileChooser.getSelectedFile());
				return files;

			} else {
				throw new Exception("Could load selected file");
			}
		} else {
			throw new Exception("Could load selected file");
		}
	}
}
