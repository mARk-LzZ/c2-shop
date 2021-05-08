package com.controller;


import com.alibaba.fastjson.JSONObject;
import com.entity.*;
import com.service.*;
import com.util.JsonReader;
import com.util.KeyUtil;
import com.util.StatusCode;
import com.vo.PageVo;
import com.vo.ResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;


@RestController
@Api(value="商品控制器")
public class CommodityController {
    @Autowired
    private CommodityService commodityService;
    @Autowired
    private CommimagesService commimagesService;
    @Autowired
    private LoginService loginService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private SoldrecordService soldrecordService;
    @Autowired
    private CollectService collectService;
    @Autowired
    private NoticesService noticesService;




    /**
     * 修改商品
     * 1、修改商品信息
     * 2、修改商品的其他图的状态
     * 3、插入商品的其他图
     */
    @PostMapping("/changegoods/rel")
    @ApiOperation("修改商品")
    @ApiImplicitParam(name="common", value="商品信息")
    public ResultVo changegoods(@RequestBody Commodity commodity,@ApiIgnore HttpSession session){
        String userid = (String) session.getAttribute("userid");
        commodity.setUpdatetime(new Date()).setCommstatus(3);
        commodity.setCommon(commodity.getCommon()+"、"+commodity.getCommon2());//常用选项拼接
        commodityService.ChangeCommodity(commodity);
        commimagesService.DelGoodImages(commodity.getCommid());
        List<Commimages> commimagesList=new ArrayList<>();
        for (String list:commodity.getOtherimg()) {
            commimagesList.add(new Commimages().setId(KeyUtil.genUniqueKey()).setCommid(commodity.getCommid()).setImage(list));
        }
        commimagesService.InsertGoodImages(commimagesList);
        /**发出待审核系统通知*/
        Notices notices = new Notices().setId(KeyUtil.genUniqueKey()).setUserid(userid).setTpname("商品审核");
        noticesService.insertNotices(notices);
        return new ResultVo(true , StatusCode.OK ,"提交成功",notices);
    }

    /**
     * 发布商品
     * 1、插入商品信息
     * 2、插入商品其他图
     */
    @PostMapping("/relgoods/rel")
    @ApiOperation("发布商品")
    public ResultVo relgoods(@RequestBody Commodity commodity,@ApiIgnore HttpSession session){
        String userid = (String) session.getAttribute("userid");
        UserInfo userInfo = userInfoService.LookUserinfoById(userid);
        String commid = KeyUtil.genUniqueKey();
        commodity.setCommid(commid).setUserid(userid).setSchool(userInfo.getSchool());//商品id
        commodity.setCommon(commodity.getCommon()+"、"+commodity.getCommon2());//常用选项拼接
        commodityService.InsertCommodity(commodity);
        List<Commimages> commimagesList=new ArrayList<>();
        for (String list:commodity.getOtherimg()) {
            commimagesList.add(new Commimages().setId(KeyUtil.genUniqueKey()).setCommid(commid).setImage(list));
        }
        commimagesService.InsertGoodImages(commimagesList);
        /**发出待审核系统通知*/
        Notices notices = new Notices().setId(KeyUtil.genUniqueKey()).setUserid(userid).setTpname("商品审核");
        noticesService.insertNotices(notices);
        return new ResultVo(true , StatusCode.OK ,"提交成功",notices);
    }

    /**
     * 上传视频和主图
     */
    @PostMapping("/relgoods/video")
    @ApiOperation("上传视频和主图")
    public JSONObject relgoodsvideo(@RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        JSONObject res = new JSONObject();
        JSONObject resUrl = new JSONObject();
        String filename = UUID.randomUUID().toString().replaceAll("-", "");
        String ext = FilenameUtils.getExtension(file.getOriginalFilename());
        String filenames = filename + "." + ext;
        String pathname = "\\c2shop\\" + filenames;
        file.transferTo(new File(pathname));
        resUrl.put("src", "/pic/"+filenames);
        res.put("msg", "上传成功");
        res.put("code", 0);
        res.put("data", resUrl);
        return res;
    }

