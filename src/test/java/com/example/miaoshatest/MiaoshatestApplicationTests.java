package com.example.miaoshatest;

import com.example.miaoshatest.btc.BtcData;
import com.example.miaoshatest.btc.trade.TradeSystem;
import com.example.miaoshatest.dao.IUserDao;
import com.example.miaoshatest.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import static java.lang.Thread.sleep;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableAutoConfiguration
@Slf4j
public class MiaoshatestApplicationTests {

    @Autowired
    IUserDao dao;

    @Autowired
    UserServiceImpl tests;

    @Autowired
    private BtcData btcData;

    ExecutorService service = Executors.newFixedThreadPool(5);

    @Test
    public void btcData() {
        btcData.analysisAmount();
//        while (true){
//            try {
//                service.execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        btcData.getAmount();
//
////                        btcData.getPrice();
//                    }
//                });
////                service.execute(new Runnable() {
////                    @Override
////                    public void run() {
////                        btcData.getDepth();
////                    }
////                });
//                sleep(3*1000);
//            }catch (Exception e){
//
//            }
//        }
    }
    @Autowired
    private TradeSystem system;

    @Test
    public void tradeSystem() throws InterruptedException {
        system.buyPrice = 40551.0000;
        system.curNum = 1250;
        system.setTP(null,"sell");
    }

    @Test
    public void contextLoads() {
        tests.contextLoads1();
//        tests.contextLoads2();
        tests.contextLoads3();
        dao.addUser("aaa");
    }




}
