package com.example.dan.opsctask2;

import java.util.Date;

/**
 * Created by Dan on 6/12/2018.
 */

public class LogWeight {

    private Date date;
    private int weight;

    public LogWeight(Date date, int weight) {
        this.date = date;
        this.weight = weight;
    }

    public Date getDate() {
        return date;
    }

    public int getWeight() {
        return weight;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
