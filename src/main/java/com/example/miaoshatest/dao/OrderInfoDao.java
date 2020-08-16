package com.example.miaoshatest.dao;

import com.example.miaoshatest.entity.OrderInfoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface OrderInfoDao {
    int deleteByPrimaryKey(Long id);

    int insert(OrderInfoVo record);

    Long insertSelective(OrderInfoVo record);

    OrderInfoVo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderInfoVo record);

    int updateByPrimaryKey(OrderInfoVo record);

    public OrderInfoVo getOrderById(@Param("orderId")long orderId);
}