    /**
     * 上传其他图片
     */
    @PostMapping(value="/relgoods/images")
    @ApiOperation("上传其他图片")
    public JSONObject relgoodsimages(@RequestParam(value = "file", required = false) MultipartFile[] file) throws IOException {
        JSONObject res = new JSONObject();
        JSONObject resUrl = new JSONObject();
        List<String> imageurls=new ArrayList<>();
        for (MultipartFile files:file){
            String filename = UUID.randomUUID().toString().replaceAll("-", "");
            String ext = FilenameUtils.getExtension(files.getOriginalFilename());
            String filenames = filename + "." + ext;
            String pathname = "\\c2shop\\" + filenames;
            files.transferTo(new File(pathname));
            imageurls.add("/pic/"+filenames);
            res.put("msg", "");
            res.put("code", 0);
        }
        resUrl.put("src", imageurls);
        res.put("data", resUrl);
        return res;
    }

    /**
     * 根据商品id（commid）判断收藏商品是否存在
     * */
    @GetMapping("/product-detail-isnull/{commid}")
    @ApiOperation(value="根据商品id（commid）判断收藏商品是否存在")
    @ApiImplicitParam(name="commid",value="商品id")
    public ResultVo product_detail_isnull(@PathVariable("commid") String commid) {
        Commodity commodity = commodityService.LookCommodity(new Commodity().setCommid(commid));
        System.out.println(commodity);
        if (commodity.getCommstatus().equals(1)) {//如果商品正常
            System.out.println("商品正常");
            return new ResultVo(true,StatusCode.OK,"查询成功");
        }else {
            System.out.println("商品不正常");
            return new ResultVo(true,StatusCode.ERROR,"您收藏的商品已经不存在，建议您取消收藏");
        }
    }

    /**
     * 商品详情
     * 根据商品id（commid）查询商品信息、用户昵称及头像
     * 用户可以查看正常的商品
     * 商品发布者和管理员可以查看
     *
     * @return*/
    @GetMapping("/product-detail/{commid}")
    @ApiOperation("商品详情")
    @ApiImplicitParams({@ApiImplicitParam(name="commid" , value="商品id"), @ApiImplicitParam(name="session")})
    public ResultVo product_detail(@PathVariable("commid") String commid, @ApiIgnore HttpSession session){
        String couserid = (String) session.getAttribute("userid");

        Commodity commodity = commodityService.LookCommodity(new Commodity().setCommid(commid).setCommstatus(1));
        int i = 0;
        if (commodity.getCommstatus().equals(1)){//如果商品正常
            i=1;
        }else if (!StringUtils.isEmpty(couserid)){//如果用户已登录
            Login login = loginService.userLogin(new Login().setUserid(couserid));
            /**商品为违规状态时：本人和管理员可查看*/
            if (commodity.getCommstatus().equals(0) && (commodity.getUserid().equals(couserid) || (login.getRoleid().equals(2) || login.getRoleid().equals(3)))){
                i=1;
                /**商品为待审核状态时：本人和管理员可查看*/
            }else if (commodity.getCommstatus().equals(3) && (commodity.getUserid().equals(couserid) || (login.getRoleid().equals(2) || login.getRoleid().equals(3)))){
                i=1;
                /**商品为已售出状态时：本人和管理员可查看*/
            }else if (commodity.getCommstatus().equals(4) && (commodity.getUserid().equals(couserid) || (login.getRoleid().equals(2) || login.getRoleid().equals(3)))){
                i=1;
            }
        }
        if(i==1){
            commodity.setOtherimg(commimagesService.LookGoodImages(commid));
            /**商品浏览量+1*/
            commodityService.ChangeCommodity(new Commodity().setCommid(commid).setRednumber(1));
            UserInfo userInfo = userInfoService.queryPartInfo(commodity.getUserid());


            //如果没有用户登录
//            if (StringUtils.isEmpty(couserid)){
//                    return new ResultVo(false , StatusCode.ACCESSERROR , "未登录", userInfo);
//            }else {
//                Collect collect = collectService.queryCollectStatus(new Collect().setCommid(commid).setCouserid(couserid));
                return new ResultVo(true,StatusCode.OK,"查询成功",commodity);
//            }

        }else{
            return new ResultVo(false , StatusCode.FINDERROR ,"未找到商品", null);
        }
    }

