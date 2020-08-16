package com.example.miaoshatest.dao;

import com.example.miaoshatest.entity.MiaoShaOrder;
import com.example.miaoshatest.entity.OrderInfoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface IMiaoShaOrderDao {
    int deleteByPrimaryKey(Long id);

    int insertSelective(MiaoShaOrder record);

    MiaoShaOrder selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MiaoShaOrder record);

    public MiaoShaOrder getMiaoshaOrderByUserIdGoodsId(@Param("userId")long userId, @Param("goodsId")long goodsId);

    public List<MiaoShaOrder> listByGoodsId(@Param("goodsId") long goodsId);
}