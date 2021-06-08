package com.example.miaoshatest.btc.trade;

import cn.hutool.core.date.StopWatch;
import cn.hutool.json.JSONUtil;
import com.example.miaoshatest.btc.AmountBean;
import com.example.miaoshatest.btc.HttpHelper;
import com.example.miaoshatest.btc.trade.helper.ApiSignature;
import com.example.miaoshatest.btc.trade.helper.UrlParamsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.example.miaoshatest.btc.trade.TradeSystem.*;

@Service
public class GoodLuckTrend {

    @Autowired
    public TradeUsdt test;

    public UrlParamsBuilder paramsBuilder = null;

    public double maxPrice = 0;
    public double minPrice = 99999;
    public double curMaxPrice;
    public double curMinPrice;
    public double prePrice;
    public double flagRate = 2.0;
    public boolean buy = false;
    public boolean sell = false;
    /**
     * 上涨趋势为true
     */
    public boolean flag = true;
    public double div = 3;
    List<Double> high = new ArrayList<>();
    List<Double> low = new ArrayList<>();

    public void analysisData() throws Exception {
        AmountBean amount = test.getAmount();
        double price = amount.getData().get(0).getClose();
        System.out.println("现在价格："+price);
        prePrice = price;
        while (true){
            amount = test.getAmount();
            price = amount.getData().get(0).getClose();
            System.out.println("现在价格："+price);
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
            findResult(price);
            if (test.fuckNum>=2){
                return;
            }
            Thread.sleep(1000);
        }
    }

    private void findResult(double price) throws Exception {
        curMaxPrice = 0;
        curMinPrice = 99999;
        int max = 0, min= 0;
        int i = high.size()-1;
        int j = low.size()-1;
        for (;i>=0&&j>=0;j--,i--){
            if (high.get(i) > curMaxPrice){
                curMaxPrice = high.get(i);
                max = i;
            }
            if (low.get(j) < curMinPrice){
                curMinPrice = low.get(j);
                min = j;
            }
            if ((curMaxPrice - curMinPrice)/curMinPrice > 0.02){
                //触发
                if (max > min){
                    //买多
                    System.out.println("触发多单买入，价格："+price);
                    test.curPrice=price;
                    test.buyPrice=price;
                    test.buy();
                    high.clear();
                    low.clear();
                    break;
                }else {
                    //买空
                    System.out.println("触发空单买入，价格："+price);
                    test.curPrice=price;
                    test.buyPrice=price;
                    test.sell();
                    high.clear();
                    low.clear();
                    break;
                }
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
