package org.techtown.care_test01;

public class marker {
    String name;
    String roadAddress;
    String tell;
    double x;
    double y;




    public marker(String name, String roadAddress, String tell, double x, double y ) {
        this.name = name;
        this.roadAddress = roadAddress;
        this.tell = tell;
        this.x = x;
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoadAddress() {
        return roadAddress;
    }

    public void setRoadAddress(String roadAddress) {
        this.roadAddress = roadAddress;
    }

    public String getTell() {
        return tell;
    }

    public void setTell(String tell) {
        this.tell = tell;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }




}
