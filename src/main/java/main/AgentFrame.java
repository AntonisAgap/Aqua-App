package main;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;


public class AgentFrame extends JFrame {
    private JScrollPane hasClientsScrollPane;
    private JTable hasClientsTable;
    private Agent agent;
    private int index;
    private static DefaultTableModel hasClientsModel;
    private String[] hasClientsColumns;
    private String[][] hasClientsData;

    private JButton deleteBtn;
    private JButton applyChangesBtn;

    private JLabel idLbl;
    private JLabel firstNameLbl;
    private JLabel lastNameLbl;
    private JLabel phoneNumberLbl;
    private JLabel addressLbl;

    private JTextField idTf;
    private JTextField firstNameTf;
    private JTextField lastNameTf;
    private JTextField phoneNumberTf;
    private JTextField addressTf;

    private Border editAgentPanelBorder;
    private static JComboBox<Client> clientJComboBox;
    private static JButton addClientBtn;

    private JPopupMenu popupMenu;
    private JMenuItem deleteItem;

    public AgentFrame(Agent agent,int index,JComboBox<Client> clientJComboBox) {
        AgentFrame.clientJComboBox = clientJComboBox;
        editAgentPanelBorder = BorderFactory.createLoweredBevelBorder();
        idLbl = new JLabel("Agent ID");
        firstNameLbl = new JLabel("First name");
        lastNameLbl = new JLabel("Last name");
        phoneNumberLbl = new JLabel("Phone number");
        addressLbl = new JLabel("Address");

        popupMenu = new JPopupMenu();
        deleteItem = new JMenuItem("Delete");
        popupMenu.add(deleteItem);

        addClientBtn = new JButton("Add client");

        idTf = new JTextField(agent.getId());
        idTf.setEditable(false);
        firstNameTf = new JTextField(agent.getFirstName());
        lastNameTf = new JTextField(agent.getLastName());
        phoneNumberTf = new JTextField(agent.getPhoneNumber());
        addressTf = new JTextField(agent.getAddress());

        deleteBtn = new JButton("Delete agent");
        applyChangesBtn = new JButton("Apply changes");

        this.agent = agent;
        this.index = index;
        hasClientsTable = new JTable();
        hasClientsData = new String[][]{};
        hasClientsColumns = new String[]{"Clients"};
        hasClientsModel = new DefaultTableModel(hasClientsData, hasClientsColumns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        hasClientsModel = agent.toData(hasClientsModel);
        hasClientsTable.setModel(hasClientsModel);
        hasClientsTable.setComponentPopupMenu(popupMenu);
    }

    public void prepareUI() {
        MainFrame.disableAgentsTable();

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        JPanel editDoneOrderPanel = new JPanel();
        editDoneOrderPanel.setLayout(new GridLayout(6, 2));
        editDoneOrderPanel.setBorder(editAgentPanelBorder);

        editDoneOrderPanel.add(idLbl);
        editDoneOrderPanel.add(idTf);
        editDoneOrderPanel.add(firstNameLbl);
        editDoneOrderPanel.add(firstNameTf);
        editDoneOrderPanel.add(lastNameLbl);
        editDoneOrderPanel.add(lastNameTf);
        editDoneOrderPanel.add(phoneNumberLbl);
        editDoneOrderPanel.add(phoneNumberTf);
        editDoneOrderPanel.add(addressLbl);
        editDoneOrderPanel.add(addressTf);
        editDoneOrderPanel.add(clientJComboBox);
        editDoneOrderPanel.add(addClientBtn);


        hasClientsScrollPane = new JScrollPane(hasClientsTable);

        buttonPanel.add(applyChangesBtn);
        buttonPanel.add(deleteBtn);

        this.add(hasClientsScrollPane, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.EAST);
        this.add(editDoneOrderPanel, BorderLayout.NORTH);

        addClientBtn.addActionListener(e -> {
            Client client = (Client) clientJComboBox.getSelectedItem();
            List<String> row = new ArrayList<>();
            row.add(client.getCompanyName());
            hasClientsModel.addRow(row.toArray());
        });

        applyChangesBtn.addActionListener(e -> {
            agent.setFirstName(firstNameTf.getText());
            agent.setLastName(lastNameTf.getText());
            agent.setPhoneNumber(phoneNumberTf.getText());
            agent.setAddress(addressTf.getText());
            ArrayList<String> hasClients = new ArrayList<>();
            for (int count = 0;count<hasClientsModel.getRowCount();count++){
                hasClients.add(hasClientsModel.getValueAt(count,0).toString());
            }
            agent.setHasClients(hasClients);
            MainFrame.updateAgent(agent,index);
            MainFrame.enableAgentsTable();
            dispose();
        });

        deleteBtn.addActionListener(e -> {
            MainFrame.deleteAgent(agent,index);
            MainFrame.enableAgentsTable();
            dispose();
        });

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);
                MainFrame.enableAgentsTable();
                dispose();
            }
        });

        deleteItem.addActionListener(e -> {
            int row = hasClientsTable.getSelectedRow();
            int modelRow = hasClientsTable.convertRowIndexToModel(row);
            hasClientsModel.removeRow(modelRow);
        });

        popupMenu.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                SwingUtilities.invokeLater(() -> {
                    int rowAtPoint = hasClientsTable.rowAtPoint(SwingUtilities.convertPoint(popupMenu, new Point(0, 0), hasClientsTable));
                    if (rowAtPoint > -1) {
                        hasClientsTable.setRowSelectionInterval(rowAtPoint, rowAtPoint);
                    }
                });
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                //TODO Auto-generated method stub
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
                //TODO Auto-generated method stub
            }
        });
    }

}
