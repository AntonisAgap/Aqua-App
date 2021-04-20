package main;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class ClientsFrame extends JFrame{
    private final JTable clientsTable;
    private JTextField searchField;

    public ClientsFrame(JTable clientsTable){
        searchField = new HintTextField("Search");
        searchField = new HintTextField("Search");
        searchField.setColumns(35);
//        searchField.addActionListener(this::setSearchField);


        this.clientsTable = clientsTable;

        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                try {
                    Robot robot = new Robot();
                    searchField.requestFocusInWindow();
                    searchField.setText("");
                    robot.keyPress(KeyEvent.VK_ENTER);
                    System.out.println("Hey");
                } catch (AWTException e) {
                    e.printStackTrace();
                }

            }
        });

        TableRowSorter<TableModel> sorter = new TableRowSorter<>(clientsTable.getModel());
        clientsTable.setRowSorter(sorter);

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                update(e);
            }

            public void changedUpdate(DocumentEvent e) {
                update(e);
            }
            public void removeUpdate(DocumentEvent e) {
                update(e);
            }

            public void update(DocumentEvent e) {
                String text = searchField.getText();
                if (text.trim().length() == 0 || text.equals("Search")) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        });

    }

    public void prepareUI(){
        JScrollPane clientsScrollPane = new JScrollPane(clientsTable);
        JPanel searchPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPane.add(searchField);
        searchPane.setPreferredSize(new Dimension(100,27));
        this.add(clientsScrollPane,BorderLayout.CENTER);
        this.add(searchPane,BorderLayout.NORTH);
    }

    private void setSearchField(ActionEvent evt) {
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(clientsTable.getModel());
        sorter.setRowFilter(RowFilter.regexFilter("(?i)"+searchField.getText()));
        clientsTable.setRowSorter(sorter);
    }




}
