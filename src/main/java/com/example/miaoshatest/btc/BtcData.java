package com.example.miaoshatest.btc;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.StopWatch;
import cn.hutool.json.JSONUtil;
import com.example.miaoshatest.dao.IAmountDao;
import com.example.miaoshatest.dao.ICompareDao;
import com.example.miaoshatest.dao.IDepthDao;
import com.example.miaoshatest.dao.IPriceDao;
import com.example.miaoshatest.dao.bean.Amount;
import com.example.miaoshatest.dao.bean.Compare;
import com.example.miaoshatest.dao.bean.Depth;
import com.example.miaoshatest.dao.bean.Price;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author EDZ
 */
@Data
@Service
public class BtcData implements Serializable {

    @Autowired
    private IPriceDao priceDao;

    @Autowired
    private IDepthDao depthDao;

    @Autowired
    private IAmountDao amountDao;

    @Autowired
    private ICompareDao compareDao;

    /**
     * ch : market.BTC-USD.depth.step0
     * status : ok
     * ts : 1620830131762
     * tick : {"mrid":95899484394,"id":1620830131,"bids":[[56833.6,23287],[56833.5,50],[56833.4,50],[56833.3,807],[56832.5,1825],[56832.2,430],[56832.1,3335],[56830.8,100],[56830.4,3967],[56830.3,886],[56829.9,834],[56829.5,6950],[56829.1,81],[56827.3,20],[56827,143],[56825.8,1107],[56825.5,348],[56824.3,1],[56823.2,417],[56822.2,67],[56822.1,120],[56821.6,20],[56821.4,80],[56821.1,10],[56820.8,400],[56819.3,1],[56818.1,381],[56817.9,1],[56816.6,80],[56816.4,80],[56816.3,10],[56815.9,2],[56815,108],[56814.9,20],[56814.1,3],[56813.7,80],[56812.7,100],[56811.3,301],[56810.8,300],[56810.7,80],[56809.9,3849],[56808.8,1],[56807.1,20],[56806.3,1],[56805.6,5],[56805.5,87],[56805.3,10],[56804.9,100],[56804.3,300],[56804.2,450],[56803.9,2],[56803.7,89],[56803.3,323],[56802.4,80],[56802.3,1],[56801.8,2],[56801.1,99],[56800.9,60],[56800.8,2],[56800.7,110],[56800.2,88],[56800.1,777],[56800,1264],[56799.9,44],[56799.2,80],[56799.1,1791],[56798.4,102],[56798.1,90],[56797.6,60],[56796.6,94],[56795.1,240],[56795,1],[56794.7,5],[56794.6,4],[56793.9,120],[56793.7,40],[56793.6,60],[56793,2],[56792.4,1],[56792.2,80],[56792,601],[56791.4,10],[56791.2,274],[56791.1,153],[56790.7,1076],[56789.2,56],[56789.1,42],[56788.4,401],[56788.3,191],[56788.2,1052],[56788,80],[56787.3,39],[56786.7,357],[56785.9,9],[56785.8,87],[56784,89],[56782.2,2],[56781.7,100],[56781.3,539],[56781.2,100],[56780.6,2],[56780.4,90],[56780.2,13],[56780,20],[56779.6,1],[56779.1,924],[56779,8831],[56778.6,141],[56778.5,1],[56778,39],[56777.4,90],[56777.2,40],[56776.8,94],[56776,80],[56775.2,1],[56775.1,3],[56774.8,538],[56773.1,326],[56772.9,469],[56772.6,329],[56772.1,1001],[56772,660],[56770.6,104],[56770.5,4],[56770.1,2],[56769.8,350],[56767.4,39],[56765.7,522],[56764.6,1],[56764.4,16],[56762.5,87],[56760.7,89],[56760,50],[56759.1,19],[56758.4,1],[56758.3,100],[56758,70],[56757.7,1],[56757.4,582],[56757.3,3],[56757.1,90],[56755.5,1],[56755.3,124],[56754.8,300],[56754.5,8],[56754,6],[56753.5,94],[56753.4,500],[56752.6,134],[56751.7,4]],"asks":[[56833.7,13290],[56833.8,2],[56833.9,2],[56834,1],[56834.1,535],[56840,50],[56842.4,1],[56842.6,681],[56844.3,912],[56845,100],[56845.1,39],[56845.3,80],[56846.5,40],[56847,111],[56847.2,60],[56848,1],[56848.8,400],[56848.9,40],[56849.3,682],[56851.7,80],[56852.2,4],[56852.3,962],[56853.2,3],[56853.6,1163],[56854.2,20],[56854.8,80],[56855,400],[56855.8,5],[56856.2,60],[56856.5,60],[56856.8,136],[56857.2,10],[56857.3,95],[56858.6,50],[56858.7,2169],[56858.8,50],[56858.9,50],[56859.1,194],[56859.2,1],[56859.4,450],[56860,20],[56860.1,3346],[56861.8,87],[56861.9,1],[56862.8,15],[56863.1,60],[56863.3,110],[56863.6,89],[56863.7,1],[56863.9,20],[56864,5],[56864.1,20],[56864.3,41],[56865,10],[56865.8,240],[56866.2,99],[56866.5,4388],[56866.6,30],[56866.7,5],[56867,2],[56867.1,90],[56867.3,2],[56868.2,20],[56868.5,80],[56868.6,25],[56869.2,75],[56869.7,7836],[56869.9,454],[56870.9,1],[56871.6,60],[56871.8,3],[56872.7,4],[56872.9,10],[56874,20],[56874.4,90],[56874.7,1016],[56875.7,40],[56875.8,1],[56875.9,1],[56876,1],[56876.5,500],[56877,95],[56877.3,9],[56877.7,10],[56877.9,1],[56878.2,1012],[56878.8,94],[56879,20],[56879.2,80],[56879.3,2],[56880,100],[56881.3,1736],[56881.4,125],[56881.5,87],[56882.1,80],[56882.7,9],[56883.3,89],[56884,80],[56884.1,39],[56884.8,39],[56885.8,20],[56886.9,90],[56888,408],[56889.7,4722],[56889.9,100],[56890,631],[56890.1,1980],[56890.4,143],[56891.2,100],[56891.6,80],[56892.2,3],[56892.8,1],[56894.2,1333],[56894.6,300],[56894.7,3],[56895.6,39],[56896,2517],[56896.7,100],[56897.3,1],[56897.5,141],[56897.7,341],[56898.7,359],[56899.2,47],[56899.6,100],[56899.8,39],[56899.9,2],[56900,20],[56900.1,2],[56900.3,95],[56900.4,373],[56901,6],[56902,274],[56902.1,247],[56903.2,9],[56903.6,1033],[56903.9,998],[56904,3300],[56904.2,40],[56904.8,87],[56907.5,2],[56907.9,4],[56908.8,100],[56908.9,357],[56909.5,1],[56909.6,1],[56910.2,90],[56910.6,522],[56911.4,300],[56911.8,1],[56912,9]],"ts":1620830131735,"version":1620830131,"ch":"market.BTC-USD.depth.step0"}
     */

