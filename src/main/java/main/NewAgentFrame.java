package main;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.DefaultTableModel;

public class NewAgentFrame extends JFrame{

    private JScrollPane clientsScrollPane;
    private JTable clientesTable;
    private static DefaultTableModel clientsModel;
    private String[] clientsColumns;

    //declare member variables
    private JLabel addAgentLbl;
    private JLabel firstNameLbl;
    private JLabel lastNameLbl;
    private JLabel addressLbl;
    private JLabel phoneNumberLbl;

    private JTextField firstNameTf;
    private JTextField lastNameTf;
    private JTextField addressTf;
    private JTextField phoneNumberTf;
    private static JComboBox<Client> clientsJCB;
    private JButton completeAgentBtn;
    private JButton addClientsBtn;
    private static JLabel errorLbl;

    private Font font;

    private EmptyBorder addAgentLblBorder;
    private Border addAgentPanelBorder;

    private JPopupMenu popupMenu;
    private JMenuItem deleteItem;

    public NewAgentFrame(JComboBox<Client> clientsJCB) {
        super();
        font = new Font("Arial", Font.BOLD,15);
        NewAgentFrame.clientsJCB = clientsJCB;
        addAgentLblBorder = new EmptyBorder(3, 0, 15, 0);
        addAgentPanelBorder = BorderFactory.createLoweredBevelBorder();

        popupMenu = new JPopupMenu();
        deleteItem = new JMenuItem("Delete");
        popupMenu.add(deleteItem);

        addAgentLbl = new JLabel("Add new agent\n");
        addAgentLbl.setBorder(addAgentLblBorder);
        addAgentLbl.setFont(font);

        firstNameLbl = new JLabel("First name");
        lastNameLbl = new JLabel("Last name");
        phoneNumberLbl = new JLabel("Phone number");
        addressLbl = new JLabel("Address");

        firstNameTf = new JTextField();
        lastNameTf  = new JTextField();
        addressTf = new JTextField();
        phoneNumberTf = new JTextField();

        completeAgentBtn = new JButton("Add agent");
        addClientsBtn = new JButton("Add client");

        errorLbl = new JLabel();
        errorLbl.setForeground(Color.RED);

        clientesTable = new JTable();
        String[][] clientsData = new String[][]{};
        clientsColumns = new String[] {"Client ID", "Company name","TIN","Address","Town","Phone number","Type Of Business"};

        clientsModel = new DefaultTableModel(clientsColumns,0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        clientesTable.setModel(clientsModel);
        clientesTable.setComponentPopupMenu(popupMenu);
    }

    public void prepareUI() {

        clientsScrollPane = new JScrollPane(clientesTable);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));

        JPanel addAgentPanel = new JPanel();

        addAgentPanel.setLayout(new GridLayout(5,2));
        addAgentPanel.setBorder(addAgentPanelBorder);

        addAgentPanel.add(firstNameLbl);
        addAgentPanel.add(firstNameTf);
        addAgentPanel.add(lastNameLbl);
        addAgentPanel.add(lastNameTf);
        addAgentPanel.add(phoneNumberLbl);
        addAgentPanel.add(phoneNumberTf);
        addAgentPanel.add(addressLbl);
        addAgentPanel.add(addressTf);
        addAgentPanel.add(clientsJCB);
        addAgentPanel.add(addClientsBtn);

        addAgentLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        completeAgentBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        errorLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel.add(addAgentLbl);
        mainPanel.add(addAgentPanel);
        mainPanel.add(clientsScrollPane);
        mainPanel.add(completeAgentBtn);
        mainPanel.add(errorLbl);

        this.add(mainPanel);

        addClientsBtn.addActionListener(e -> {
            Client client = (Client) clientsJCB.getSelectedItem();
            clientsModel.addRow(client.toRow());
        });

        completeAgentBtn.addActionListener(e -> {
            String firstName = firstNameTf.getText();
            String lastName = lastNameTf.getText();
            String phoneNumber = phoneNumberTf.getText();
            String address = addressTf.getText();
            ArrayList<String> hasClients = new ArrayList<>();
            for (int count = 0; count < clientsModel.getRowCount(); count++){
                hasClients.add(clientsModel.getValueAt(count, 1).toString());
            }
            Agent agent = new Agent("",firstName,lastName,phoneNumber,address,hasClients);
            MainFrame.addAgentToDb(agent);
            MainFrame.addAgent(agent);
            dispose();
        });

        deleteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = clientesTable.getSelectedRow();
                int modelRow = clientesTable.convertRowIndexToModel(row);
                clientsModel.removeRow(modelRow);
            }
        });

        popupMenu.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                SwingUtilities.invokeLater(() -> {
                    int rowAtPoint = clientesTable.rowAtPoint(SwingUtilities.convertPoint(popupMenu, new Point(0, 0), clientesTable));
                    if (rowAtPoint > -1) {
                        clientesTable.setRowSelectionInterval(rowAtPoint, rowAtPoint);
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

    private boolean _checkInput(String companyName, String address,String typeOfBusiness){

        errorLbl.setVisible(false);

        if (companyName.isEmpty()) {
            errorLbl.getText();
            errorLbl.setText("First name field is empty");
            errorLbl.setVisible(true);
            return false;
        }

        if (typeOfBusiness.isEmpty()) {
            errorLbl.getText();
            errorLbl.setText("Business type field is empty");
            errorLbl.setVisible(true);
            return false;
        }
        if (address.isEmpty()) {
            errorLbl.getText();
            errorLbl.setText("Address field is empty");
            errorLbl.setVisible(true);
            return false;
        }
        return true;
    }


}