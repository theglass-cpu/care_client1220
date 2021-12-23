package org.techtown.care_test01;

public class request_item {

    String id;
    String request_index;
    String cs_name;

    public request_item(String id, String request_index, String cs_name) {
        this.id = id;
        this.request_index = request_index;
        this.cs_name = cs_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRequest_index() {
        return request_index;
    }

    public void setRequest_index(String request_index) {
        this.request_index = request_index;
    }

    public String getCs_name() {
        return cs_name;
    }

    public void setCs_name(String cs_name) {
        this.cs_name = cs_name;
    }
}
