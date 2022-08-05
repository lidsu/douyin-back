package com.lidsu.controller;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.lidsu.pojo.Users;
import com.lidsu.pojo.vo.PublisherVideo;
import com.lidsu.pojo.vo.UsersVO;
import com.lidsu.service.UserService;
import com.lidsu.utils.IMoocJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import net.sf.jsqlparser.schema.MultiPartName;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

@RestController
@Api(value = "用户相关业务的接口",tags = {"用户相关业务的Controller"})
@RequestMapping("/user")
public class UserController extends BasicController{

    @Autowired
    private UserService userService;

    @ApiOperation(value="用户上传头像", notes="用户上传头像的接口")
    @ApiImplicitParam(name="userId", value="用户id", required=true,
            dataType="String", paramType="query")
    @PostMapping("/uploadFace")
    public IMoocJSONResult uploadFace(String userId,
                                      @RequestParam("file") MultipartFile[] files) throws Exception {
        if(StringUtils.isBlank(userId)){
            return IMoocJSONResult.errorMsg("用户id不能为空...");
        }

        String fileSpace ="E:\\github\\wxchatapp\\videos_dev";

        String uploadPathDB="/"+userId+"/face";

        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;
        try {
            if (files != null && files.length > 0) {

                String fileName = files[0].getOriginalFilename();
                if (StringUtils.isNotBlank(fileName)) {
                    // 文件上传的最终保存路径
                    String finalFacePath = fileSpace + uploadPathDB + "/" + fileName;
                    System.out.println("finalFacePath:"+finalFacePath);
                    // 设置数据库保存的路径 E:\github\wxchatapp\videos_dev\220730FPY0CBTYA8\face
                    uploadPathDB += ("/" + fileName);
                    System.out.println("uploadPathDB:"+uploadPathDB);
                    File outFile = new File(finalFacePath);
                    if (outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()) {
                        // 创建父文件夹
                        outFile.getParentFile().mkdirs();
                    }

                    fileOutputStream = new FileOutputStream(outFile);
                    inputStream = files[0].getInputStream();
                    IOUtils.copy(inputStream, fileOutputStream);
                }

            } else {
                return IMoocJSONResult.errorMsg("上传出错...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return IMoocJSONResult.errorMsg("上传出错...");
        } finally {
            if (fileOutputStream != null) {
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        }

        Users user = new Users();
        user.setId(userId);
        user.setFaceImage(uploadPathDB);
        userService.updateUserInfo(user);

        return IMoocJSONResult.ok(uploadPathDB);
    }
    @ApiOperation(value="查询用户信息", notes="查询用户信息的接口")
    @ApiImplicitParam(name="userId", value="用户id", required=true,
            dataType="String", paramType="query")
    @PostMapping("/query")
    public IMoocJSONResult query(String userId,String fanId) throws Exception{
        if(StringUtils.isBlank(userId)){
            return IMoocJSONResult.errorMsg("用户ID不能为空");
        }
        Users userInfo = userService.queryUserInfo(userId);
        UsersVO userVO=new UsersVO();
        BeanUtils.copyProperties(userInfo, userVO);
        userVO.setFollow(userService.queryIfFollow(userId,fanId));
        return IMoocJSONResult.ok(userVO);
    }
    @PostMapping("/queryPublisher")
    public IMoocJSONResult queryPublisher(String loginUserId,String videoId
                                          ,String publishUserId) throws Exception{
        if(StringUtils.isBlank(publishUserId)){
            return IMoocJSONResult.errorMsg("判空错误");
        }
        Users userInfo = userService.queryUserInfo(publishUserId);
        UsersVO publisher=new UsersVO();
        BeanUtils.copyProperties(userInfo, publisher);

        boolean userLikeVideo = userService.isUserLikeVideo(loginUserId, videoId);

        PublisherVideo bean = new PublisherVideo();
        bean.setPublisher(publisher);
        bean.setUserLikeVideo(userLikeVideo);

        return IMoocJSONResult.ok(bean);
    }

    @PostMapping("/beyourfans")
    public IMoocJSONResult beyourfans(String userId,String fanId
            ) throws Exception{
        if(StringUtils.isBlank(userId)&&StringUtils.isBlank(fanId)){
            return IMoocJSONResult.errorMsg("");
        }
        userService.saveUserFanRelation(userId,fanId);
        return IMoocJSONResult.ok("关注成功...");
    }

    @PostMapping("/dontbeyourfans")
    public IMoocJSONResult dontbeyourfans(String userId,String fanId
    ) throws Exception{
        if(StringUtils.isBlank(userId)&&StringUtils.isBlank(fanId)){
            return IMoocJSONResult.errorMsg("");
        }
        userService.deleteUserFanRelation(userId,fanId);
        return IMoocJSONResult.ok("取消关注成功...");
    }
}
