package com.lidsu.controller;

import com.lidsu.pojo.Users;
import com.lidsu.service.UserService;
import com.lidsu.utils.IMoocJSONResult;
import com.lidsu.utils.MD5Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Api(value = "用户注册登录的接口",tags = {"注册和登录的Controller"})
public class RegistLoginController {
    @Autowired
    private UserService userService;

    @ApiOperation(value = "用户登录的接口",notes ="用户注册的接口" )
    @PostMapping("/regist")
    public IMoocJSONResult hello(@RequestBody Users user) throws Exception{
        // 1. 判断用户名和密码必须不为空
        if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())) {
            return IMoocJSONResult.errorMsg("用户名和密码不能为空");
        }

        // 2. 判断用户名是否存在
        boolean usernameIsExist = userService.queryUsernameIsExist(user.getUsername());

        // 3. 保存用户，注册信息
        if (!usernameIsExist) {
            user.setNickname(user.getUsername());
            user.setPassword(MD5Utils.getMD5Str(user.getPassword()));
            user.setFansCounts(0);
            user.setReceiveLikeCounts(0);
            user.setFollowCounts(0);
            userService.saveUser(user);
        } else {
            return IMoocJSONResult.errorMsg("用户名已经存在，请换一个再试");
        }

        user.setPassword("");

//		String uniqueToken = UUID.randomUUID().toString();
//		redis.set(USER_REDIS_SESSION + ":" + user.getId(), uniqueToken, 1000 * 60 * 30);
//
//		UsersVO userVO = new UsersVO();
//		BeanUtils.copyProperties(user, userVO);
//		userVO.setUserToken(uniqueToken);

        //UsersVO userVO = setUserRedisSessionToken(user);

        return IMoocJSONResult.ok(user);
        //return IMoocJSONResult.ok(userVO);
    }

    @ApiOperation(value="用户登录", notes="用户登录的接口")
    @PostMapping("/login")
    public IMoocJSONResult login(@RequestBody Users user) throws Exception {
    //public String login(@RequestBody Users user) throws Exception {
        String username = user.getUsername();
        String password = user.getPassword();
//		Thread.sleep(3000);
        // 1. 判断用户名和密码必须不为空
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            //return "用户名或密码不能为空...";
            return IMoocJSONResult.ok("用户名或密码不能为空...");
        }
        // 2. 判断用户是否存在
        Users userResult = userService.queryUserForLogin(username,
                MD5Utils.getMD5Str(user.getPassword()));
        // 3. 返回
        if (userResult != null) {
            userResult.setPassword("");
            return IMoocJSONResult.ok(userResult);
            //return "ok";
        } else {
            //return "用户名或密码不正确, 请重试...";
            return IMoocJSONResult.errorMsg("用户名或密码不正确, 请重试...");
        }
    }



}
