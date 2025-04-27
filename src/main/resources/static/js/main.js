/**
 * Mock Easy 主JavaScript文件
 */

// 页面加载完成后执行
$(document).ready(function() {
    // 通用的AJAX错误处理
    $(document).ajaxError(function(event, jqXHR, settings, thrownError) {
        console.error('AJAX请求失败:', thrownError);
        let errorMessage = '请求失败';
        
        if (jqXHR.responseJSON && jqXHR.responseJSON.message) {
            errorMessage = jqXHR.responseJSON.message;
        } else if (jqXHR.responseText) {
            try {
                const response = JSON.parse(jqXHR.responseText);
                if (response.message) {
                    errorMessage = response.message;
                }
            } catch (e) {
                errorMessage = jqXHR.responseText;
            }
        }
        
        // 可以在这里添加全局错误提示
        console.error(errorMessage);
    });
    
    // 表单验证
    $('.needs-validation').each(function() {
        $(this).on('submit', function(event) {
            if (!this.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            $(this).addClass('was-validated');
        });
    });
    
    // 工具提示初始化
    $('[data-bs-toggle="tooltip"]').tooltip();
    
    // 返回顶部按钮
    $(window).scroll(function() {
        if ($(this).scrollTop() > 300) {
            $('#back-to-top').fadeIn();
        } else {
            $('#back-to-top').fadeOut();
        }
    });
    
    $('#back-to-top').click(function() {
        $('html, body').animate({scrollTop: 0}, 800);
        return false;
    });
});

// 格式化日期时间
function formatDateTime(dateTime) {
    if (!dateTime) return '';
    
    const date = new Date(dateTime);
    return date.toLocaleString();
}

// 显示确认对话框
function confirmAction(message, callback) {
    if (confirm(message)) {
        callback();
    }
}

// 显示通知消息
function showNotification(message, type = 'success') {
    // 使用Element UI风格的消息提示组件
    switch(type) {
        case 'success':
            ElementMessage.success(message);
            break;
        case 'warning':
            ElementMessage.warning(message);
            break;
        case 'danger':
        case 'error':
            ElementMessage.error(message);
            break;
        default:
            ElementMessage.info(message);
    }
}

// 复制文本到剪贴板
function copyToClipboard(text) {
    const textarea = document.createElement('textarea');
    textarea.value = text;
    document.body.appendChild(textarea);
    textarea.select();
    document.execCommand('copy');
    document.body.removeChild(textarea);
    
    showNotification('已复制到剪贴板');
}