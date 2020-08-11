package com.example.miaoshatest.service.impl;

import com.example.miaoshatest.dao.IGoodsDao;
import com.example.miaoshatest.dao.IUserDao;
import com.example.miaoshatest.entity.GoodsVo;
import com.example.miaoshatest.entity.ResultGeekQ;
import com.example.miaoshatest.entity.ResultStatus;
import com.example.miaoshatest.service.IGoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class GoodsServiceImpl implements IGoodsService {

    @Autowired
    IGoodsDao goodsDao;

    @Override
    public ResultGeekQ<List<GoodsVo>> goodsVoList() {
        ResultGeekQ<List<GoodsVo>> resultGeekQ = ResultGeekQ.build();

        try{
            log.info("***goodsVoList查询***start!");
            resultGeekQ.setData(goodsDao.goodsVoList());
        }catch(Exception e){
            log.error(" *****查询goodsvoList发生错误***** error:{}",e);
            resultGeekQ.withErrorCodeAndMessage(ResultStatus.DATA_NOT_EXISTS);
            return resultGeekQ;
        }
        return resultGeekQ;

    }

    @Override
    public ResultGeekQ<GoodsVo> goodsVoByGoodId(Long valueOf) {
        ResultGeekQ<GoodsVo> resultGeekQ = ResultGeekQ.build();

        try{
            log.info("***goodsVoList查询***start!");
            resultGeekQ.setData(goodsDao.goodsVoByGoodsId(valueOf));
        }catch(Exception e){
            log.error(" *****查询goodsVoByGoodId发生错误***** error:{}",e);
            resultGeekQ.withErrorCodeAndMessage(ResultStatus.DATA_NOT_EXISTS);
            return resultGeekQ;
        }
        return resultGeekQ;
    }
}
