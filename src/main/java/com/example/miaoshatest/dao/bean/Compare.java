package com.example.miaoshatest.dao.bean;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Compare {

    private String time;
    private double close;
    private double div;
    private double rate = 0.0000;
    private double thisDiv;
    private double divPrice;

}
