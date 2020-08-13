package com.example.miaoshatest.redis.keysbean;


import com.example.miaoshatest.common.CustomerConstant;
import com.example.miaoshatest.redis.BasePrefix;

public class MiaoshaKey extends BasePrefix {

	private MiaoshaKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}
	public static MiaoshaKey isGoodsOver = new MiaoshaKey(0, "go");
	public static MiaoshaKey getMiaoshaPath = new MiaoshaKey(60, "mp");
	public static MiaoshaKey getMiaoshaVerifyCode = new MiaoshaKey(300, "vc");
	public static MiaoshaKey getMiaoshaVerifyCodeRegister = new MiaoshaKey(300, "register");

	public static String getMiaoshaOrderRedisKey(String accountId, String productId) {
		return CustomerConstant.RedisKeyPrefix.MIAOSHA_ORDER + "_" + accountId + "_" + productId;
	}

	public static String getMiaoshaOrderWaitFlagRedisKey(String accountId, String productId) {
		return CustomerConstant.RedisKeyPrefix.MIAOSHA_ORDER_WAIT + "_" + accountId + "_" + productId;
	}
}
