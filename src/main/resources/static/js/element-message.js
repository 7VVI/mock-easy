/**
 * Element UI 风格的消息提示组件
 */

const ElementMessage = (function() {
    // 消息计数器，用于生成唯一ID
    let messageCount = 0;
    // 消息队列
    const messages = [];
    // 默认配置
    const defaults = {
        type: 'info',  // 消息类型: info, success, warning, error
        message: '',   // 消息内容
        duration: 3000, // 显示时间，单位毫秒
        showClose: true, // 是否显示关闭按钮
        onClose: null,  // 关闭回调
        offset: 20      // 消息距离顶部的偏移量
    };

    // 创建消息DOM元素
    function createMessage(options) {
        const id = 'el-message-' + messageCount++;
        const messageEl = document.createElement('div');
        messageEl.id = id;
        messageEl.className = `el-message el-message--${options.type}`;
        messageEl.style.top = options.offset + 'px';
        
        // 图标
        let iconClass = '';
        switch(options.type) {
            case 'success':
                iconClass = 'fa-check-circle';
                break;
            case 'warning':
                iconClass = 'fa-exclamation-circle';
                break;
            case 'error':
                iconClass = 'fa-times-circle';
                break;
            default: // info
                iconClass = 'fa-info-circle';
        }
        
        // 构建消息内容
        messageEl.innerHTML = `
            <i class="el-message__icon fas ${iconClass}"></i>
            <div class="el-message__content">${options.message}</div>
            ${options.showClose ? '<i class="el-message__closeBtn fas fa-times"></i>' : ''}
        `;
        
        // 添加到body
        document.body.appendChild(messageEl);
        
        // 添加关闭事件
        if (options.showClose) {
            const closeBtn = messageEl.querySelector('.el-message__closeBtn');
            closeBtn.addEventListener('click', function() {
                close(id, options.onClose);
            });
        }
        
        // 自动关闭
        if (options.duration > 0) {
            setTimeout(function() {
                close(id, options.onClose);
            }, options.duration);
        }
        
        // 添加到消息队列
        messages.push({
            id: id,
            el: messageEl,
            height: messageEl.offsetHeight,
            offset: options.offset
        });
        
        // 重新计算位置
        reposition();
        
        return id;
    }
    
    // 关闭消息
    function close(id, callback) {
        const index = messages.findIndex(msg => msg.id === id);
        if (index === -1) return;
        
        const message = messages[index];
        const el = message.el;
        
        // 添加淡出动画
        el.classList.add('el-message-fade-leave-active');
        
        // 从DOM中移除
        setTimeout(function() {
            document.body.removeChild(el);
            messages.splice(index, 1);
            reposition();
            
            // 执行回调
            if (typeof callback === 'function') {
                callback();
            }
        }, 300);
    }
    
    // 重新计算消息位置
    function reposition() {
        let currentOffset = defaults.offset;
        messages.forEach(msg => {
            msg.offset = currentOffset;
            msg.el.style.top = currentOffset + 'px';
            currentOffset += msg.height + 16; // 16px为消息间距
        });
    }
    
    // 创建不同类型的消息方法
    function createMessageMethod(type) {
        return function(message, options = {}) {
            if (typeof message === 'object') {
                options = message;
            } else {
                options.message = message;
            }
            
            return createMessage(Object.assign({}, defaults, options, { type }));
        };
    }
    
    // 公开的API
    return {
        info: createMessageMethod('info'),
        success: createMessageMethod('success'),
        warning: createMessageMethod('warning'),
        error: createMessageMethod('error'),
        close: close
    };
})();