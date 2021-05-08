package com.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.entity.*;
import com.service.*;
import com.util.JsonReader;
import com.util.JustPhone;
import com.util.KeyUtil;
import com.util.StatusCode;
import com.vo.PageVo;
import com.vo.ResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;


@RestController

@Api(description= "管理员控制器" )
public class AdminController {
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private LoginService loginService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private CommodityService commodityService;
    @Autowired
    private NoticesService noticesService;

    /**
     * 管理员跳转登录
     */
//    @GetMapping("/admin")
//    public String admintologin() {
//        return "admin/login/login";
//    }

    /**
     * 管理员登录
     * 1.判断输入账号的类型
     * 2.判断是否为管理员或者超级管理员
     * 3.登录
     * */
    @PostMapping("/admin/login")
    @ApiOperation(value="管理员登录")
    @ApiImplicitParams({@ApiImplicitParam(name="username", value="用户名"),@ApiImplicitParam(name="password",value="密码"),
            @ApiImplicitParam(name="session", value="session")})
    public ResultVo adminlogin(@RequestBody Login login,@ApiIgnore HttpSession session){
        String account=login.getUsername();
        String password=login.getPassword();
//        String vercode=login.getVercode();
        UsernamePasswordToken token;
//        if(!ValidateCode.code.equalsIgnoreCase(vercode)){
//            return new ResultVo(false,StatusCode.ERROR,"请输入正确的验证码");
//        }
        //判断输入的账号是否手机号
        if (!JustPhone.justPhone(account)) {
            //输入的是用户名
            String username = account;
            //盐加密
            token=new UsernamePasswordToken(username, new Md5Hash(password,"Campus-shops").toString());
        }else {
            //输入的是手机号
            String mobilephone = account;
            login.setMobilephone(mobilephone);
            //将封装的login中username变为null
            login.setUsername(null);
            //盐加密
            token=new UsernamePasswordToken(mobilephone, new Md5Hash(password,"Campus-shops").toString());
        }
        Subject subject= SecurityUtils.getSubject();
        try {
            subject.login(token);
            //盐加密
            String passwords = new Md5Hash(password, "Campus-shops").toString();
            login.setPassword(passwords);
            Login login1 = loginService.userLogin(login);
            //查询登录者的权限
            Integer roleId = userRoleService.LookUserRoleId(login1.getUserid());
            if (roleId == 2 || roleId == 3){
                session.setAttribute("admin",login1.getUsername());
                session.setAttribute("username",login1.getUsername());
                return new ResultVo(true,StatusCode.OK,"登录成功");
            }
            return new ResultVo(true,StatusCode.ACCESSERROR,"权限不足");
        }catch (UnknownAccountException e){
            return new ResultVo(true,StatusCode.LOGINERROR,"用户名不存在");
        }catch (IncorrectCredentialsException e){
            return new ResultVo(true,StatusCode.LOGINERROR,"密码错误");
        }
    }

    /*
    *   根据id查用户
    *
    */
    @PostMapping("/admin/finduserbyid")
    @ApiOperation(value="根据id查用户")
    @ApiImplicitParam(name="userid" , value="用户id")
    public ResultVo findOneUserById(HttpServletRequest httpServletRequest) throws IOException {
        JSONObject jsonObject = JsonReader.receivePost(httpServletRequest);
        String userid = jsonObject.getString("userid");
        UserInfo user = userInfoService.LookUserinfoById(userid);
        if (user != null){
            return new ResultVo(true, StatusCode.OK , "查询成功" , user);
        }
        return new ResultVo(false,StatusCode.FINDERROR , "无此用户" , user);
    }

    @PostMapping("/admin/finduserbyname")
    @ApiOperation(value="根据用户名查用户")
    @ApiImplicitParam(name="username" , value="用户名")
    public ResultVo findOneUserByName(HttpServletRequest httpServletRequest) throws IOException {
        JSONObject jsonObject = JsonReader.receivePost(httpServletRequest);
        String username = jsonObject.getString("username");
        List<UserInfo> user = userInfoService.LookUserinfoByName(username);
        if (user != null){
            return new ResultVo(true, StatusCode.OK , "查询成功" , user);
        }
        return new ResultVo(false,StatusCode.FINDERROR , "无此用户" , user);
    }
    /**
     * 用户列表
     * */

    @PostMapping("/admin/userlist")
    @ApiOperation(value="用户列表")
    @ApiImplicitParams({@ApiImplicitParam(name="count" , value="分页数量"), @ApiImplicitParam(name="page" , value="页码")})
    public PageVo userlist(HttpServletRequest httpServletRequest) throws IOException {
        JSONObject jsonObject =JsonReader.receivePost(httpServletRequest);
        Integer page=jsonObject.getInteger("page");
        Integer count=jsonObject.getInteger("count");
        Integer roleid = 1;
        Integer userstatus = jsonObject.getInteger("userstatus");
        System.out.println("page "+ page + " count " + count + " roleid "+roleid + " userstatus" + userstatus  );
        List<UserInfo> userInfoList =  userInfoService.queryAllUserInfo(page, count,roleid,userstatus);
        return new PageVo(0,"查询成功",page,userInfoList);


    }

