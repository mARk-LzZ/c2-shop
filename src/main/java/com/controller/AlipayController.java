package com.controller;


import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.config.AlipayConfig;
import com.entity.Commodity;
import com.entity.OrderEntity;
import com.service.CommodityService;
import com.service.OrderService;
import com.util.StatusCode;
import com.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.apache.log4j.Logger;


import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@RestController
public class AlipayController {
    @Autowired
   private OrderService orderService ;
    @Autowired
    private CommodityService commodityService;

    private static final Logger logger = Logger.getLogger(AlipayController.class);

    private final AlipayConfig alipayConfig=new AlipayConfig();

    private OrderEntity orderEntity = new OrderEntity();

    private Commodity commodity = new Commodity();

    @RequestMapping("/alipay")
    public String aliPay (String proid) throws AlipayApiException {
        orderEntity = orderService.findOrderByProid(proid);

         commodity = commodityService.LookCommodity(new Commodity().setCommid(orderEntity.getCommid()));



        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(alipayConfig.GATEWAY_URL, alipayConfig.APP_ID,alipayConfig.APP_PRIVATE_KEY, "json", alipayConfig.CHARSET, alipayConfig.ALIPAY_PUBLIC_KEY, alipayConfig.SIGN_TYPE);

        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(alipayConfig.RETURN_URL);
        alipayRequest.setNotifyUrl(alipayConfig.NOTIFY_URL);

        String out_trade_no = proid;
        //付款金额，必填
        BigDecimal total_amount = orderEntity.getPrice();
        //订单名称，必填
        String subject = orderEntity.getCommname();

        //商品描述
        String body = orderEntity.getCommdesc();

//       该笔订单允许的最晚付款时间
        String timeout_express = "1c";

        alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                + "\"total_amount\":\""+ total_amount +"\","
                + "\"subject\":\""+ subject +"\","
                + "\"body\":\""+ body +"\","
                + "\"timeout_express\":\""+ timeout_express +"\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        return alipayClient.pageExecute(alipayRequest).getBody();


    }

    /*
    * 阿里同步通知（本地）
    * */




    /*
    *
    * 阿里异步通知（服务器）
    * */

    @RequestMapping(value = "alipayNotifyNotice")
    public ResultVo alipayNotifyNotice(HttpServletRequest request, HttpServletRequest response) throws Exception {

        logger.info("支付成功, 进入异步通知接口...");

        //获取支付宝POST过来反馈信息
        Map<String,String> params = new HashMap<>();
        Map<String,String[]> requestParams = request.getParameterMap();
        for (String name : requestParams.keySet()) {
            String[] values=requestParams.get(name);
            String valueStr="";
            for (int i=0; i < values.length; i++) {
                valueStr=(i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            /*valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");*/
            params.put(name, valueStr);
        }

        //调用SDK验证签名
        boolean signVerified = AlipaySignature.rsaCheckV1(params,alipayConfig.ALIPAY_PUBLIC_KEY , alipayConfig.CHARSET, alipayConfig.SIGN_TYPE);

   /* 实际验证过程建议商户务必添加以下校验：
   1、需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
   2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
   3、校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）
   4、验证app_id是否为该商户本身。
   */
        //验证成功
        if(signVerified) {
            //商户订单号
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);

            //支付宝交易号
            String trade_no = new String(request.getParameter("trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);

            //交易状态
            String trade_status = new String(request.getParameter("trade_status").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);

            //付款金额
            String total_amount = new String(request.getParameter("total_amount").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);

            if("TRADE_FINISHED".equals(trade_status)){
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //如果有做过处理，不执行商户的业务程序
                orderEntity = orderService.findOrderByProid(out_trade_no);
                orderEntity.setTradestatus(0);
                orderService.updateOrders(orderEntity);
                //退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
            }else if ("TRADE_SUCCESS".equals(trade_status)){
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //如果有做过处理，不执行商户的业务程序
                orderEntity = orderService.findOrderByProid(out_trade_no);
                orderEntity.setTradestatus(1);
                //注意：
                //付款完成后，支付宝系统发送该交易状态通知

                // 修改叮当状态，改为 支付成功，已付款; 同时新增支付流水
                orderService.updateOrders(orderEntity);

                OrderEntity orderEntity1 = orderService.findOrderByProid(out_trade_no);
                commodity = commodityService.LookCommodity(new Commodity().setCommid(orderEntity1.getCommid()));

                logger.info("********************** 支付成功(支付宝异步通知) **********************");
                logger.info("* 订单号: {}" +  out_trade_no);
                logger.info("* 支付宝交易号: {}" + trade_no);
                logger.info("* 实付金额: {}" + total_amount);
                logger.info("* 购买产品: {}" + commodity.getCommname());
                logger.info("***************************************************************");
            }
            logger.info("支付成功...");
        }else {//验证失败
            logger.info("支付, 验签失败...");
        }
        return new ResultVo(true , StatusCode.OK , "支付完成");
    }

}
