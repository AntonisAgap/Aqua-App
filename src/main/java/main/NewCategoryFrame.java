package main;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class NewCategoryFrame extends JFrame{

    //declare member variables
    private JLabel addCategoryLbl;

    private JLabel nameLbl;
    private JLabel parentIDLbl;

    private JTextField nameTf;
    private JTextField parentIDTf;

    private JButton completeCategoryBtn;
    private static JLabel errorLbl;

    private Font font;

    private EmptyBorder addCategoryLblBorder;
    private Border addCategoryPanelBorder;

    public NewCategoryFrame (String parentID) {
        super();

        font = new Font("Arial", Font.BOLD,15);

        addCategoryLblBorder = new EmptyBorder(3, 0, 10, 0);
        addCategoryPanelBorder = BorderFactory.createLoweredBevelBorder();

        addCategoryLbl = new JLabel("Add new category\n");
        addCategoryLbl.setBorder(addCategoryLblBorder);
        addCategoryLbl.setFont(font);

        nameLbl = new JLabel("Category name");
        parentIDLbl = new JLabel("Parent Category");

        nameTf = new JTextField();
        parentIDTf = new JTextField();
        parentIDTf.getText();
        parentIDTf.setText(parentID);
        parentIDTf.setEditable(false);

        completeCategoryBtn = new JButton("Add category");

        errorLbl = new JLabel();
        errorLbl.setForeground(Color.RED);
    }

    public void prepareUI() {

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));

        JPanel addProductPanel = new JPanel();

        addProductPanel.setLayout(new GridLayout(2,2));
        addProductPanel.setBorder(addCategoryPanelBorder);

        addProductPanel.add(nameLbl);
        addProductPanel.add(nameTf);
        addProductPanel.add(parentIDLbl);
        addProductPanel.add(parentIDTf);

        addCategoryLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        completeCategoryBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        errorLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel.add(addCategoryLbl);
        mainPanel.add(addProductPanel);
        mainPanel.add(completeCategoryBtn);
        mainPanel.add(errorLbl);

        this.add(mainPanel);

        completeCategoryBtn.addActionListener(e -> {
            String name = nameTf.getText();
            String parentID = parentIDTf.getText();

            if (_checkInput(name,parentID)) {
                Category category = new Category("idnotSet",name,parentID);
                MainFrame.addCategoryToDb(category);
                MainFrame.addCategory(category);
                dispose();
            }
        });

    }

    private boolean _checkInput(String name,String parentID){

        errorLbl.setVisible(false);

        if (name.isEmpty()) {
            errorLbl.getText();
            errorLbl.setText("Name field is empty");
            errorLbl.setVisible(true);
            return false;
        }
        if (parentID.isEmpty()) {
            errorLbl.getText();
            errorLbl.setText("Parent ID name is empty");
            errorLbl.setVisible(true);
            return false;
        }
        return true;
    }


}
