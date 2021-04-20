package main;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.util.Objects;

public class DoneOrderFrame extends JFrame{
    private JScrollPane doneOrderScrollPane;
    private JTable doneOrderTable;
    private Order doneOrder;
    private static DefaultTableModel doneOrderModel;
    private String[] doneOrderColumns;
    private String[][] doneOrderData;

    private JButton deleteBtn;
    private JButton applyChangesBtn;
    private JButton moveToOrdersBtn;
    private JButton moveToPendingOrdersBtn;
    private JButton printBtn;

    private JLabel idLbl;
    private JLabel clientLbl;
    private JLabel agentLbl;
    private JLabel dateLbl;
    private JLabel doneDateLbl;

    private JTextField idTf;
    private JTextField clientTf;
    private JTextField agentTf;
    private JTextField dateTf;
    private JTextField doneDateTf;

    private JTextArea notesTa;

    private Border editDoneOrderPanelBorder;
    private Integer index;
    private JComboBox<Agent> agentJComboBox;
    private JComboBox<Client> clientJComboBox;

    public DoneOrderFrame(Order doneOrder,Integer index,JComboBox<Agent> agentJComboBox,JComboBox<Client> clientJComboBox){
        editDoneOrderPanelBorder = BorderFactory.createLoweredBevelBorder();
        this.agentJComboBox = agentJComboBox;
        this.clientJComboBox = clientJComboBox;
        agentJComboBox.setSelectedItem(MainFrame.agentsHashMapNames.get(doneOrder.getAgent()));
        clientJComboBox.setSelectedItem(MainFrame.clientsHashMapNames.get(doneOrder.getClient()));
        idLbl = new JLabel("Order ID");
        clientLbl = new JLabel("Client");
        agentLbl = new JLabel("Agent");
        dateLbl = new JLabel("Date");
        doneDateLbl = new JLabel("Done date");

        idTf = new JTextField(doneOrder.getId());
        idTf.setEditable(false);
        clientTf = new JTextField(doneOrder.getClient());
        agentTf = new JTextField(doneOrder.getAgent());
        dateTf = new JTextField(doneOrder.getDate());
        dateTf.setEditable(false);
        doneDateTf = new JTextField(doneOrder.getDoneDate());
        doneDateTf.setEditable(false);

        notesTa = new JTextArea(10,20);
        notesTa.getText();
        notesTa.setText(doneOrder.getNotes());
        notesTa.setWrapStyleWord(true);
        notesTa.setLineWrap(true);

        deleteBtn = new JButton("Delete order");
        applyChangesBtn = new JButton("Apply changes");
        moveToOrdersBtn = new JButton("Move to Orders");
        moveToPendingOrdersBtn = new JButton("Move to Pending orders");

        this.index = index;
        this.doneOrder = doneOrder;
        doneOrderTable = new JTable();
        doneOrderData = new String[][] {};
        doneOrderColumns = new String[] {"Product","Amount"};
        doneOrderModel = new DefaultTableModel(doneOrderData,doneOrderColumns){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        doneOrderModel = doneOrder.toData(doneOrderModel);
        doneOrderTable.setModel(doneOrderModel);
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

        JPanel editDoneOrderPanel = new JPanel();
        editDoneOrderPanel.setLayout(new GridLayout(5,2));
        editDoneOrderPanel.setBorder(editDoneOrderPanelBorder);

        editDoneOrderPanel.add(idLbl);
        editDoneOrderPanel.add(idTf);
        editDoneOrderPanel.add(clientLbl);
        editDoneOrderPanel.add(clientJComboBox);
        editDoneOrderPanel.add(agentLbl);
        editDoneOrderPanel.add(agentJComboBox);
        editDoneOrderPanel.add(dateLbl);
        editDoneOrderPanel.add(dateTf);
        editDoneOrderPanel.add(doneDateLbl);
        editDoneOrderPanel.add(doneDateTf);

        doneOrderScrollPane = new JScrollPane(doneOrderTable);

//        mainPanel.add(orderScrollPane);
        buttonPanel.add(moveToOrdersBtn);
        buttonPanel.add(moveToPendingOrdersBtn);
        buttonPanel.add(applyChangesBtn);
        buttonPanel.add(deleteBtn);

        this.add(doneOrderScrollPane,BorderLayout.CENTER);
        this.add(buttonPanel,BorderLayout.EAST);
        this.add(notesPanel,BorderLayout.WEST);
        this.add(editDoneOrderPanel,BorderLayout.NORTH);


        moveToOrdersBtn.addActionListener(e -> {
            MainFrame.deleteDoneOrder(index);
            doneOrder.setDoneDate("");
            doneOrder.setIsOrder(0);
            MainFrame.updateOrderState(doneOrder);
            MainFrame.addOrder(doneOrder);
            dispose();
        });

        moveToPendingOrdersBtn.addActionListener(e -> {
            MainFrame.deleteDoneOrder(index);
            doneOrder.setDoneDate("");
            doneOrder.setIsOrder(1);
            MainFrame.updateOrderState(doneOrder);
            MainFrame.addPendingOrder(doneOrder);
            dispose();
        });

        deleteBtn.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to" +
                            " delete "+doneOrder.getId()+" ?",
                    "Confirmation", JOptionPane.OK_CANCEL_OPTION);
            if (result == 0) {
                MainFrame.deleteDoneOrder(index);
                MainFrame.deleteOrderFromDB(doneOrder);
                dispose();
            }
        });

        applyChangesBtn.addActionListener(e -> {
            doneOrder.setClient(Objects.requireNonNull(clientJComboBox.getSelectedItem()).toString());
            doneOrder.setAgent(Objects.requireNonNull(agentJComboBox.getSelectedItem()).toString());
            doneOrder.setNotes(notesTa.getText());
            MainFrame.updateOrder(doneOrder,index);
            dispose();
        });
    }

}
