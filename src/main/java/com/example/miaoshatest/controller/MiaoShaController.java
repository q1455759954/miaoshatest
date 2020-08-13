package com.example.miaoshatest.controller;

import com.alibaba.fastjson.JSON;
import com.example.miaoshatest.common.ProductSoutOutMap;
import com.example.miaoshatest.dao.bean.MiaoShaUser;
import com.example.miaoshatest.entity.*;
import com.example.miaoshatest.redis.RedisClient;
import com.example.miaoshatest.redis.keysbean.GoodsKey;
import com.example.miaoshatest.redis.keysbean.MiaoshaKey;
import com.example.miaoshatest.service.IGoodsService;
import com.example.miaoshatest.service.IMiaoShaService;
import com.example.miaoshatest.service.impl.RandomValidateCodeService;
import com.example.miaoshatest.util.valiadator.UserCheckAndLimit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.List;

import static com.example.miaoshatest.common.CustomerConstant.MiaoShaStatus.MIAO_SHA_NOT_START;
import static com.example.miaoshatest.entity.ResultStatus.*;

@Controller
@RequestMapping("/miaosha")
@Slf4j
public class MiaoShaController {

    @Autowired
    IGoodsService goodsService;

    @Autowired
    RedisClient redisService;

    @Autowired
    RandomValidateCodeService codeService;

    @Autowired
    IMiaoShaService miaoShaService;

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


    @UserCheckAndLimit(seconds = 5, maxCount = 5, needLogin = true)
    @RequestMapping(value = "/{path}/confirm", method = RequestMethod.POST)
    @ResponseBody
    public ResultGeekQ<Integer> miaosha( MiaoShaUser user, @RequestParam("goodsId") long goodsId,
                                        @PathVariable("path") String path) {
        ResultGeekQ<Integer> result = ResultGeekQ.build();
        try {
            //验证路径
            ResultGeekQ<Boolean> checkPath = miaoShaService.checkPath(user, goodsId, path);
            if (!ResultGeekQ.isSuccess(checkPath)){
                result.withError(REQUEST_ILLEGAL.getCode(), REQUEST_ILLEGAL.getMessage());
                return result;
            }

            //用zk标记判断是否卖光
            if (ProductSoutOutMap.getProductSoldOutMap().get(goodsId)!=null){
                result.withError(MIAO_SHA_OVER.getCode(), MIAO_SHA_OVER.getMessage());
                return result;
            }

            //判断是否在排队中，
            String queueKey = MiaoshaKey.getMiaoshaOrderWaitFlagRedisKey(String.valueOf(user.getId()), String.valueOf(goodsId));
            if (!redisService.set(queueKey,String.valueOf(goodsId),"NX","EX",120)){
                //NX只有KV不存在的时候才会保存返回true，这里已经在排队了
                result.withError(MIAOSHA_QUEUE_ING.getCode(), MIAOSHA_QUEUE_ING.getMessage());
            }

            //校验时间
            Long startTime = (Long) redisService.get(GoodsKey.getMiaoshaGoodsStartTime,""+goodsId,Long.class);
            if (startTime>System.currentTimeMillis()){
                result.withErrorCodeAndMessage(ResultStatus.MIAO_SHA_NOT_START);
                return result;
            }

            //是否已经秒杀到了,查找订单


            //扣减内存 + zk内存标志



        }catch (Exception e){

        }
        return null;
    }


    @RequestMapping("/verifyCode")
    @UserCheckAndLimit(seconds = 5, maxCount = 5, needLogin = true)
    @ResponseBody
    public ResultGeekQ<String> getVerifyCode(HttpServletResponse response, MiaoShaUser user,
                                             @RequestParam("goodsId") long goodsId) {
        ResultGeekQ resultGeekQ = ResultGeekQ.build();
        try {
            //判断时间
            Long startTime = (Long) redisService.get(GoodsKey.getMiaoshaGoodsStartTime,""+goodsId,Long.class);
            if (startTime>System.currentTimeMillis()){
                resultGeekQ.withErrorCodeAndMessage(ResultStatus.MIAO_SHA_NOT_START);
                return resultGeekQ;
            }
            BufferedImage image = codeService.getRandcode(user,goodsId);
            OutputStream outputStream =response.getOutputStream();
            ImageIO.write(image, "JPEG", outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            resultGeekQ.withErrorCodeAndMessage(ResultStatus.MIAOSHA_FAIL);
        }
        return resultGeekQ;

    }
    @RequestMapping("/path")
    @UserCheckAndLimit(seconds = 5, maxCount = 5, needLogin = true)
    @ResponseBody
    public ResultGeekQ<String> getMiaoShaPath(HttpServletRequest request, MiaoShaUser user,
                                              @RequestParam("goodsId") long goodsId,
                                              @RequestParam(value = "verifyCode", defaultValue = "0") String verifyCode) {
        ResultGeekQ<String> resultGeekQ = ResultGeekQ.build();
        try {
            boolean check = codeService.checkVerifyCode(user,goodsId,verifyCode);
            if (!check){
                resultGeekQ.withErrorCodeAndMessage(ResultStatus.CODE_FAIL);
                return resultGeekQ;
            }
            ResultGeekQ<String> path = miaoShaService.creatMiaoShaPath(user,goodsId);
            if (!ResultGeekQ.isSuccess(path)){
                resultGeekQ.withErrorCodeAndMessage(ResultStatus.MIAOSHA_FAIL);
                return resultGeekQ;
            }
            resultGeekQ.setData(path.getData());
        } catch (Exception e) {
            resultGeekQ.withErrorCodeAndMessage(ResultStatus.MIAOSHA_FAIL);
        }
        return resultGeekQ;
    }

}
