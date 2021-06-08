package com.example.miaoshatest.btc.trade;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.StopWatch;
import cn.hutool.json.JSONUtil;
import com.example.miaoshatest.btc.AmountBean;
import com.example.miaoshatest.btc.HttpHelper;
import com.example.miaoshatest.btc.trade.helper.ApiSignature;
import com.example.miaoshatest.btc.trade.helper.UrlParamsBuilder;
import com.example.miaoshatest.dao.IAmountDao;
import com.example.miaoshatest.dao.IPriceDao;
import com.example.miaoshatest.dao.bean.Amount;
import com.example.miaoshatest.dao.bean.Price;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LGL
 */
@Service
public class Test {
    private String secretKey = "20e51f74-f70e98ec-66928696-5b5de";
    private String key = "vftwcr5tnh-5d184dc9-439346e2-6060b";
    private String end = "AccessKeyId="+key+"&SignatureMethod=HmacSHA256&SignatureVersion=2&Timestamp=";
    private static String contract_code = "ETH-USDT";

    public static String ORDER = "/linear-swap-api/v1/swap_order";
    public static String ORDER_INFO = "/linear-swap-api/v1/swap_order_info";
    public static String ORDER_TPSL = "/linear-swap-api/v1/swap_tpsl_order";
    public static String AMOUNT = "https://api.hbdm.com/linear-swap-ex/market/history/kline?contract_code="+contract_code+"&period=1min&from=";
    public static String LIGHTNING = "/linear-swap-api/v1/swap_lightning_close_position";
    public static String ACCOUNT = "/linear-swap-api/v1/swap_account_info";
    public static String CANCEL_TPSL = "/linear-swap-api/v1/swap_tpsl_cancelall";
    public static String QUERY_TPSL = "/linear-swap-api/v1/swap_tpsl_openorders";
    public static String HTTPS = "https://";
    public static String HOST = "api.hbdm.com";
    public UrlParamsBuilder paramsBuilder = null;

    @Autowired
    public IPriceDao priceDao;
    public int win = 0;
    public int lose = 0;
    public int ok = 0;

    public double money = 0;

    private Long volume = 1L;

    private int lever_rate = 1;

    public double slRate = 0.99;

    public double profitRate = 0.005;

    public double firstProfitRate = 0.0025;

    public double fixRate = 0.02;

    private double fuckRate = 0.012;

    public double curPrice;

    public double maxPrice;

    public double buyPrice;

    public String order_id;

    public OrderInfo orderInfo;

//    private Order order = Order.builder()
//            .contract_code(contract_code)
//            .volume(1L)
//            .direction("buy")
//            .offset("open")
//            .lever_rate(10)
//            .order_price_type("optimal_5")
//            .sl_trigger_price(curPrice * slRate)
//            .sl_order_price_type("optimal_5")
//            .build();

    public static void main(String[] args) throws Exception {
//        List<String> list = new ArrayList<>();
//        list.add("aa");
//        list.contains(null);
//        list.stream().anyMatch( x -> x.contains("aa"));

        TradeUsdt system = new TradeUsdt();
        system.getTPSL();
        system.cancelTPSL();
        system.getTPSL();
        AmountBean amount = system.getAmount();
        system.curPrice = amount.getData().get(0).getClose();
        system.buy();
//        system.sell();
    }

    public String getResult(String json,String method) {
        int i=0;
        while (i<3){
            try {
                paramsBuilder = new UrlParamsBuilder();
                String requestUrl =  HTTPS + HOST + method;
                new ApiSignature().createSignature(ApiSignature.API_KEY, ApiSignature.SECRET_KEY, "POST", HOST, method, paramsBuilder);
                requestUrl += paramsBuilder.buildUrl();
                String result = HttpHelper.sendPostRequest(requestUrl,json);
                System.out.println(result);
                return result;
            }catch (Exception e){
                i++;
            }
        }
        return null;
    }

    public void getAccount() throws Exception {
        Map<String,String> map  = new HashMap();
        map.put("contract_code",contract_code);
        String result = getResult(JSONUtil.toJsonStr(map),ACCOUNT);
        System.out.println(result);
    }

    /**
     * 开多
     */
    public void buy() throws Exception {
        String jsonStr = JSONUtil.toJsonStr(getOrder((double)(int)(curPrice * (slRate+0.001)),"buy","open"));
//        String result = getResult(jsonStr,ORDER);
//        OrderBack orderBack = JSONUtil.toBean(result, OrderBack.class);
//        setSL(orderBack,"sell");
        setTP(null,"sell");
    }

