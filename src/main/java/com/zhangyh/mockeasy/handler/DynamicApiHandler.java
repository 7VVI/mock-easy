package com.zhangyh.mockeasy.handler;

import com.zhangyh.mockeasy.model.ApiConfig;
import com.zhangyh.mockeasy.service.DataGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import jakarta.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 动态API处理器，负责注册和处理动态API请求
 * 支持动态创建、更新和删除API端点
 */
@Component
public class DynamicApiHandler {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(DynamicApiHandler.class);

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;
    
    @Autowired
    private DataGeneratorService dataGeneratorService;
    
    // 存储已注册的API映射信息
    private final Map<String, RequestMappingInfo> requestMappingInfoMap = new ConcurrentHashMap<>();
    
    // 存储API配置信息，用于处理请求时获取
    private final Map<String, ApiConfig> apiConfigMap = new ConcurrentHashMap<>();
    
    // 处理动态API请求的方法
    private Method handleMethod;
    
    @PostConstruct
    public void init() throws NoSuchMethodException {
        // 获取处理请求的方法引用，不再需要ApiConfig参数
        handleMethod = DynamicApiHandler.class.getDeclaredMethod("handleRequest");
        log.info("动态API处理器初始化完成");
    }
    
    /**
     * 注册API到Spring MVC
     * @param apiConfig API配置信息
     * @return 是否注册成功
     */
    public boolean registerApi(ApiConfig apiConfig) {
        try {
            // 如果已存在，先取消注册
            unregisterApi(apiConfig);
            
            // 创建RequestMappingInfo
            RequestMethod requestMethod = RequestMethod.valueOf(apiConfig.getMethod());
            RequestMappingInfo mappingInfo = RequestMappingInfo
                    .paths(apiConfig.getPath())
                    .methods(requestMethod)
                    .build();
            
            // 注册处理器方法
            handlerMapping.registerMapping(mappingInfo, this, handleMethod);
            
            // 保存映射信息
            String key = getApiKey(apiConfig);
            requestMappingInfoMap.put(key, mappingInfo);
            
            // 保存API配置信息，用于处理请求时获取
            apiConfigMap.put(key, apiConfig);
            
            log.info("成功注册API: {} {}", apiConfig.getMethod(), apiConfig.getPath());
            return true;
        } catch (Exception e) {
            log.error("注册API失败: {} {}, 错误: {}", apiConfig.getMethod(), apiConfig.getPath(), e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * 取消注册API
     * @param apiConfig API配置信息
     * @return 是否取消成功
     */
    public boolean unregisterApi(ApiConfig apiConfig) {
        try {
            String key = getApiKey(apiConfig);
            RequestMappingInfo mappingInfo = requestMappingInfoMap.get(key);
            
            if (mappingInfo != null) {
                handlerMapping.unregisterMapping(mappingInfo);
                requestMappingInfoMap.remove(key);
                apiConfigMap.remove(key);
                log.info("成功取消注册API: {} {}", apiConfig.getMethod(), apiConfig.getPath());
            } else {
                log.debug("尝试取消注册不存在的API: {} {}", apiConfig.getMethod(), apiConfig.getPath());
            }
            
            return true;
        } catch (Exception e) {
            log.error("取消注册API失败: {} {}, 错误: {}", apiConfig.getMethod(), apiConfig.getPath(), e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * 处理动态API请求
     * 从当前请求路径和方法获取对应的API配置
     * @return 响应数据
     */
    public ResponseEntity<Object> handleRequest() {
        // 获取当前请求信息
        jakarta.servlet.http.HttpServletRequest request = 
            ((org.springframework.web.context.request.ServletRequestAttributes) 
            org.springframework.web.context.request.RequestContextHolder.getRequestAttributes()).getRequest();
        
        String path = request.getRequestURI();
        String method = request.getMethod();
        String key = path + ":" + method;
        
        // 从配置映射中获取API配置
        ApiConfig apiConfig = apiConfigMap.get(key);
        
        if (apiConfig == null) {
            log.error("未找到API配置: {} {}", method, path);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("API配置不存在");
        }
        
        log.info("处理API请求: {} {}", method, path);
        
        try {
            // 模拟延迟
            if (apiConfig.getDelay() != null && apiConfig.getDelay() > 0) {
                Thread.sleep(apiConfig.getDelay());
                log.debug("API请求延迟: {}ms, {} {}", apiConfig.getDelay(), method, path);
            }
            
            // 生成响应数据
            Object responseData = dataGeneratorService.generateData(apiConfig);
            
            // 设置响应头
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            if (apiConfig.getHeaders() != null) {
                apiConfig.getHeaders().forEach(headers::add);
            }
            
            // 添加默认的Content-Type头
            if (!headers.containsKey("Content-Type")) {
                headers.add("Content-Type", "application/json;charset=UTF-8");
            }
            
            // 设置状态码
            HttpStatus status = HttpStatus.OK;
            if (apiConfig.getStatusCode() != null) {
                status = HttpStatus.valueOf(apiConfig.getStatusCode());
            }
            
            log.debug("API请求处理完成: {} {}, 状态码: {}", method, path, status.value());
            return new ResponseEntity<>(responseData, headers, status);
        } catch (Exception e) {
            log.error("处理API请求失败: {} {}, 错误: {}", method, path, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("处理请求时发生错误: " + e.getMessage());
        }
    }
    
    /**
     * 生成API唯一键
     */
    private String getApiKey(ApiConfig apiConfig) {
        return apiConfig.getPath() + ":" + apiConfig.getMethod();
    }
    
    /**
     * 根据路径和方法获取API配置
     * @param path API路径
     * @param method HTTP方法
     * @return API配置信息，如果不存在则返回null
     */
    public ApiConfig getApiConfig(String path, String method) {
        String key = path + ":" + method;
        return apiConfigMap.get(key);
    }
    
    /**
     * 获取所有已注册的API配置
     * @return API配置列表
     */
    public java.util.List<ApiConfig> getAllApiConfigs() {
        return new java.util.ArrayList<>(apiConfigMap.values());
    }
    
    /**
     * 清除所有注册的API
     * 通常在应用关闭或重新加载配置时使用
     */
    public void clearAllApis() {
        log.info("开始清除所有注册的API，共{}个", requestMappingInfoMap.size());
        
        try {
            // 复制一份键集合，避免并发修改异常
            java.util.Set<String> keys = new java.util.HashSet<>(requestMappingInfoMap.keySet());
            
            for (String key : keys) {
                RequestMappingInfo mappingInfo = requestMappingInfoMap.get(key);
                if (mappingInfo != null) {
                    handlerMapping.unregisterMapping(mappingInfo);
                }
            }
            
            // 清空映射和配置
            requestMappingInfoMap.clear();
            apiConfigMap.clear();
            
            log.info("所有API已清除");
        } catch (Exception e) {
            log.error("清除API时发生错误: {}", e.getMessage(), e);
        }
    }
}