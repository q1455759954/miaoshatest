package com.example.miaoshatest.service;

import com.example.miaoshatest.entity.GoodsVo;
import com.example.miaoshatest.entity.ResultGeekQ;

import java.util.List;

public interface IGoodsService {


    ResultGeekQ<List<GoodsVo>> goodsVoList();

    ResultGeekQ<GoodsVo> goodsVoByGoodId(Long valueOf);
}
