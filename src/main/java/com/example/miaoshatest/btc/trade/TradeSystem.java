package com.example.miaoshatest.btc.trade;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.StopWatch;
import cn.hutool.json.JSONUtil;
import com.example.miaoshatest.btc.AmountBean;
import com.example.miaoshatest.btc.HttpHelper;
import com.example.miaoshatest.dao.IAmountDao;
import com.example.miaoshatest.dao.bean.Amount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LGL
 */
@Service
public class TradeSystem {

    public static String ORDER = "https://api.hbdm.com/swap-api/v1/swap_order";
    public static String ORDER_INFO = "https://api.hbdm.com/swap-api/v1/swap_order_info";
    public static String ORDER_TPSL = "https://api.hbdm.com/swap-api/v1/swap_tpsl_order";
    public static String AMOUNT = "https://api.hbdm.com/swap-ex/market/history/kline?contract_code=BTC-USD&period=1min&from=";
    public static String LIGHTNING = "https://api.hbdm.com/swap-api/v1/swap_lightning_close_position";

    public double slRate = 0.99;

    public double winRate;

    public double curPrice;

    public double maxPrice;

    public double buyPrice;

    public String order_id;

    public OrderInfo orderInfo;

//    private Order order = Order.builder()
//            .contract_code("BTC-USD")
//            .volume(1L)
//            .direction("buy")
//            .offset("open")
//            .lever_rate(10)
//            .order_price_type("optimal_5")
//            .sl_trigger_price(curPrice * slRate)
//            .sl_order_price_type("optimal_5")
//            .build();

    /**
     * 开多
     */
    public void buy() throws InterruptedException {
        String jsonStr = JSONUtil.toJsonStr(getSLOrder(curPrice,"buy","open"));
        String result = HttpHelper.sendPostRequest(ORDER, jsonStr);
        OrderBack orderBack = JSONUtil.toBean(result, OrderBack.class);
        setSL(orderBack,"sell");
        setTP(orderBack,"sell");
    }

    /**
     * 开空
     */
    public void sell() throws InterruptedException {
        String jsonStr = JSONUtil.toJsonStr(getSLOrder(curPrice,"sell","open"));
        String result = HttpHelper.sendPostRequest(ORDER, jsonStr);
        OrderBack orderBack = JSONUtil.toBean(result, OrderBack.class);
        setSL(orderBack,"buy");
        setTP(orderBack,"buy");
        System.out.println("平仓成功！");
    }

    @Autowired
    public IAmountDao dao;

    public int curNum;

    /**
     * 实时价格
     */
    public AmountBean getAmount() {
//        StopWatch stopWatch = new StopWatch();
//        stopWatch.start();
//        String t = String.valueOf(System.currentTimeMillis() / 1000);
//        String result = HttpHelper.sendGetRequest(AMOUNT+ t + "&to=" + t);
//        stopWatch.stop();
//        if (stopWatch.getTotalTimeMillis()>1000){
//            return null;
//        }
//        return JSONUtil.toBean(result, AmountBean.class);
        List<Amount> amount = dao.getAmount(curNum, 1);
        curNum+=10;
        AmountBean amountBean = new AmountBean();
        List<AmountBean.DataBean> list = new ArrayList<>();
        AmountBean.DataBean dataBean = new AmountBean.DataBean();
        BeanUtil.copyProperties(amount.get(0),dataBean);
        list.add(dataBean);
        amountBean.setData(list);
        return amountBean;
    }

    /**
     * 设置止盈
     */
    public void setTP(OrderBack orderBack,String direction) throws InterruptedException {
        double price = 0.008 * buyPrice + buyPrice;
        double tpPrice = 0.005 * buyPrice + buyPrice;
        //达到止盈点
        boolean flag = false;
        while (true){
            AmountBean amount = getAmount();
            if (amount==null){
                continue;
            }
            curPrice = amount.getData().get(0).getClose();
            System.out.println("买入价格："+buyPrice+" 现在价格: "+curPrice + " 盈利："+((curPrice-buyPrice)/buyPrice)*100+"%");
            //当前价格大于0.008*买价时记录最高价格设置0.005*买价为止盈
            //之后每升高0.005止盈提高0.005
            if (buyPrice < curPrice){
                 if (curPrice > price){
                     HttpHelper.sendPostRequest(ORDER_TPSL, JSONUtil.toJsonStr(getTPOrder(tpPrice,direction,null)));
                     price = price + buyPrice * 0.005;
                     tpPrice = tpPrice + buyPrice * 0.005;
                     flag = true;
                 }else if (curPrice < tpPrice && flag){
                     //闪电平仓
                     lightning(direction);
                     break;
                 }
            }else if (curPrice < buyPrice * slRate){
                //闪电平仓
                lightning(direction);
                break;
            }
            Thread.sleep(1000);

        }
    }

    /**
     * 闪电平仓
     */
    public void lightning(String direction){
        Order order = Order.builder()
                .contract_code("BTC-USD")
                .volume(1L)
                .direction(direction)
                .order_price_type("lightning")
                .build();

        HttpHelper.sendPostRequest(LIGHTNING, JSONUtil.toJsonStr(order));
    }


    /**
     * 设置止损
     */
    public void setSL(OrderBack orderBack,String direction){
        //获取订单信息
        OrderBack.DataBean dataBean = orderBack.getData().get(0);
        dataBean.setContract_code("BTC-USD");
        double trade_avg_price = 0;
        while (true){
            try {
                String info = HttpHelper.sendPostRequest(ORDER_INFO, JSONUtil.toJsonStr(dataBean));
                orderInfo = JSONUtil.toBean(info, OrderInfo.class);
                trade_avg_price = orderInfo.getData().get(0).getTrade_avg_price();
                if (trade_avg_price!=0){
                    System.out.println("开仓成功:"+orderInfo);
                    break;
                }
            }catch (Exception e){
            }
        }
        buyPrice = trade_avg_price;
        maxPrice = trade_avg_price;
        //设置止损
        String result = HttpHelper.sendPostRequest(ORDER_TPSL, JSONUtil.toJsonStr(getSLOrder(trade_avg_price,direction,null)));
    }

    public Order getTPOrder(double price,String direction,String offset){
        return Order.builder()
                .contract_code("BTC-USD")
                .volume(1L)
                .offset(offset)
                .direction(direction)
                .lever_rate(10)
                .order_price_type("optimal_5")
                .tp_trigger_price(price)
                .sl_order_price_type("optimal_5")
                .build();
    }
    public Order getSLOrder(double price,String direction,String offset){
        return Order.builder()
                .contract_code("BTC-USD")
                .volume(1L)
                .offset(offset)
                .direction(direction)
                .lever_rate(10)
                .order_price_type("optimal_5")
                .sl_trigger_price(price * slRate)
                .sl_order_price_type("optimal_5")
                .build();
    }



}
