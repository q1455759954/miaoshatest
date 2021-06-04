package com.example.miaoshatest.btc.trade;

import cn.hutool.core.date.StopWatch;
import cn.hutool.json.JSONUtil;
import com.example.miaoshatest.btc.AmountBean;
import com.example.miaoshatest.btc.HttpHelper;
import com.example.miaoshatest.btc.trade.helper.ApiSignature;
import com.example.miaoshatest.btc.trade.helper.UrlParamsBuilder;

import java.util.ArrayList;
import java.util.List;

import static com.example.miaoshatest.btc.trade.TradeSystem.*;

public class TrendTracking {

    public UrlParamsBuilder paramsBuilder = null;

    public double maxPrice = 0;
    public double minPrice = 99999;
    public double curMaxPrice;
    public double curMinPrice;
    public double prePrice;
    /**
     * 上涨趋势为true
     */
    public boolean flag = true;
    public double div = 3;
    List<Double> high = new ArrayList<>();
    List<Double> low = new ArrayList<>();

    public void analysisData(){
        TradeSystem system = new TradeSystem();
        AmountBean amount = system.getAmount();
        double price = amount.getData().get(0).getClose();
        if (flag && price > prePrice + div){
            //上涨
            prePrice = price;
        }else if (flag && price + div < prePrice){
            //上涨转下跌
            high.add(price);
            prePrice = price;
            flag = false;
            if (price > maxPrice){
                maxPrice = price;
            }
        }else if (!flag && price + div < prePrice){
            //下跌
            prePrice = price;
        }else if (!flag && price > prePrice + div){
            //下跌转上涨
            low.add(price);
            prePrice = price;
            flag = true;
            if (price < minPrice){
                minPrice = price;
            }
        }

    }


    /**
     * 实时价格
     */
    public AmountBean getAmount() {
        try {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            String t = String.valueOf(System.currentTimeMillis() / 1000);
            String result = HttpHelper.sendGetRequest(AMOUNT + t + "&to=" + t);
            stopWatch.stop();
            if (stopWatch.getTotalTimeMillis() > 1500) {
                getAmount();
            }
            return JSONUtil.toBean(result, AmountBean.class);
        } catch (Exception e) {
            return getAmount();
        }
    }

    public String getResult(String json, String method) {
        int i = 0;
        while (i < 3) {
            try {
                paramsBuilder = new UrlParamsBuilder();
                String requestUrl = HTTPS + HOST + method;
                new ApiSignature().createSignature(ApiSignature.API_KEY, ApiSignature.SECRET_KEY, "POST", HOST, method, paramsBuilder);
                requestUrl += paramsBuilder.buildUrl();
                String result = HttpHelper.sendPostRequest(requestUrl, json);
                System.out.println(result);
                return result;
            } catch (Exception e) {
                i++;
            }
        }
        return null;
    }


}
