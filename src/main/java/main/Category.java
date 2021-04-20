package main;

import java.util.ArrayList;

public class Category {
    String id;
    String name;
    String parentID;
    ArrayList<Category> subcategoriesList = new ArrayList<>();
    ArrayList<Product> productsList = new ArrayList<>();
    String fullBranch;

    public Category() {}

    public Category(String id,String name, String parentID){
        this.id = id;
        this.name = name;
        this.parentID = parentID;
    }

    public String getId() {
        return id;
    }

    public String getFullBranch() {
        return fullBranch;
    }

    public void setFullBranch(String fullBranch) {
        this.fullBranch = fullBranch;
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

    public String getParentID() {
        return parentID;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    public void insertSubcategory(Category subcategory){
        subcategoriesList.add(subcategory);
    }

    public void insertProduct(Product product){
        productsList.add(product);
    }

    public ArrayList<Category> getSubcategoriesList() {
        return subcategoriesList;
    }

    public void setSubcategoriesList(ArrayList<Category> subcategoriesList) {
        this.subcategoriesList = subcategoriesList;
    }

    public ArrayList<Product> getProductsList() {
        return productsList;
    }

    public void setProductsList(ArrayList<Product> productsList) {
        this.productsList = productsList;
    }

    @Override
    public String toString() {
        return name;
    }
}
