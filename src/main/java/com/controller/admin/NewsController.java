package com.controller.admin;


import com.alibaba.fastjson.JSONObject;
import com.entity.News;
import com.service.NewsService;
import com.util.JsonReader;
import com.util.KeyUtil;
import com.util.StatusCode;
import com.vo.PageVo;
import com.vo.ResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;


@Controller
@Api(description="公告控制器")
public class NewsController {
    @Autowired
    private NewsService newsService;

    /**
     * 发布公告
     * 1.传入公告标题（newstitle），公告简介（newsdesc）、公告内容（newscontent），简介图（image）
     * 2.填写session获取的发布者
     * */
    @ResponseBody
    @PostMapping("/news/insert")
    @ApiOperation(value="发布公告")
    @ApiImplicitParams({@ApiImplicitParam(name="newstitle",value="公告标题"),@ApiImplicitParam(name="newsdesc",value="公告简介"),
            @ApiImplicitParam(name="newscontent",value="公告内容"),@ApiImplicitParam(name="session")})
    public ResultVo insertNews(@RequestBody News news,@ApiIgnore HttpSession session){
        String username=(String) session.getAttribute("username");
        news.setId(KeyUtil.genUniqueKey()).setUsername(username);
        Integer i = newsService.insertNews(news);
        if (i == 1){
            return new ResultVo(true, StatusCode.OK,"公告发布成功");
        }
        return new ResultVo(false,StatusCode.ERROR,"公告发布失败，请重新发布");
    }

    /**
     * 删除公告
     * 1.前端传入需删除公告的id
     * 2.判断是否是本人或超级管理员
     * */
    @ResponseBody
    @PutMapping("/news/delect/{id}")
    @ApiOperation(value="删除公告")
    @ApiImplicitParams({@ApiImplicitParam(name="id",value="需要删除公告的id"),@ApiImplicitParam(name="session")})
    public ResultVo delectNews (@PathVariable("id") String id,@ApiIgnore HttpSession session) {
        String username = (String) session.getAttribute("username");
        News news = newsService.queryNewsById(id);
        if (StringUtils.isEmpty(news)){
            return new ResultVo(false,StatusCode.FINDERROR,"找不到要删除的公告");
        }else {
            /**判断是否是本人或超级管理员*/
            if (news.getUsername().equals(username) || username.equals("admin")){
                Integer i = newsService.delectNews(id);
                if (i == 1){
                    return new ResultVo(true,StatusCode.OK,"删除成功");
                }
                return new ResultVo(false,StatusCode.ERROR,"删除失败");
            }else {
                return new ResultVo(false,StatusCode.ACCESSERROR,"权限不足，无法删除");
            }
        }
    }

    /**
     *查看公告详情
     * **/
    @GetMapping("/news/detail/{id}")
    @ApiOperation(value="查看公告详情")
    @ApiImplicitParams({@ApiImplicitParam(name="id",value="公告的id")})
    public ResultVo queryNewsById (@PathVariable("id") String id){
        //浏览量+1
        newsService.addNewsReadnumber(id);
        News news = newsService.queryNewsById(id);
        if (StringUtils.isEmpty(news)){
            return new ResultVo(false,StatusCode.FINDERROR,"查无此公告",news);
        }

        return new ResultVo(true,StatusCode.OK,"查询成功",news);

    }

    /**
     *修改公告
     * **/
    @ResponseBody
    @PutMapping("/news/update")
    @ApiImplicitParam(name="",value="需要修改的部分")
    public ResultVo updateNews (@RequestBody News news){
        Integer i = newsService.updateNews(news);
        if (i == 1){
            return new ResultVo(true,StatusCode.OK,"修改成功");
        }
        return new ResultVo(false,StatusCode.ERROR,"修改失败");
    }

    /**
     *查询前三条公告
     * **/

    @GetMapping("/news/all")
    @ApiOperation(value="查询前三条公告")
    public ResultVo queryNews (){
        List<News> newslist = newsService.queryNews();
        return new ResultVo(true,StatusCode.OK,"查询成功",newslist);
    }

    /**
     * 后台分页查看公告列表
     * 1.前端传入页码、分页数量
     * 2.查询分页数据
     */
    @PostMapping("/news/queryall")
    @ApiOperation(value="后台分页查看公告列表")
    @ApiImplicitParams({@ApiImplicitParam(name="limit" , value="分页数量"), @ApiImplicitParam(name="page" , value="页码")})
    public List<News> queryAllNews(HttpServletRequest httpServletRequest , Integer limit, Integer page) throws IOException {
        JSONObject jsonObject =JsonReader.receivePost(httpServletRequest);
        limit = jsonObject.getInteger("limit");
        page = jsonObject.getInteger("page");
        return  newsService.queryAllNews(page, limit);

    }

    /**
     * 首页公告分页数据
     * */
    @GetMapping("/news/index/number")
    @ResponseBody
    public PageVo newsNumber(){
        Integer dataNumber = newsService.LookNewsCount();
        return new PageVo(StatusCode.OK,"查询成功",dataNumber);
    }

    /**
     * 首页网站公告
     * 1.前端传入页码、分页数量
     * 2.查询分页数据
     * */
    @GetMapping("/news/index/{page}")
    @ApiOperation("首页网站公告")
    @ApiImplicitParams({@ApiImplicitParam(name="page" , value="页码")})
    public ResultVo newsIndex(@PathVariable("page") Integer page){
        List<News> newsList = newsService.queryAllNews((page - 1) * 9, 9);
        return new ResultVo(true,StatusCode.OK,"查询成功",newsList);
    }

}