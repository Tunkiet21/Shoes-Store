package com.example.shoesstore.Domain;

public class Review {
    private String username;
    private String productId; // Thêm thuộc tính productId
    private String productTitle;
    private String comment;
    private float rating;

    public Review() {}

    public Review(String username, String productId, String productTitle, String comment, float rating) {
        this.username = username;
        this.productId = productId; // Khởi tạo productId
        this.productTitle = productTitle;
        this.comment = comment;
        this.rating = rating;
    }

    // Getter và Setter
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
