package com.example.miaoshatest.service;

import com.example.miaoshatest.dao.bean.MiaoShaUser;
import com.example.miaoshatest.entity.ResultGeekQ;

public interface IMiaoShaService {

    ResultGeekQ<String> creatMiaoShaPath(MiaoShaUser user, long goodsId);

    ResultGeekQ<Boolean> checkPath(MiaoShaUser user, long goodsId, String path);
}