    /**
     * 管理员列表
     * */
//    @RequiresPermissions("admin:set")
//    @GetMapping("/admin/adminlist")
//    public String adminlist(){
//        return "/admin/user/adminlist";
//    }

    /**
     * 分页查询不同角色用户信息
     * roleid：1普通成员 2管理员
     * userstatus：1正常 0封号
     */

    @GetMapping("/admin/userlist/{roleid}/{userstatus}")
    @ApiOperation(value="分页查询用户信息")
    @ApiImplicitParams({@ApiImplicitParam(name="limit",value="每页数量限制"),@ApiImplicitParam(name="roleid",value="用户权限"),@ApiImplicitParam(name="userstatus",value="是否封号状态")})
    public PageVo userlist(int limit, int page,@PathVariable("roleid") Integer roleid,@PathVariable("userstatus") Integer userstatus) {
        List<UserInfo> userInfoList = userInfoService.queryAllUserInfo((page - 1) * limit, limit,roleid,userstatus);
        Integer dataNumber = userInfoService.queryAllUserCount(roleid);
        return new PageVo(0,"查询成功",dataNumber,userInfoList);
    }

    /**
     * 设置为管理员或普通成员（roleid）
     * 1：普通成员   2：管理员
     */
    @PutMapping("/admin/set/administrator/{userid}/{roleid}")
    @ApiOperation(value="设置管理员或普通用户")
    @ApiImplicitParam(name="userid" ,value="用户id")
    @ApiImplicitParams({@ApiImplicitParam(name="userid" ,value="用户id"), @ApiImplicitParam(name="roleid" , value="用户权限 1普通 2管理")})

    public ResultVo setadmin(@PathVariable("userid") String userid,@PathVariable("roleid") Integer roleid) {
        if (roleid == 2){
            Integer i = loginService.updateLogin(new Login().setUserid(userid).setRoleid(roleid));
            if (i == 1){
                userRoleService.UpdateUserRole(new UserRole().setUserid(userid).setRoleid(2).setIdentity("网站管理员"));
                /**发出设置为管理员的系统通知*/
                Notices notices = new Notices().setId(KeyUtil.genUniqueKey()).setUserid(userid).setTpname("系统通知")
                        .setWhys("您已被设置为网站管理员");
                noticesService.insertNotices(notices);
                return new ResultVo(true, StatusCode.OK, "设置管理员成功");
            }
            return new ResultVo(true, StatusCode.ERROR, "设置管理员失败");
        }else if (roleid == 1){
            Integer i = loginService.updateLogin(new Login().setUserid(userid).setRoleid(roleid));
            if (i == 1){
                userRoleService.UpdateUserRole(new UserRole().setUserid(userid).setRoleid(1).setIdentity("网站用户"));
                /**发出设置为网站用户的系统通知*/
                Notices notices = new Notices().setId(KeyUtil.genUniqueKey()).setUserid(userid).setTpname("系统通知")
                        .setWhys("您已被设置为网站用户");
                noticesService.insertNotices(notices);
                return new ResultVo(true, StatusCode.OK, "设置成员成功");
            }
            return new ResultVo(true, StatusCode.ERROR, "设置成员失败");
        }
        return new ResultVo(false,StatusCode.ACCESSERROR,"违规操作");
    }

    /**
     * 将用户封号或解封（userstatus）
     * 0：封号  1：解封
     */
    @PutMapping("/admin/user/forbid/{userid}/{userstatus}")
    @ApiOperation(value="用户封号或解封")
    @ApiImplicitParams({@ApiImplicitParam(name="userid" ,value="用户id"), @ApiImplicitParam(name="userstatus" , value="用户状态")})
    public ResultVo adminuserlist(@PathVariable("userid") String userid,@PathVariable("userstatus") Integer userstatus) {
        if (userstatus == 0){
            Integer i = loginService.updateLogin(new Login().setUserid(userid).setUserstatus(userstatus));
            Integer j = userInfoService.UpdateUserInfo(new UserInfo().setUserid(userid).setUserstatus(userstatus));
            if (i ==1 && j == 1){
                /**发出封号的系统通知*/

                Notices notices = new Notices().setId(KeyUtil.genUniqueKey()).setUserid(userid).setTpname("系统通知")
                        .setWhys("您在该网站的账号已被封号。");
                noticesService.insertNotices(notices);
                return new ResultVo(true, StatusCode.OK, "封号成功");
            }
            return new ResultVo(true, StatusCode.ERROR, "封号失败");
        }else if (userstatus == 1){
            Integer i = loginService.updateLogin(new Login().setUserid(userid).setUserstatus(userstatus));
            Integer j = userInfoService.UpdateUserInfo(new UserInfo().setUserid(userid).setUserstatus(userstatus));
            if (i ==1 && j == 1){
                /**发出解封的系统通知*/
                Notices notices = new Notices().setId(KeyUtil.genUniqueKey()).setUserid(userid).setTpname("系统通知")
                        .setWhys("您在该网站的账号已被解封");
                noticesService.insertNotices(notices);
                return new ResultVo(true, StatusCode.OK, "解封成功");
            }
            return new ResultVo(true, StatusCode.ERROR, "解封失败");
        }
        return new ResultVo(false,StatusCode.ACCESSERROR,"违规操作");
    }


//    /**
//     * 管理员商品列表
//     * */
//    @GetMapping("/admin/product")
//    public String adminproduct(){
//        return "/admin/product/productlist";
//    }

