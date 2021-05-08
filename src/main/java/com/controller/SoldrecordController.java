package com.controller;

import com.entity.Soldrecord;
import com.service.SoldrecordService;
import com.util.StatusCode;
import com.vo.PageVo;
import com.vo.ResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * <p>
 *  销售记录控制器
 * </p>
 *
 * @author lzz
 * @since 2021-3-18
 */
@Api(description="销售记录控制器")
@RestController
public class SoldrecordController {
    @Autowired
    private SoldrecordService soldrecordService;

    /**
     * 删除售出记录
     * 1.前端传入需删除记录的id（id）
     * 2.判断是否是本人
     * */
    @PutMapping("/soldrecord/delect/{id}")
    @ApiOperation(value="删除售出记录")
    @ApiImplicitParam(name="id" , value="删除记录的id")
    public ResultVo delectSold (@PathVariable("id") String id) {
        Integer i = soldrecordService.deleteSold(id);
        if (i == 1){
            return new ResultVo(true, StatusCode.OK,"删除记录成功");
        }
        return new ResultVo(false, StatusCode.ERROR,"删除记录失败");
    }

    /**
     * 分页查看用户所有售出记录
     * 1.前端传入页码、分页数量
     * 2.查询分页数据
     */
    @ResponseBody
    @GetMapping("/soldrecord/lookuser")
    @ApiOperation(value="分页查看用户所有售出记录")
    @ApiImplicitParams({@ApiImplicitParam(name="limit" , value="分页数量"),@ApiImplicitParam(name="page", value="页码"),@ApiImplicitParam(name="session")})
    public PageVo LookUserSold(int limit, int page, @ApiIgnore HttpSession session) {
        String userid = (String) session.getAttribute("userid");

        if(StringUtils.isEmpty(userid)){
            userid = "-1";
        }
        List<Soldrecord> soldrecordList = soldrecordService.queryAllSoldrecord((page - 1) * limit, limit, userid);
        Integer dataNumber = soldrecordService.querySoldCount(userid);
        return new PageVo(0,"",dataNumber,soldrecordList);
    }

    /**
     * 分页查看全部的售出记录
     * 1.前端传入页码、分页数量
     * 2.查询分页数据
     */
    @ResponseBody
    @GetMapping("/soldrecord/queryall")
    @ApiOperation(value="分页查看全部的售出记录")
    @ApiImplicitParams({@ApiImplicitParam(name="limit" , value="分页数量"),@ApiImplicitParam(name="page", value="页码")})
    public PageVo queryAllSold(int limit, int page) {
        List<Soldrecord> soldrecordList = soldrecordService.queryAllSoldrecord((page - 1) * limit, limit, null);
        Integer dataNumber = soldrecordService.querySoldCount(null);
        return new PageVo(0,"",dataNumber,soldrecordList);
    }

}

