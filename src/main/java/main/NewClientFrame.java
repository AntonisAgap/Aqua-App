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

public class NewClientFrame extends JFrame{

    //declare member variables
    private JLabel addClientLbl;

    private JLabel clientIDLbl;
    private JLabel companyNameLbl;
    private JLabel tinLbl;
    private JLabel phoneNumberLbl;
    private JLabel addressLbl;
    private JLabel townLbl;
    private JLabel typeOfBusinessLbl;

    private JTextField clientIDTf;
    private JTextField companyNameTf;
    private JTextField tinTf;
    private JTextField phoneNumberTf;
    private JTextField addressTf;
    private JTextField townTf;
    private JTextField typeOfBusinessTf;

    private JButton completeClientBtn;
    private static JLabel errorLbl;

    private Font font;

    private EmptyBorder addClientLblBorder;
    private Border addClientPanelBorder;

    public NewClientFrame() {
        super();
        font = new Font("Arial", Font.BOLD,15);

        addClientLblBorder = new EmptyBorder(3, 0, 15, 0);
        addClientPanelBorder = BorderFactory.createLoweredBevelBorder();

        addClientLbl = new JLabel("Add new client\n");
        addClientLbl.setBorder(addClientLblBorder);
        addClientLbl.setFont(font);

        clientIDLbl = new JLabel("Client ID");
        companyNameLbl = new JLabel("Company name");
        tinLbl = new JLabel("TIN");
        phoneNumberLbl = new JLabel("Phone number");
        addressLbl = new JLabel("Address");
        townLbl = new JLabel("Town");
        typeOfBusinessLbl = new JLabel("Business type");

        clientIDTf = new JTextField();
        companyNameTf = new JTextField();
        tinTf = new JTextField();
        phoneNumberTf = new JTextField();
        addressTf = new JTextField();
        townTf = new JTextField();
        typeOfBusinessTf = new JTextField();

        completeClientBtn = new JButton("Add client");

        errorLbl = new JLabel();
        errorLbl.setForeground(Color.RED);
    }

    public void prepareUI() {

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));

        JPanel addClientPanel = new JPanel();

        addClientPanel.setLayout(new GridLayout(7,2));
        addClientPanel.setBorder(addClientPanelBorder);

        addClientPanel.add(clientIDLbl);
        addClientPanel.add(clientIDTf);
        addClientPanel.add(companyNameLbl);
        addClientPanel.add(companyNameTf);
        addClientPanel.add(tinLbl);
        addClientPanel.add(tinTf);
        addClientPanel.add(phoneNumberLbl);
        addClientPanel.add(phoneNumberTf);
        addClientPanel.add(addressLbl);
        addClientPanel.add(addressTf);
        addClientPanel.add(townLbl);
        addClientPanel.add(townTf);
        addClientPanel.add(typeOfBusinessLbl);
        addClientPanel.add(typeOfBusinessTf);

        addClientLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        completeClientBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        errorLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel.add(addClientLbl);
        mainPanel.add(addClientPanel);
        mainPanel.add(completeClientBtn);
        mainPanel.add(errorLbl);

        this.add(mainPanel);

        completeClientBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = clientIDTf.getText();
                String companyName = companyNameTf.getText();
                String town = townTf.getText();
                String tin = tinTf.getText();
                String phoneNumber = phoneNumberTf.getText();
                String address = addressTf.getText();
                String typeOfBusiness = typeOfBusinessTf.getText();
                if (_checkInput(id,companyName,address,typeOfBusiness)) {
                    Client client = new Client(id,companyName,phoneNumber,address,town,tin,typeOfBusiness);
                    MainFrame.addClient(client);
                    MainFrame.addClientToDb(client);
                    dispose();
                }
            }
        });

    }

    private boolean _checkInput(String id,String companyName, String address,String typeOfBusiness){

        errorLbl.setVisible(false);

        if (companyName.isEmpty()) {
            errorLbl.getText();
            errorLbl.setText("First name field is empty");
            errorLbl.setVisible(true);
            return false;
        }

        if (id.isEmpty()) {
            errorLbl.getText();
            errorLbl.setText("Client ID field is empty");
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
