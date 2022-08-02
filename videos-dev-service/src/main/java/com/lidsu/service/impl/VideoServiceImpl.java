package com.lidsu.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lidsu.mapper.SearchRecordsMapper;
import com.lidsu.mapper.VideosMapper;
import com.lidsu.mapper.VideosMapperCustom;
import com.lidsu.pojo.SearchRecords;
import com.lidsu.pojo.Videos;
import com.lidsu.pojo.vo.VideosVO;
import com.lidsu.service.VideoService;
import com.lidsu.utils.PagedResult;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideosMapper videosMapper;

    @Autowired
    private VideosMapperCustom videosMapperCustom;

    @Autowired
    private SearchRecordsMapper searchRecordsMapper;

    @Autowired
    private Sid sid;



    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public String saveVideo(Videos video) {
        String id = sid.nextShort();
        video.setId(id);
        videosMapper.insertSelective(video);
        return id;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateVideo(String videoId, String coverPath) {
        Videos video = new Videos();
        video.setId(videoId);
        video.setCoverPath(coverPath);
        videosMapper.updateByPrimaryKeySelective(video);
    }

    /**
     *
     * @param video
     * @param isSaveRecord 1-需要保存
     *                     0-不需要保存，或者为空的时候
     * @param page
     * @param pageSize
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public PagedResult getAllVideos(Videos video,Integer isSaveRecord,Integer page, Integer pageSize) {
        //保存热搜词
        String desc=video.getVideoDesc();
        if(isSaveRecord!=null&& isSaveRecord==1 ){
            SearchRecords record = new SearchRecords();
            String id = sid.nextShort();
            record.setId(id);
            record.setContent(desc);
            searchRecordsMapper.insert(record);

        }
        PageHelper.startPage(page,pageSize);
        List<VideosVO> list = videosMapperCustom.queryAllVideos(desc);
        PageInfo<VideosVO> pageList=new PageInfo<>(list);
        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(page);
        pagedResult.setTotal(pageList.getPages());
        pagedResult.setRows(list);
        pagedResult.setRecords(pageList.getTotal());
        return pagedResult;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public List<String> getHotwords() {

        return searchRecordsMapper.getHotwords();
    }
}
