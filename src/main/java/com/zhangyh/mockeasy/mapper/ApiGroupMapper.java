package com.zhangyh.mockeasy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhangyh.mockeasy.model.ApiGroup;
import org.apache.ibatis.annotations.Mapper;

/**
 * API分组Mapper接口，用于操作数据库中的API分组信息
 */
@Mapper
public interface ApiGroupMapper extends BaseMapper<ApiGroup> {
}