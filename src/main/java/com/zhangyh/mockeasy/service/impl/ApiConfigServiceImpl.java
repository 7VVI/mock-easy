package com.zhangyh.mockeasy.service.impl;

import com.zhangyh.mockeasy.handler.DynamicApiHandler;
import com.zhangyh.mockeasy.mapper.ApiConfigMapper;
import com.zhangyh.mockeasy.model.ApiConfig;
import com.zhangyh.mockeasy.service.ApiConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * API配置服务实现类
 */
@Service
public class ApiConfigServiceImpl implements ApiConfigService {
    
    @Autowired
    private ApiConfigMapper apiConfigMapper;
    
    @Autowired
    private DynamicApiHandler dynamicApiHandler;
    
    // 路径方法映射缓存
    private final Map<String, String> pathMethodIdMap = new ConcurrentHashMap<>();

    @Override
    public ApiConfig saveApiConfig(ApiConfig apiConfig) {
        // 保存到数据库
        apiConfigMapper.insert(apiConfig);
        
        // 更新路径方法映射缓存
        String pathMethodKey = getPathMethodKey(apiConfig.getPath(), apiConfig.getMethod());
        pathMethodIdMap.put(pathMethodKey, apiConfig.getId());
        
        // 注册API
        registerApi(apiConfig);
        
        return apiConfig;
    }

    @Override
    public ApiConfig getApiConfigById(String id) {
        return apiConfigMapper.selectById(id);
    }

    @Override
    public List<ApiConfig> getAllApiConfigs() {
        return apiConfigMapper.selectList(null);
    }

    @Override
    public ApiConfig updateApiConfig(ApiConfig apiConfig) {
        if (apiConfig.getId() == null) {
            throw new IllegalArgumentException("API配置ID不能为空");
        }
        
        // 获取旧配置
        ApiConfig oldConfig = apiConfigMapper.selectById(apiConfig.getId());
        if (oldConfig == null) {
            throw new IllegalArgumentException("API配置不存在");
        }
        
        String oldPathMethodKey = getPathMethodKey(oldConfig.getPath(), oldConfig.getMethod());
        
        // 如果路径或方法发生变化，需要取消旧的注册并重新注册
        if (!oldConfig.getPath().equals(apiConfig.getPath()) || 
            !oldConfig.getMethod().equals(apiConfig.getMethod())) {
            // 取消旧的注册
            unregisterApi(oldConfig);
            pathMethodIdMap.remove(oldPathMethodKey);
            
            // 更新路径方法映射
            String newPathMethodKey = getPathMethodKey(apiConfig.getPath(), apiConfig.getMethod());
            pathMethodIdMap.put(newPathMethodKey, apiConfig.getId());
        }
        
        // 更新配置到数据库
        apiConfigMapper.updateById(apiConfig);
        
        // 重新注册API
        registerApi(apiConfig);
        
        return apiConfig;
    }

    @Override
    public boolean deleteApiConfig(String id) {
        ApiConfig apiConfig = apiConfigMapper.selectById(id);
        if (apiConfig == null) {
            return false;
        }
        
        // 取消注册API
        unregisterApi(apiConfig);
        
        // 从数据库删除配置
        apiConfigMapper.deleteById(id);
        
        // 删除路径方法映射
        String pathMethodKey = getPathMethodKey(apiConfig.getPath(), apiConfig.getMethod());
        pathMethodIdMap.remove(pathMethodKey);
        
        return true;
    }

    @Override
    public ApiConfig getApiConfigByPathAndMethod(String path, String method) {
        String pathMethodKey = getPathMethodKey(path, method);
        String id = pathMethodIdMap.get(pathMethodKey);
        if (id == null) {
            // 如果缓存中没有，从数据库查询
            com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<ApiConfig> queryWrapper = 
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
            queryWrapper.eq(ApiConfig::getPath, path).eq(ApiConfig::getMethod, method);
            ApiConfig apiConfig = apiConfigMapper.selectOne(queryWrapper);
            
            // 更新缓存
            if (apiConfig != null) {
                pathMethodIdMap.put(pathMethodKey, apiConfig.getId());
            }
            
            return apiConfig;
        }
        
        return apiConfigMapper.selectById(id);
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