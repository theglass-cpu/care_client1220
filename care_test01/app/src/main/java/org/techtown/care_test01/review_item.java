package org.techtown.care_test01;

public class review_item {

    String cs_id;
    String cl_id;
    String review_index;
    String review_rating;
    String review_msg;
    String review_data;
    String review_status;

    public review_item(String cs_id, String cl_id, String review_index, String review_rating, String review_msg, String review_data, String review_status) {
        this.cs_id = cs_id;
        this.cl_id = cl_id;
        this.review_index = review_index;
        this.review_rating = review_rating;
        this.review_msg = review_msg;
        this.review_data = review_data;
        this.review_status = review_status;
    }

    public String getCs_id() {
        return cs_id;
    }

    public void setCs_id(String cs_id) {
        this.cs_id = cs_id;
    }

    public String getCl_id() {
        return cl_id;
    }

    public void setCl_id(String cl_id) {
        this.cl_id = cl_id;
    }

    public String getReview_index() {
        return review_index;
    }

    public void setReview_index(String review_index) {
        this.review_index = review_index;
    }

    public String getReview_rating() {
        return review_rating;
    }

    public void setReview_rating(String review_rating) {
        this.review_rating = review_rating;
    }

    public String getReview_msg() {
        return review_msg;
    }

    public void setReview_msg(String review_msg) {
        this.review_msg = review_msg;
    }

    public String getReview_data() {
        return review_data;
    }

    public void setReview_data(String review_data) {
        this.review_data = review_data;
    }

    public String getReview_status() {
        return review_status;
    }

    public void setReview_status(String review_status) {
        this.review_status = review_status;
    }
}
