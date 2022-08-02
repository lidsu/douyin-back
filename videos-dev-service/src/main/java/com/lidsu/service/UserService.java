package com.lidsu.service;

import com.lidsu.pojo.Users;

public interface UserService {
    /**
     * 判断用户名是否存在
     * @param name
     * @return
     */
     boolean queryUsernameIsExist(String name);

    /**
     * 保存用户
     * @param users
     */
     void saveUser(Users users);


     Users queryUserForLogin(String username, String password);

    /**
     * 用户修改信息
     * @param uses
     */
     void updateUserInfo(Users uses);

    /**
     * 查询用户信息
     * @param userId
     * @return
     */
     Users queryUserInfo(String  userId);
}
