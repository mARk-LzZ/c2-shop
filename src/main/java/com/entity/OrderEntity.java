package com.entity;


import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * 
 * @author lzz
 * @email sunlightcs@gmail.com
 * @date 2021-05-10 19:45:52
 */
@Data
@TableName("order")
public class OrderEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 订单id
	 */
	@TableId
	private String proid;
	/**
	 * 商品id
	 */
	private String commid;
	/**
	 * 买家id
	 */
	private String buyerid;
	/**
	 * 卖家id
	 */
	private String sellerid;
	/**
	 * 发货地址
	 */
	private String senturl;
	/**
	 * 收货地址
	 */
	private String receiveurl;
	/**
	 * 订单创建时间
	 */
	private Date procreatetime;
	/**
	 * 买家名称
	 */
	private String buyername;
	/**
	 * 卖家名称
	 */
	private String sellername;
	/**
	 * 订单金额（含运费）
	 */
	private BigDecimal price;
	/**
	 * 运费
	 */
	private BigDecimal freight;
	/**
	 * 交易状态 0交易失败 1交易成功 2待交易
	 */
	private Integer tradestatus;
	/**
	 * 支付方式
	 */
	private String payway;
	/**
	 * 商品名称
	 */
	private String commname;
	/**
	 * 商品类型
	 */
	private String category;
	/**
	 * 买家手机号
	 */
	private String buyerphone;
	/**
	 * 卖家手机号

	 */
	private String sellerphone;
	/**
	 * 商品描述
	 */
	private String commdesc;

}
