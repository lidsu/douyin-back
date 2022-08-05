package com.lidsu.mapper;

import com.lidsu.pojo.Users;
import com.lidsu.utils.MyMapper;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface UsersMapper extends MyMapper<Users> {

    /**
     * 用户受喜欢数累加
     * @param userId
     */
        public void addReceiveLikeCount(String userId);

    /**
     * 用户受喜欢数累减
     * @param userId
     */
        public void reduceReceiveLikeCount(String userId);


    /**
     * 增加粉丝数
     * @param userId
     */
    public void addFansCount(String userId);

    /**
     * 增加关注数
     * @param userId
     */
    public void addFollersCount(String userId);

    /**
     * 减少粉丝数
     * @param userId
     */
    public void reduceFansCount(String userId);

    /**
     * 减少关注数
     * @param userId
     */
    public void reduceFollersCount(String userId);

}