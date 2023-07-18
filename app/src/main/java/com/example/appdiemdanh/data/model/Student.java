package com.example.appdiemdanh.data.model;
import java.io.Serializable;

public class Student extends ApiResponse implements Serializable {

    private int id;
    private String uid;
    private String name;
    private String gender;
    private String mshs;
    private String mobile;
    private String avatar;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMshs() {
        return mshs;
    }

    public void setMshs(String mshs) {
        this.mshs = mshs;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


}
