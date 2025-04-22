package com.zhangyh.mockeasy.model;

/**
 * 字段配置模型，用于定义API响应字段的类型、规则等信息
 */
public class FieldConfig {
    private String name;           // 字段名称
    private String type;           // 字段类型 (String, Integer, Boolean, Array, Object等)
    private String rule;           // 数据生成规则 (例如：随机、递增、固定值等)
    private String value;          // 固定值或规则参数
    private Integer minLength;     // 最小长度/值
    private Integer maxLength;     // 最大长度/值
    private Boolean required;      // 是否必需
    private String description;    // 字段描述
    private Boolean isNested;      // 是否为嵌套对象
    private java.util.List<FieldConfig> children; // 嵌套字段配置（当type为Object或Array时）

    public FieldConfig() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getMinLength() {
        return minLength;
    }

    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsNested() {
        return isNested;
    }

    public void setIsNested(Boolean isNested) {
        this.isNested = isNested;
    }

    public java.util.List<FieldConfig> getChildren() {
        return children;
    }

    public void setChildren(java.util.List<FieldConfig> children) {
        this.children = children;
    }
}