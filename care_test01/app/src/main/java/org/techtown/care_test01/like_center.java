package org.techtown.care_test01;

public class like_center {

    String c_name;
    String c_roadAddress;
    String c_tell;
    double c_x;
    double c_y;

    public like_center(String c_name, String c_roadAddress, String c_tell, double c_x, double c_y) {
        this.c_name = c_name;
        this.c_roadAddress = c_roadAddress;
        this.c_tell = c_tell;
        this.c_x = c_x;
        this.c_y = c_y;
    }

    public String getC_name() {
        return c_name;
    }

    public void setC_name(String c_name) {
        this.c_name = c_name;
    }

    public String getC_roadAddress() {
        return c_roadAddress;
    }

    public void setC_roadAddress(String c_roadAddress) {
        this.c_roadAddress = c_roadAddress;
    }

    public String getC_tell() {
        return c_tell;
    }

    public void setC_tell(String c_tell) {
        this.c_tell = c_tell;
    }

    public double getC_x() {
        return c_x;
    }

    public void setC_x(double c_x) {
        this.c_x = c_x;
    }

    public double getC_y() {
        return c_y;
    }

    public void setC_y(double c_y) {
        this.c_y = c_y;
    }
}
