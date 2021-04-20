package main;

import org.apache.commons.lang3.StringUtils;

import javax.swing.table.DefaultTableModel;
import java.util.*;

// TODO: Do it with HashMap hasProducts
public class Order {
  private String id;
  private String client;
  private String agent;
  private String date;
  private LinkedHashMap<String, Integer> hasProducts = new LinkedHashMap<>();
  private int isOrder; //0 for order,1 for pending,2 for done
  private String doneDate;
  private String notes;

  public Order(){}

  public Order(String id, String client, String agent, String date,int isOrder,String notes,String doneDate){
      this.id = id;
      this.client = client;
      this.agent = agent;
      this.date = StringUtils.left(date, 10);
      this.isOrder = isOrder;
      this.notes = notes;
      this.doneDate = doneDate;
  }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDoneDate() {
        return doneDate;
    }

    public void setDoneDate(String doneDate) {
        this.doneDate = doneDate;
    }

    public int getIsOrder() {
        return isOrder;
    }

    public void setIsOrder(int isOrder) {
        this.isOrder = isOrder;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public LinkedHashMap<String, Integer> getHasProducts() {
        return hasProducts;
    }

    public void setHasProducts(LinkedHashMap<String, Integer> hasProducts) {
        this.hasProducts = hasProducts;
    }

    public void insertProduct(String productName,String productAmount){
      try {
          hasProducts.put(productName, Integer.parseInt(productAmount));
      }catch (Exception e) {
          System.out.println(e);
      }
    }

    public Object[] toRow(){
        List<String> row = new ArrayList<String>();
        row.add(id);
        row.add(client);
        row.add(agent);
        row.add(date);
        return row.toArray();
    }

    public Object[] toRowDone(){
        List<String> row = new ArrayList<String>();
        row.add(id);
        row.add(client);
        row.add(agent);
        row.add(date);
        row.add(this.doneDate);
        return row.toArray();
    }

    public DefaultTableModel toData(DefaultTableModel orderModel){
        for (HashMap.Entry<String, Integer> entry : hasProducts.entrySet()) {
            List<String> row = new ArrayList<String>();
            String key = entry.getKey();
            Integer value = entry.getValue();
            row.add(key);
            row.add(value.toString());
            orderModel.addRow(row.toArray());
        }
      return orderModel;
    }

    public String toHTML() {
        String htmlListItem;
        StringBuilder htmlString = new StringBuilder("<h1 style=\"font-size:90%;\" ><u>Order ID: " + this.id + "</u></h1>" +
                "<p style=\"font-size:90%;\"><b><em>Client:</em></b> " + this.client +
                "<br><b><em>Agent:</em></b> " + this.agent +
                "<br><b><em>Date:</em></b> " + this.date +
                " <ol style=\"font-size:90%;\" >\n");
                for (HashMap.Entry<String, Integer> entry : hasProducts.entrySet()) {
                    String key = entry.getKey();
                    Integer value = entry.getValue();
                    htmlListItem = "<li>" + key + ": " + value + "</li>";
                    htmlString.append(htmlListItem);
                }
                htmlString.append("</ol> <br><b><em>Notes: </em></b>").append(this.notes).append("</p>");
      return htmlString.toString();
    }

}
