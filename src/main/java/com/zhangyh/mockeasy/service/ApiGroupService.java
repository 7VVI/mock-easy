package com.zhangyh.mockeasy.service;

import com.zhangyh.mockeasy.model.ApiGroup;

import java.util.List;

/**
 * API分组服务接口
 */
public interface ApiGroupService {
    
    /**
     * 保存API分组
     * @param apiGroup API分组信息
     * @return 保存后的API分组
     */
    ApiGroup saveApiGroup(ApiGroup apiGroup);
    
    /**
     * 根据ID获取API分组
     * @param id 分组ID
     * @return API分组
     */
    ApiGroup getApiGroupById(String id);
    
    /**
     * 获取所有API分组
     * @return API分组列表
     */
    List<ApiGroup> getAllApiGroups();
    
    /**
     * 更新API分组
     * @param apiGroup API分组信息
     * @return 更新后的API分组
     */
    ApiGroup updateApiGroup(ApiGroup apiGroup);
    
    /**
     * 删除API分组
     * @param id 分组ID
     * @return 是否删除成功
     */
    boolean deleteApiGroup(String id);
}