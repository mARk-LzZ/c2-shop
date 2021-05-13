package com.service;



import com.baomidou.mybatisplus.service.IService;
import com.entity.Collect;
import com.entity.OrderEntity;
import com.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 
 *
 * @author lzz
 * @email sunlightcs@gmail.com
 * @date 2021-05-10 19:45:52
 */

@Service
@Transactional
public class OrderService  {
        @Autowired
        private OrderMapper orderMapper;

    /*
     * 分页按交易状态查看所有订单 100查看全部 1根据交易状态 2买家id 3卖家id 4商品分类 5订单号
     * */
   public List<OrderEntity> queryAll(Integer page , Integer count , Integer tradestatus , String buyerid ,String sellerid , String category , String proid){
        return orderMapper.queryAll(page, count, tradestatus, buyerid, sellerid, category, proid);
    }

    /*
     * 增加订单
     * */
    public Integer addNewOrder(OrderEntity orderEntity){
        return orderMapper.addNewOrder(orderEntity);
    }

    /*
     * 根据订单id查询订单
     * */
    public OrderEntity findOrderByProid(String proid){
        return orderMapper.findOrderByProid(proid);
    }

    /*
     * 根据买家id查询订单
     * */
    public List<OrderEntity> findOrderByBuyerId(Integer page , Integer count , String buyerid){
        return orderMapper.findOrderByBuyerId(page , count,buyerid);
    }

    /*
     * 根据卖家id查询订单
     * */
    public List<OrderEntity> findOrderBySellerId(Integer page , Integer count  , String sellerid){
        return  orderMapper.findOrderBySellerId( page, count , sellerid);
    }

    /*
     * 修改订单信息
     * */
    public Integer updateOrders (OrderEntity orderEntity){
        return orderMapper.updateOrders(orderEntity);
    }

    /*
     * 删除订单
     * */
    public Integer deleteOrder(String proid){
        return orderMapper.deleteOrder(proid);
    }

    /*
     * 查询所有订单总数(100查询所有 1根据卖家id 2根据买家id 3根据交易状态 4根据商品分类)
     * */
    public Integer countAllOrders(String sellerid ,Integer tradestatus , String buyerid , String category){
        return orderMapper.countAllOrders(sellerid, tradestatus, buyerid, category);
    }


}

