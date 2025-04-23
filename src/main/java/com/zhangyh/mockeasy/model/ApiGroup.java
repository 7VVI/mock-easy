package com.zhangyh.mockeasy.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * API分组模型，用于对API接口进行分类管理
 */
@TableName("api_group")
public class ApiGroup {
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    private String name;        // 分组名称
    private String description; // 分组描述
    
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;    // 创建时间
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;    // 更新时间

    public ApiGroup() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}