    /**
     * 开空
     */
    public void sell() throws Exception {
        String jsonStr = JSONUtil.toJsonStr(getOrder((double)(int)(curPrice * (2-slRate+0.001)),"sell","open"));
//        String result = getResult(jsonStr,ORDER);
//        OrderBack orderBack = JSONUtil.toBean(result, OrderBack.class );
//        setLessSL(orderBack,"buy");
        setLessTP(null,"buy");
    }

    @Autowired
    public IAmountDao dao;

    public int curNum = 1;

    /**
     * 实时价格
     */
    public AmountBean getAmount() {
//        try {
//            StopWatch stopWatch = new StopWatch();
//            stopWatch.start();
//            String t = String.valueOf(System.currentTimeMillis() / 1000);
//            String result = HttpHelper.sendGetRequest(AMOUNT+ t + "&to=" + t);
//            stopWatch.stop();
//            if (stopWatch.getTotalTimeMillis()>1500){
//                getAmount();
//            }
//            return JSONUtil.toBean(result, AmountBean.class);
//        }catch (Exception e){
//            return getAmount();
//        }

        try{
            List<Amount> amount = dao.getAmount(curNum, 1);
            curNum++;
            AmountBean amountBean = new AmountBean();
            List<AmountBean.DataBean> list = new ArrayList<>();
            AmountBean.DataBean dataBean = new AmountBean.DataBean();
            BeanUtil.copyProperties(amount.get(0),dataBean);
            list.add(dataBean);
            amountBean.setData(list);
            return amountBean;
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("win:"+win+" lose:"+lose +" ok:"+ok +" money:"+money+"%");
        }
        return null;
    }

    public void getTPSL() {
        Map<String,String> map  = new HashMap();
        map.put("contract_code",contract_code);
        String result = getResult(JSONUtil.toJsonStr(map),QUERY_TPSL);
    }

    /**
     * 设置空方止盈
     */
    public void setLessTP(OrderBack orderBack,String direction) throws Exception {
        double price = buyPrice - (fixRate + firstProfitRate) * buyPrice  ;
        double tpPrice = buyPrice - fixRate * buyPrice  ;
        double judgePrice = tpPrice;
        double fuckPrice = buyPrice - fuckRate * buyPrice;
        //达到止盈点
        boolean flag = false;
        boolean fuckFlag = false;
        while (true){
            AmountBean amount = getAmount();
            if (amount==null){
                continue;
            }
            curPrice = amount.getData().get(0).getClose();
//            System.out.println("空单：买入价格："+buyPrice+" 现在价格: "+curPrice + " 盈利："+((curPrice-buyPrice)/buyPrice)*(-100)+"%");
            if (curPrice < fuckPrice && !fuckFlag){
                fuckFlag = true;
            }
            if (fuckFlag && curPrice + curPrice*0.002 > buyPrice){
                //涨到fixRate又跌回去，闪电平仓
                lightning(direction);
                ok++;
                money += ((curPrice-buyPrice)/buyPrice)*(-100);
                System.out.println("空单结束1：买入价格："+buyPrice+" 现在价格："+curPrice+" 盈利："+((curPrice-buyPrice)/buyPrice)*(-100)+"%");
                Price p = new Price();
                p.setTime("ok");
                p.setContent("空单结束1：买入价格："+buyPrice+" 现在价格："+curPrice+" 盈利："+((curPrice-buyPrice)/buyPrice)*(-100)+"%");
                p.setCost(0L);
                priceDao.savePrice(p);
                buy();
                break;
            }
            //当前价格大于0.008*买价时记录最高价格设置profitRate*买价为止盈
            //之后每升高profitRate止盈提高profitRate
            if (buyPrice > curPrice){
                if (curPrice < price){
//                    getResult(JSONUtil.toJsonStr(getSLOrder((double)(int)tpPrice,direction)),ORDER_TPSL);
                    judgePrice = tpPrice;
//                    System.out.println("设置空单止盈："+tpPrice+" 盈利："+((tpPrice-buyPrice)/buyPrice)*(-100)+"%");
//                     HttpHelper.sendPostRequest(ORDER_TPSL+end+gmtNow(), JSONUtil.toJsonStr(getTPOrder(tpPrice,direction,null)));
                    price = price - buyPrice * (profitRate + firstProfitRate);
                    tpPrice = tpPrice - buyPrice * profitRate;
                    flag = true;
                }else if (curPrice > judgePrice && flag){
                    //闪电平仓
                    win++;
                    money += ((curPrice-buyPrice)/buyPrice)*(-100);
                    System.out.println("空单结束2：买入价格："+buyPrice+" 现在价格："+curPrice+" 盈利："+((curPrice-buyPrice)/buyPrice)*(-100)+"%");
                    lightning(direction);
                    Price p = new Price();
                    p.setTime("win");
                    p.setContent("空单结束2：买入价格："+buyPrice+" 现在价格："+curPrice+" 盈利："+((curPrice-buyPrice)/buyPrice)*(-100)+"%");
                    p.setCost(0L);
                    priceDao.savePrice(p);
                    break;
                }
            }else if (curPrice > buyPrice * (2-slRate)){
                //闪电平仓
                lose++;
                money += ((curPrice-buyPrice)/buyPrice)*(-100);
                System.out.println("空单结束3：买入价格："+buyPrice+" 现在价格："+curPrice+" 盈利："+((curPrice-buyPrice)/buyPrice)*(-100)+"%");
                lightning(direction);
                Price p = new Price();
                p.setTime("lose");
                p.setContent("空单结束3：买入价格："+buyPrice+" 现在价格："+curPrice+" 盈利："+((curPrice-buyPrice)/buyPrice)*(-100)+"%");
                p.setCost(0L);
                priceDao.savePrice(p);
                buy();
                break;
            }
//            Thread.sleep(1000);

        }
    }

