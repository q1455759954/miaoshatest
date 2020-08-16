package com.example.miaoshatest.controller;

import com.alibaba.fastjson.JSON;
import com.example.miaoshatest.common.CustomerConstant;
import com.example.miaoshatest.common.ProductSoutOutMap;
import com.example.miaoshatest.dao.bean.MiaoShaUser;
import com.example.miaoshatest.entity.*;
import com.example.miaoshatest.redis.RedisClient;
import com.example.miaoshatest.redis.keysbean.GoodsKey;
import com.example.miaoshatest.redis.keysbean.MiaoshaKey;
import com.example.miaoshatest.service.IGoodsService;
import com.example.miaoshatest.service.IMiaoShaService;
import com.example.miaoshatest.service.impl.RandomValidateCodeService;
import com.example.miaoshatest.util.rabbitmq.MQSender;
import com.example.miaoshatest.util.valiadator.UserCheckAndLimit;
import com.example.miaoshatest.util.zk.WatcherApi;
import com.example.miaoshatest.util.zk.ZkApi;
import com.sun.org.apache.xpath.internal.operations.Or;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.session.RedisSessionProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static com.example.miaoshatest.common.CustomerConstant.MS_ING;
import static com.example.miaoshatest.entity.ResultStatus.*;

/**
 * 1.排队标记没有删除
 * 2.zk的监听 修改map
 */
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

    @Autowired
    private ZkApi zooKeeper;

    @Autowired
    MQSender sender;

    @Autowired
    WatcherApi watcherApi;

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

    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    public ResultGeekQ<Long> getMiaoshaResult(Model model, MiaoShaUser user,
                                              @RequestParam("goodsId") long goodsId) {
        ResultGeekQ result = ResultGeekQ.build();
        try {
            String queueKey = MiaoshaKey.getMiaoshaOrderWaitFlagRedisKey(String.valueOf(user.getId()), String.valueOf(goodsId));
            if (redisService.get(queueKey,String.class)!=null){
                result.withError(MIAOSHA_QUEUE_ING.getCode(),MIAOSHA_QUEUE_ING.getMessage());
                return result;
            }
            String msKey  = CustomerConstant.RedisKeyPrefix.MIAOSHA_ORDER + "_" + user.getId() + "_" + goodsId;
            Object order = redisService.get(msKey, OrderInfoVo.class);
            if (order!=null){
                OrderInfoVo info = (OrderInfoVo)order;
                result.setData(info.getId());
                return result;
            }
            result.withErrorCodeAndMessage(ResultStatus.MIAOSHA_FAIL);
            return result;
        }catch (Exception e) {
            result.withErrorCodeAndMessage(ResultStatus.MIAOSHA_FAIL);
            return result;
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
            if (zooKeeper.exists(CustomerConstant.ZookeeperPathPrefix.getZKSoldOutProductPath(String.valueOf(goodsId)), watcherApi) != null) {
                zooKeeper.updateNode(CustomerConstant.ZookeeperPathPrefix.getZKSoldOutProductPath(String.valueOf(goodsId)), "false");
            }
            //用zk标记判断是否卖光
            if (ProductSoutOutMap.getProductSoldOutMap().get(goodsId+"")!=null){
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
            String msKey  = CustomerConstant.RedisKeyPrefix.MIAOSHA_ORDER + "_" + user.getId() + "_" + goodsId;
            OrderInfoVo orderInfo = (OrderInfoVo) redisService.get(msKey, OrderInfoVo.class);
            if (orderInfo!=null){
                result.withErrorCodeAndMessage(GOOD_EXIST);
                return result;
            }

            //扣减内存 + zk内存标志
            ResultGeekQ<Boolean> deductR = deductStockCache(goodsId+"");
            if (!ResultGeekQ.isSuccess(deductR)){
                result.withError(deductR.getCode(), deductR.getMessage());
                return result;
            }

            //入队
            MiaoShaMessage mm = new MiaoShaMessage();
            mm.setUser(user);
            mm.setGoodsId(goodsId);
            sender.sendMiaoshaMessage(mm);
            //正在进行中
            result.setData(MS_ING);

        }catch (AmqpException amqpE){
            log.error("创建订单失败", amqpE);
            redisService.incr(GoodsKey.getMiaoshaGoodsStock,""+goodsId);
            ProductSoutOutMap.getProductSoldOutMap().remove(goodsId);
            //修改zk
            if (zooKeeper.exists(CustomerConstant.ZookeeperPathPrefix.getZKSoldOutProductPath(String.valueOf(goodsId)),true)!=null){
                zooKeeper.updateNode(CustomerConstant.ZookeeperPathPrefix.getZKSoldOutProductPath(String.valueOf(goodsId)),"false");
            }
            result.withErrorCodeAndMessage(MIAOSHA_MQ_SEND_FAIL);
            return result ;
        }catch (Exception e){
            result.withErrorCodeAndMessage(MIAOSHA_FAIL);
            return result;
        }
        return result;
    }

    /**
     * 扣减库存
     * @param goodsId
     * @return
     */
    private ResultGeekQ<Boolean> deductStockCache(String goodsId) {
        ResultGeekQ<Boolean> result = ResultGeekQ.build();
        try {
            Long stock = redisService.decr(GoodsKey.getMiaoshaGoodsStock,""+goodsId);
            if (stock == null) {
                log.error("***数据还未准备好***");
                result.withError(MIAOSHA_DEDUCT_FAIL.getCode(), MIAOSHA_DEDUCT_FAIL.getMessage());
                return result;
            }
            ConcurrentHashMap hashMap = ProductSoutOutMap.productSoldOutMap;

            if (stock<0){
                log.info("***stock 扣减减少*** stock:{}",stock);
                redisService.incr(GoodsKey.getMiaoshaGoodsStock,""+goodsId);
                ProductSoutOutMap.productSoldOutMap.put(goodsId, true);
                //写zk售完标志
                if (zooKeeper.exists(CustomerConstant.ZookeeperPathPrefix.PRODUCT_SOLD_OUT, false) == null) {
                    zooKeeper.createNode(CustomerConstant.ZookeeperPathPrefix.PRODUCT_SOLD_OUT,"");
                }
                if (zooKeeper.exists(CustomerConstant.ZookeeperPathPrefix.getZKSoldOutProductPath(goodsId), true) == null) {
                    zooKeeper.createNode(CustomerConstant.ZookeeperPathPrefix.getZKSoldOutProductPath(goodsId), "true");
                }
                if ("false".equals(zooKeeper.getData(CustomerConstant.
                        ZookeeperPathPrefix.getZKSoldOutProductPath(goodsId),watcherApi))){
                    zooKeeper.updateNode(CustomerConstant.ZookeeperPathPrefix.getZKSoldOutProductPath(goodsId), "true");
                    //监听zk售完标记节点
                    zooKeeper.exists(CustomerConstant.ZookeeperPathPrefix.getZKSoldOutProductPath(goodsId), true);
                }

                if (zooKeeper.exists(CustomerConstant.ZookeeperPathPrefix.getZKSoldOutProductPath(String.valueOf(goodsId)), watcherApi) != null) {
                    zooKeeper.updateNode(CustomerConstant.ZookeeperPathPrefix.getZKSoldOutProductPath(String.valueOf(goodsId)), "false");
                }

                result.withError(MIAO_SHA_OVER.getCode(), MIAO_SHA_OVER.getMessage());
                return result;
            }
        }catch (Exception e){
            log.error("***deductStockCache error***");
            result.withError(MIAO_SHA_OVER.getCode(), MIAO_SHA_OVER.getMessage());
            return result;
        }
        return result;
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
