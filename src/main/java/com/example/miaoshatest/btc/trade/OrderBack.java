package com.example.miaoshatest.btc.trade;

import com.example.miaoshatest.btc.AmountBean;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class OrderBack {

    private String status;

    private List<DataBean> data;

    @Data
    public static class DataBean implements Serializable {

        private String contract_code;

        private Long order_id;

        private String order_id_str;

        private Long client_order_id;

        private Long ts;
    }

}
