package com.zhangyh.mockeasy.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StreamUtils;

import java.nio.charset.StandardCharsets;

/**
 * 数据库初始化配置，用于在应用启动时自动执行SQL脚本
 */
@Configuration
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        // 读取schema.sql文件
        ClassPathResource resource = new ClassPathResource("db/schema.sql");
        String sql = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);

        // 将SQL脚本拆分为单独的语句
        String[] sqlStatements = sql.split(";");
        
        // 逐条执行SQL语句
        for (String statement : sqlStatements) {
            if (!statement.trim().isEmpty()) {
                try {
                    jdbcTemplate.execute(statement);
                } catch (Exception e) {
                    System.err.println("执行SQL语句失败: " + statement);
                    e.printStackTrace();
                }
            }
        }
    }
}