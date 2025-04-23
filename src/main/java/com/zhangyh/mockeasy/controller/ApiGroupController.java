package com.zhangyh.mockeasy.controller;

import com.zhangyh.mockeasy.model.ApiGroup;
import com.zhangyh.mockeasy.service.ApiGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * API分组控制器，提供Web界面和REST API
 */
@Controller
public class ApiGroupController {

    @Autowired
    private ApiGroupService apiGroupService;
    
    /**
     * 分组管理页面
     */
    @GetMapping("/groups")
    public String groupsPage(Model model) {
        List<ApiGroup> apiGroups = apiGroupService.getAllApiGroups();
        model.addAttribute("apiGroups", apiGroups);
        return "groups";
    }
    
    /**
     * 创建分组页面
     */
    @GetMapping("/group/create")
    public String createGroupPage(Model model) {
        model.addAttribute("apiGroup", new ApiGroup());
        return "create-group";
    }
    
    /**
     * 编辑分组页面
     */
    @GetMapping("/group/edit/{id}")
    public String editGroupPage(@PathVariable String id, Model model) {
        ApiGroup apiGroup = apiGroupService.getApiGroupById(id);
        model.addAttribute("apiGroup", apiGroup);
        return "edit-group";
    }
    
    /**
     * 保存API分组
     */
    @PostMapping("/api/group")
    @ResponseBody
    public ResponseEntity<ApiGroup> saveApiGroup(@RequestBody ApiGroup apiGroup) {
        ApiGroup savedGroup = apiGroupService.saveApiGroup(apiGroup);
        return ResponseEntity.ok(savedGroup);
    }
    
    /**
     * 获取API分组
     */
    @GetMapping("/api/group/{id}")
    @ResponseBody
    public ResponseEntity<ApiGroup> getApiGroup(@PathVariable String id) {
        ApiGroup apiGroup = apiGroupService.getApiGroupById(id);
        if (apiGroup == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(apiGroup);
    }
    
    /**
     * 获取所有API分组
     */
    @GetMapping("/api/groups")
    @ResponseBody
    public ResponseEntity<List<ApiGroup>> getAllApiGroups() {
        List<ApiGroup> apiGroups = apiGroupService.getAllApiGroups();
        return ResponseEntity.ok(apiGroups);
    }
    
    /**
     * 更新API分组
     */
    @PutMapping("/api/group/{id}")
    @ResponseBody
    public ResponseEntity<ApiGroup> updateApiGroup(@PathVariable String id, @RequestBody ApiGroup apiGroup) {
        apiGroup.setId(id);
        ApiGroup updatedGroup = apiGroupService.updateApiGroup(apiGroup);
        return ResponseEntity.ok(updatedGroup);
    }
    
    /**
     * 删除API分组
     */
    @DeleteMapping("/api/group/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteApiGroup(@PathVariable String id) {
        boolean deleted = apiGroupService.deleteApiGroup(id);
        if (deleted) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}