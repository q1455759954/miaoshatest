package com.example.miaoshatest.btc.trade;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Order {

    private String contract_code = "BTC-USD";

    private long client_order_id;

    private double price;

    private long volume;

    private String direction;

    private String offset;

    private int lever_rate;

    private String order_price_type;

    private double tp_trigger_price;

    private double tp_order_price;

    private String tp_order_price_type;

    private double sl_trigger_price;

    private double sl_order_price;

    private String sl_order_price_type;


}
