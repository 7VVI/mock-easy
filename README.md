# Mock Easy - 接口模拟工具

## 项目介绍

Mock Easy 是一个基于 Spring Boot 开发的接口模拟工具，可以通过 Web 页面快速配置和创建模拟接口，实现接口的动态注册和数据模拟。

### 主要功能

- 通过 Web 界面动态创建和管理 API 接口
- 配置接口的响应数据，包括字段类型、数据生成规则、数据长度等
- 支持嵌套对象和数组的数据生成
- 自定义响应状态码、响应头和响应延迟
- 支持多种数据类型和生成规则

## 技术栈

- Java 17
- Spring Boot 3.4.4
- Thymeleaf 模板引擎
- Bootstrap 5.3.2
- jQuery 3.7.1

## 快速开始

### 环境要求

- JDK 17+
- Gradle 或 Maven

### 运行项目

1. 克隆项目到本地

```bash
git clone https://github.com/yourusername/mock-easy.git
cd mock-easy
```

2. 构建并运行项目

```bash
# 使用 Gradle
./gradlew bootRun

# 或使用 Maven
./mvnw spring-boot:run
```

3. 访问应用

打开浏览器，访问 http://localhost:8080

## 使用指南

![image-20250511085119204](https://picgo-liziyuan.oss-cn-hangzhou.aliyuncs.com/zhangyhimage-20250511085119204.png)

### 创建模拟接口

![image-20250511085137190](https://picgo-liziyuan.oss-cn-hangzhou.aliyuncs.com/zhangyhimage-20250511085137190.png)

1. 在首页点击「创建新接口」按钮
2. 填写接口基本信息：
   - API 路径：接口的访问路径，如 `/api/users`
   - 请求方法：GET、POST、PUT、DELETE 等
   - API 描述：接口的用途描述
   - 状态码：响应的 HTTP 状态码
   - 响应数据条数：返回单个对象或多条数据的数组
   - 响应延迟：模拟接口响应延迟时间
3. 配置响应字段：
   - 字段名称：响应 JSON 中的字段名
   - 字段类型：String、Integer、Boolean、Array 等
   - 数据生成规则：随机、UUID、姓名、邮箱等
   - 字段长度/值范围：设置数据的长度或值范围
4. 配置响应头（可选）
5. 点击「保存 API」按钮完成创建

### 测试模拟接口

创建完成后，可以通过以下方式测试接口：

1. 在 API 列表中点击「测试」按钮
2. 直接访问配置的 API 路径
3. 使用 Postman 或其他 API 测试工具访问

### 管理接口

在首页的 API 列表中，可以对已创建的接口进行以下操作：

- 编辑：修改接口配置
- 删除：删除不需要的接口
- 测试：直接访问接口查看响应

## 项目结构

```
src/main/java/com/zhangyh/mockeasy/
├── MockEasyApplication.java        # 应用入口
├── controller/                     # 控制器
│   └── ApiConfigController.java    # API配置控制器
├── handler/                        # 处理器
│   └── DynamicApiHandler.java      # 动态API处理器
├── model/                          # 数据模型
│   ├── ApiConfig.java              # API配置模型
│   └── FieldConfig.java            # 字段配置模型
└── service/                        # 服务层
    ├── ApiConfigService.java       # API配置服务接口
    ├── DataGeneratorService.java   # 数据生成服务接口
    └── impl/                       # 服务实现
        ├── ApiConfigServiceImpl.java    # API配置服务实现
        └── DataGeneratorServiceImpl.java # 数据生成服务实现
```

## 扩展功能

### 添加新的数据生成规则

1. 在 `DataGeneratorServiceImpl.java` 中添加新的生成方法
2. 在前端模板 `create.html` 和 `edit.html` 中的规则选择器中添加新选项

### 支持更多数据类型

1. 在 `FieldConfig.java` 中扩展字段类型
2. 在 `DataGeneratorServiceImpl.java` 中添加对应的数据生成逻辑
3. 更新前端模板中的类型选择器

## 贡献指南

欢迎贡献代码或提出建议！请遵循以下步骤：

1. Fork 项目
2. 创建特性分支 (`git checkout -b feature/amazing-feature`)
3. 提交更改 (`git commit -m 'Add some amazing feature'`)
4. 推送到分支 (`git push origin feature/amazing-feature`)
5. 创建 Pull Request

## 许可证

本项目采用 MIT 许可证 - 详情请参阅 [LICENSE](LICENSE) 文件