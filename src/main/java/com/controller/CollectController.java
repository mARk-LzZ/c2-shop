package com.controller;


import com.entity.Collect;
import com.service.CollectService;
import com.util.GetDate;
import com.util.KeyUtil;
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
 *  收藏控制器
 * </p>
 *
 * @author lzz
 * @since 2021-3-18
 */
@RestController
@Api(description="收藏")
public class CollectController {
    @Autowired
    private CollectService collectService;

    /**
     * 商品详情界面：收藏商品or取消收藏
     * 前端传入收藏操作（colloperate：1收藏，2取消收藏）,获取session中用户id信息，判断是否登录
     * (1). 收藏商品
     * 1.前端传入商品id（commid）、商品名（commname）、商品描述（commdesc）、商品用户id（cmuserid）
     *   商品用户名（username）、商品所在学校（school）
     * 2.session中获取收藏用户id（couserid）
     * 3.进行收藏操作
     * (2). 取消收藏
     * 1.前端传入商品id（commid）
     * 2.判断是否本人取消收藏
     * 3.进行取消收藏操作
     */
    @PostMapping("/collect/operate")
    @ApiOperation("商品详情界面：收藏商品or取消收藏")
    @ApiImplicitParams({@ApiImplicitParam(name="commid",value="商品id"),@ApiImplicitParam(name="commname",value="商品名"),
    @ApiImplicitParam(name="commdesc",value="商品描述"), @ApiImplicitParam(name="cmuserid",value="商品用户id"),
    @ApiImplicitParam(name="username",value="商品用户名"),@ApiImplicitParam(name="school",value="商品所属学校")})
    public ResultVo insertcollect(@RequestBody Collect collect,@ApiIgnore HttpSession session){
        String couserid = (String) session.getAttribute("userid");
        Integer colloperate = collect.getColloperate();
        collect.setCouserid(couserid);

        if (StringUtils.isEmpty(couserid)){
            return new ResultVo(false, StatusCode.ACCESSERROR,"请先登录");
        }

        if (colloperate == 1){
            Collect collect1 = collectService.queryCollectStatus(collect);
            if(!StringUtils.isEmpty(collect1)){
                /**更改原来的收藏信息和状态*/
                collect1.setCommname(collect.getCommname()).setCommdesc(collect.getCommdesc()).setSchool(collect.getSchool())
                        .setSoldtime(GetDate.strToDate());
                Integer i = collectService.updateCollect(collect);
                if (i == 1){
                    return new ResultVo(true, StatusCode.OK,"收藏成功");
                }
                return new ResultVo(false,StatusCode.ERROR,"收藏失败");
            }else{
                collect.setId(KeyUtil.genUniqueKey());
                Integer i = collectService.insertCollect(collect);
                if (i == 1){
                    return new ResultVo(true, StatusCode.OK,"收藏成功");
                }
                return new ResultVo(false,StatusCode.ERROR,"收藏失败");
            }

        }else {
            Collect collect1 = collectService.queryCollectStatus(collect);
            /**判断是否为本人操作*/
            if (collect1.getCouserid().equals(couserid)){
                Integer i = collectService.updateCollect(collect);
                if (i == 1){
                    return new ResultVo(true, StatusCode.OK,"取消成功");
                }
                return new ResultVo(false,StatusCode.ERROR,"取消失败");
            }
            return new ResultVo(false,StatusCode.ACCESSERROR,"禁止操作");
        }
    }

    /**
     * 收藏列表界面取消收藏
     * 1.前端传入收藏id（id）
     * 2.判断是否本人取消收藏
     * 3.进行取消收藏操作
     */
    @PutMapping("/collect/delete/{id}")
    @ApiOperation("收藏列表界面取消收藏")
    @ApiImplicitParam(name="id" , value="收藏id")
    public ResultVo deletecollect(@PathVariable("id") String id,@ApiIgnore HttpSession session){
        String couserid = (String) session.getAttribute("userid");
        Collect collect = new Collect().setId(id).setCouserid(couserid);
        Collect collect1 = collectService.queryCollectStatus(collect);
        /**判断是否为本人操作*/
        if (collect1.getCouserid().equals(couserid)){
            collect.setColloperate(2);
            Integer i = collectService.updateCollect(collect);
            if (i == 1){
                return new ResultVo(true, StatusCode.OK,"取消成功");
            }
            return new ResultVo(false,StatusCode.ERROR,"取消失败");
        }
        return new ResultVo(false,StatusCode.ACCESSERROR,"禁止操作");
    }

    /**
     * 分页查看用户所有收藏内容
     * 前端传入页码、分页数量
     * 查询分页数据
     */
    @GetMapping("/user/collect/queryall")
    @ApiOperation("分页查看用户所有收藏内容")
    @ApiImplicitParams({@ApiImplicitParam(name="page", value="页码"), @ApiImplicitParam(name="limit" , value="分页数量")})
    public PageVo usercollect(int limit, int page,@ApiIgnore HttpSession session) {
        String couserid = (String) session.getAttribute("userid");
        List<Collect> collectList = collectService.queryAllCollect((page - 1) * limit, limit, couserid);
        Integer dataNumber = collectService.queryCollectCount(couserid);
        return new PageVo(0,"",dataNumber,collectList);
    }
}

