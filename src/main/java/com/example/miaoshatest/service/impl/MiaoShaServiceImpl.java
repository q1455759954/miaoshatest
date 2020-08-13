package com.example.miaoshatest.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.example.miaoshatest.dao.bean.MiaoShaUser;
import com.example.miaoshatest.entity.ResultGeekQ;
import com.example.miaoshatest.entity.ResultStatus;
import com.example.miaoshatest.redis.RedisClient;
import com.example.miaoshatest.redis.keysbean.MiaoshaKey;
import com.example.miaoshatest.service.IMiaoShaService;
import com.example.miaoshatest.util.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class MiaoShaServiceImpl implements IMiaoShaService {

    @Autowired
    RedisClient redisClient;

    @Override
    public ResultGeekQ<String> creatMiaoShaPath(MiaoShaUser user, long goodsId) {
        ResultGeekQ<String> resultGeekQ = ResultGeekQ.build();
        try {
            String str = MD5Util.md5(UUID.randomUUID().toString().replace("-","")+ "123456");
            redisClient.set(MiaoshaKey.getMiaoshaPath,user.getNickname()+"_"+goodsId,str);
            resultGeekQ.setData(str);
        }catch (Exception e){
            resultGeekQ.withErrorCodeAndMessage(ResultStatus.MIAOSHA_FAIL);
        }
        return resultGeekQ;
    }

    @Override
    public ResultGeekQ<Boolean> checkPath(MiaoShaUser user, long goodsId, String path) {
        ResultGeekQ<Boolean> result = ResultGeekQ.build();
        try {
            String p = (String) redisClient.get(MiaoshaKey.getMiaoshaPath,user.getNickname()+"_"+goodsId,String.class);
            result.setData(StringUtils.equals(path,p));
            return result;
        }catch (Exception e){
            result.withErrorCodeAndMessage(ResultStatus.MIAOSHA_FAIL);
            return result;
        }
    }

}
