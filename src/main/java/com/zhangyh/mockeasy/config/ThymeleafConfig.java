package com.zhangyh.mockeasy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;

/**
 * Thymeleaf配置类
 */
@Configuration
public class ThymeleafConfig {

    /**
     * 配置Thymeleaf布局方言
     */
    @Bean
    @Description("Thymeleaf布局方言，用于页面布局")
    public LayoutDialect layoutDialect() {
        return new LayoutDialect();
    }
}