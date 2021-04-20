package main;

import javax.swing.*;

public class AgentsFrame extends JFrame{
    private final JTable agentsTable;

    public AgentsFrame(JTable agentsTable){
        this.agentsTable = agentsTable;
    }

    public void prepareUI(){
        JScrollPane agentsScrollPane = new JScrollPane(agentsTable);
        this.add(agentsScrollPane);
    }
}
