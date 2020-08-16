package com.example.miaoshatest.dao;

import com.example.miaoshatest.entity.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface IGoodsDao {


    List<GoodsVo> goodsVoList();

    GoodsVo goodsVoByGoodsId(Long goodId);

    int reduceStock(Long id);
}
