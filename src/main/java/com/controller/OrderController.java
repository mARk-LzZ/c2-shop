package com.controller;


import com.entity.Commodity;
import com.entity.OrderEntity;
import com.entity.UserInfo;
import com.service.CommodityService;
import com.service.OrderService;
import com.service.UserInfoService;
import com.util.KeyUtil;
import com.util.StatusCode;
import com.vo.PageVo;
import com.vo.ResultVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.sql.Date;
import java.util.List;


/**
 * 
 *
 * @author lzz
 * @email sunlightcs@gmail.com
 * @date 2021-05-10 19:45:52
 */
@RestController
@RequestMapping("order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private CommodityService commodityService;



    /**
     * 列表
     * @return
     */
    @RequestMapping(value="/list" , method=RequestMethod.GET)
    public PageVo list(int count , int page , HttpSession httpSession){
        String buyerid =(String) httpSession.getAttribute("userid");
        List<OrderEntity> orderList = orderService.findOrderByBuyerId((page-1)*count, count ,buyerid);

        return new PageVo(0 , "" , count , orderList);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{proid}")
    public ResultVo info(@PathVariable("proid") String proid){
		OrderEntity order = orderService.findOrderByProid(proid);

        return new ResultVo(true , StatusCode.OK ,"查询成功", order);
    }

    /**
     * 保存
     * 前端传入 卖家id 商品id 运费 发货地址 收货地址 支付方式
     * 0交易失败 1交易成功 2待交易
     */
    @PostMapping("/insertorder")
    public ResultVo save(@RequestBody OrderEntity order , HttpSession session){
		String userid = (String) session.getAttribute("userid");
		order.setProid(KeyUtil.genUniqueKey());
		UserInfo buyer = userInfoService.LookUserinfoById(userid);
		UserInfo seller = userInfoService.LookUserinfoById(order.getSellerid());
		Commodity commodity = commodityService.LookCommodity( new Commodity().setCommid(order.getCommid()));
		order.setBuyerid(userid);
        order.setBuyername(buyer.getUsername());
        order.setBuyerphone(buyer.getMobilephone());
        order.setCommname(commodity.getCommname());
        order.setCategory(commodity.getCategory());
        order.setCommdesc(commodity.getCommdesc());
        order.setSellername(seller.getUsername());
        order.setSellerphone(seller.getMobilephone());
        order.setTradestatus(2);
//        order.setProcreatetime();
        order.setPrice(commodity.getThinkmoney().add(order.getFreight()));
        if (seller.getUserid().equals(commodity.getUserid())){
            orderService.addNewOrder(order);
            return new ResultVo(true , StatusCode.OK , "订单创建成功 ，待管理员审核");
        }
        return new ResultVo(false ,StatusCode.ACCESSERROR , "该商品不属于这个卖家噢");


    }

    /**
     * 修改 (买家id 发货地址 收货地址 价格 运费 支付方式 买家电话 卖家电话)
     */
    @PutMapping("/update")

    public ResultVo update(@RequestBody OrderEntity order){
        order = orderService.findOrderByProid(order.getProid());

		orderService.updateOrders(order);

        return new ResultVo(true , StatusCode.OK , "修改成功");
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public ResultVo delete(@RequestBody String proid){
        if (proid != null){
            orderService.deleteOrder(proid);
            return new ResultVo(true , StatusCode.OK , "删除成功");

        }
		return new ResultVo(false ,StatusCode.FINDERROR , "删除失败");

    }

}
