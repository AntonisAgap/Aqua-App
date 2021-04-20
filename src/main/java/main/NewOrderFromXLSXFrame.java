package main;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
public class NewOrderFromXLSXFrame extends JFrame{

    //declare member variables
    private JLabel addCategoryLbl;

    private JLabel agentLbl;
    private JLabel clientLbl;
    private JLabel filePathLbl;

    private JComboBox<Agent> agentJComboBox;
    private JComboBox<Client> clientJComboBox;
    private JTextField filePathTf;

    private JButton completeOrderBtn;

    private Font font;

    private EmptyBorder addOrderLblBorder;
    private Border addOrderPanelBorder;
    private static String fileName = "";
    private static ArrayList<String> orderProduct = new ArrayList<>();
    private static ArrayList<String> orderCount = new ArrayList<>();

    public NewOrderFromXLSXFrame (JComboBox<Agent> agentJComboBox,JComboBox<Client> clientJComboBox,String filePath) {
        super();
        this.agentJComboBox = agentJComboBox;
        this.clientJComboBox = clientJComboBox;
        fileName = filePath;

        font = new Font("Arial", Font.BOLD,15);

        addOrderLblBorder = new EmptyBorder(3, 0, 10, 0);
        addOrderPanelBorder = BorderFactory.createLoweredBevelBorder();

        addCategoryLbl = new JLabel("Add new order\n");
        addCategoryLbl.setBorder(addOrderLblBorder);
        addCategoryLbl.setFont(font);

        agentLbl = new JLabel("Agent");
        clientLbl = new JLabel("Client");
        filePathLbl = new JLabel("Filepath");

        filePathTf = new JTextField();
        filePathTf.getText();
        filePathTf.setText(filePath);
        filePathTf.setEditable(false);

        completeOrderBtn = new JButton("Add order");

    }

    public void prepareUI() {

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));

        JPanel addProductPanel = new JPanel();

        addProductPanel.setLayout(new GridLayout(3,2));
        addProductPanel.setBorder(addOrderPanelBorder);

        addProductPanel.add(agentLbl);
        addProductPanel.add(agentJComboBox);
        addProductPanel.add(clientLbl);
        addProductPanel.add(clientJComboBox);
        addProductPanel.add(filePathLbl);
        addProductPanel.add(filePathTf);

        addCategoryLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        completeOrderBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel.add(addCategoryLbl);
        mainPanel.add(addProductPanel);
        mainPanel.add(completeOrderBtn);

        this.add(mainPanel);

        completeOrderBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    _loadData();
                } catch (IOException ioException) {
                }
                String clientName = Objects.requireNonNull(clientJComboBox.getSelectedItem()).toString();
                String sellerName = Objects.requireNonNull(agentJComboBox.getSelectedItem()).toString();
                MainFrame.addOrderToDb(clientName,0,"",sellerName,orderProduct,orderCount);
                dispose();
            }
        });

    }


    private static void _loadData() throws IOException {
        File excelFile = new File(fileName);
        FileInputStream fis = new FileInputStream(excelFile);
        // we create an XSSF Workbook object for our XLSX Excel File
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        // we get first sheet
        XSSFSheet sheet = workbook.getSheet("order");
        Iterator<Row> rowIt = sheet.iterator();

        Row initRow = sheet.getRow(1);
        Iterator<Cell> initcellIterator = initRow.cellIterator();
        int quantityCell = 0;
        while (initcellIterator.hasNext()) {
            Cell cell = initcellIterator.next();
            if (cell.toString().contains("ORDER Q'TY")) {
              quantityCell = cell.getColumnIndex();
                break;
            }
        }

        while (rowIt.hasNext()) {
            Row row = rowIt.next();
            try {
                String productID = row.getCell(0).getStringCellValue();
                String firstLetter = String.valueOf(productID.charAt(0));
                if (firstLetter.equals("0")) {
                    System.out.println(row.getCell(1).getStringCellValue()+" : "+
                            row.getCell(quantityCell).getNumericCellValue());
                    if (row.getCell(quantityCell).getNumericCellValue()>0) {
                        orderProduct.add(row.getCell(1).getStringCellValue());
                        orderCount.add(String.valueOf((int) row.getCell(quantityCell).getNumericCellValue()));
                    }
                }
            } catch (Exception e) {
            }
            workbook.close();
            fis.close();
        }
    }
}
