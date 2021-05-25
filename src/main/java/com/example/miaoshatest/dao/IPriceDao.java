package com.example.miaoshatest.dao;

import com.example.miaoshatest.dao.bean.Price;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author EDZ
 */
@Mapper
@Component
public interface IPriceDao {

    void savePrice(Price price);

    List<Price> getPrice(int num,int size);

}