    /**
     * 设置空方止损
     */
    public void setLessSL(OrderBack orderBack,String direction){
        //获取订单信息
        OrderBack.DataBean dataBean = orderBack.getData().get(0);
        dataBean.setContract_code(contract_code);
        double trade_avg_price = 0;
        while (true){
            try {
                String info = getResult(JSONUtil.toJsonStr(dataBean),ORDER_INFO);
//                String info = HttpHelper.sendPostRequest(ORDER_INFO+end+gmtNow(), JSONUtil.toJsonStr(dataBean));
                orderInfo = JSONUtil.toBean(info, OrderInfo.class);
                trade_avg_price = orderInfo.getData().get(0).getTrade_avg_price();
                if (trade_avg_price!=0){
                    System.out.println("开仓空单成功:"+orderInfo);
                    break;
                }
            }catch (Exception e){
            }
        }
        buyPrice = trade_avg_price;
        maxPrice = trade_avg_price;
        //设置止损
        String result = getResult(JSONUtil.toJsonStr(getSLOrder((double)(int)(trade_avg_price * (2-slRate)),direction)),ORDER_TPSL);
//        String result = HttpHelper.sendPostRequest(ORDER_TPSL+end+gmtNow(), JSONUtil.toJsonStr(getSLOrder(trade_avg_price,direction,null)));
    }


    /**
     * 设置止盈
     */
    public void setTP(OrderBack orderBack,String direction) throws Exception {
        double price = (fixRate + firstProfitRate) * buyPrice + buyPrice;
        double tpPrice = fixRate * buyPrice + buyPrice;
        double judgePrice = tpPrice;
        double fuckPrice = buyPrice + fuckRate * buyPrice;
        //达到止盈点
        boolean flag = false;
        boolean fuckFlag = false;
        while (true){
            AmountBean amount = getAmount();
            if (amount==null){
                continue;
            }
            curPrice = amount.getData().get(0).getClose();
//            System.out.println("多单：买入价格："+buyPrice+" 现在价格: "+curPrice + " 盈利："+((curPrice-buyPrice)/buyPrice)*100+"%");
            if (curPrice > fuckPrice && !fuckFlag){
                fuckFlag = true;
            }
            if (fuckFlag && curPrice - curPrice*0.002 < buyPrice){
                //涨到fuckRate又跌回去，闪电平仓
                ok++;
                money += ((curPrice-buyPrice)/buyPrice)*(100);
                System.out.println("多单结束1：买入价格："+buyPrice+" 现在价格："+curPrice+" 盈利："+((curPrice-buyPrice)/buyPrice)*(100)+"%");
                lightning(direction);
                Price p = new Price();
                p.setTime("ok");
                p.setContent("多单结束1：买入价格："+buyPrice+" 现在价格: "+curPrice + " 盈利："+((curPrice-buyPrice)/buyPrice)*100+"%");
                p.setCost(1L);
                priceDao.savePrice(p);
                sell();
                break;
            }
            //当前价格大于0.008*买价时记录最高价格设置profitRate*买价为止盈
            //之后每升高profitRate止盈提高profitRate
            if (buyPrice < curPrice){
                if (curPrice > price){
//                    getResult(JSONUtil.toJsonStr(getSLOrder((double)(int)tpPrice,direction)),ORDER_TPSL);
                    judgePrice = tpPrice;
//                    System.out.println("设置多单止盈："+tpPrice+" 盈利："+((tpPrice-buyPrice)/buyPrice)*(100)+"%");
//                     HttpHelper.sendPostRequest(ORDER_TPSL+end+gmtNow(), JSONUtil.toJsonStr(getTPOrder(tpPrice,direction,null)));
                    price = price + buyPrice * (profitRate + firstProfitRate);
                    tpPrice = tpPrice + buyPrice * profitRate;
                    flag = true;
                }else if (curPrice < judgePrice && flag){
                    //闪电平仓
                    win++;
                    money += ((curPrice-buyPrice)/buyPrice)*(100);
                    System.out.println("多单结束2：买入价格："+buyPrice+" 现在价格："+curPrice+" 盈利："+((curPrice-buyPrice)/buyPrice)*(100)+"%");
                    lightning(direction);
                    Price p = new Price();
                    p.setTime("win");
                    p.setContent("多单结束2：买入价格："+buyPrice+" 现在价格: "+curPrice + " 盈利："+((curPrice-buyPrice)/buyPrice)*100+"%");
                    p.setCost(1L);
                    priceDao.savePrice(p);
                    break;
                }
            }else if (curPrice < buyPrice * slRate){
                //闪电平仓
                lose++;
                money += ((curPrice-buyPrice)/buyPrice)*(100);
                System.out.println("多单结束3：买入价格："+buyPrice+" 现在价格："+curPrice+" 盈利："+((curPrice-buyPrice)/buyPrice)*(100)+"%");
                lightning(direction);
                Price p = new Price();
                p.setTime("lose");
                p.setContent("多单结束3：买入价格："+buyPrice+" 现在价格: "+curPrice + " 盈利："+((curPrice-buyPrice)/buyPrice)*100+"%");
                p.setCost(1L);
                priceDao.savePrice(p);
                sell();
                break;
            }
//            Thread.sleep(1000);

        }
    }

