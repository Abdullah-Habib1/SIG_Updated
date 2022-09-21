package sig.codebind;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import sig.codebind.dialogs.InvoiceItemDialog;
import sig.codebind.listners.LeftPanelBtnListner;
import sig.codebind.listners.MenuBarListner;
import sig.codebind.listners.RightPanelBtnListner;
import sig.codebind.model.FileOperations;
import sig.codebind.model.InvoiceHeader;

public class SalesInvoiceGenerator extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<String> invoices = new ArrayList<>();
	private ArrayList<InvoiceHeader> invoiceItems = new ArrayList<>();

	private JPanel contentPane;
	private JTable invoicesTable;
	private JMenuItem loadFile;
	private JMenuItem saveFile;
	private JLabel invoiceNumValueLabel;
	private String invoicesTableHeader[] = { "No.", "Date", "Customer", "Total" };
	private static Object invoicesTableData[][];
	private DefaultTableModel invoicesTableModel = new DefaultTableModel(invoicesTableData, invoicesTableHeader) {
		@Override
		public boolean isCellEditable(int row, int column) {
			return column != 3;
		}
	};

	private JTextField customerNametextField;

	private String InvoicesDetailsTableHeader[] = { "No.", "Item Name", "Item Price", "Count", "Item Total" };
	private String InvoicesDetailsTableData[][];
	private DefaultTableModel InvoicesDetailsTableModel = new DefaultTableModel(InvoicesDetailsTableData,
			InvoicesDetailsTableHeader) {
		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}
	};

	private JTable InvoicesDetailsTable;

	private JLabel invoiceTotalValueLabel;

	private JFormattedTextField invoiceDateTextField;

	private JButton createNewInvoiceBtn;

	private JButton deleteInvoiceBtn;

	private InvoiceItemDialog invoiceItemDialog;

	private MenuBarListner customListner = new MenuBarListner(this);

	private LeftPanelBtnListner leftPanelBtnListner = new LeftPanelBtnListner(this);

	private RightPanelBtnListner rightPanelBtnListner = new RightPanelBtnListner(this);

	private JButton saveChangeButton;

	private JButton cancelButton;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SalesInvoiceGenerator frame = new SalesInvoiceGenerator();
					frame.setSize(852, 420);
					frame.setResizable(true);
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public SalesInvoiceGenerator() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		readInvoiceItemFile();
		// test
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);

		loadFile = new JMenuItem("Load file");
		loadFile.addActionListener(customListner);
		fileMenu.add(loadFile);

		saveFile = new JMenuItem("Save file");
		saveFile.addActionListener(customListner);
		fileMenu.add(saveFile);
		contentPane = new JPanel();

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout());

		JSplitPane splitPane = new JSplitPane();

		contentPane.add(splitPane);

		JPanel leftPanel = new JPanel();
		splitPane.setLeftComponent(leftPanel);
		leftPanel.setLayout(new GridLayout(2, 1, 0, 0));

		JPanel tablePanel = new JPanel();
		leftPanel.add(tablePanel);
		tablePanel.setLayout(new BorderLayout(0, 0));

		invoicesTable = new JTable(invoicesTableModel);
		invoicesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		invoicesTable.setBorder(new LineBorder(new Color(0, 0, 0)));
		JScrollPane scrollPane = new JScrollPane(invoicesTable);
		tablePanel.add(scrollPane);
		invoicesTable.getSelectionModel().addListSelectionListener(leftPanelBtnListner);
		invoicesTable.addMouseListener(leftPanelBtnListner);
		JPanel leftBtnPanel = new JPanel();
		leftPanel.add(leftBtnPanel);
		leftBtnPanel.setLayout(new BoxLayout(leftBtnPanel, BoxLayout.LINE_AXIS));
		leftBtnPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		leftBtnPanel.add(Box.createHorizontalGlue());
		createNewInvoiceBtn = new JButton("Create New Invoice");
		createNewInvoiceBtn.addActionListener(leftPanelBtnListner);
		leftBtnPanel.add(createNewInvoiceBtn);
		leftBtnPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		deleteInvoiceBtn = new JButton("Delete Invoice");
		deleteInvoiceBtn.addActionListener(leftPanelBtnListner);

		leftBtnPanel.add(deleteInvoiceBtn);
		leftBtnPanel.add(Box.createHorizontalGlue());

		JPanel rightPanel = new JPanel();
		splitPane.setRightComponent(rightPanel);
		rightPanel.setLayout(new GridLayout(3, 1, 0, 0));

		JPanel panel_1 = new JPanel();
		rightPanel.add(panel_1);
		panel_1.setLayout(new GridLayout(0, 2, 0, 0));

		JLabel invoiceNumLabel = new JLabel("Invoice Number");
		panel_1.add(invoiceNumLabel);
		invoiceNumLabel.setHorizontalAlignment(SwingConstants.LEFT);
		invoiceNumLabel.setLabelFor(invoiceNumValueLabel);

		invoiceNumValueLabel = new JLabel("");
		panel_1.add(invoiceNumValueLabel);
		invoiceNumValueLabel.setHorizontalAlignment(SwingConstants.LEFT);

		JLabel invoiceDateLabel = new JLabel("Invoice Date");
		panel_1.add(invoiceDateLabel);
		invoiceDateLabel.setHorizontalAlignment(SwingConstants.LEFT);
		invoiceDateLabel.setLabelFor(invoiceDateTextField);

		invoiceDateTextField = new JFormattedTextField();
		panel_1.add(invoiceDateTextField);
		invoiceDateTextField.setHorizontalAlignment(SwingConstants.LEFT);

		JLabel customerNameLabel = new JLabel("Customer name");
		panel_1.add(customerNameLabel);
		customerNameLabel.setHorizontalAlignment(SwingConstants.LEFT);
		customerNameLabel.setLabelFor(customerNametextField);

		customerNametextField = new JTextField();
		panel_1.add(customerNametextField);
		customerNametextField.setHorizontalAlignment(SwingConstants.LEFT);
		customerNametextField.setColumns(10);

		JLabel invocieTotalLabel = new JLabel("Invoice Total");
		panel_1.add(invocieTotalLabel);
		invocieTotalLabel.setLabelFor(invoiceTotalValueLabel);

		invoiceTotalValueLabel = new JLabel("");
		panel_1.add(invoiceTotalValueLabel);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Invoice Items", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		rightPanel.add(panel);
		panel.setLayout(new BorderLayout(0, 0));

		InvoicesDetailsTable = new JTable(InvoicesDetailsTableModel);
		panel.add(new JScrollPane(InvoicesDetailsTable), BorderLayout.CENTER);

		JPanel rightBtnPanel = new JPanel();
		rightPanel.add(rightBtnPanel);
		rightBtnPanel.setLayout(new BoxLayout(rightBtnPanel, BoxLayout.LINE_AXIS));
		rightBtnPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		rightBtnPanel.add(Box.createHorizontalGlue());
		JButton createInvoiceItemButton = new JButton("Create Invoice Item");
		createInvoiceItemButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		createInvoiceItemButton.setActionCommand("createNewInvoiceItem");
		createInvoiceItemButton.addActionListener(rightPanelBtnListner);

		rightBtnPanel.add(createInvoiceItemButton);
		rightBtnPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		saveChangeButton = new JButton("Save");
		saveChangeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		rightBtnPanel.add(saveChangeButton);
		rightBtnPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		saveChangeButton.setActionCommand("saveChange");
		saveChangeButton.addActionListener(rightPanelBtnListner);
		cancelButton = new JButton("Cancel");
		cancelButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		rightBtnPanel.add(cancelButton);
		rightBtnPanel.add(Box.createHorizontalGlue());
		cancelButton.setActionCommand("cancel");
		cancelButton.addActionListener(rightPanelBtnListner);
	}

	public void initState() {

	}

	public void readInvoiceItemFile() {
		FileOperations fileOperations = new FileOperations();
		try {
			invoiceItems = fileOperations.readFile(new File("src/resources/InvoiceHeader.csv"),
					new File("src/resources/InvoiceLine.csv"));
		} catch (Exception e) {
			System.out.println("Couldn't read file");
		}
	}

	public void toCsv() {
		try {
			FileOperations fileOperations = new FileOperations();
			fileOperations.writeFile(invoiceItems);
		} catch (Exception e) {
			System.out.println("Couldn't write to file");
		}
	}

	public void toCsvFromList(List<String> data, File file) {
		try {

			data.forEach(System.out::println);

			List<String> neededData = data.stream().map(s -> s.substring(0, s.lastIndexOf(",")))
					.collect(Collectors.toList());

			FileWriter Csv = new FileWriter(file);
			for (String string : data) {
				Csv.write(string);
				Csv.write("\n");
			}

			Csv.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}

//	public List<String> fromCsv(File file) {
//		BufferedReader fileReader = null;
//		invoicesTableModel.setRowCount(0);
//
//		try {
//			// Create the file reader
//			fileReader = new BufferedReader(new FileReader(file));
//
//			List<String> invoice = new ArrayList<>();
//
//			List<String> invoiceItems = readInvoiceItemFile();
//
//			int number = 0;
//			int date = 1;
//			int customer = 2;
//
//			int itemPrice = 2;
//			int count = 3;
//
//			String line = "";
//			// Read the file line by line starting from the second line
//			while ((line = fileReader.readLine()) != null) {
//
//				invoice.add(line);
//
//				// Get all tokens available in line
//				String[] tokens = line.split(",");
//
//				Long invoiceTotal = invoiceItems.stream().filter(it -> it.startsWith(String.valueOf(tokens[number])))
//						.map(t -> t.split(",")).map(m -> Long.valueOf(m[itemPrice]) * Long.valueOf(m[count]))
//						.map(n -> Long.valueOf(n)).collect(Collectors.summingLong(Long::longValue));
//
//				invoicesTableModel.insertRow(invoicesTableModel.getRowCount(),
//						new Object[] { Long.parseLong(tokens[number]), tokens[date], tokens[customer], invoiceTotal });
//
//			}
//
//			fileReader.close();
//
//			return invoice;
//
//		} catch (Exception e) {
//			System.out.println("Error in CsvFileReader !!!");
//			e.printStackTrace();
//		}
//		return null;
//
//	}

//	public void invoiceItems(Long invoiceNum) {
//		System.out.println(invoiceNum);
//		if (null != invoiceNum) {
//			BufferedReader fileReader = null;
//			invoicesTableModel.setRowCount(0);
//
//			try {
//				fileReader = new BufferedReader(new FileReader(new File("src/resources/InvoiceLine.csv")));
//
//				List<String> invoiceItems = new ArrayList<>();
//
//				String line = "";
//				// Read the file line by line starting from the second line
//				while ((line = fileReader.readLine()) != null) {
//					if (line.startsWith(String.valueOf(invoiceNum))) {
//						invoiceItems.add(line);
//					}
//				}
//
//				fileReader.close();
//
//			} catch (Exception e) {
//				System.out.println("Error in CsvFileReader !!!");
//				e.printStackTrace();
//			}
//		}
//	}

	public void updateView(ArrayList<InvoiceHeader> invoiceHeaders) {
		invoiceItems = invoiceHeaders;
		invoicesTableModel.setRowCount(0);
		for (int index = 0; index < invoiceHeaders.size(); index++) {
			InvoiceHeader invoiceHeader = invoiceHeaders.get(index);
			Object[] model = new Object[] {
				invoiceHeader.invoiceNum, invoiceHeader.invoiceDate, invoiceHeader.customerName, invoiceHeader.getTotal()
			};
			invoicesTableModel.insertRow(index, model);
		}
	}

	public void updateInvoiceItemsModel(ArrayList<InvoiceHeader> invoices) {
		invoiceItems = invoices;
	}

	public List<String> getInvoices() {
		return invoices;
	}

	public JPanel getContentPane() {
		return contentPane;
	}

	public JTable getInvoicesTable() {
		return invoicesTable;
	}

	public JMenuItem getLoadFile() {
		return loadFile;
	}

	public JMenuItem getSaveFile() {
		return saveFile;
	}

	public JLabel getInvoiceNumValueLabel() {
		return invoiceNumValueLabel;
	}

	public String[] getInvoicesTableHeader() {
		return invoicesTableHeader;
	}

	public static Object[][] getInvoicesTableData() {
		return invoicesTableData;
	}

	public DefaultTableModel getInvoicesTableModel() {
		return invoicesTableModel;
	}

	public JTextField getCustomerNametextField() {
		return customerNametextField;
	}

	public String[] getInvoicesDetailsTableHeader() {
		return InvoicesDetailsTableHeader;
	}

	public String[][] getInvoicesDetailsTableData() {
		return InvoicesDetailsTableData;
	}

	public DefaultTableModel getInvoicesDetailsTableModel() {
		return InvoicesDetailsTableModel;
	}

	public JTable getInvoicesDetailsTable() {
		return InvoicesDetailsTable;
	}

	public JLabel getInvoiceTotalValueLabel() {
		return invoiceTotalValueLabel;
	}

	public JFormattedTextField getInvoiceDateTextField() {
		return invoiceDateTextField;
	}

	public MenuBarListner getCustomListner() {
		return customListner;
	}

	public LeftPanelBtnListner getAddNewInvoiceListner() {
		return leftPanelBtnListner;
	}

	public JButton getCreateNewInvoiceBtn() {
		return createNewInvoiceBtn;
	}

	public JButton getDeleteInvoiceBtn() {
		return deleteInvoiceBtn;
	}

	public InvoiceItemDialog getInvoiceItemDialog() {
		return invoiceItemDialog;
	}

	public void setInvoiceItemDialog(InvoiceItemDialog invoiceItemDialog) {
		this.invoiceItemDialog = invoiceItemDialog;
	}

	public RightPanelBtnListner getRightPanelBtnListner() {
		return rightPanelBtnListner;
	}

	public JButton getSaveChangeButton() {
		return saveChangeButton;
	}

	public JButton getCancelButton() {
		return cancelButton;
	}

	public ArrayList<InvoiceHeader> getInvoiceItems() {
		return invoiceItems;
	}
}
