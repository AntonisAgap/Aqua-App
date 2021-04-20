package main;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class NewOrderFrame extends JFrame{
    private JScrollPane orderScrollPane;
    private JTable orderTable;
    private static DefaultTableModel orderModel;
    private String[] orderColumns;
    private String[][] orderData;

    private JButton addProductBtn;
    private JButton completeOrderBtn;

    private JLabel clientLbl;
    private JLabel agentLbl;
    private JLabel notesLbl;
    private JLabel productLbl;

    private JTextField clientTf;
    private JTextField agentTf;

    private JTextArea notesTa;

    private JComboBox<Client> clientJComboBox;
    private JComboBox<Agent> agentJComboBox;
    private JComboBox<Product> productJComboBox;
    private Border editOrderPanelBorder;

    private JPopupMenu popupMenu;
    private JMenuItem deleteItem;

    public NewOrderFrame(JComboBox<Client> clientJComboBox, JComboBox<Agent> agentJComboBox,JComboBox<Product> productJComboBox){
        this.clientJComboBox = clientJComboBox;
        this.agentJComboBox = agentJComboBox;
        this.productJComboBox = productJComboBox;

        popupMenu = new JPopupMenu();
        deleteItem = new JMenuItem("Delete");
        popupMenu.add(deleteItem);

        editOrderPanelBorder = BorderFactory.createLoweredBevelBorder();

//        idLbl = new JLabel("Order ID");
        clientLbl = new JLabel("Client");
        agentLbl = new JLabel("Agent");
        productLbl = new JLabel("Choose products");
//        dateLbl = new JLabel("Date");
        notesLbl = new JLabel("Notes");

//        idTf = new JTextField(order.getId());
        clientTf = new JTextField();
        agentTf = new JTextField();
//        dateTf = new JTextField(order.getDate());

        notesTa = new JTextArea(10,10);

        addProductBtn = new JButton("Add product");
        completeOrderBtn = new JButton("Complete Order");

        orderTable = new JTable();
        orderData = new String[][] {};
        orderColumns = new String[] {"Product","Amount"};
        orderModel = new DefaultTableModel(orderData,orderColumns){
            // only edit amount cell
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 1)
                    return super.isCellEditable(row, column);
                else
                    return false;
            }

            // only allow integer values to be entered
            @Override
            public void setValueAt(Object aValue, int row, int column) {
                if (column == 1)
                {
                    System.out.println(aValue);
                    try{
                        int i = Integer.parseInt((String) aValue);
                        super.setValueAt(aValue,row,column);
                    }catch (Exception isNotInt){
                        System.out.println(isNotInt);
                    }
                }else
                    super.setValueAt(aValue, row, column);
            }
        };

        orderTable.setModel(orderModel);
        orderTable.setComponentPopupMenu(popupMenu);
    }

    public void prepareUI(){
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.X_AXIS));
        JPanel mainestPanel = new JPanel();
        mainestPanel.setLayout(new BoxLayout(mainestPanel,BoxLayout.Y_AXIS));


        JPanel editOrderPanel = new JPanel();
        editOrderPanel.setLayout(new GridLayout(3,2));
        editOrderPanel.setBorder(editOrderPanelBorder);

        editOrderPanel.add(clientLbl);
        editOrderPanel.add(clientJComboBox);
        editOrderPanel.add(agentLbl);
        editOrderPanel.add(agentJComboBox);
        editOrderPanel.add(productLbl);
        editOrderPanel.add(productJComboBox);

        orderScrollPane = new JScrollPane(orderTable);
        mainPanel.add(orderScrollPane);
        mainPanel.add(addProductBtn);


        mainestPanel.add(mainPanel);
        mainestPanel.add(completeOrderBtn);

        this.add(mainestPanel,BorderLayout.CENTER);
        this.add(editOrderPanel,BorderLayout.NORTH);

        addProductBtn.addActionListener(e -> {
            String productName = String.valueOf(productJComboBox.getSelectedItem());
            System.out.println("Product is:"+productName);
//            if (node != null) {
                if (!productName.isEmpty()) {
                    List<String> row = new ArrayList<String>();
                    row.add(productName);
                    row.add("0");
                    orderModel.addRow(row.toArray());
                }
            //}
            });

        completeOrderBtn.addActionListener(e -> {
            ArrayList<String> orderProduct = new ArrayList<>();
            ArrayList<String> orderCount = new ArrayList<>();
            String clientName = Objects.requireNonNull(clientJComboBox.getSelectedItem()).toString();
            String sellerName = Objects.requireNonNull(agentJComboBox.getSelectedItem()).toString();
            for (int count = 0;count < orderModel.getRowCount();count++){
                orderProduct.add(orderModel.getValueAt(count,0).toString());
                orderCount.add(orderModel.getValueAt(count,1).toString());
            }
            MainFrame.addOrderToDb(clientName,0,"",sellerName,orderProduct,orderCount);
            dispose();
        });

        deleteItem.addActionListener(e -> {
            int row = orderTable.getSelectedRow();
            int modelRow = orderTable.convertRowIndexToModel(row);
            orderModel.removeRow(modelRow);
        });

        popupMenu.addPopupMenuListener(new PopupMenuListener() {

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                SwingUtilities.invokeLater(() -> {
                    int rowAtPoint = orderTable.rowAtPoint(SwingUtilities.convertPoint(popupMenu, new Point(0, 0), orderTable));
                    if (rowAtPoint > -1) {
                        orderTable.setRowSelectionInterval(rowAtPoint, rowAtPoint);
                    }
                });
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                // TODO Auto-generated method stub
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
                // TODO Auto-generated method stub
            }
        });
    }

}

