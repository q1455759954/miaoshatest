package com.example.miaoshatest.dao;

import com.example.miaoshatest.dao.bean.Amount;
import com.example.miaoshatest.dao.bean.Compare;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface ICompareDao {

    void saveCompare(Compare compare);

    List<Compare> getCompare(int num, int size);

}
