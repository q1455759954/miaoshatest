package com.example.miaoshatest.controller;

import com.alibaba.fastjson.JSON;
import com.example.miaoshatest.dao.bean.MiaoShaUser;
import com.example.miaoshatest.entity.*;
import com.example.miaoshatest.redis.RedisClient;
import com.example.miaoshatest.redis.keysbean.GoodsKey;
import com.example.miaoshatest.service.IGoodsService;
import com.example.miaoshatest.util.valiadator.UserCheckAndLimit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/miaosha")
@Slf4j
public class MiaoShaController {

    @Autowired
    IGoodsService goodsService;

    @Autowired
    RedisClient redisService;

    @PostConstruct
    public void init() throws Exception {
        ResultGeekQ<List<GoodsVo>> goodsListR = goodsService.goodsVoList();
        if (!ResultGeekQ.isSuccess(goodsListR)) {
            log.error("***系统初始化商品预热失败***");
            return;
        }
        List<GoodsVo> goodsList = goodsListR.getData();
        for (GoodsVo goods : goodsList) {
            redisService.set(GoodsKey.getMiaoshaGoodsStock, "" + goods.getId(), goods.getStockCount());
            redisService.set(GoodsKey.getMiaoshaGoodsStartTime, "" + goods.getId(), goods.getStartDate());
        }
    }

    @RequestMapping("/verifyCode")
    @UserCheckAndLimit(seconds = 5, maxCount = 5, needLogin = true)
    @ResponseBody
    public ResultGeekQ<String> getVerifyCode(HttpServletResponse response, MiaoShaUser user,
                                             @RequestParam("goodsId") long goodsId) {
        ResultGeekQ resultGeekQ = ResultGeekQ.build();
        try {

            ResultGeekQ result = null;
            if(!AbstractResult.isSuccess(result)){
                resultGeekQ.withError(result.getCode(),result.getMessage());
                return resultGeekQ;
            }
        } catch (Exception e) {
            resultGeekQ.withErrorCodeAndMessage(ResultStatus.SYSTEM_ERROR);
        }
        return resultGeekQ;
    }


}
