package com.lidsu.mapper;

import com.lidsu.pojo.SearchRecords;
import com.lidsu.utils.MyMapper;

import java.util.List;

public interface SearchRecordsMapper extends MyMapper<SearchRecords> {
    public List<String> getHotwords();
}