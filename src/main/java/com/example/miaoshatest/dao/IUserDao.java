package com.example.miaoshatest.dao;

import com.example.miaoshatest.dao.bean.MiaoShaUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface IUserDao {

    MiaoShaUser getUserByMobile(String mobile);


}
