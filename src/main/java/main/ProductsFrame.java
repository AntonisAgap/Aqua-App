package main;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class ProductsFrame extends JFrame{
    private JScrollPane productsScrollPane;
    private JTable productsTable;

    public ProductsFrame(JTable productsTable){

        this.productsTable = productsTable;
    }

    public void prepareUI(){
        productsScrollPane = new JScrollPane(productsTable);
        this.add(productsScrollPane);

        productsTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table =(JTable) mouseEvent.getSource();
                Point point = mouseEvent.getPoint();
                int row = table.rowAtPoint(point);
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
//                    MainFrame.deleteProduct(row);
                }
            }
        });
    }

}
