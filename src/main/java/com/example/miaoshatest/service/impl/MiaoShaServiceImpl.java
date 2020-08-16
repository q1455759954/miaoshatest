package com.example.miaoshatest.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.example.miaoshatest.dao.IGoodsDao;
import com.example.miaoshatest.dao.IMiaoShaOrderDao;
import com.example.miaoshatest.dao.OrderInfoDao;
import com.example.miaoshatest.dao.bean.MiaoShaUser;
import com.example.miaoshatest.entity.*;
import com.example.miaoshatest.redis.RedisClient;
import com.example.miaoshatest.redis.keysbean.MiaoshaKey;
import com.example.miaoshatest.service.IMiaoShaService;
import com.example.miaoshatest.util.MD5Util;
import com.sun.org.apache.xpath.internal.operations.Or;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
public class MiaoShaServiceImpl implements IMiaoShaService {

    @Autowired
    RedisClient redisClient;

    @Autowired
    IMiaoShaOrderDao miaoShaOrderDao;

    @Autowired
    OrderInfoDao orderInfoDao;

    @Autowired
    IGoodsDao goodsDao;

    @Override
    public ResultGeekQ<String> creatMiaoShaPath(MiaoShaUser user, long goodsId) {
        ResultGeekQ<String> resultGeekQ = ResultGeekQ.build();
        try {
            String str = MD5Util.md5(UUID.randomUUID().toString().replace("-","")+ "123456");
            redisClient.set(MiaoshaKey.getMiaoshaPath,user.getNickname()+"_"+goodsId,str);
            resultGeekQ.setData(str);
        }catch (Exception e){
            resultGeekQ.withErrorCodeAndMessage(ResultStatus.MIAOSHA_FAIL);
        }
        return resultGeekQ;
    }

    @Override
    public ResultGeekQ<Boolean> checkPath(MiaoShaUser user, long goodsId, String path) {
        ResultGeekQ<Boolean> result = ResultGeekQ.build();
        try {
            String p = (String) redisClient.get(MiaoshaKey.getMiaoshaPath,user.getNickname()+"_"+goodsId,String.class);
            result.setData(StringUtils.equals(path,p));
            return result;
        }catch (Exception e){
            result.withErrorCodeAndMessage(ResultStatus.MIAOSHA_FAIL);
            return result;
        }
    }

    @Override
    public MiaoShaOrder getMiaoshaOrderByUserIdGoodsId(Long userId, long goodsId) {
            return miaoShaOrderDao.getMiaoshaOrderByUserIdGoodsId(userId,goodsId);
    }

    @Override
    public ResultGeekQ<OrderInfoVo> miaosha(MiaoShaUser user, GoodsVo goods) {
        ResultGeekQ<OrderInfoVo> result = ResultGeekQ.build();
        try {
            //减库存
            boolean reduceResult= goodsDao.reduceStock(goods.getId())>0;
            if (!reduceResult){
                log.error(" *****reduceSorF扣减库存发生错误*****");
                result.withErrorCodeAndMessage(ResultStatus.DATA_NOT_EXISTS);
                return result;
            }
            //生成订单
            OrderInfoVo orderInfoVo = createOrderInfo(user,goods);
            result.setData(orderInfoVo);
            return result;
        }catch(Exception e){
            log.error("***秒杀下订单失败*** error:{}",e);
            result.withErrorCodeAndMessage(ResultStatus.MIAOSHA_FAIL);
            return result;
        }
    }

    private OrderInfoVo createOrderInfo(MiaoShaUser user, GoodsVo goods) {
        OrderInfoVo orderInfo = new OrderInfoVo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getMiaoshaPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(user.getId());
        orderInfoDao.insertSelective(orderInfo);
        MiaoShaOrder miaoshaOrder = new MiaoShaOrder();
        miaoshaOrder.setGoodsId(goods.getId());
        miaoshaOrder.setOrderId(orderInfo.getId());
        miaoshaOrder.setUserId(user.getId());
        miaoShaOrderDao.insertSelective(miaoshaOrder);
        return orderInfo;
    }

}
