package main;

public class Promotion {
    private String month;
    private String details;

    public Promotion(String month,String details){
        this.month = month;
        this.details = details;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
