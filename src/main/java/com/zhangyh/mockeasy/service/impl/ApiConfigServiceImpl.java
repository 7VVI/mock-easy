package com.zhangyh.mockeasy.service.impl;

import com.zhangyh.mockeasy.model.ApiConfig;
import com.zhangyh.mockeasy.service.ApiConfigService;
import com.zhangyh.mockeasy.handler.DynamicApiHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * API配置服务实现类
 */
@Service
public class ApiConfigServiceImpl implements ApiConfigService {

    // 使用内存存储API配置信息，实际项目中可以替换为数据库存储
    private final Map<String, ApiConfig> apiConfigMap = new ConcurrentHashMap<>();
    private final Map<String, ApiConfig> pathMethodMap = new ConcurrentHashMap<>();
    
    @Autowired
    private DynamicApiHandler dynamicApiHandler;

    @Override
    public ApiConfig saveApiConfig(ApiConfig apiConfig) {
        // 生成唯一ID
        if (apiConfig.getId() == null || apiConfig.getId().isEmpty()) {
            apiConfig.setId(UUID.randomUUID().toString());
        }
        
        // 存储API配置
        apiConfigMap.put(apiConfig.getId(), apiConfig);
        String pathMethodKey = getPathMethodKey(apiConfig.getPath(), apiConfig.getMethod());
        pathMethodMap.put(pathMethodKey, apiConfig);
        
        // 注册API
        registerApi(apiConfig);
        
        return apiConfig;
    }

    @Override
    public ApiConfig getApiConfigById(String id) {
        return apiConfigMap.get(id);
    }

    @Override
    public List<ApiConfig> getAllApiConfigs() {
        return new ArrayList<>(apiConfigMap.values());
    }

    @Override
    public ApiConfig updateApiConfig(ApiConfig apiConfig) {
        if (apiConfig.getId() == null || !apiConfigMap.containsKey(apiConfig.getId())) {
            throw new IllegalArgumentException("API配置不存在");
        }
        
        // 获取旧配置
        ApiConfig oldConfig = apiConfigMap.get(apiConfig.getId());
        String oldPathMethodKey = getPathMethodKey(oldConfig.getPath(), oldConfig.getMethod());
        
        // 如果路径或方法发生变化，需要取消旧的注册并重新注册
        if (!oldConfig.getPath().equals(apiConfig.getPath()) || 
            !oldConfig.getMethod().equals(apiConfig.getMethod())) {
            // 取消旧的注册
            unregisterApi(oldConfig);
            pathMethodMap.remove(oldPathMethodKey);
            
            // 更新路径方法映射
            String newPathMethodKey = getPathMethodKey(apiConfig.getPath(), apiConfig.getMethod());
            pathMethodMap.put(newPathMethodKey, apiConfig);
        }
        
        // 更新配置
        apiConfigMap.put(apiConfig.getId(), apiConfig);
        
        // 重新注册API
        registerApi(apiConfig);
        
        return apiConfig;
    }

    @Override
    public boolean deleteApiConfig(String id) {
        ApiConfig apiConfig = apiConfigMap.get(id);
        if (apiConfig == null) {
            return false;
        }
        
        // 取消注册API
        unregisterApi(apiConfig);
        
        // 删除配置
        apiConfigMap.remove(id);
        String pathMethodKey = getPathMethodKey(apiConfig.getPath(), apiConfig.getMethod());
        pathMethodMap.remove(pathMethodKey);
        
        return true;
    }

    @Override
    public ApiConfig getApiConfigByPathAndMethod(String path, String method) {
        String pathMethodKey = getPathMethodKey(path, method);
        return pathMethodMap.get(pathMethodKey);
    }

    @Override
    public boolean registerApi(ApiConfig apiConfig) {
        return dynamicApiHandler.registerApi(apiConfig);
    }

    @Override
    public boolean unregisterApi(ApiConfig apiConfig) {
        return dynamicApiHandler.unregisterApi(apiConfig);
    }
    
    /**
     * 生成路径和方法的组合键
     */
    private String getPathMethodKey(String path, String method) {
        return path + ":" + method;
    }
}