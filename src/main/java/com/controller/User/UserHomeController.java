package com.controller.User;

import com.entity.Commodity;
import com.entity.UserInfo;
import com.service.CommodityService;
import com.service.UserInfoService;
import com.util.StatusCode;
import com.vo.PageVo;
import com.vo.ResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: lzz
 * @Description: 个人主页
 * @Date: 2021/3/23 18:07
 */
@RestController
@Api(description="个人主页")
public class UserHomeController {
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private CommodityService commodityService;

    /**
     * 个人简介
     * 前端传入用户id（userid）
     */
    @GetMapping("/user/userinfo/{userid}")
    @ApiOperation(value="个人简介")
    @ApiImplicitParam(name="userid" , value="id")
    public ResultVo userinfo(@PathVariable("userid") String userid) {
        UserInfo userInfo = userInfoService.LookUserinfoById(userid);
        if (!StringUtils.isEmpty(userInfo)){
            return new ResultVo(true, StatusCode.OK, "查询成功",userInfo);
        }
        return new ResultVo(false, StatusCode.ERROR, "查询失败");
    }

    /**
     * 分页展示个人已审核的商品信息（状态码：1）
     *前端传入用户id（userid）、当前页码（nowPaging）、
     */
    @GetMapping("/user/usercommodity/{userid}")
    @ApiOperation(value="分页展示个人已审核的商品信息")
    @ApiImplicitParams({@ApiImplicitParam(name="userid",value="用户id"), @ApiImplicitParam(name="page",value="页码"), @ApiImplicitParam(name="limit",value="每页条数")})
    public PageVo userHomeCommodity(@PathVariable("userid") String userid,int limit, int page) {
        List<Commodity> commodityList = commodityService.queryAllCommodity((page - 1) * limit, limit, userid,1);
        Integer dataNumber = commodityService.queryCommodityCount(userid,1);
        return new PageVo(1, "",dataNumber,commodityList);
    }

}
