package org.techtown.care_test01;

public class client_request_item {

    String cl_id;
    String index;
    String write_date;
    String request_date;

    public client_request_item(String cl_id, String index, String write_date, String request_date) {
        this.cl_id = cl_id;
        this.index = index;
        this.write_date = write_date;
        this.request_date = request_date;
    }

    public String getCl_id() {
        return cl_id;
    }

    public void setCl_id(String cl_id) {
        this.cl_id = cl_id;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getWrite_date() {
        return write_date;
    }

    public void setWrite_date(String write_date) {
        this.write_date = write_date;
    }

    public String getRequest_date() {
        return request_date;
    }

    public void setRequest_date(String request_date) {
        this.request_date = request_date;
    }
}
