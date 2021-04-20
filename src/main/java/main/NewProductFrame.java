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

public class NewProductFrame extends JFrame{

    //declare member variables
    private JLabel addProductLbl;

    private JLabel idLbl;
    private JLabel nameLbl;
    private JLabel categoryLbl;
    private JLabel barcodeLbl;

    private JTextField idTf;
    private JTextField nameTf;
    private JTextField categoryTf;
    private JTextField barcodeTf;

    private JButton completeProductBtn;
    private static JLabel errorLbl;

    private Font font;

    private EmptyBorder addProductLblBorder;
    private Border addProductPanelBorder;

    public NewProductFrame(String categoryBranch) {
        super();
        font = new Font("Arial", Font.BOLD,15);

        addProductLblBorder = new EmptyBorder(3, 0, 10, 0);
        addProductPanelBorder = BorderFactory.createLoweredBevelBorder();

        addProductLbl = new JLabel("Add new product\n");
        addProductLbl.setBorder(addProductLblBorder);
        addProductLbl.setFont(font);

        idLbl = new JLabel("Product ID");
        nameLbl = new JLabel("Product name");
        categoryLbl = new JLabel("Category");
        barcodeLbl = new JLabel("Barcode");

        idTf = new JTextField();
        nameTf = new JTextField();
        categoryTf = new JTextField();
        barcodeTf = new JTextField();
        categoryTf.getText();
        categoryTf.setText(categoryBranch);
        categoryTf.setEditable(false);

        completeProductBtn = new JButton("Add product");

        errorLbl = new JLabel();
        errorLbl.setForeground(Color.RED);
    }

    public void prepareUI() {

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));

        JPanel addProductPanel = new JPanel();

        addProductPanel.setLayout(new GridLayout(4,2));
        addProductPanel.setBorder(addProductPanelBorder);

        addProductPanel.add(idLbl);
        addProductPanel.add(idTf);
        addProductPanel.add(nameLbl);
        addProductPanel.add(nameTf);
        addProductPanel.add(barcodeLbl);
        addProductPanel.add(barcodeTf);
        addProductPanel.add(categoryLbl);
        addProductPanel.add(categoryTf);

        addProductLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        completeProductBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        errorLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel.add(addProductLbl);
        mainPanel.add(addProductPanel);
        mainPanel.add(completeProductBtn);
        mainPanel.add(errorLbl);

        this.add(mainPanel);

        completeProductBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idTf.getText();
                String name = nameTf.getText();
                String category = categoryTf.getText();
                String barcode = barcodeTf.getText();
                int price = 0;
                if (_checkInput(id,name,category,barcode)) {
                    Product product = new Product(id,name,category,barcode,(float) price);
                    product.setAquaID(id);
                    MainFrame.addProductToDb(product);
                    MainFrame.addProduct(product);
                    dispose();
                }
            }
        });

    }

    private boolean _checkInput(String id,String name,String category,String barcode){

        errorLbl.setVisible(false);

        if (id.isEmpty()) {
            errorLbl.getText();
            errorLbl.setText("Product ID field is empty");
            errorLbl.setVisible(true);
            return false;
        }
        if (name.isEmpty()) {
            errorLbl.getText();
            errorLbl.setText("Product name is empty");
            errorLbl.setVisible(true);
            return false;
        }
        if (category.isEmpty()) {
            errorLbl.getText();
            errorLbl.setText("Category field is empty");
            errorLbl.setVisible(true);
            return false;
        }
        if (barcode.isEmpty()) {
            errorLbl.getText();
            errorLbl.setText("Barcode field is empty");
            errorLbl.setVisible(true);
            return false;
        }
        return true;
    }


}
