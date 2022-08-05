package com.lidsu.service;

import com.lidsu.pojo.Videos;
import com.lidsu.utils.PagedResult;

import java.util.List;

public interface VideoService {
    /**
     * 保存视频
     * @param video
     * @return
     */
    String  saveVideo(Videos video);

    /**
     * 修改视频的封面
     * @param videoId
     * @param coverPath
     * @return
     */
    void updateVideo(String videoId,String coverPath);

    /**
     * 分页查询视频
     * @param video
     * @param isSaveRecord
     * @param page
     * @param pageSize
     * @return
     */
    PagedResult getAllVideos(Videos video,Integer isSaveRecord,Integer page,Integer pageSize);

    /**
     *获取热搜词列表
     * @return
     */
    public List<String> getHotwords();

    /**
     * 用户喜欢视频、点赞视频
     * @param userId
     * @param videoId
     * @param videoCreaterId
     */
    public void userLikeVideo(String userId,String videoId,String videoCreaterId);

    /**
     *用户不喜欢视频./取消点赞
     * @param userId
     * @param videoId
     * @param videoCreaterId
     */
    public void userUnLikeVideo(String userId,String videoId,String videoCreaterId);

}
