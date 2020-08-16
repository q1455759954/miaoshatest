package com.example.miaoshatest.service.impl;

import com.example.miaoshatest.common.CustomerConstant;
import com.example.miaoshatest.common.ProductSoutOutMap;
import com.example.miaoshatest.dao.IGoodsDao;
import com.example.miaoshatest.dao.bean.MiaoShaUser;
import com.example.miaoshatest.entity.*;
import com.example.miaoshatest.exception.MqOrderException;
import com.example.miaoshatest.redis.RedisClient;
import com.example.miaoshatest.redis.keysbean.GoodsKey;
import com.example.miaoshatest.redis.keysbean.MiaoshaKey;
import com.example.miaoshatest.service.IGoodsService;
import com.example.miaoshatest.service.IMiaoShaService;
import com.example.miaoshatest.util.rabbitmq.MQConfig;
import com.example.miaoshatest.util.zk.ZkApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 邱润泽 bullock
 */
@Service
@Slf4j
public class RabbitMqServiceImpl {

    @Autowired
    IGoodsDao goodsDao;

    @Autowired
    IMiaoShaService miaoShaService;

    @Autowired
    RedisClient redisClient;

    @Autowired
    ZkApi zkApi;

    @RabbitListener(queues = MQConfig.MIAOSHA_QUEUE)
    public void receive(String message) {
        log.info("receive message:" + message);
        MiaoShaMessage mm = RedisClient.stringToBean(message, MiaoShaMessage.class);
        MiaoShaUser user = mm.getUser();
        long goodsId = mm.getGoodsId();
        GoodsVo goods = goodsDao.goodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if (stock <= 0) {
            return;
        }
        //判断是否已经秒杀到了
        MiaoShaOrder order = miaoShaService.getMiaoshaOrderByUserIdGoodsId(Long.valueOf(user.getId()),
                goodsId);
        if (order != null) {
            throw new MqOrderException(ResultStatus.GOOD_EXIST);
        }
        //减库存 下订单 写入秒杀订单
        //秒杀失败
        ResultGeekQ<OrderInfoVo> msR = miaoShaService.miaosha(user, goods);
        if(!ResultGeekQ.isSuccess(msR)){
            //************************ 秒杀失败 回退操作 **************************************
            redisClient.incr(GoodsKey.getMiaoshaGoodsStock, "" + goodsId);

            if (ProductSoutOutMap.productSoldOutMap.get(goodsId) != null) {
                ProductSoutOutMap.productSoldOutMap.remove(goodsId);
            }
            //修改zk的商品售完标记为false
            try {
                if (zkApi.exists(CustomerConstant.ZookeeperPathPrefix.getZKSoldOutProductPath(String.valueOf(goodsId)), true) != null) {
                    zkApi.updateNode(CustomerConstant.ZookeeperPathPrefix.getZKSoldOutProductPath(String.valueOf(goodsId)), "false");
                }
            } catch (Exception e1) {
                log.error("修改zk商品售完标记异常", e1);
            }
            return;
        }
        OrderInfoVo orderInfo = msR.getData();
        //******************  如果成功则进行保存redis + flag ****************************
        String msKey  = CustomerConstant.RedisKeyPrefix.MIAOSHA_ORDER + "_" + orderInfo.getUserId() + "_" + orderInfo.getGoodsId();
        redisClient.set(msKey, msR.getData());
        //删除排队标志
        String queueKey = MiaoshaKey.getMiaoshaOrderWaitFlagRedisKey(String.valueOf(user.getId()), String.valueOf(goodsId));
        redisClient.delete(queueKey);
    }
}
