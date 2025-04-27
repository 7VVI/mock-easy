/**
 * API表单处理脚本
 */

$(document).ready(function() {
    // 字段计数器
    let fieldCounter = 0;
    let headerCounter = 0;
    
    // 添加字段按钮点击事件
    $('#addField').click(function() {
        addField();
    });
    
    // 添加响应头按钮点击事件
    $('#addHeader').click(function() {
        addHeader();
    });
    
    // 保存API按钮点击事件
    $('#saveApi').click(function() {
        saveApiConfig();
    });
    
    // 如果是编辑页面，加载现有配置
    if ($('#apiId').length && $('#apiId').val()) {
        loadApiConfig($('#apiId').val());
    }
    
    // 添加字段函数
    function addField(fieldData = {}) {
        const fieldId = 'field-' + fieldCounter++;
        const fieldHtml = `
            <div class="field-row" id="${fieldId}">
                <div class="d-flex justify-content-between align-items-center mb-2">
                    <h5 class="mb-0">字段配置</h5>
                    <button type="button" class="btn btn-sm btn-danger remove-field" data-field-id="${fieldId}">
                        <i class="fas fa-trash"></i> 删除
                    </button>
                </div>
                <div class="row">
                    <div class="col-md-6 mb-2">
                        <label class="form-label">字段名称 <span class="text-danger">*</span></label>
                        <input type="text" class="form-control field-name" value="${fieldData.name || ''}" required>
                    </div>
                    <div class="col-md-6 mb-2">
                        <label class="form-label">字段类型 <span class="text-danger">*</span></label>
                        <select class="form-select field-type">
                            <option value="String" ${fieldData.type === 'String' ? 'selected' : ''}>String</option>
                            <option value="Integer" ${fieldData.type === 'Integer' ? 'selected' : ''}>Integer</option>
                            <option value="Double" ${fieldData.type === 'Double' ? 'selected' : ''}>Double</option>
                            <option value="Boolean" ${fieldData.type === 'Boolean' ? 'selected' : ''}>Boolean</option>
                            <option value="Date" ${fieldData.type === 'Date' ? 'selected' : ''}>Date</option>
                            <option value="DateTime" ${fieldData.type === 'DateTime' ? 'selected' : ''}>DateTime</option>
                            <option value="Array" ${fieldData.type === 'Array' ? 'selected' : ''}>Array</option>
                            <option value="Object" ${fieldData.type === 'Object' ? 'selected' : ''}>Object</option>
                        </select>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6 mb-2">
                        <label class="form-label">数据生成规则</label>
                        <select class="form-select field-rule">
                            <option value="random" ${fieldData.rule === 'random' ? 'selected' : ''}>随机</option>
                            <option value="uuid" ${fieldData.rule === 'uuid' ? 'selected' : ''}>UUID</option>
                            <option value="name" ${fieldData.rule === 'name' ? 'selected' : ''}>姓名</option>
                            <option value="email" ${fieldData.rule === 'email' ? 'selected' : ''}>邮箱</option>
                            <option value="phone" ${fieldData.rule === 'phone' ? 'selected' : ''}>手机号</option>
                            <option value="address" ${fieldData.rule === 'address' ? 'selected' : ''}>地址</option>
                            <option value="date" ${fieldData.rule === 'date' ? 'selected' : ''}>日期</option>
                            <option value="datetime" ${fieldData.rule === 'datetime' ? 'selected' : ''}>日期时间</option>
                            <option value="fixed" ${fieldData.rule === 'fixed' ? 'selected' : ''}>固定值</option>
                        </select>
                    </div>
                    <div class="col-md-6 mb-2">
                        <label class="form-label">固定值/规则参数</label>
                        <input type="text" class="form-control field-value" value="${fieldData.value || ''}">
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-4 mb-2">
                        <label class="form-label">最小长度/值</label>
                        <input type="number" class="form-control field-min-length" value="${fieldData.minLength || '0'}">
                    </div>
                    <div class="col-md-4 mb-2">
                        <label class="form-label">最大长度/值</label>
                        <input type="number" class="form-control field-max-length" value="${fieldData.maxLength || '100'}">
                    </div>
                    <div class="col-md-4 mb-2">
                        <label class="form-label">是否必需</label>
                        <select class="form-select field-required">
                            <option value="true" ${fieldData.required !== false ? 'selected' : ''}>是</option>
                            <option value="false" ${fieldData.required === false ? 'selected' : ''}>否</option>
                        </select>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12 mb-2">
                        <label class="form-label">字段描述</label>
                        <input type="text" class="form-control field-description" value="${fieldData.description || ''}">
                    </div>
                </div>
                <div class="nested-fields-container" style="display: ${(fieldData.type === 'Array' || fieldData.type === 'Object') ? 'block' : 'none'}">
                    <div class="d-flex justify-content-between align-items-center mb-2">
                        <h6 class="mb-0">嵌套字段</h6>
                        <button type="button" class="btn btn-sm btn-primary add-nested-field" data-parent-id="${fieldId}">
                            <i class="fas fa-plus"></i> 添加嵌套字段
                        </button>
                    </div>
                    <div class="nested-fields" id="${fieldId}-children">
                        <!-- 嵌套字段将在这里动态添加 -->
                    </div>
                </div>
            </div>
        `;
        
        $('#fieldsContainer').append(fieldHtml);
        
        // 添加嵌套字段的事件处理
        $(`#${fieldId} .field-type`).change(function() {
            const type = $(this).val();
            const nestedContainer = $(this).closest('.field-row').find('.nested-fields-container');
            
            if (type === 'Array' || type === 'Object') {
                nestedContainer.show();
            } else {
                nestedContainer.hide();
            }
        });
        
        // 添加嵌套字段按钮点击事件
        $(`#${fieldId} .add-nested-field`).click(function() {
            const parentId = $(this).data('parent-id');
            addNestedField(parentId);
        });
        
        // 删除字段按钮点击事件
        $(`#${fieldId} .remove-field`).click(function() {
            const fieldId = $(this).data('field-id');
            $(`#${fieldId}`).remove();
        });
        
        // 如果有子字段，添加它们
        if (fieldData.children && fieldData.children.length > 0) {
            fieldData.children.forEach(childField => {
                addNestedField(fieldId, childField);
            });
        }
    }
    
    // 添加嵌套字段函数
    function addNestedField(parentId, fieldData = {}) {
        const fieldId = parentId + '-child-' + fieldCounter++;
        const fieldHtml = `
            <div class="field-row nested-field" id="${fieldId}">
                <div class="d-flex justify-content-between align-items-center mb-2">
                    <h6 class="mb-0">嵌套字段</h6>
                    <button type="button" class="btn btn-sm btn-danger remove-field" data-field-id="${fieldId}">
                        <i class="fas fa-trash"></i> 删除
                    </button>
                </div>
                <div class="row">
                    <div class="col-md-6 mb-2">
                        <label class="form-label">字段名称 <span class="text-danger">*</span></label>
                        <input type="text" class="form-control field-name" value="${fieldData.name || ''}" required>
                    </div>
                    <div class="col-md-6 mb-2">
                        <label class="form-label">字段类型 <span class="text-danger">*</span></label>
                        <select class="form-select field-type">
                            <option value="String" ${fieldData.type === 'String' ? 'selected' : ''}>String</option>
                            <option value="Integer" ${fieldData.type === 'Integer' ? 'selected' : ''}>Integer</option>
                            <option value="Double" ${fieldData.type === 'Double' ? 'selected' : ''}>Double</option>
                            <option value="Boolean" ${fieldData.type === 'Boolean' ? 'selected' : ''}>Boolean</option>
                            <option value="Date" ${fieldData.type === 'Date' ? 'selected' : ''}>Date</option>
                            <option value="DateTime" ${fieldData.type === 'DateTime' ? 'selected' : ''}>DateTime</option>
                            <option value="Array" ${fieldData.type === 'Array' ? 'selected' : ''}>Array</option>
                            <option value="Object" ${fieldData.type === 'Object' ? 'selected' : ''}>Object</option>
                        </select>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6 mb-2">
                        <label class="form-label">数据生成规则</label>
                        <select class="form-select field-rule">
                            <option value="random" ${fieldData.rule === 'random' ? 'selected' : ''}>随机</option>
                            <option value="uuid" ${fieldData.rule === 'uuid' ? 'selected' : ''}>UUID</option>
                            <option value="name" ${fieldData.rule === 'name' ? 'selected' : ''}>姓名</option>
                            <option value="email" ${fieldData.rule === 'email' ? 'selected' : ''}>邮箱</option>
                            <option value="phone" ${fieldData.rule === 'phone' ? 'selected' : ''}>手机号</option>
                            <option value="address" ${fieldData.rule === 'address' ? 'selected' : ''}>地址</option>
                            <option value="date" ${fieldData.rule === 'date' ? 'selected' : ''}>日期</option>
                            <option value="datetime" ${fieldData.rule === 'datetime' ? 'selected' : ''}>日期时间</option>
                            <option value="fixed" ${fieldData.rule === 'fixed' ? 'selected' : ''}>固定值</option>
                        </select>
                    </div>
                    <div class="col-md-6 mb-2">
                        <label class="form-label">固定值/规则参数</label>
                        <input type="text" class="form-control field-value" value="${fieldData.value || ''}">
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-4 mb-2">
                        <label class="form-label">最小长度/值</label>
                        <input type="number" class="form-control field-min-length" value="${fieldData.minLength || '0'}">
                    </div>
                    <div class="col-md-4 mb-2">
                        <label class="form-label">最大长度/值</label>
                        <input type="number" class="form-control field-max-length" value="${fieldData.maxLength || '100'}">
                    </div>
                    <div class="col-md-4 mb-2">
                        <label class="form-label">是否必需</label>
                        <select class="form-select field-required">
                            <option value="true" ${fieldData.required !== false ? 'selected' : ''}>是</option>
                            <option value="false" ${fieldData.required === false ? 'selected' : ''}>否</option>
                        </select>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12 mb-2">
                        <label class="form-label">字段描述</label>
                        <input type="text" class="form-control field-description" value="${fieldData.description || ''}">
                    </div>
                </div>
            </div>
        `;
        
        $(`#${parentId}-children`).append(fieldHtml);
        
        // 删除嵌套字段按钮点击事件
        $(`#${fieldId} .remove-field`).click(function() {
            const fieldId = $(this).data('field-id');
            $(`#${fieldId}`).remove();
        });
    }
    
    // 添加响应头函数
    function addHeader(headerData = {}) {
        const headerId = 'header-' + headerCounter++;
        const headerHtml = `
            <div class="row mb-2" id="${headerId}">
                <div class="col-md-5">
                    <input type="text" class="form-control header-name" placeholder="响应头名称" value="${headerData.name || ''}">
                </div>
                <div class="col-md-5">
                    <input type="text" class="form-control header-value" placeholder="响应头值" value="${headerData.value || ''}">
                </div>
                <div class="col-md-2">
                    <button type="button" class="btn btn-danger remove-header" data-header-id="${headerId}">
                        <i class="fas fa-trash"></i>
                    </button>
                </div>
            </div>
        `;
        
        $('#headersContainer').append(headerHtml);
        
        // 删除响应头按钮点击事件
        $(`#${headerId} .remove-header`).click(function() {
            const headerId = $(this).data('header-id');
            $(`#${headerId}`).remove();
        });
    }
    
    // 收集字段数据函数
    function collectFieldData(container) {
        const fields = [];
        
        container.find('> .field-row').each(function() {
            const $field = $(this);
            const fieldData = {
                name: $field.find('.field-name').val(),
                type: $field.find('.field-type').val(),
                rule: $field.find('.field-rule').val(),
                value: $field.find('.field-value').val(),
                minLength: parseInt($field.find('.field-min-length').val()) || 0,
                maxLength: parseInt($field.find('.field-max-length').val()) || 100,
                required: $field.find('.field-required').val() === 'true',
                description: $field.find('.field-description').val()
            };
            
            // 如果是数组或对象类型，收集子字段
            if (fieldData.type === 'Array' || fieldData.type === 'Object') {
                fieldData.children = collectFieldData($field.find('.nested-fields'));
            }
            
            fields.push(fieldData);
        });
        
        return fields;
    }
    
    // 收集响应头数据函数
    function collectHeaderData() {
        const headers = {};
        
        $('#headersContainer .row').each(function() {
            const name = $(this).find('.header-name').val();
            const value = $(this).find('.header-value').val();
            
            if (name && value) {
                headers[name] = value;
            }
        });
        
        return headers;
    }
    
    // 保存API配置函数
    function saveApiConfig() {
        // 收集表单数据
        const apiConfig = {
            id: $('#apiId').val(),
            path: $('#path').val(),
            method: $('#method').val(),
            description: $('#description').val(),
            statusCode: parseInt($('#statusCode').val()) || 200,
            responseCount: parseInt($('#responseCount').val()) || 1,
            delay: parseInt($('#delay').val()) || 0,
            responseFields: collectFieldData($('#fieldsContainer')),
            headers: collectHeaderData()
        };
        
        // 验证必填字段
        if (!apiConfig.path) {
            showNotification('请填写API路径', 'error');
            return;
        }
        
        if (!apiConfig.path.startsWith('/')) {
            apiConfig.path = '/' + apiConfig.path;
        }
        
        // 发送AJAX请求保存配置
        const url = apiConfig.id ? `/api/config/${apiConfig.id}` : '/api/config';
        const method = apiConfig.id ? 'PUT' : 'POST';
        
        $.ajax({
            url: url,
            method: method,
            contentType: 'application/json',
            data: JSON.stringify(apiConfig),
            success: function(response) {
                showNotification('API配置保存成功', 'success');
                setTimeout(function() {
                    window.location.href = '/';
                }, 1000);
            },
            error: function(xhr, status, error) {
                showNotification('保存失败: ' + error, 'error');
            }
        });
    }
    
    // 加载API配置函数
    function loadApiConfig(id) {
        $.ajax({
            url: `/api/config/${id}`,
            method: 'GET',
            success: function(apiConfig) {
                // 填充基本信息
                $('#path').val(apiConfig.path);
                $('#method').val(apiConfig.method);
                $('#description').val(apiConfig.description);
                $('#statusCode').val(apiConfig.statusCode || 200);
                $('#responseCount').val(apiConfig.responseCount || 1);
                $('#delay').val(apiConfig.delay || 0);
                
                // 清空现有字段和响应头
                $('#fieldsContainer').empty();
                $('#headersContainer').empty();
                
                // 添加响应字段
                if (apiConfig.responseFields && apiConfig.responseFields.length > 0) {
                    apiConfig.responseFields.forEach(field => {
                        addField(field);
                    });
                }
                
                // 添加响应头
                if (apiConfig.headers) {
                    Object.keys(apiConfig.headers).forEach(name => {
                        addHeader({
                            name: name,
                            value: apiConfig.headers[name]
                        });
                    });
                }
            },
            error: function(xhr, status, error) {
                showNotification('加载API配置失败: ' + error, 'error');
            }
        });
    }
});