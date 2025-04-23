package com.zhangyh.mockeasy.service.impl;

import com.zhangyh.mockeasy.mapper.ApiGroupMapper;
import com.zhangyh.mockeasy.model.ApiGroup;
import com.zhangyh.mockeasy.service.ApiGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * API分组服务实现类
 */
@Service
public class ApiGroupServiceImpl implements ApiGroupService {
    
    @Autowired
    private ApiGroupMapper apiGroupMapper;
    
    @Override
    public ApiGroup saveApiGroup(ApiGroup apiGroup) {
        apiGroupMapper.insert(apiGroup);
        return apiGroup;
    }
    
    @Override
    public ApiGroup getApiGroupById(String id) {
        return apiGroupMapper.selectById(id);
    }
    
    @Override
    public List<ApiGroup> getAllApiGroups() {
        return apiGroupMapper.selectList(null);
    }
    
    @Override
    public ApiGroup updateApiGroup(ApiGroup apiGroup) {
        apiGroupMapper.updateById(apiGroup);
        return apiGroup;
    }
    
    @Override
    public boolean deleteApiGroup(String id) {
        return apiGroupMapper.deleteById(id) > 0;
    }
}