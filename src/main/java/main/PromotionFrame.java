package main;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.event.*;

public class PromotionFrame extends JFrame{
    private  Promotion promotion;
    private JButton deleteBtn;
    private final JButton applyChangesBtn;

    private JLabel monthLbl;
    private JLabel detailsLbl;

    private JTextField monthTf;
    private JTextArea detailsTA;

    private final Border editPromotionPanelBorder;

    public PromotionFrame(Promotion promotion){
        editPromotionPanelBorder = BorderFactory.createLoweredBevelBorder();
        this.promotion = promotion;

        monthLbl = new JLabel("Month");
        detailsLbl = new JLabel("Details");

        monthTf = new JTextField(promotion.getMonth());
        detailsTA = new JTextArea(promotion.getDetails());

        applyChangesBtn = new JButton("Apply changes");

    }

    public void prepareUI(){
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.Y_AXIS));

        JPanel editPromotionPanel = new JPanel();
        editPromotionPanel.setLayout(new GridLayout(2,2));
        editPromotionPanel.setBorder(editPromotionPanelBorder);

        editPromotionPanel.add(monthLbl);
        editPromotionPanel.add(monthTf);
        editPromotionPanel.add(detailsLbl);
        editPromotionPanel.add(detailsTA);

        buttonPanel.add(applyChangesBtn);

        this.add(editPromotionPanel,BorderLayout.CENTER);

        this.add(buttonPanel,BorderLayout.SOUTH);


        applyChangesBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

    }

}
