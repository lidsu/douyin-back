package com.lidsu.controller;

import com.lidsu.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.ws.soap.Addressing;

@RestController
public class BasicController {

    @Autowired
    public RedisOperator redis;

    public static final String USER_REDIS_SESSION="user-redis-session";

    public static final String FILE_SPACE ="E:\\github\\wxchatapp\\videos_dev";

    public static final String FFMPEG_EXE="E:\\java_install\\ffmpeg-5.1-essentials_build\\bin\\ffmpeg.exe";

    public  static final Integer PAGE_SIZE=12;
}
