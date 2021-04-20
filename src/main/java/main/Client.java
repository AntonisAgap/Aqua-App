package main;

import java.util.ArrayList;
import java.util.List;

public class Client {
    private String id;
    private String companyName;
    private String tin;
    private String phoneNumber;
    private String address;
    private String town;
    private String typeOfBusiness;

    public Client() {}

    public Client(String id, String companyName, String phoneNumber,String address,String town,String tin,String typeOfBusiness){
        this.id = id;
        this.companyName = companyName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.town = town;
        this.tin = tin;
        this.typeOfBusiness = typeOfBusiness;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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

    public String getTin() {
        return tin;
    }

    public void setTin(String tin) {
        this.tin = tin;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getTypeOfBusiness() {
        return typeOfBusiness;
    }

    public void setTypeOfBusiness(String typeOfBusiness) {
        this.typeOfBusiness = typeOfBusiness;
    }

    public Object[] toRow(){
        List<String> row = new ArrayList<String>();
        row.add(id);
        row.add(companyName);
        row.add(tin);
        row.add(address);
        row.add(town);
        row.add(phoneNumber);
        row.add(typeOfBusiness);
        return row.toArray();
    }

    @Override
    public String toString() {
        return companyName;
    }
}