    private String ch;
    private String status;
    private long ts;
    private TickBean tick;

    public void getPrice() throws Exception {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String url = "https://api.hbdm.com/swap-api/v1/swap_index?contract_code=BTC-USD";
        String time = DateUtil.format((DateUtil.date(Calendar.getInstance())), "yyyy-MM-dd HH:mm:ss");
        String result = HttpHelper.sendGetRequest(url);
        stopWatch.stop();

        PriceBean priceBean = JSONUtil.toBean(result, PriceBean.class);

        Price price = new Price();
        price.setTime(time);
        price.setContent(result);
        price.setCost(stopWatch.getLastTaskTimeMillis());

        priceDao.savePrice(price);
    }

    public void getDepth() throws Exception {
        StopWatch stopWatch = new StopWatch();
        String time = DateUtil.format((DateUtil.date(Calendar.getInstance())), "yyyy-MM-dd HH:mm:ss");
        stopWatch.start();
        String url = "https://api.hbdm.com/swap-ex/market/depth?symbol=BTC_NW&type=step0&contract_code=BTC-USD";
        String result = HttpHelper.sendGetRequest(url);
        stopWatch.stop();


        Depth depth = new Depth();
        depth.setTime(time);
        depth.setContent(result);
        depth.setCost(stopWatch.getTotalTimeMillis());
        depthDao.saveDepth(depth);
    }

