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

    /**
     * 查询用户是否喜欢/点赞视频
     * @param userId
     * @param videoId
     * @return
     */
     boolean isUserLikeVideo(String userId,String videoId);

    /**
     * 增加用户和粉丝的关系
     * @param userId
     * @param fanId
     */
     void saveUserFanRelation(String userId,String fanId);

    /**
     * 删除用户和粉丝的关系
     * @param userId
     * @param fanId
     */
     void deleteUserFanRelation(String userId,String fanId);

    /**
     * 查询用户是否被关注
     * @param userId
     * @param fanId
     * @return
     */
     boolean queryIfFollow(String userId,String fanId);
}
