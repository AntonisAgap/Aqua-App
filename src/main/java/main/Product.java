package main;

import java.util.ArrayList;
import java.util.List;

public class Product {
    private String id;
    private String name;
    private String categoryID;
    private String barcode;
    private int amount;
    private String branch;
    private String aquaID;
    private double price;

    public Product(String id, String name, String categoryID, String barcode,double price){
        this.id = id;
        this.name = name;
        this.categoryID = categoryID;
        this.barcode = barcode;
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public String getAquaID() {
        return aquaID;
    }

    public void setAquaID(String aquaID) {
        this.aquaID = aquaID;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }


    public Object[] toRow(){
        List<String> row = new ArrayList<String>();
        row.add(id);
        row.add(name);
        row.add(branch);
        row.add(barcode);
        return row.toArray();
    }

    @Override
    public String toString() {
        return name;
    }
}
