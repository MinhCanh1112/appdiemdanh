package com.example.appdiemdanh.data.model;

import java.io.Serializable;

public class DateTimediemdanh extends Attendance implements Serializable {
    private int idngaydd;
    private int idsetupdd;
    private String timestart;
    private String timevao;
    private String timera;
    private String timevesom;


    public String getTimestart() {
        return timestart;
    }

    public void setTimestart(String timestart) {
        this.timestart = timestart;
    }



    public int getIdsetupdd() {
        return idsetupdd;
    }

    public void setIdsetupdd(int idsetupdd) {
        this.idsetupdd = idsetupdd;
    }

    public String getTimevesom() {
        return timevesom;
    }

    public void setTimevesom(String timevesom) {
        this.timevesom = timevesom;
    }

    public int getIdngaydd() {
        return idngaydd;
    }

    public void setIdngaydd(int idngaydd) {
        this.idngaydd = idngaydd;
    }


    public String getTimevao() {
        return timevao;
    }

    public void setTimevao(String timevao) {
        this.timevao = timevao;
    }

    public String getTimera() {
        return timera;
    }

    public void setTimera(String timera) {
        this.timera = timera;
    }


}
