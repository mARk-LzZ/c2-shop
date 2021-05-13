package com.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.entity.OrderEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 * 
 * @author lzz
 * @email sunlightcs@gmail.com
 * @date 2021-05-10 19:45:52
 */
@Mapper
public interface OrderMapper extends BaseMapper<OrderEntity> {
    /*
    * 分页按交易状态查看所有订单(管理员) 100查看全部 1根据交易状态 2买家id 3卖家id 4商品分类 5订单号
    * */
	List<OrderEntity> queryAll(@Param("page") Integer page, @Param("count") Integer count  ,@Param("tradestatus") Integer tradestatus ,
                               @Param("buyerid") String buyerid , @Param("sellerid") String sellerid , @Param("category") String category,
                               @Param("proid") String proid);


	/*
	* 增加订单
	* */
    Integer addNewOrder(OrderEntity orderEntity);

    /*
    * 根据订单id查询订单(用户)
    * */
    OrderEntity findOrderByProid(@Param("proid") String proid);

    /*
    * 根据买家id查询订单（用户）
    * */
    List<OrderEntity> findOrderByBuyerId( @Param("page") int page ,@Param("count") int count , @Param("buyerid") String buyerid);

    /*
    * 根据卖家id查询订单（用户）
    * */
    List<OrderEntity> findOrderBySellerId (@Param("page") int page , @Param("count") int count , @Param("sellerid") String sellerid);

    /*
    * 修改订单信息
    * */
    Integer updateOrders (OrderEntity orderEntity);

    /*
    * 删除订单
    * */
    Integer deleteOrder(@Param("proid") String proid);

    /*
    * 查询所有订单总数(100查询所有 1根据卖家id 2根据买家id 3根据交易状态 4根据商品分类)
    * */
    Integer countAllOrders(@Param("sellerid") String selerid , @Param("tradestatus") Integer tradestatus ,
                                     @Param("buyerid") String buyerid ,@Param("category") String category);


}
