package main;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.tree.DefaultMutableTreeNode;

public class ProductFrame extends JFrame{
    private Product product;

    private JButton deleteBtn;
    private JButton applyChangesBtn;

    private JLabel dbIdLbl;
    private JLabel idLbl;
    private JLabel nameLbl;
    private JLabel categoryIDLbl;
    private JLabel barcodeLbl;
    private JLabel priceLbl;

    private JTextField dbIdTf;
    private JTextField idTf;
    private JTextField nameTf;
    private JTextField categoryIDTf;
    private JTextField barcodeTf;
    private JTextField priceTf;

    private Border editProductPanelBorder;
    private DefaultMutableTreeNode selectedNode;

    public ProductFrame(Product product, DefaultMutableTreeNode selectedNode){
        editProductPanelBorder = BorderFactory.createLoweredBevelBorder();
        this.product = product;
        this.selectedNode = selectedNode;

        dbIdLbl = new JLabel("Database ID");
        idLbl = new JLabel("Product ID");
        nameLbl = new JLabel("Name");
        categoryIDLbl = new JLabel("Category ID");
        barcodeLbl = new JLabel("Barcode");
        priceLbl = new JLabel("Price");

        dbIdTf = new JTextField(product.getId());
        dbIdTf.setEditable(false);
        idTf = new JTextField(product.getAquaID());
        nameTf = new JTextField(product.getName());
        categoryIDTf = new JTextField(product.getCategoryID());
        categoryIDTf.setEditable(false);
        barcodeTf = new JTextField(product.getBarcode());
        priceTf = new JTextField(Double.toString(product.getPrice()));

        deleteBtn = new JButton("Delete product");
        applyChangesBtn = new JButton("Apply changes");

    }

    public void prepareUI(){
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.Y_AXIS));

        JPanel editOrderPanel = new JPanel();
        editOrderPanel.setLayout(new GridLayout(6,2));
        editOrderPanel.setBorder(editProductPanelBorder);

        editOrderPanel.add(dbIdLbl);
        editOrderPanel.add(dbIdTf);
        editOrderPanel.add(idLbl);
        editOrderPanel.add(idTf);
        editOrderPanel.add(nameLbl);
        editOrderPanel.add(nameTf);
        editOrderPanel.add(barcodeLbl);
        editOrderPanel.add(barcodeTf);
        editOrderPanel.add(categoryIDLbl);
        editOrderPanel.add(categoryIDTf);
        editOrderPanel.add(priceLbl);
        editOrderPanel.add(priceTf);

        buttonPanel.add(applyChangesBtn);
        buttonPanel.add(deleteBtn);

        this.add(editOrderPanel,BorderLayout.CENTER);

        this.add(buttonPanel,BorderLayout.SOUTH);


        applyChangesBtn.addActionListener(e -> {
            product.setAquaID(idTf.getText());
            product.setBarcode(barcodeTf.getText());
            product.setCategoryID(categoryIDTf.getText());
            product.setName(nameTf.getText());
            product.setPrice(Double.parseDouble(priceTf.getText()));
            MainFrame.updateProduct(product);
            dispose();
        });

        deleteBtn.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to" +
                            " delete "+product.getName()+" ?",
                    "Confirmation", JOptionPane.OK_CANCEL_OPTION);
            if (result == 0)
                MainFrame.deleteProduct(product,selectedNode);
            dispose();
        });

    }

}
