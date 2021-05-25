package com.example.miaoshatest.dao;

import com.example.miaoshatest.dao.bean.Depth;
import com.example.miaoshatest.dao.bean.Price;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author EDZ
 */
@Mapper
@Component
public interface IDepthDao {

    void saveDepth(Depth depth);

    List<Depth> getDepth(int num, int size);


}
