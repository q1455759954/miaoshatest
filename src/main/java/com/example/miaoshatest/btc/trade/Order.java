package com.example.miaoshatest.btc.trade;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class Order {

    private String contract_code = "BTC-USD";

    private Long client_order_id;

    private Double price;

    private Long volume;

    private String direction;

    private String offset;

    private Integer lever_rate;

    private String order_price_type;

    private Double tp_trigger_price;

    private Double tp_order_price;

    private String tp_order_price_type;

    private Double sl_trigger_price;

    private Double sl_order_price;

    private String sl_order_price_type;


}
