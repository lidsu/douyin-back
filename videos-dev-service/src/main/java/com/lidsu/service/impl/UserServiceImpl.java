package com.lidsu.service.impl;

import com.lidsu.mapper.UsersFansMapper;
import com.lidsu.mapper.UsersLikeVideosMapper;
import com.lidsu.mapper.UsersMapper;
import com.lidsu.pojo.Users;
import com.lidsu.pojo.UsersFans;
import com.lidsu.pojo.UsersLikeVideos;
import com.lidsu.service.UserService;
import com.lidsu.utils.IMoocJSONResult;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private UsersFansMapper usersFansMapper ;

    @Autowired
    private UsersLikeVideosMapper usersLikeVideosMapper;

    @Autowired
    private Sid sid;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean queryUsernameIsExist(String username) {
        Users user = new Users();
        user.setUsername(username);
       Users result= usersMapper.selectOne(user);
        return result == null ? false : true;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public void saveUser(Users user) {
        String userId = sid.nextShort();
        user.setId(userId);
        usersMapper.insert(user);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserForLogin(String username, String password) {
        Example userExample = new Example(Users.class);
        Example.Criteria criteria = userExample.createCriteria();
        criteria.andEqualTo("username", username);
        criteria.andEqualTo("password", password);
        Users result = usersMapper.selectOneByExample(userExample);

        return result;
    }
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateUserInfo(Users user) {
        Example userExample = new Example(Users.class);
        Example.Criteria criteria = userExample.createCriteria();
        criteria.andEqualTo("id",user.getId());
        usersMapper.updateByExampleSelective(user,userExample);

    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserInfo(String userId) {
        Example userExample = new Example(Users.class);
        Example.Criteria criteria = userExample.createCriteria();
        criteria.andEqualTo("id",userId);
        Users user = usersMapper.selectOneByExample(userExample);
        return user;
    }
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean isUserLikeVideo(String userId, String videoId) {
        if(StringUtils.isBlank(userId)&&StringUtils.isBlank(videoId)){
            return false;
        }

        Example userExample = new Example(UsersLikeVideos.class);
        Example.Criteria criteria = userExample.createCriteria();
        criteria.andEqualTo("userId",userId);
        criteria.andEqualTo("videoId",videoId);
        List<UsersLikeVideos> list = usersLikeVideosMapper.selectByExample(userExample);
        if(list!=null&&list.size()>0){
            return true;
        }
        return false;
    }
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveUserFanRelation(String userId, String fanId) {
        String relid = sid.nextShort();
        UsersFans usersFans = new UsersFans();
        usersFans.setId(relid);
        usersFans.setUserId(userId);
        usersFans.setFanId(fanId);
        usersFansMapper.insert(usersFans);

        usersMapper.addFansCount(userId);
        usersMapper.addFollersCount(fanId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteUserFanRelation(String userId, String fanId) {
        Example userExample = new Example(UsersFans.class);
        Example.Criteria criteria = userExample.createCriteria();
        criteria.andEqualTo("userId",userId);
        criteria.andEqualTo("fanId",fanId);
        usersFansMapper.deleteByExample(userExample);
        usersMapper.reduceFansCount(userId);
        usersMapper.reduceFollersCount(fanId);
    }

    @Override
    public boolean queryIfFollow(String userId, String fanId) {
        Example userExample = new Example(UsersFans.class);
        Example.Criteria criteria = userExample.createCriteria();
        criteria.andEqualTo("userId",userId);
        criteria.andEqualTo("fanId",fanId);
        List<UsersFans> list = usersFansMapper.selectByExample(userExample);

        if(list!=null&&!list.isEmpty()&&list.size()>0){
            return true;
        }
        return false;
    }
}
