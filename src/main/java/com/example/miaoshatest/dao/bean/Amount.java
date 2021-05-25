package com.example.miaoshatest.dao.bean;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Amount {

    private int id;
    private String time;
    private double open;
    private double close;
    private double low;
    private double high;
    private double amount;
    private double vol;
    private double count;

}
