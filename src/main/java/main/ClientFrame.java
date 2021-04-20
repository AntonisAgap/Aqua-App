package main;


import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class ClientFrame extends JFrame{
    private Client client;
    private int index;

    private JButton deleteBtn;
    private JButton applyChangesBtn;
    private JButton statisticsBtn;

    private JLabel idLbl;
    private JLabel companyNameLbl;
    private JLabel addressLbl;
    private JLabel phoneNumberLbl;
    private JLabel townLbl;
    private JLabel tinLbl;
    private JLabel typeOfBusinessLbl;

    private JTextField idTf;
    private JTextField companyNameTf;
    private JTextField addressTf;
    private JTextField phoneNumberTf;
    private JTextField townTf;
    private JTextField  tinTf;
    private JTextField typeOfBusinessTf;

    private Border editClientPanelBorder;

    public ClientFrame(Client client,int index){
        editClientPanelBorder = BorderFactory.createLoweredBevelBorder();
        this.client = client;
        this.index = index;

        idLbl = new JLabel("Client ID");
        companyNameLbl = new JLabel("Company name");
        addressLbl = new JLabel("TIN");
        phoneNumberLbl = new JLabel("Address");
        townLbl = new JLabel("Town");
        tinLbl = new JLabel("TIN");
        typeOfBusinessLbl = new JLabel("Type of business");


        idTf = new JTextField(client.getId());
        idTf.setEditable(false);
        companyNameTf = new JTextField(client.getCompanyName());
        addressTf = new JTextField(client.getAddress());
        phoneNumberTf = new JTextField(client.getPhoneNumber());
        townTf = new JTextField(client.getTown());
        tinTf = new JTextField(client.getTin());
        typeOfBusinessTf = new JTextField(client.getTypeOfBusiness());

        deleteBtn = new JButton("Delete client");
        deleteBtn.setEnabled(false);
        applyChangesBtn = new JButton("Apply changes");
        statisticsBtn = new JButton("Statistics");
    }

    public void prepareUI(){
        MainFrame.disableClientsTable();

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.Y_AXIS));

        JPanel editClientPanel = new JPanel();
        editClientPanel.setLayout(new GridLayout(7,2));
        editClientPanel.setBorder(editClientPanelBorder);

        editClientPanel.add(idLbl);
        editClientPanel.add(idTf);
        editClientPanel.add(companyNameLbl);
        editClientPanel.add(companyNameTf);
        editClientPanel.add(addressLbl);
        editClientPanel.add(addressTf);
        editClientPanel.add(townLbl);
        editClientPanel.add(townTf);
        editClientPanel.add(tinLbl);
        editClientPanel.add(tinTf);
        editClientPanel.add(phoneNumberLbl);
        editClientPanel.add(phoneNumberTf);
        editClientPanel.add(typeOfBusinessLbl);
        editClientPanel.add(typeOfBusinessTf);

        buttonPanel.add(applyChangesBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(statisticsBtn);

        this.add(editClientPanel,BorderLayout.CENTER);

        this.add(buttonPanel,BorderLayout.SOUTH);

        this.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {

            }

            @Override
            public void windowClosed(WindowEvent e) {
                MainFrame.enableClientsTable();
                dispose();
            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });
        applyChangesBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.setCompanyName(companyNameTf.getText());
                client.setAddress(addressTf.getText());
                client.setTown(townTf.getText());
                client.setTin(tinTf.getText());
                client.setPhoneNumber(phoneNumberTf.getText());
                client.setTypeOfBusiness(typeOfBusinessTf.getText());
                MainFrame.updateClient(client,index);
                MainFrame.enableClientsTable();
                dispose();
            }
        });

        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to" +
                                " delete "+client.getCompanyName()+" ?",
                        "Confirmation", JOptionPane.OK_CANCEL_OPTION);
                System.out.println(result);
//                MainFrame.deleteClient(client,index);
//                MainFrame.enableClientsTable();
//                dispose();
            }
        });

        statisticsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String uri = "http://aquaaromatics.azurewebsites.net/stats/api/v1.0/"+idTf.getText().replaceAll(" ", "%20");
                    Desktop.getDesktop().browse(new URI(uri));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                } catch (URISyntaxException uriSyntaxException) {
                    uriSyntaxException.printStackTrace();
                }
            }
        });

    }

}
