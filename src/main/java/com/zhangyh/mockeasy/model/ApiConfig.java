package com.zhangyh.mockeasy.model;

import java.util.List;
import java.util.Map;

/**
 * API配置模型，用于存储动态API的配置信息
 */
public class ApiConfig {
    private String id;
    private String path;           // API路径
    private String method;         // 请求方法 (GET, POST, PUT, DELETE等)
    private String description;    // API描述
    private List<FieldConfig> responseFields; // 响应字段配置
    private Integer responseCount; // 响应数据条数
    private Map<String, String> headers; // 响应头
    private Integer delay;         // 模拟延迟(毫秒)
    private Integer statusCode;    // 响应状态码

    public ApiConfig() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<FieldConfig> getResponseFields() {
        return responseFields;
    }

    public void setResponseFields(List<FieldConfig> responseFields) {
        this.responseFields = responseFields;
    }

    public Integer getResponseCount() {
        return responseCount;
    }

    public void setResponseCount(Integer responseCount) {
        this.responseCount = responseCount;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Integer getDelay() {
        return delay;
    }

    public void setDelay(Integer delay) {
        this.delay = delay;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }
}