    public void getAmount() throws Exception {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String t = String.valueOf(System.currentTimeMillis() / 1000);
        String url = "https://api.hbdm.com/swap-ex/market/history/kline?contract_code=BTC-USD&period=1min&from=" + t + "&to=" + t;
        String result = HttpHelper.sendGetRequest(url);
        stopWatch.stop();
        if (stopWatch.getTotalTimeMillis()>2500){
            return;
        }
        AmountBean bean = JSONUtil.toBean(result, AmountBean.class);
        Amount amount = new Amount();
        BeanUtil.copyProperties(bean.getData().get(0), amount);
        amount.setTime(stampToDate(String.valueOf(bean.getTs())));
        amountDao.saveAmount(amount);
    }

    @Transactional
    public void getETHAmount() throws Exception {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String t = String.valueOf(System.currentTimeMillis() / 1000);
        String url = "https://api.hbdm.com/linear-swap-ex/market/history/kline?contract_code=ETH-USDT&period=1min&size=2000";
        String result = HttpHelper.sendGetRequest(url);
        stopWatch.stop();
        if (stopWatch.getTotalTimeMillis()>2500){
            return;
        }
        AmountBean bean = JSONUtil.toBean(result, AmountBean.class);
        for (int i=0;i<bean.getData().size();i++){
            AmountBean.DataBean dataBean = bean.getData().get(i);
            System.out.println(i);
            Amount amount = new Amount();
            BeanUtil.copyProperties(dataBean, amount);
            amount.setTime(stampToDate(String.valueOf(bean.getTs())));
            System.out.println(amount);
            amountDao.saveAmount(amount);
        }
    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }
    public static int divTime(String end,String begin) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date endTime = simpleDateFormat.parse(end);
            Date beginTime = simpleDateFormat.parse(begin);
            long a = endTime.getTime();
            long b = beginTime.getTime();
            if (a==b){
                return 1;
            }
            int c = (int)((a - b) / 1000);
            return c;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 1;
    }



    public void analysis() {
        List<Depth> depth = depthDao.getDepth(0, 200);
        for (Depth d : depth) {
            String result = d.getContent();
            DepthBean depthBean = JSONUtil.toBean(result, DepthBean.class);
            List<List<Double>> bids = depthBean.getTick().getBids().subList(0, 5);
            List<List<Double>> asks = depthBean.getTick().getAsks().subList(0, 5);
            System.out.println(bids + "\n" + asks);
            System.out.println("--------------------------------------------");
        }
    }
    Queue<Double> divQueue = new LinkedList<>();
    Amount preAmount = null;
    double preDiv = 0.00;
    double sum = 0;
    public void analysisAmount() {
        List<Amount> amount = amountDao.getAmount(39067, 101);
        preAmount= amount.get(amount.size()-1);
        sum = initData(amount);
        System.out.println(sum/100);
        preDiv = sum/100;
        List<Amount> list = amountDao.getAmount(39168, 4000);
        for (Amount a : list){
            double thisDiv = a.getAmount()-preAmount.getAmount() >= 0 ? a.getAmount()-preAmount.getAmount() : a.getAmount();
            thisDiv = thisDiv / divTime(a.getTime() , preAmount.getTime());
            divQueue.offer(thisDiv);
            sum = sum - divQueue.poll() + thisDiv;
            Compare compare = new Compare();
            compare.setTime(a.getTime());
            compare.setClose(a.getClose());
            compare.setDiv(sum/100);
            compare.setRate((((sum/100)-preDiv)/preDiv)*100);
            compare.setThisDiv(thisDiv);
            compare.setDivPrice(a.getClose() - preAmount.getClose());
            preDiv = sum/100;
            compareDao.saveCompare(compare);
            preAmount = a;
        }
    }

    private double initData(List<Amount> amount) {
        double sum = 0;
        Amount a ,b = null;
        for (int i = 0; i < 100; i++){
            a = amount.get(i);
            b = amount.get(i+1);
            double thisDiv = b.getAmount()-a.getAmount() >= 0 ? b.getAmount()-a.getAmount() : b.getAmount();
            thisDiv = thisDiv / divTime(b.getTime() , a.getTime());
            divQueue.offer(thisDiv);
            sum+=thisDiv;
        }
        return sum;
    }




}