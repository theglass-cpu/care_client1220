package org.techtown.care_test01;

public class match_item {
    String cs_id;
    String cs_name;
    String cl_id;
    String room_index;
    String cs_resum_index;
    String cl_request_index;

    public match_item(String cs_id, String cs_name, String cl_id, String room_index, String cs_resum_index, String cl_request_index) {
        this.cs_id = cs_id;
        this.cs_name = cs_name;
        this.cl_id = cl_id;
        this.room_index = room_index;
        this.cs_resum_index = cs_resum_index;
        this.cl_request_index = cl_request_index;
    }

    public String getCs_id() {
        return cs_id;
    }

    public void setCs_id(String cs_id) {
        this.cs_id = cs_id;
    }

    public String getCs_name() {
        return cs_name;
    }

    public void setCs_name(String cs_name) {
        this.cs_name = cs_name;
    }

    public String getCl_id() {
        return cl_id;
    }

    public void setCl_id(String cl_id) {
        this.cl_id = cl_id;
    }

    public String getRoom_index() {
        return room_index;
    }

    public void setRoom_index(String room_index) {
        this.room_index = room_index;
    }

    public String getCs_resum_index() {
        return cs_resum_index;
    }

    public void setCs_resum_index(String cs_resum_index) {
        this.cs_resum_index = cs_resum_index;
    }

    public String getCl_request_index() {
        return cl_request_index;
    }

    public void setCl_request_index(String cl_request_index) {
        this.cl_request_index = cl_request_index;
    }
}
