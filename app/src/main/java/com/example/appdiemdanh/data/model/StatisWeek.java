package com.example.appdiemdanh.data.model;

import java.io.Serializable;

public class StatisWeek extends DateTimediemdanh implements Serializable {
    private int idhs;
    private int idweek;
    private String week;
    private String year;
    private int vaotre;
    private int vang;
    private int vesom;
    private int solandd;

    public int getIdweek() {
        return idweek;
    }

    public void setIdweek(int idweek) {
        this.idweek = idweek;
    }

    public int getSolandd() {
        return solandd;
    }

    public void setSolandd(int solandd) {
        this.solandd = solandd;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public int getVaotre() {
        return vaotre;
    }

    public void setVaotre(int vaotre) {
        this.vaotre = vaotre;
    }

    public int getVang() {
        return vang;
    }

    public void setVang(int vang) {
        this.vang = vang;
    }

    public int getVesom() {
        return vesom;
    }

    public void setVesom(int vesom) {
        this.vesom = vesom;
    }

    public int getIdhs() {
        return idhs;
    }

    public void setIdhs(int idhs) {
        this.idhs = idhs;
    }
}
