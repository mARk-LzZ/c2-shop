package com.controller;

import com.alibaba.fastjson.JSONObject;
import com.entity.UserInfo;
import com.entity.chat.*;
import com.service.ChatmsgService;
import com.service.FriendsService;
import com.service.UserInfoService;
import com.util.StatusCode;
import com.vo.ResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@Api(description="聊天控制器")
public class ChatCtrl {
    @Autowired
    FriendsService friendsService;
    @Autowired
    UserInfoService mineService;
    @Autowired
    ChatmsgService chatmsgService;

    /**
     * 上传聊天图片
     * **/
    @PostMapping(value = "/chat/upimg")
    @ApiOperation("上传聊天图片")
    public JSONObject upimg(@RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        JSONObject res = new JSONObject();
        JSONObject resUrl = new JSONObject();
        String filename = UUID.randomUUID().toString().replaceAll("-", "");
        String ext = FilenameUtils.getExtension(file.getOriginalFilename());//获得文件扩展名
        String filenames = filename + "." + ext;
        file.transferTo(new File("\\c2shop\\" + filenames));
        resUrl.put("src", "/pic/" + filenames);
        res.put("msg", "");
        res.put("code", 0);
        res.put("data", resUrl);
        return res;
    }
    /**
     * 上传聊天文件
     * **/
    @PostMapping(value = "/chat/upfile")
    @ApiOperation("上传聊天文件")

    public JSONObject upfile(@RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        JSONObject res = new JSONObject();
        JSONObject resUrl = new JSONObject();
        String filename = UUID.randomUUID().toString().replaceAll("-", "");
        String ext = FilenameUtils.getExtension(file.getOriginalFilename());//获得文件扩展名
        String filenames = filename + "." + ext;
        file.transferTo(new File("\\c2shop\\" + filenames));
        resUrl.put("src", "/pic/" + filenames);
        resUrl.put("name",file.getOriginalFilename());
        res.put("msg", "");
        res.put("code", 0);
        res.put("data", resUrl);
        return res;
    }

    /**
     * 添加好友跳转到个人中心聊天
     * */
    @PutMapping("/addfrend/{fuserid}")
    @ApiOperation("添加好友跳转到个人中心聊天")
@ApiImplicitParam(name="session")
    public ResultVo addfrend(@PathVariable("fuserid") String fuserid,@ApiIgnore HttpSession session){
        String userid = (String)session.getAttribute("userid");
        if(userid.equals(fuserid)){
            //不能对自己的商品感兴趣
           return new ResultVo(false, StatusCode.ERROR,"不能对自己的商品感兴趣");
        }
        Friends friends=new Friends().setUserid(userid).setFuserid(fuserid);
        Integer integer = friendsService.JustTwoUserIsFriend(friends);
        if(integer==null){
            //如果不存在好友关系插入好友关系
            friendsService.insertFriend(friends);
            friendsService.insertFriend(new Friends().setFuserid(userid).setUserid(fuserid));
        }
        return new ResultVo(false, StatusCode.OK,"正在跳转到聊天界面");
    }

    /**
     *  查询聊天记录
     * */
    @GetMapping("/chatlog/{uid}")
    @ApiOperation("查询聊天记录")

    public List<UserInfo> chatlog(@PathVariable("uid")String uid,@ApiIgnore HttpSession session){
        String userid=(String) session.getAttribute("userid");
        List<UserInfo> mines = chatmsgService.LookChatMsg(new ChatMsg().setSenduserid(userid).setReciveuserid(uid));
        return mines;
    }
    /**
     * TODO 初始化聊天
     * */
    @GetMapping("/initim")
    @ApiOperation("初始化聊天")

    public InitImVo initim(@ApiIgnore HttpSession session){
        String userid = (String) session.getAttribute("userid");
        InitImVo initImVo=new InitImVo();
        //个人信息
        UserInfo mine=friendsService.LookUserMine(userid);
        //好友列表
        List<UserInfo> list=friendsService.LookUserFriend(userid);
        Friend friend=new Friend().setId("2").setGroupname("分组").setList(list);
        List<Friend> friendList=new ArrayList<>();
        friendList.add(friend);
        //群组信息
        List<Groups> groupList=new ArrayList<>();
        //Data数据
        ImData imData=new ImData().setMine(mine).setFriend(friendList).setGroup(groupList);
        initImVo.setCode(0).setMsg("").setData(imData);
        return initImVo;
    }
}
