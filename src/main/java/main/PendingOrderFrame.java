package main;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class PendingOrderFrame extends JFrame{
    private JScrollPane pendingOrderScrollPane;
    private JTable pendingOrderTable;
    private Order pendingOrder;
    private static DefaultTableModel pendingOrderModel;
    private String[] pendingOrderColumns;
    private String[][] pendingOrderData;

    private JButton deleteBtn;
    private JButton applyChangesBtn;
    private JButton moveToOrdersBtn;
    private JButton moveToDoneOrdersBtn;
    private JButton printBtn;

    private JLabel idLbl;
    private JLabel clientLbl;
    private JLabel agentLbl;
    private JLabel dateLbl;

    private JTextField idTf;
    private JTextField clientTf;
    private JTextField agentTf;
    private JTextField dateTf;

    private Border editPendingOrderPanelBorder;
    private Integer index;
    private JComboBox<Agent> agentJComboBox;
    private JComboBox<Client> clientJComboBox;
    private JTextArea notesTa;

    public PendingOrderFrame(Order pendingOrder,Integer index,JComboBox<Agent> agentJComboBox,JComboBox<Client> clientJComboBox){
        editPendingOrderPanelBorder = BorderFactory.createLoweredBevelBorder();
        this.agentJComboBox = agentJComboBox;
        this.clientJComboBox = clientJComboBox;
        agentJComboBox.setSelectedItem(MainFrame.agentsHashMapNames.get(pendingOrder.getAgent()));
        clientJComboBox.setSelectedItem(MainFrame.clientsHashMapNames.get(pendingOrder.getClient()));
        idLbl = new JLabel("Order ID");
        clientLbl = new JLabel("Client");
        agentLbl = new JLabel("Agent");
        dateLbl = new JLabel("Date");

        this.pendingOrder = pendingOrder;

        idTf = new JTextField(this.pendingOrder.getId());
        idTf.setEditable(false);
        clientTf = new JTextField(this.pendingOrder.getClient());
        agentTf = new JTextField(this.pendingOrder.getAgent());
        dateTf = new JTextField(this.pendingOrder.getDate());
        dateTf.setEditable(false);

        notesTa = new JTextArea(10,20);
        notesTa.getText();
        notesTa.setText(pendingOrder.getNotes());
        notesTa.setWrapStyleWord(true);
        notesTa.setLineWrap(true);

        deleteBtn = new JButton("Delete order");
        applyChangesBtn = new JButton("Apply changes");
        moveToOrdersBtn = new JButton("Move to Orders");
        moveToDoneOrdersBtn = new JButton("Move to Done orders");
        printBtn = new JButton("Print order");

        this.index = index;
        pendingOrderTable = new JTable();
        pendingOrderData = new String[][] {};
        pendingOrderColumns = new String[] {"Product","Amount"};
        pendingOrderModel = new DefaultTableModel(pendingOrderData,pendingOrderColumns){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };;
        pendingOrderModel = pendingOrder.toData(pendingOrderModel);
        pendingOrderTable.setModel(pendingOrderModel);

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

        JPanel editPendingOrderPanel = new JPanel();
        editPendingOrderPanel.setLayout(new GridLayout(4,2));
        editPendingOrderPanel.setBorder(editPendingOrderPanelBorder);

        editPendingOrderPanel.add(idLbl);
        editPendingOrderPanel.add(idTf);
        editPendingOrderPanel.add(clientLbl);
        editPendingOrderPanel.add(clientJComboBox);
        editPendingOrderPanel.add(agentLbl);
        editPendingOrderPanel.add(agentJComboBox);
        editPendingOrderPanel.add(dateLbl);
        editPendingOrderPanel.add(dateTf);


        pendingOrderScrollPane = new JScrollPane(pendingOrderTable);

//        mainPanel.add(orderScrollPane);
        buttonPanel.add(moveToOrdersBtn);
        buttonPanel.add(moveToDoneOrdersBtn);
        buttonPanel.add(applyChangesBtn);
        buttonPanel.add(deleteBtn);

        this.add(pendingOrderScrollPane,BorderLayout.CENTER);
        this.add(buttonPanel,BorderLayout.EAST);
        this.add(notesPanel,BorderLayout.WEST);
        this.add(editPendingOrderPanel,BorderLayout.NORTH);


        moveToOrdersBtn.addActionListener(e -> {
            pendingOrder.setIsOrder(0);
            MainFrame.updateOrderState(pendingOrder);
            MainFrame.deletePendingOrder(index);
            MainFrame.addOrder(pendingOrder);
            dispose();
        });

        moveToDoneOrdersBtn.addActionListener(e -> {
            pendingOrder.setDoneDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            pendingOrder.setIsOrder(2);
            MainFrame.updateOrderState(pendingOrder);
            MainFrame.deletePendingOrder(index);
            MainFrame.addDoneOrder(pendingOrder);
            dispose();
        });

        deleteBtn.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to" +
                            " delete "+pendingOrder.getId()+" ?",
                    "Confirmation", JOptionPane.OK_CANCEL_OPTION);
            if (result == 0) {
                MainFrame.deletePendingOrder(index);
                MainFrame.deleteOrderFromDB(pendingOrder);
                dispose();
            }
        });

        applyChangesBtn.addActionListener(e -> {
            pendingOrder.setClient(Objects.requireNonNull(clientJComboBox.getSelectedItem()).toString());
            pendingOrder.setAgent(Objects.requireNonNull(agentJComboBox.getSelectedItem()).toString());
            pendingOrder.setNotes(notesTa.getText());
            MainFrame.updateOrder(pendingOrder,index);
            dispose();
        });

    }

}
