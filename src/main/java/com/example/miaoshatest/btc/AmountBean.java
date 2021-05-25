package com.example.miaoshatest.btc;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AmountBean implements Serializable {
    /**
     * ch : market.BTC-USD.kline.1min
     * ts : 1621504771978
     * status : ok
     * data : [{"id":1621504740,"open":39953.7,"close":39997.8,"low":39948.1,"high":40031,"amount":62.27681891437142,"vol":24904,"count":229}]
     */

    private String ch;
    private long ts;
    private String status;
    private List<DataBean> data;

    @Data
    public static class DataBean implements Serializable {
        /**
         * id : 1621504740
         * open : 39953.7
         * close : 39997.8
         * low : 39948.1
         * high : 40031
         * amount : 62.27681891437142
         * vol : 24904
         * count : 229
         */

        private int id;
        private double open;
        private double close;
        private double low;
        private double high;
        private double amount;
        private double vol;
        private double count;
    }
}
