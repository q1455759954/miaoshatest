package com.example.miaoshatest.entity;


import com.example.miaoshatest.dao.bean.MiaoShaUser;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MiaoShaMessage {
	private MiaoShaUser user;
	private Long goodsId;
	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer("MiaoShaMessage{");
		sb.append("user=").append(user);
		sb.append(", goodsId=").append(goodsId);
		sb.append('}');
		return sb.toString();
	}
}
