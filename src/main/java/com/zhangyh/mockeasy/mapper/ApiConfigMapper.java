package com.zhangyh.mockeasy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhangyh.mockeasy.model.ApiConfig;
import org.apache.ibatis.annotations.Mapper;

/**
 * API配置Mapper接口，用于操作数据库中的API配置信息
 */
@Mapper
public interface ApiConfigMapper extends BaseMapper<ApiConfig> {
}