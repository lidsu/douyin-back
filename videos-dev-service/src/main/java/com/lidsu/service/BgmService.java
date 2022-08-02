package com.lidsu.service;

import com.lidsu.pojo.Bgm;
import com.lidsu.pojo.Users;

import java.util.List;

public interface BgmService {
    /**
     * 查询背景音乐列表
     * @return
     */
     List<Bgm> queryBgmList();

    /**
     * 根据id查询bgm信息
     * @param bgmId
     * @return
     */
     Bgm queryBgmById(String bgmId);

}