    /**
     * 分页管理员查看各类商品信息
     *前端传入页码、分页数量
     *前端传入商品信息状态码（commstatus）-->全部:100，违规:0，已审核:1，待审核:3 已完成:4
     * 因为是管理员查询，将userid设置为空
     */
    @GetMapping("/admin/commodity/{commstatus}")
    @ApiOperation(value="分页管理员查看各类商品信息")
    @ApiImplicitParams({@ApiImplicitParam(name="limit",value="每页数量"),@ApiImplicitParam(name="page",value="页数"),@ApiImplicitParam(name="commstatus",value="@PathVariable 评论状态" +
            "全部:100，违规:0，已审核:1，待审核:3 已完成:4")})
    @ResponseBody
    public PageVo userCommodity(@PathVariable("commstatus") Integer commstatus, int limit, int page) {
        Integer dataNumber = -1;
        switch (commstatus){
            case (100) : {List<Commodity> commodityList = commodityService.queryAllCommodity((page - 1) * limit, limit, null, null);
                 dataNumber = commodityService.queryCommodityCount(null, null);
                return new PageVo(0, "全部商品信息已显示",dataNumber,commodityList);}
            case (0) : {
                List<Commodity> commodityList = commodityService.queryAllCommodity((page - 1) * limit, limit, null, commstatus);
                 dataNumber = commodityService.queryCommodityCount(null, commstatus);
                return new PageVo(0,"违规商品信息已显示",dataNumber,commodityList);
            }

            case (1) : {
                List<Commodity> commodityList = commodityService.queryAllCommodity((page - 1) * limit, limit, null, commstatus);
                 dataNumber = commodityService.queryCommodityCount(null, commstatus);
                return new PageVo(0,"已审核商品信息已显示",dataNumber,commodityList);
            }

            case (3) : {
                List<Commodity> commodityList = commodityService.queryAllCommodity((page - 1) * limit, limit, null, commstatus);
                 dataNumber = commodityService.queryCommodityCount(null, commstatus);
                return new PageVo(0,"待审核商品信息已显示",dataNumber,commodityList);
            }

            case (4) : {
                List<Commodity> commodityList = commodityService.queryAllCommodity((page - 1) * limit, limit, null, commstatus);
                 dataNumber = commodityService.queryCommodityCount(null, commstatus);
                return new PageVo(0,"已完成商品信息已显示",dataNumber,commodityList);
            }
        }

        return new PageVo(0,"已完成商品信息已显示",dataNumber,null);

    }

    /**
     * 管理员对商品的操作
     * 前端传入商品id（commid）
     * 前端传入操作的商品状态（commstatus）-->违规:0  通过审核:1
     * */
    @PutMapping("/admin/changecommstatus/{commid}/{commstatus}")
    @ApiOperation(value="管理员对商品的操作")
    @ApiImplicitParams({@ApiImplicitParam(name="commid" , value="商品id"), @ApiImplicitParam(name="commstatus" , value="商品状态 违规:0  通过审核:1")})

    public ResultVo ChangeCommstatus(@PathVariable("commid") String commid, @PathVariable("commstatus") Integer commstatus) {
        Integer i = commodityService.ChangeCommstatus(commid, commstatus);
        if (i == 1){
            /**发出商品审核结果的系统通知*/
            Commodity commodity = commodityService.LookCommodity(new Commodity().setCommid(commid));
            if (commstatus == 0){
                Notices notices = new Notices().setId(KeyUtil.genUniqueKey()).setUserid(commodity.getUserid()).setTpname("商品审核")
                        .setWhys("您的商品 "+commodity.getCommid()+" "+commodity.getCommname()+" 未通过审核，目前不支持公开发布。");
                noticesService.insertNotices(notices);
            }else if (commstatus == 1){
                Notices notices = new Notices().setId(KeyUtil.genUniqueKey()).setUserid(commodity.getUserid()).setTpname("商品审核")
                        .setWhys("您的商品 "+commodity.getCommid()+" "+commodity.getCommname()+" 已通过审核，快去看看吧。");
                noticesService.insertNotices(notices);
            }
            return new ResultVo(true,StatusCode.OK,"操作成功");
        }
        return new ResultVo(false,StatusCode.ERROR,"操作失败");
    }

}
