package org.techtown.care_test01;

public class chat_msg {

    String user;
    String msg;
    private int viewType;

    public chat_msg(String user, String msg) {
        this.user = user;
        this.msg = msg;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getViewType() {
        return viewType;
    }
}
