package com.zhangyh.mockeasy.service;

import com.zhangyh.mockeasy.model.ApiConfig;

import java.util.List;

/**
 * API配置服务接口
 */
public interface ApiConfigService {
    
    /**
     * 保存API配置
     * @param apiConfig API配置信息
     * @return 保存后的API配置
     */
    ApiConfig saveApiConfig(ApiConfig apiConfig);
    
    /**
     * 根据ID获取API配置
     * @param id API配置ID
     * @return API配置信息
     */
    ApiConfig getApiConfigById(String id);
    
    /**
     * 获取所有API配置
     * @return API配置列表
     */
    List<ApiConfig> getAllApiConfigs();
    
    /**
     * 根据分组ID获取API配置列表
     * @param groupId 分组ID
     * @return API配置列表
     */
    List<ApiConfig> getApiConfigsByGroupId(String groupId);
    
    /**
     * 更新API配置
     * @param apiConfig API配置信息
     * @return 更新后的API配置
     */
    ApiConfig updateApiConfig(ApiConfig apiConfig);
    
    /**
     * 删除API配置
     * @param id API配置ID
     * @return 是否删除成功
     */
    boolean deleteApiConfig(String id);
    
    /**
     * 根据路径获取API配置
     * @param path API路径
     * @param method 请求方法
     * @return API配置信息
     */
    ApiConfig getApiConfigByPathAndMethod(String path, String method);
    
    /**
     * 注册API到Spring MVC
     * @param apiConfig API配置信息
     * @return 是否注册成功
     */
    boolean registerApi(ApiConfig apiConfig);
    
    /**
     * 取消注册API
     * @param apiConfig API配置信息
     * @return 是否取消成功
     */
    boolean unregisterApi(ApiConfig apiConfig);
}