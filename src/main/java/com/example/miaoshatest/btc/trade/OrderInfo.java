package com.example.miaoshatest.btc.trade;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class OrderInfo implements Serializable {
    /**
     * status : ok
     * data : [{"symbol":"THETA","contract_code":"THETA-USD","volume":1,"price":0.6,"order_price_type":"post_only","order_type":1,"direction":"buy","offset":"open","lever_rate":20,"order_id":771043577949732864,"client_order_id":null,"created_at":1603872714279,"trade_volume":0,"trade_turnover":0,"fee":0,"trade_avg_price":null,"margin_frozen":0.8333333333333334,"profit":0,"status":3,"order_source":"api","order_id_str":"771043577949732864","fee_asset":"THETA","liquidation_type":"0","canceled_at":0,"real_profit":0,"is_tpsl":0}]
     * ts : 1603872729321
     */

    private String status;
    private long ts;
    private List<DataBean> data;

    @Data
    public static class DataBean implements Serializable {
        /**
         * symbol : THETA
         * contract_code : THETA-USD
         * volume : 1
         * price : 0.6
         * order_price_type : post_only
         * order_type : 1
         * direction : buy
         * offset : open
         * lever_rate : 20
         * order_id : 771043577949732864
         * client_order_id : null
         * created_at : 1603872714279
         * trade_volume : 0
         * trade_turnover : 0
         * fee : 0
         * trade_avg_price : null
         * margin_frozen : 0.8333333333333334
         * profit : 0
         * status : 3
         * order_source : api
         * order_id_str : 771043577949732864
         * fee_asset : THETA
         * liquidation_type : 0
         * canceled_at : 0
         * real_profit : 0
         * is_tpsl : 0
         */

        private String symbol;
        private String contract_code;
        private double volume;
        private double price;
        private String order_price_type;
        private double order_type;
        private String direction;
        private String offset;
        private double lever_rate;
        private long order_id;
        private long client_order_id;
        private long created_at;
        private double trade_volume;
        private double trade_turnover;
        private double fee;
        private double trade_avg_price;
        private double margin_frozen;
        private double profit;
        private double status;
        private String order_source;
        private String order_id_str;
        private String fee_asset;
        private String liquidation_type;
        private double canceled_at;
        private double real_profit;
        private double is_tpsl;
    }
}
