package main;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class LoadFrame {
    private JFrame frame;
    private JLabel label;
    private JLabel loadingLbl;
    public Icon icon;
    private URL url;

    public LoadFrame() {
        initGUI();
    }

    private void initGUI() {
        frame = new JFrame("Loader"); // windows title is Loader, change
        frame.setUndecorated(true);
        // it if you don't like it!

        try {
            url = new URL("https://i.pinimg.com/originals/ce/ca/e6/cecae62ec79ddc1d9d95c3131510f3e6.gif");
//            url = new URL("https://media3.giphy.com/media/depb1Ex1DUbsY/giphy.gif");

        } catch (Exception e) { // do nothing or just print a message }
            System.out.println("Couldnt load image");
        }

        icon = new ImageIcon(url);
        label = new JLabel(icon);
        loadingLbl = new JLabel(" Loading data...");
        frame.setSize(300, 200);
        frame.add(label,BorderLayout.CENTER);
        frame.add(loadingLbl,BorderLayout.SOUTH);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
//        frame.setBackground(new Color(0,0,0,55));
        frame.setVisible(true);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void dispose(){
        frame.dispose();
    }

}