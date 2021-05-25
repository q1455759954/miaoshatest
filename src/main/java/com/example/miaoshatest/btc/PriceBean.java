package com.example.miaoshatest.btc;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PriceBean implements Serializable {
    /**
     * status : ok
     * data : [{"index_price":56823.24,"index_ts":1620830157012,"contract_code":"BTC-USD"}]
     * ts : 1620830158976
     */

    private String status;
    private long ts;
    private List<DataBean> data;

    @Data
    public static class DataBean implements Serializable {
        /**
         * index_price : 56823.24
         * index_ts : 1620830157012
         * contract_code : BTC-USD
         */

        private double index_price;
        private long index_ts;
        private String contract_code;
    }
}
