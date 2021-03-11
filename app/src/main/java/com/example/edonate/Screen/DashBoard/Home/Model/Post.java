package com.example.edonate.Screen.DashBoard.Home.Model;

public class Post {

    private String id;
    private String title;
    private String des;
    private String postimg;
    private String uid;
    private String uname;
    private String uimg;
    private String time;
    private String utype;

    public String getUtype() {
        return utype;
    }

    public Post(){}

    public Post(String id, String title, String des, String postimg, String uid, String uname, String uimg, String time, String utype) {
        this.id = id;
        this.title = title;
        this.des = des;
        this.postimg = postimg;
        this.uid = uid;
        this.uname = uname;
        this.uimg = uimg;
        this.time = time;
        this.utype = utype;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getPostimg() {
        return postimg;
    }

    public void setPostimg(String postimg) {
        this.postimg = postimg;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUimg() {
        return uimg;
    }

    public void setUimg(String uimg) {
        this.uimg = uimg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
