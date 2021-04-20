package main;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class OrderFrame extends JFrame{
    private JScrollPane orderScrollPane;
    private JTable orderTable;
    private Order order;
    private static DefaultTableModel orderModel;
    private String[] orderColumns;
    private String[][] orderData;

    private JButton deleteBtn;
    private JButton applyChangesBtn;
    private JButton moveToDoneOrdersBtn;
    private JButton moveToPendingOrdersBtn;
    private JButton printBtn;

    private JLabel idLbl;
    private JLabel clientLbl;
    private JLabel agentLbl;
    private JLabel dateLbl;
    private JLabel notesLbl;

    private JTextField idTf;
    private JTextField clientTf;
    private JTextField agentTf;
    private JTextField dateTf;

    private JTextArea notesTa;

    private Border editOrderPanelBorder;
    private Integer index;
    private JComboBox<Agent> agentsComboBox;
    private JComboBox<Client> clientsComboBox;

    public OrderFrame(Order order,Integer index,JComboBox<Agent> agentsComboBox,JComboBox<Client> clientJComboBox){
        editOrderPanelBorder = BorderFactory.createLoweredBevelBorder();
        this.agentsComboBox = agentsComboBox;
        this.clientsComboBox = clientJComboBox;
        agentsComboBox.setSelectedItem(MainFrame.agentsHashMapNames.get(order.getAgent()));
        clientJComboBox.setSelectedItem(MainFrame.clientsHashMapNames.get(order.getClient()));
        idLbl = new JLabel("Order ID");
        clientLbl = new JLabel("Client");
        agentLbl = new JLabel("Agent");
        dateLbl = new JLabel("Date");
        notesLbl = new JLabel("Notes");

        idTf = new JTextField(order.getId());
        idTf.setEditable(false);
        clientTf = new JTextField(order.getClient());
        agentTf = new JTextField(order.getAgent());
        dateTf = new JTextField(order.getDate());
        dateTf.setEditable(false);

        notesTa = new JTextArea(10,20);
        notesTa.getText();
        notesTa.setText(order.getNotes());
        notesTa.setWrapStyleWord(true);
        notesTa.setLineWrap(true);

        deleteBtn = new JButton("Delete order");
        applyChangesBtn = new JButton("Apply changes");
        moveToDoneOrdersBtn = new JButton("Move to Done orders");
        moveToPendingOrdersBtn = new JButton("Move to Pending orders");

        this.index = index;
        this.order = order;
        orderTable = new JTable();
        orderData = new String[][] {};
        orderColumns = new String[] {"Product","Amount"};
        orderModel = new DefaultTableModel(orderData,orderColumns){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };;
        orderModel = order.toData(orderModel);
        orderTable.setModel(orderModel);

    }

    public void prepareUI(){
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.Y_AXIS));

        JPanel notesPanel = new JPanel();

       JScrollPane notesScroller = new JScrollPane();
        notesScroller.setBorder(
                BorderFactory.createTitledBorder("Notes"));
        notesScroller.setViewportView(notesTa);

        notesPanel.add(notesScroller);

        JPanel editOrderPanel = new JPanel();
        editOrderPanel.setLayout(new GridLayout(4,2));
        editOrderPanel.setBorder(editOrderPanelBorder);

        editOrderPanel.add(idLbl);
        editOrderPanel.add(idTf);
        editOrderPanel.add(clientLbl);
        editOrderPanel.add(clientsComboBox);
        editOrderPanel.add(agentLbl);
        editOrderPanel.add(agentsComboBox);
        editOrderPanel.add(dateLbl);
        editOrderPanel.add(dateTf);


        orderScrollPane = new JScrollPane(orderTable);

//        mainPanel.add(orderScrollPane);
        buttonPanel.add(moveToPendingOrdersBtn);
        buttonPanel.add(moveToDoneOrdersBtn);
        buttonPanel.add(applyChangesBtn);
        buttonPanel.add(deleteBtn);

        this.add(orderScrollPane,BorderLayout.CENTER);
        this.add(buttonPanel,BorderLayout.EAST);
        this.add(notesPanel,BorderLayout.WEST);
        this.add(editOrderPanel,BorderLayout.NORTH);


        moveToDoneOrdersBtn.addActionListener(e -> {
            order.setDoneDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            order.setIsOrder(2);
            MainFrame.updateOrderState(order);
            MainFrame.deleteOrder(index);
            MainFrame.addDoneOrder(order);
            dispose();
        });

        moveToPendingOrdersBtn.addActionListener(e -> {
            order.setIsOrder(1);
            MainFrame.updateOrderState(order);
            MainFrame.deleteOrder(index);
            MainFrame.addPendingOrder(order);
            dispose();
        });

        deleteBtn.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to" +
                            " delete "+order.getId()+" ?",
                    "Confirmation", JOptionPane.OK_CANCEL_OPTION);
            if (result == 0) {
                MainFrame.deleteOrder(index);
                MainFrame.deleteOrderFromDB(order);
                dispose();
            }
        });

        applyChangesBtn.addActionListener(e -> {
            order.setClient(Objects.requireNonNull(clientsComboBox.getSelectedItem()).toString());
            order.setAgent(Objects.requireNonNull(agentsComboBox.getSelectedItem()).toString());
            order.setNotes(notesTa.getText());
            MainFrame.updateOrder(order,index);
            dispose();
        });
    }

}
