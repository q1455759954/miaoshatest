package com.example.miaoshatest.redis.keysbean;


import com.example.miaoshatest.redis.BasePrefix;

public class GoodsKey extends BasePrefix {

	private GoodsKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}
	public static GoodsKey getGoodsList = new GoodsKey(60, "gl");
	public static GoodsKey getGoodsDetail = new GoodsKey(60, "gd");
	public static GoodsKey getMiaoshaGoodsStock = new GoodsKey(0, "gs");
	public static GoodsKey getMiaoshaGoodsStartTime = new GoodsKey(0, "sj");

}
