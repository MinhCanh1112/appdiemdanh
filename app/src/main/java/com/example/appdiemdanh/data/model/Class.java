package com.example.appdiemdanh.data.model;

import java.io.Serializable;

public class Class extends ApiResponse implements Serializable {
    private String lop;
    private int siso;
    private int dadiemdanh;
    private int vang;
    private int vaotre;
    private int vesom;

    public int getVesom() {
        return vesom;
    }

    public void setVesom(int vesom) {
        this.vesom = vesom;
    }

    public int getVaotre() {
        return vaotre;
    }

    public void setVaotre(int vaotre) {
        this.vaotre = vaotre;
    }

    public int getDadiemdanh() {
        return dadiemdanh;
    }

    public void setDadiemdanh(int dadiemdanh) {
        this.dadiemdanh = dadiemdanh;
    }

    public int getVang() {
        return vang;
    }

    public void setVang(int vang) {
        this.vang = vang;
    }

    public String getLop() {
        return lop;
    }

    public void setLop(String lop) {
        this.lop = lop;
    }

    public int getSiso() {
        return siso;
    }

    public void setSiso(int siso) {
        this.siso = siso;
    }


}
