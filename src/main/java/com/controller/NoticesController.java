package com.controller;


import com.entity.Notices;
import com.service.NoticesService;
import com.util.StatusCode;
import com.vo.PageVo;
import com.vo.ResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * <p>
 *  消息通知控制器
 * </p>
 *
 * @author lzz
 * @since 2021-3-18
 */
@RestController
@Api(description="消息通知控制器")
public class NoticesController {
    @Autowired
    private NoticesService noticesService;

    /**
     * 用户查看通知消息后
     * 1.前端传入通知id（id）
     * 2.将其设置为已读
     * */
    @PutMapping("/notices/look/{id}")
    @ApiOperation("用户查看通知消息后 设置为已读")
    @ApiImplicitParam(name="id" , value="消息id")
    public ResultVo LookNoticesById (@PathVariable("id") String id) {
        Integer i = noticesService.updateNoticesById(id);
        if (i == 1){
            return new ResultVo(true, StatusCode.OK,"设置成功");
        }
        return new ResultVo(true, StatusCode.ERROR,"设置失败");
    }

    /**
     *查询前10条公告
     * **/
    @GetMapping("/notices/queryNotices")
    @ApiOperation(value="查询前10条公告")
    public ResultVo queryNotices (@ApiIgnore HttpSession session){
        String userid = (String) session.getAttribute("userid");
        List<Notices> noticesList = noticesService.queryNotices(userid);
        return new ResultVo(true,StatusCode.OK,"查询成功",noticesList);
    }

    /**
     * 取消新通知标志
     * 用户点击查看最新通知后会将所有通知设置为非最新通知
     * */
    @ApiOperation(value="用户点击查看最新通知后会将所有通知设置为非最新通知")
    @GetMapping("/notices/cancelLatest")
    public ResultVo CancelLatest (@ApiIgnore HttpSession session){
        String userid = (String) session.getAttribute("userid");
        Integer i = noticesService.CancelLatest(userid);
        if (i == 1){
            return new ResultVo(true,StatusCode.OK,"设置成功");
        }
        return new ResultVo(true,StatusCode.ERROR,"设置失败");
    }

    /**
     * 分类分页查询用户所有通知消息
     * 1.前端传入消息通知类型（tpname）
     * 2.session中获取用户id（userid）
     * 3.返回分页数据
     * */
    @GetMapping("/notices/queryall")
    @ApiOperation(value="分类分页查询用户所有通知消息")
    @ApiImplicitParams({@ApiImplicitParam(name="limit",value="数量限制"),@ApiImplicitParam(name="page",value="页数")})

    public PageVo queryallSold(int limit, int page, @ApiIgnore HttpSession session) {
        String userid = (String) session.getAttribute("userid");
        List<Notices> noticesList = noticesService.queryAllNotices((page - 1) * limit, limit, userid);
        Integer dataNumber = noticesService.queryNoticesCount(userid);
        return new PageVo(0, "",dataNumber,noticesList);
    }

}

