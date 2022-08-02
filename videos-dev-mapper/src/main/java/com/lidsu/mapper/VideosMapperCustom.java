package com.lidsu.mapper;

import com.lidsu.pojo.Videos;
import com.lidsu.pojo.vo.VideosVO;
import com.lidsu.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VideosMapperCustom extends MyMapper<Videos> {

    public List<VideosVO> queryAllVideos(@Param("videoDesc")String videoDesc );
}