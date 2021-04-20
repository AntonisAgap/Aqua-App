package main;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;

public class CategoryFrame extends JFrame{
    private Category category;
    private int index;

    private JButton deleteBtn;
    private JButton applyChangesBtn;

    private JLabel idLbl;
    private JLabel nameLbl;
    private JLabel parentIDLbl;

    private JTextField idTf;
    private JTextField nameTf;
    private JTextField parentIDTf;

    private Border editClientPanelBorder;

    public CategoryFrame(Category category){
        editClientPanelBorder = BorderFactory.createLoweredBevelBorder();
        this.category = category;

        idLbl = new JLabel("Category ID");
        nameLbl = new JLabel("Category name");
        parentIDLbl = new JLabel("Parent ID");

        idTf = new JTextField(category.getId());
        idTf.setEditable(false);
        nameTf = new JTextField(category.getName());
        parentIDTf = new JTextField(category.getParentID());
        parentIDTf.setEditable(false
        );
        deleteBtn = new JButton("Delete category");
        applyChangesBtn = new JButton("Apply changes");

    }

    public void prepareUI(){
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.Y_AXIS));

        JPanel editClientPanel = new JPanel();
        editClientPanel.setLayout(new GridLayout(3,2));
        editClientPanel.setBorder(editClientPanelBorder);

        editClientPanel.add(idLbl);
        editClientPanel.add(idTf);
        editClientPanel.add(nameLbl);
        editClientPanel.add(nameTf);
        editClientPanel.add(parentIDLbl);
        editClientPanel.add(parentIDTf);

        buttonPanel.add(applyChangesBtn);
        buttonPanel.add(deleteBtn);

        this.add(editClientPanel,BorderLayout.CENTER);

        this.add(buttonPanel,BorderLayout.SOUTH);


        applyChangesBtn.addActionListener(e -> {
            category.setName(nameTf.getText());
            MainFrame.updateCategory(category);
            dispose();
        });


    }

}
