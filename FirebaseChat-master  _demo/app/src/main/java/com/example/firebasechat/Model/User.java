package com.example.firebasechat.Model;

public class User {

    //在此宣告的變數名稱必須與建構Database的標籤名稱相同(大小寫一致),否則取不出資料
    private String id;
    private String username;
    private String userClass;
    private String imageURL;
    private String status;
    private String search;

    public User(String id, String username,String userClass , String imageURL, String status, String search) {
        this.id = id;
        this.username = username;
        this.userClass = userClass;
        this.imageURL = imageURL;
        this.status = status;
        this.search = search;
    }

    public User(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserClass() {
        return userClass;
    }

    public void setUserClass(String userClass) {
        this.userClass = userClass;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
