package org.techtown.care_test01;

public class use_cs_item {

    String cs_id;
    String cs_index;
    String cs_review;
    String cs_name;

    public use_cs_item(String cs_id, String cs_index, String cs_review, String cs_name) {
        this.cs_id = cs_id;
        this.cs_index = cs_index;
        this.cs_review = cs_review;
        this.cs_name = cs_name;
    }

    public String getCs_id() {
        return cs_id;
    }

    public void setCs_id(String cs_id) {
        this.cs_id = cs_id;
    }

    public String getCs_index() {
        return cs_index;
    }

    public void setCs_index(String cs_index) {
        this.cs_index = cs_index;
    }

    public String getCs_review() {
        return cs_review;
    }

    public void setCs_review(String cs_review) {
        this.cs_review = cs_review;
    }

    public String getCs_name() {
        return cs_name;
    }

    public void setCs_name(String cs_name) {
        this.cs_name = cs_name;
    }
}