    /**
     * 搜索商品分页数据
     * 前端传入搜索的商品名（commname）
     * */
    @GetMapping("/product/search/number/{commname}")
    @ApiOperation("搜索商品分页数据")
    @ApiImplicitParam(name="commname" , value="商品名")

    public PageVo searchCommodityNumber(@PathVariable("commname") String commname){
        Integer dataNumber = commodityService.queryCommodityByNameCount(commname);
        return new PageVo(StatusCode.OK,"查询成功",dataNumber);
    }

    /**
     * 搜索商品
     * 前端传入当前页数（nowPaging）、搜索的商品名（commname）
     * */
    @GetMapping("/product/search/{nowPaging}/{commname}")
    @ApiOperation(value="搜索商品")
    @ApiImplicitParams({@ApiImplicitParam(name="nowpaging", value="当前页数"), @ApiImplicitParam(name="commname" ,value="商品名")})

    public ResultVo searchCommodity(@PathVariable("nowPaging") Integer page, @PathVariable("commname") String commname){
        List<Commodity> commodityList = commodityService.queryCommodityByName((page - 1) * 20, 20, commname);

        if(!StringUtils.isEmpty(commodityList)){//如果有对应商品
            for (Commodity commodity : commodityList) {
                /**查询商品对应的其它图片*/
                List<String> imagesList = commimagesService.LookGoodImages(commodity.getCommid());
                commodity.setOtherimg(imagesList);
            }
            return new ResultVo(true,StatusCode.OK,"查询成功",commodityList);
        }else{
            return new ResultVo(true,StatusCode.ERROR,"没有相关商品");
        }
    }

    /**
     * 首页分类展示商品 --> 按照分类查询商品
     * 前端传入商品类别（category）
     * */
    @PostMapping("/index/product")
    @ApiOperation("首页分类展示商品 --> 按照分类查询商品")
    @ApiImplicitParam(name="category" , value="商品类别")
    public ResultVo indexCommodity(HttpServletRequest httpServletRequest) throws IOException {
        JSONObject jsonObject =JsonReader.receivePost(httpServletRequest);
        String category = jsonObject.getString("category");
        List<Commodity> commodityList = commodityService.queryCommodityByCategory(category);
        for (Commodity commodity : commodityList) {
            /**查询商品对应的其它图片*/
            List<String> imagesList = commimagesService.LookGoodImages(commodity.getCommid());
            commodity.setOtherimg(imagesList);
        }
            return new ResultVo(true,StatusCode.OK,"查询成功",commodityList);
    }

    /**
     * 查询最新发布的8条商品
     * */
    @GetMapping("/product/latest")
    @ApiOperation("查询最新发布的8条商品")

    public ResultVo latestCommodity() {
        String category = "全部";
        List<Commodity> commodityList = commodityService.queryCommodityByCategory(category);
        for (Commodity commodity : commodityList) {
            /**查询商品对应的其它图片*/
            List<String> imagesList = commimagesService.LookGoodImages(commodity.getCommid());
            commodity.setOtherimg(imagesList);
        }
        return new ResultVo(true,StatusCode.OK,"查询成功",commodityList);
    }

