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
}