    /**
     * 设置止损
     */
    public void setSL(OrderBack orderBack,String direction){
        //获取订单信息
        OrderBack.DataBean dataBean = orderBack.getData().get(0);
        dataBean.setContract_code(contract_code);
        double trade_avg_price = 0;
        while (true){
            try {
                String info = getResult(JSONUtil.toJsonStr(dataBean),ORDER_INFO);
//                String info = HttpHelper.sendPostRequest(ORDER_INFO+end+gmtNow(), JSONUtil.toJsonStr(dataBean));
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
        String result = getResult(JSONUtil.toJsonStr(getSLOrder((double)(int)(trade_avg_price * slRate),direction)),ORDER_TPSL);
//        String result = HttpHelper.sendPostRequest(ORDER_TPSL+end+gmtNow(), JSONUtil.toJsonStr(getSLOrder(trade_avg_price,direction,null)));
    }

    /**
     * 闪电平仓
     */
    public void lightning(String direction){
        System.out.println("平仓成功！");

//        try {
//            Order order = Order.builder()
//                    .contract_code(contract_code)
//                    .volume(volume)
//                    .direction(direction)
//                    .order_price_type("lightning")
//                    .build();
//            getResult(JSONUtil.toJsonStr(order),LIGHTNING);
//            cancelTPSL();
//            System.out.println("平仓成功！");
//        }catch (Exception e){
//            e.printStackTrace();
//            lightning(direction);
//        }

//        HttpHelper.sendPostRequest(LIGHTNING+end+gmtNow(), JSONUtil.toJsonStr(order));
    }

    /**
     * 止盈止损撤单
     */
    public void cancelTPSL(){
        try {
            Order order = Order.builder()
                    .contract_code(contract_code)
                    .direction("buy")
                    .build();
            getResult(JSONUtil.toJsonStr(order),CANCEL_TPSL);
            order.setDirection("sell");
            getResult(JSONUtil.toJsonStr(order),CANCEL_TPSL);
            System.out.println("止盈止损撤单成功");
        }catch (Exception e){
            e.printStackTrace();
            cancelTPSL();
        }
    }

    public Order getTPOrder(double price,String direction){
        price = (double)(int)price;
        return Order.builder()
                .contract_code(contract_code)
                .volume(volume)
                .direction(direction)
                .tp_trigger_price(price)
                .tp_order_price_type("optimal_5")
                .build();
    }
    public Order getSLOrder(double price,String direction){
        return Order.builder()
                .contract_code(contract_code)
                .volume(volume)
                .direction(direction)
                .sl_trigger_price(price)
                .sl_order_price_type("optimal_5")
                .build();
    }

    public Order getOrder(double price,String direction,String offset){
        return Order.builder()
                .contract_code(contract_code)
                .volume(volume)
                .offset(offset)
                .direction(direction)
                .lever_rate(lever_rate)
                .order_price_type("optimal_5")
                .sl_trigger_price(price)
                .sl_order_price_type("optimal_5")
                .build();
    }

}
