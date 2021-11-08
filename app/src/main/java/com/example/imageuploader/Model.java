package com.example.imageuploader;

public class Model {

    private String imageUrl;
    private String title;
    private String Enddate;
    private String EndTime;
    private String category;
    private String StartPrice;
    private String EndPrice;
    private String PricePoint;

    public Model(){

    }
    public Model(String imageUrl, String title, String enddate, String endTime, String category, String startPrice, String endPrice, String pricePoint){
        this.imageUrl = imageUrl;
        this.title = title;
        this.Enddate = enddate;
        this.EndTime = endTime;
        this.category = category;
        this.StartPrice = startPrice;
        this.EndPrice = endPrice;
        this.PricePoint = pricePoint;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public String getEndPrice() {
        return EndPrice;
    }

    public String getPricePoint() {
        return PricePoint;
    }

    public String  getStartPrice() {
        return StartPrice;
    }

    public String getEnddate() {
        return Enddate;
    }

    public void setEnddate(String enddate) {
        Enddate = enddate;
    }

    public void setEndPrice(String endPrice) {
        EndPrice = endPrice;
    }

    public void setPricePoint(String pricePoint) {
        PricePoint = pricePoint;
    }

    public void setStartPrice(String startPrice) {
        StartPrice = startPrice;
    }

}