    /*
    * 查询所有商品分类
    * */
    @GetMapping("/product/getallcategorys")
    @ApiOperation("查询所有商品分类")
    public ResultVo findAllCategorys(){
        List<String> categorys = commodityService.findAllCategorys();


        return new ResultVo(true , StatusCode.OK , "查询成功", categorys);
    }
    /**
     * 产品清单分页数据
     * 前端传入商品类别（category）、区域（area）
     * 最低价（minmoney）、最高价（maxmoney）
     * 后端根据session查出个人本校信息（school）
     * */
    @GetMapping("/list-number/{category}/{area}/{minmoney}/{maxmoney}")
    @ApiOperation("产品清单分页数据")

    @ApiImplicitParams({@ApiImplicitParam(name="category" , value="类别") , @ApiImplicitParam(name="area" , value="区域"),
            @ApiImplicitParam(name="minmoney", value="最低价"),@ApiImplicitParam(name="maxmoney", value="最高价"), @ApiImplicitParam(name="session")})
    public PageVo productListNumber(@PathVariable("category") String category, @PathVariable("area") String area,
                                    @PathVariable("minmoney") BigDecimal minmoney, @PathVariable("maxmoney") BigDecimal maxmoney,
                                    @ApiIgnore HttpSession session) {
        String school=null;
        if(!area.equals("全部")){
            String userid = (String) session.getAttribute("userid");
            UserInfo userInfo = userInfoService.LookUserinfoById(userid);
            school = userInfo.getSchool();
        }
        Integer dataNumber = commodityService.queryAllCommodityByCategoryCount(area, school, category, minmoney, maxmoney);
        return new PageVo(StatusCode.OK,"查询成功",dataNumber);
    }

    /**
     * 产品清单界面
     * 前端传入商品类别（category）、当前页码（nowPaging）、区域（area）
     * 最低价（minmoney）、最高价（maxmoney）、价格升序降序（price：0.不排序 1.升序 2.降序）
     * 后端根据session查出个人本校信息（school）
     * */
    @GetMapping("/product-listing/{category}/{nowPaging}/{area}/{minmoney}/{maxmoney}/{price}")
    @ApiOperation("产品清单界面")
    @ApiImplicitParams({@ApiImplicitParam(name="category" , value="类别") ,@ApiImplicitParam(name="nowPaging", value="当前页码"), @ApiImplicitParam(name="area" , value="区域"),
            @ApiImplicitParam(name="minmoney", value="最低价"),@ApiImplicitParam(name="maxmoney", value="最高价"), @ApiImplicitParam(name="session"), @ApiImplicitParam(name="session")})
    public ResultVo productlisting(@PathVariable("category") String category, @PathVariable("nowPaging") Integer page,
                                 @PathVariable("area") String area, @PathVariable("minmoney") BigDecimal minmoney, @PathVariable("maxmoney") BigDecimal maxmoney,
                                 @PathVariable("price") Integer price,@ApiIgnore HttpSession session) {
        String school=null;
        if(!area.equals("全部")) {
            String userid = (String) session.getAttribute("userid");
            UserInfo userInfo = userInfoService.LookUserinfoById(userid);
            school = userInfo.getSchool();
        }
        List<Commodity> commodityList = commodityService.queryAllCommodityByCategory((page - 1) * 16, 16, area, school, category, minmoney, maxmoney);
        for (Commodity commodity : commodityList) {
            /**查询商品对应的其它图片*/
            List<String> imagesList = commimagesService.LookGoodImages(commodity.getCommid());
            commodity.setOtherimg(imagesList);
        }

        /**自定义排序*/
        if (price != 0){
            if (price == 1){
                /**升序*/
                Collections.sort(commodityList, new Comparator<Commodity>() {//此处创建了一个匿名内部类
                    int i;
                    @Override
                    public int compare(Commodity o1, Commodity o2) {
                        if (o1.getThinkmoney().compareTo(o2.getThinkmoney()) > -1) {
                            System.out.println("===o1大于等于o2===");
                            i = 1;
                        } else if (o1.getThinkmoney().compareTo(o2.getThinkmoney()) < 1) {
                            i = -1;
                            System.out.println("===o1小于等于o2===");
                        }
                        return i;
                    }
                });
            }else if (price == 2){
                /**降序*/
                Collections.sort(commodityList, new Comparator<Commodity>() {//此处创建了一个匿名内部类
                    int i;
                    @Override
                    public int compare(Commodity o1, Commodity o2) {
                        if (o1.getThinkmoney().compareTo(o2.getThinkmoney()) > -1) {
                            System.out.println("===o1大于等于o2===");
                            i = -1;
                        } else if (o1.getThinkmoney().compareTo(o2.getThinkmoney()) < 1) {
                            System.out.println("===o1小于等于o2===");
                            i = 1;
                        }
                        return i;
                    }
                });
            }
        }
        return new ResultVo(true,StatusCode.OK,"查询成功",commodityList);
    }

