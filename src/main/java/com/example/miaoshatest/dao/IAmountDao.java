package com.example.miaoshatest.dao;

import com.example.miaoshatest.dao.bean.Amount;
import com.example.miaoshatest.dao.bean.Depth;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
@Mapper
@Component
public interface IAmountDao {

    void saveAmount(Amount amount);

    List<Amount> getAmount(int num, int size);

}
