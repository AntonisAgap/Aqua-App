package main;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class Agent {
    private String id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
    private ArrayList<String> hasClients;

    public Agent(){
    }

    public Agent(String id,String firstName,String lastName,String phoneNumber, String address, ArrayList<String> hasClients){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.hasClients = hasClients;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ArrayList<String> getHasClients() {
        return hasClients;
    }

    public void setHasClients(ArrayList<String> hasClients) {
        this.hasClients = hasClients;
    }

    public DefaultTableModel toData(DefaultTableModel hasClientsModel){
        for (String client : hasClients){
            List<String> row = new ArrayList<>();
            row.add(client);
            hasClientsModel.addRow(row.toArray());
        }
        return hasClientsModel;
    }

    public Object[] toRow(){
        List<String> row = new ArrayList<>();
        row.add(id);
        row.add(firstName);
        row.add(lastName);
        row.add(phoneNumber);
        row.add(address);
        return row.toArray();
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}