    /**
     * 分页展示个人各类商品信息
     *前端传入页码、分页数量
     *前端传入商品信息状态码（commstatus）-->全部:100，已审核:1，待审核:3，违规:0，已完成:4
     */
    @GetMapping("/user/commodity/{commstatus}")
    @ApiOperation("分页展示个人各类商品信息")
    @ApiImplicitParams({@ApiImplicitParam(name="limit" , value="分页数量") ,@ApiImplicitParam(name="nowPaging", value="当前页码"),
            @ApiImplicitParam(name="commstatus", value="品信息状态码 全部:100，已审核:1，待审核:3，违规:0，已完成:4"), @ApiImplicitParam(name="session")})
    public PageVo userCommodity(@PathVariable("commstatus") Integer commstatus, int limit, int page,@ApiIgnore HttpSession session) {
        String userid = (String) session.getAttribute("userid");
        //如果未登录，给一个假id
        if(StringUtils.isEmpty(userid)){
            userid = "123456";
        }
        List<Commodity> commodityList=null;
        Integer dataNumber;
        if(commstatus==100){
            commodityList = commodityService.queryAllCommodity((page - 1) * limit, limit, userid,null);
            dataNumber = commodityService.queryCommodityCount(userid,null);
        }else{
            commodityList = commodityService.queryAllCommodity((page - 1) * limit, limit, userid,commstatus);
            dataNumber = commodityService.queryCommodityCount(userid,commstatus);
        }
        return new PageVo(0,"",dataNumber,commodityList);
    }

    /**
     * 个人对商品的操作
     * 前端传入商品id（commid）
     * 前端传入操作的商品状态（commstatus）-->删除:2  已完成:4
     * */
    @GetMapping("/user/changecommstatus/{commid}/{commstatus}")
    @ApiOperation("个人对商品的操作")
    @ApiImplicitParams({@ApiImplicitParam(name="commid", value="商品id") , @ApiImplicitParam(name="commstatus" , value="状态 删除:2  已完成:4"), @ApiImplicitParam(name="session")})
    public ResultVo ChangeCommstatus(@PathVariable("commid") String commid, @PathVariable("commstatus") Integer commstatus,@ApiIgnore HttpSession session) {
        Integer i = commodityService.ChangeCommstatus(commid, commstatus);
        if (i == 1){
            /**如果商品已售出*/
            if (commstatus == 4){
                String userid = (String) session.getAttribute("userid");
                /**查询售出商品的信息*/
                Commodity commodity = commodityService.LookCommodity(new Commodity().setCommid(commid));
                Soldrecord soldrecord = new Soldrecord();
                /**将商品信息添加到售出记录中*/
                soldrecord.setId(KeyUtil.genUniqueKey()).setCommid(commid).setCommname(commodity.getCommname()).setCommdesc(commodity.getCommdesc())
                .setThinkmoney(commodity.getThinkmoney()).setUserid(userid);
                /**添加售出记录*/
                soldrecordService.insertSold(soldrecord);
            }
            return new ResultVo(true,StatusCode.OK,"操作成功");
        }
        return new ResultVo(false,StatusCode.ERROR,"操作失败");
    }
}

