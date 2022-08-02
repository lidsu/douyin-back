package com.lidsu.controller;

import com.lidsu.pojo.Bgm;
import com.lidsu.pojo.Users;
import com.lidsu.pojo.Videos;
import com.lidsu.pojo.vo.VideosVO;
import com.lidsu.service.BgmService;
import com.lidsu.service.VideoService;
import com.lidsu.utils.IMoocJSONResult;
import com.lidsu.utils.MergeVideoMp3;
import com.lidsu.utils.PagedResult;
import com.lidsu.utils.VideoStatusEnum;
import io.swagger.annotations.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

@RestController
@Api(value = "视频相关业务的接口",tags = {"视频相关业务的Controller"})
@RequestMapping("/video")
public class VedioController extends BasicController{

    @Autowired
    private BgmService bgmService;

    @Autowired
    private VideoService videoService;

    @ApiOperation(value="用户上传视频", notes="用户上传视频的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name="userId", value="用户id", required=true,
                    dataType="String", paramType="form"),
            @ApiImplicitParam(name="bgmId", value="背景音乐id", required=false,
                    dataType="String", paramType="form"),
            @ApiImplicitParam(name="videoSeconds", value="背景音乐时长", required=true,
                    dataType="String", paramType="form"),
            @ApiImplicitParam(name="videoWith", value="视频宽度", required=true,
                    dataType="String", paramType="form"),
            @ApiImplicitParam(name="videoHeight", value="视频高度", required=true,
                    dataType="String", paramType="form"),
            @ApiImplicitParam(name="desc", value="视频描述", required=false,
                    dataType="String", paramType="form"),

    })
    @PostMapping(value = "/upload",headers = "content-type=multipart/form-data")
    public IMoocJSONResult upload(String userId
                                      ,String bgmId,double videoSeconds,Integer videoWidth,Integer videoHeight
                                      ,String desc,
                                      @ApiParam(value = "短视频",required = true) MultipartFile file) throws Exception {
        if(StringUtils.isBlank(userId)){
            return IMoocJSONResult.errorMsg("用户id不能为空...");
        }

        String uploadPathDB="/"+userId+"/video";

        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;
        String finalVideoPath="";
        try {
            if (file != null) {

                String fileName = file.getOriginalFilename();
                if (StringUtils.isNotBlank(fileName)) {
                    // 文件上传的最终保存路径
                    finalVideoPath = FILE_SPACE + uploadPathDB + "/" + fileName;
                    //System.out.println("finalFacePath:" + finalVideoPath);
                    // 设置数据库保存的路径 E:\github\wxchatapp\videos_dev\220730FPY0CBTYA8\face
                    uploadPathDB += ("/" + fileName);
                    //System.out.println("uploadPathDB:" + uploadPathDB);
                    File outFile = new File(finalVideoPath);
                    if (outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()) {
                        // 创建父文件夹
                        outFile.getParentFile().mkdirs();
                    }
                        fileOutputStream = new FileOutputStream(outFile);
                        inputStream = file.getInputStream();
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
        //判断bgmid是否为空

        if(StringUtils.isNotBlank(bgmId)){
            Bgm bgm = bgmService.queryBgmById(bgmId);
            String mp3InputPath = FILE_SPACE+bgm.getPath();

            MergeVideoMp3 tool=new MergeVideoMp3(FFMPEG_EXE);
            String videoInputPath = finalVideoPath;
            String videoOutputName= UUID.randomUUID().toString()+".mp4";
            uploadPathDB="//"+userId+"//video"+ "//"+videoOutputName;
            finalVideoPath=FILE_SPACE+uploadPathDB;
            tool.convertor(videoInputPath,mp3InputPath,videoSeconds,finalVideoPath);
        }
        //System.out.println("uploadPathDB"+uploadPathDB);
        //System.out.println("finalVideoPath"+finalVideoPath);

        // 保存视频信息到数据库
        Videos video = new Videos();
        video.setAudioId(bgmId);
        video.setUserId(userId);
        video.setVideoSeconds((float)videoSeconds);
        video.setVideoHeight(videoHeight);
        video.setVideoWidth(videoWidth);
        video.setVideoDesc(desc);
        video.setVideoPath(uploadPathDB);
        //video.setCoverPath(coverPathDB);
        video.setStatus(VideoStatusEnum.SUCCESS.value);
        video.setCreateTime(new Date());
        String videoId = videoService.saveVideo(video);


        return IMoocJSONResult.ok(videoId);
    }


    @ApiOperation(value="上传封面", notes="上传封面的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name="userId", value="用户id", required=true,
                    dataType="String", paramType="form"),
            @ApiImplicitParam(name="videoId", value="视频主键id", required=true,
                    dataType="String", paramType="form")
    })
    @PostMapping(value="/uploadCover", headers="content-type=multipart/form-data")
    public IMoocJSONResult uploadCover(String userId,
                                       String videoId,
                                       @ApiParam(value="视频封面", required=true)
                                               MultipartFile file) throws Exception {

        if (StringUtils.isBlank(videoId) || StringUtils.isBlank(userId)) {
            return IMoocJSONResult.errorMsg("视频主键id和用户id不能为空...");
        }

        // 文件保存的命名空间
//		String fileSpace = "C:/imooc_videos_dev";
        // 保存到数据库中的相对路径
        String uploadPathDB = "/" + userId + "/video";

        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;
        // 文件上传的最终保存路径
        String finalCoverPath = "";
        try {
            if (file != null) {

                String fileName = file.getOriginalFilename();
                if (StringUtils.isNotBlank(fileName)) {

                    finalCoverPath = FILE_SPACE + uploadPathDB + "/" + fileName;
                    // 设置数据库保存的路径
                    uploadPathDB += ("/" + fileName);

                    File outFile = new File(finalCoverPath);
                    if (outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()) {
                        // 创建父文件夹
                        outFile.getParentFile().mkdirs();
                    }

                    fileOutputStream = new FileOutputStream(outFile);
                    inputStream = file.getInputStream();
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

        videoService.updateVideo(videoId, uploadPathDB);

        return IMoocJSONResult.ok();
    }

    @ApiOperation(value="分页和搜索查询展示视频", notes="分页和搜索查询展示视频的接口")
    @PostMapping(value="/showAll")
    public IMoocJSONResult showAll(@RequestBody Videos video,Integer isSaveRecord, Integer page) throws Exception {
        if(page==null){
            page=1;
        }
        PagedResult result = videoService.getAllVideos(video,isSaveRecord,page, PAGE_SIZE);
        return IMoocJSONResult.ok(result);
    }

    @ApiOperation(value="搜索热搜词", notes="搜索热搜词的接口")
    @PostMapping(value="/hot")
    public IMoocJSONResult hot() {
        return IMoocJSONResult.ok(videoService.getHotwords());
    }
}
