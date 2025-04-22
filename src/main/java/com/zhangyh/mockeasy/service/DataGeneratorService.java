package com.zhangyh.mockeasy.service;

import com.zhangyh.mockeasy.model.ApiConfig;
import com.zhangyh.mockeasy.model.FieldConfig;

/**
 * 数据生成服务接口，用于根据配置生成模拟数据
 */
public interface DataGeneratorService {
    
    /**
     * 根据API配置生成响应数据
     * @param apiConfig API配置信息
     * @return 生成的响应数据
     */
    Object generateData(ApiConfig apiConfig);
    
    /**
     * 根据字段配置生成单个字段数据
     * @param fieldConfig 字段配置
     * @return 生成的字段数据
     */
    Object generateFieldData(FieldConfig fieldConfig);
}