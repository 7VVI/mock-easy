package com.zhangyh.mockeasy.controller;

import com.zhangyh.mockeasy.model.ApiConfig;
import com.zhangyh.mockeasy.service.ApiConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * API配置控制器，提供Web界面和REST API
 */
@Controller
public class ApiConfigController {

    @Autowired
    private ApiConfigService apiConfigService;
    
    /**
     * 首页，显示API配置列表
     */
    @GetMapping("/")
    public String index(Model model) {
        List<ApiConfig> apiConfigs = apiConfigService.getAllApiConfigs();
        model.addAttribute("apiConfigs", apiConfigs);
        return "index";
    }
    
    /**
     * 创建API配置页面
     */
    @GetMapping("/create")
    public String createPage(Model model) {
        model.addAttribute("apiConfig", new ApiConfig());
        return "create";
    }
    
    /**
     * 编辑API配置页面
     */
    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable String id, Model model) {
        ApiConfig apiConfig = apiConfigService.getApiConfigById(id);
        model.addAttribute("apiConfig", apiConfig);
        return "edit";
    }
    
    /**
     * 保存API配置
     */
    @PostMapping("/api/config")
    @ResponseBody
    public ResponseEntity<ApiConfig> saveApiConfig(@RequestBody ApiConfig apiConfig) {
        ApiConfig savedConfig = apiConfigService.saveApiConfig(apiConfig);
        return ResponseEntity.ok(savedConfig);
    }
    
    /**
     * 获取API配置
     */
    @GetMapping("/api/config/{id}")
    @ResponseBody
    public ResponseEntity<ApiConfig> getApiConfig(@PathVariable String id) {
        ApiConfig apiConfig = apiConfigService.getApiConfigById(id);
        if (apiConfig == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(apiConfig);
    }
    
    /**
     * 获取所有API配置
     */
    @GetMapping("/api/configs")
    @ResponseBody
    public ResponseEntity<List<ApiConfig>> getAllApiConfigs() {
        List<ApiConfig> apiConfigs = apiConfigService.getAllApiConfigs();
        return ResponseEntity.ok(apiConfigs);
    }
    
    /**
     * 更新API配置
     */
    @PutMapping("/api/config/{id}")
    @ResponseBody
    public ResponseEntity<ApiConfig> updateApiConfig(@PathVariable String id, @RequestBody ApiConfig apiConfig) {
        apiConfig.setId(id);
        ApiConfig updatedConfig = apiConfigService.updateApiConfig(apiConfig);
        return ResponseEntity.ok(updatedConfig);
    }
    
    /**
     * 删除API配置
     */
    @DeleteMapping("/api/config/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteApiConfig(@PathVariable String id) {
        boolean deleted = apiConfigService.deleteApiConfig(id);
        if (deleted) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}