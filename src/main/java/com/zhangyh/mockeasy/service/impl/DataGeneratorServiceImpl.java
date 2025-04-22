package com.zhangyh.mockeasy.service.impl;

import com.zhangyh.mockeasy.model.ApiConfig;
import com.zhangyh.mockeasy.model.FieldConfig;
import com.zhangyh.mockeasy.service.DataGeneratorService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 数据生成服务实现类，根据配置生成模拟数据
 */
@Service
public class DataGeneratorServiceImpl implements DataGeneratorService {

    @Override
    public Object generateData(ApiConfig apiConfig) {
        // 根据响应数据条数决定返回对象还是数组
        if (apiConfig.getResponseCount() == null || apiConfig.getResponseCount() <= 1) {
            // 生成单个对象
            return generateObject(apiConfig.getResponseFields());
        } else {
            // 生成对象数组
            List<Object> dataList = new ArrayList<>();
            for (int i = 0; i < apiConfig.getResponseCount(); i++) {
                dataList.add(generateObject(apiConfig.getResponseFields()));
            }
            return dataList;
        }
    }

    @Override
    public Object generateFieldData(FieldConfig fieldConfig) {
        // 根据字段类型生成数据
        switch (fieldConfig.getType().toLowerCase()) {
            case "string":
                return generateStringData(fieldConfig);
            case "integer":
            case "int":
                return generateIntegerData(fieldConfig);
            case "double":
            case "float":
                return generateDoubleData(fieldConfig);
            case "boolean":
            case "bool":
                return generateBooleanData(fieldConfig);
            case "date":
                return generateDateData(fieldConfig);
            case "datetime":
                return generateDateTimeData(fieldConfig);
            case "time":
                return generateTimeData(fieldConfig);
            case "array":
                return generateArrayData(fieldConfig);
            case "object":
                return generateObjectData(fieldConfig);
            case "ip":
                return generateIpData(fieldConfig);
            case "url":
                return generateUrlData(fieldConfig);
            case "id_card":
                return generateIdCardData(fieldConfig);
            case "color":
                return generateColorData(fieldConfig);
            default:
                return null;
        }
    }

    /**
     * 生成对象数据
     */
    private Map<String, Object> generateObject(List<FieldConfig> fieldConfigs) {
        Map<String, Object> result = new LinkedHashMap<>();
        if (fieldConfigs != null) {
            for (FieldConfig fieldConfig : fieldConfigs) {
                // 判断是否需要生成该字段
                if (fieldConfig.getRequired() == null || fieldConfig.getRequired()) {
                    result.put(fieldConfig.getName(), generateFieldData(fieldConfig));
                } else if (ThreadLocalRandom.current().nextBoolean()) {
                    // 非必需字段随机生成
                    result.put(fieldConfig.getName(), generateFieldData(fieldConfig));
                }
            }
        }
        return result;
    }

    /**
     * 生成字符串数据
     */
    private String generateStringData(FieldConfig fieldConfig) {
        // 如果有固定值，直接返回
        if (fieldConfig.getValue() != null && !fieldConfig.getValue().isEmpty()) {
            return fieldConfig.getValue();
        }

        // 根据规则生成数据
        String rule = fieldConfig.getRule();
        if (rule != null) {
            switch (rule.toLowerCase()) {
                case "uuid":
                    return UUID.randomUUID().toString();
                case "name":
                    return generateRandomName();
                case "email":
                    return generateRandomEmail();
                case "phone":
                    return generateRandomPhone();
                case "address":
                    return generateRandomAddress();
                case "date":
                    return LocalDate.now().format(DateTimeFormatter.ISO_DATE);
                case "datetime":
                    return LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
                default:
                    // 默认生成随机字符串
                    break;
            }
        }

        // 生成随机字符串
        int minLength = fieldConfig.getMinLength() != null ? fieldConfig.getMinLength() : 5;
        int maxLength = fieldConfig.getMaxLength() != null ? fieldConfig.getMaxLength() : 10;
        int length = ThreadLocalRandom.current().nextInt(minLength, maxLength + 1);

        return generateRandomString(length);
    }

    /**
     * 生成整数数据
     */
    private Integer generateIntegerData(FieldConfig fieldConfig) {
        // 如果有固定值，直接返回
        if (fieldConfig.getValue() != null && !fieldConfig.getValue().isEmpty()) {
            try {
                return Integer.parseInt(fieldConfig.getValue());
            } catch (NumberFormatException e) {
                // 忽略解析错误
            }
        }

        // 根据规则生成数据
        int min = fieldConfig.getMinLength() != null ? fieldConfig.getMinLength() : 0;
        int max = fieldConfig.getMaxLength() != null ? fieldConfig.getMaxLength() : 100;

        String rule = fieldConfig.getRule();
        if (rule != null && rule.equalsIgnoreCase("increment")) {
            // 递增规则，使用当前时间戳作为基础值
            return (int) (System.currentTimeMillis() % 10000) + min;
        }

        // 生成随机整数
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    /**
     * 生成浮点数数据
     */
    private Double generateDoubleData(FieldConfig fieldConfig) {
        // 如果有固定值，直接返回
        if (fieldConfig.getValue() != null && !fieldConfig.getValue().isEmpty()) {
            try {
                return Double.parseDouble(fieldConfig.getValue());
            } catch (NumberFormatException e) {
                // 忽略解析错误
            }
        }

        // 生成随机浮点数
        double min = fieldConfig.getMinLength() != null ? fieldConfig.getMinLength() : 0;
        double max = fieldConfig.getMaxLength() != null ? fieldConfig.getMaxLength() : 100;

        return min + (max - min) * ThreadLocalRandom.current().nextDouble();
    }

    /**
     * 生成布尔数据
     */
    private Boolean generateBooleanData(FieldConfig fieldConfig) {
        // 如果有固定值，直接返回
        if (fieldConfig.getValue() != null && !fieldConfig.getValue().isEmpty()) {
            return Boolean.parseBoolean(fieldConfig.getValue());
        }

        // 生成随机布尔值
        return ThreadLocalRandom.current().nextBoolean();
    }

    /**
     * 生成日期数据
     */
    private String generateDateData(FieldConfig fieldConfig) {
        // 如果有固定值，直接返回
        if (fieldConfig.getValue() != null && !fieldConfig.getValue().isEmpty()) {
            return fieldConfig.getValue();
        }

        // 生成随机日期
        LocalDate now = LocalDate.now();
        int daysToAdd = ThreadLocalRandom.current().nextInt(-365, 365);
        LocalDate randomDate = now.plusDays(daysToAdd);

        return randomDate.format(DateTimeFormatter.ISO_DATE);
    }

    /**
     * 生成日期时间数据
     */
    private String generateDateTimeData(FieldConfig fieldConfig) {
        // 如果有固定值，直接返回
        if (fieldConfig.getValue() != null && !fieldConfig.getValue().isEmpty()) {
            return fieldConfig.getValue();
        }

        // 生成随机日期时间
        LocalDateTime now = LocalDateTime.now();
        int daysToAdd = ThreadLocalRandom.current().nextInt(-365, 365);
        int hoursToAdd = ThreadLocalRandom.current().nextInt(-24, 24);
        int minutesToAdd = ThreadLocalRandom.current().nextInt(-60, 60);
        LocalDateTime randomDateTime = now.plusDays(daysToAdd).plusHours(hoursToAdd).plusMinutes(minutesToAdd);

        return randomDateTime.format(DateTimeFormatter.ISO_DATE_TIME);
    }

    /**
     * 生成数组数据
     */
    private List<Object> generateArrayData(FieldConfig fieldConfig) {
        List<Object> result = new ArrayList<>();
        int count = ThreadLocalRandom.current().nextInt(1, 5); // 默认生成1-5个元素

        // 如果有子字段配置，使用第一个子字段配置生成数组元素
        if (fieldConfig.getChildren() != null && !fieldConfig.getChildren().isEmpty()) {
            FieldConfig childConfig = fieldConfig.getChildren().get(0);
            for (int i = 0; i < count; i++) {
                result.add(generateFieldData(childConfig));
            }
        }

        return result;
    }

    /**
     * 生成对象数据
     */
    private Map<String, Object> generateObjectData(FieldConfig fieldConfig) {
        // 如果有子字段配置，使用子字段配置生成对象
        if (fieldConfig.getChildren() != null && !fieldConfig.getChildren().isEmpty()) {
            return generateObject(fieldConfig.getChildren());
        }

        // 没有子字段配置，返回空对象
        return new LinkedHashMap<>();
    }

    /**
     * 生成随机字符串
     */
    private String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }

        return sb.toString();
    }

    /**
     * 生成随机姓名
     */
    private String generateRandomName() {
        String[] firstNames = {"张", "李", "王", "赵", "刘", "陈", "杨", "黄", "周", "吴"};
        String[] lastNames = {"伟", "芳", "娜", "秀英", "敏", "静", "强", "磊", "洋", "艳"};

        Random random = new Random();
        return firstNames[random.nextInt(firstNames.length)] + lastNames[random.nextInt(lastNames.length)];
    }

    /**
     * 生成随机邮箱
     */
    private String generateRandomEmail() {
        String[] domains = {"gmail.com", "yahoo.com", "hotmail.com", "outlook.com", "163.com", "qq.com"};
        Random random = new Random();

        return generateRandomString(5) + "@" + domains[random.nextInt(domains.length)];
    }

    /**
     * 生成随机电话号码
     */
    private String generateRandomPhone() {
        String[] prefixes = {"138", "139", "150", "151", "152", "157", "158", "159", "186", "188"};
        Random random = new Random();

        StringBuilder sb = new StringBuilder();
        sb.append(prefixes[random.nextInt(prefixes.length)]);

        for (int i = 0; i < 8; i++) {
            sb.append(random.nextInt(10));
        }

        return sb.toString();
    }

    /**
     * 生成随机地址
     */
    private String generateRandomAddress() {
        String[] provinces = {"北京市", "上海市", "广东省", "江苏省", "浙江省", "四川省", "湖北省"};
        String[] cities = {"北京市", "上海市", "广州市", "深圳市", "南京市", "杭州市", "成都市", "武汉市"};
        String[] districts = {"东城区", "西城区", "朝阳区", "海淀区", "江干区", "下城区", "武侯区", "锦江区"};
        String[] streets = {"中关村大街", "长安街", "人民路", "解放路", "建国路", "复兴路"};

        Random random = new Random();
        return provinces[random.nextInt(provinces.length)] + 
               cities[random.nextInt(cities.length)] + 
               districts[random.nextInt(districts.length)] + 
               streets[random.nextInt(streets.length)] + 
               random.nextInt(100) + "号";
    }
    
    /**
     * 生成时间数据
     */
    private String generateTimeData(FieldConfig fieldConfig) {
        // 如果有固定值，直接返回
        if (fieldConfig.getValue() != null && !fieldConfig.getValue().isEmpty()) {
            return fieldConfig.getValue();
        }
        
        // 生成随机时间
        LocalTime now = LocalTime.now();
        int hoursToAdd = ThreadLocalRandom.current().nextInt(-12, 12);
        int minutesToAdd = ThreadLocalRandom.current().nextInt(-59, 59);
        int secondsToAdd = ThreadLocalRandom.current().nextInt(-59, 59);
        LocalTime randomTime = now.plusHours(hoursToAdd).plusMinutes(minutesToAdd).plusSeconds(secondsToAdd);
        
        return randomTime.format(DateTimeFormatter.ISO_LOCAL_TIME);
    }
    
    /**
     * 生成IP地址数据
     */
    private String generateIpData(FieldConfig fieldConfig) {
        // 如果有固定值，直接返回
        if (fieldConfig.getValue() != null && !fieldConfig.getValue().isEmpty()) {
            return fieldConfig.getValue();
        }
        
        // 生成随机IP地址
        Random random = new Random();
        return random.nextInt(256) + "." + 
               random.nextInt(256) + "." + 
               random.nextInt(256) + "." + 
               random.nextInt(256);
    }
    
    /**
     * 生成URL数据
     */
    private String generateUrlData(FieldConfig fieldConfig) {
        // 如果有固定值，直接返回
        if (fieldConfig.getValue() != null && !fieldConfig.getValue().isEmpty()) {
            return fieldConfig.getValue();
        }
        
        // 生成随机URL
        String[] protocols = {"http", "https"};
        String[] domains = {"example.com", "test.org", "mockdata.cn", "demo.net", "api.dev"};
        String[] paths = {"", "api", "users", "products", "services", "data"};
        
        Random random = new Random();
        StringBuilder url = new StringBuilder();
        
        // 协议
        url.append(protocols[random.nextInt(protocols.length)]).append("://");
        
        // 域名
        url.append(domains[random.nextInt(domains.length)]);
        
        // 路径
        if (random.nextBoolean()) {
            url.append("/").append(paths[random.nextInt(paths.length)]);
            // 可能的子路径
            if (random.nextBoolean()) {
                url.append("/").append(random.nextInt(100));
            }
        }
        
        return url.toString();
    }
    
    /**
     * 生成身份证号数据
     */
    private String generateIdCardData(FieldConfig fieldConfig) {
        // 如果有固定值，直接返回
        if (fieldConfig.getValue() != null && !fieldConfig.getValue().isEmpty()) {
            return fieldConfig.getValue();
        }
        
        // 生成随机身份证号
        String[] areaCodes = {"110101", "310101", "440101", "440300", "330100", "320100", "510100"};
        
        Random random = new Random();
        StringBuilder idCard = new StringBuilder();
        
        // 区域码
        idCard.append(areaCodes[random.nextInt(areaCodes.length)]);
        
        // 出生日期码 (1970-2000年)
        int year = 1970 + random.nextInt(30);
        int month = 1 + random.nextInt(12);
        int day = 1 + random.nextInt(28); // 简化处理，避免月份天数问题
        
        idCard.append(String.format("%04d%02d%02d", year, month, day));
        
        // 顺序码 (3位数)
        idCard.append(String.format("%03d", random.nextInt(1000)));
        
        // 校验码 (简化处理，随机生成)
        char[] checkCodes = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'X'};
        idCard.append(checkCodes[random.nextInt(checkCodes.length)]);
        
        return idCard.toString();
    }
    
    /**
     * 生成颜色数据
     */
    private String generateColorData(FieldConfig fieldConfig) {
        // 如果有固定值，直接返回
        if (fieldConfig.getValue() != null && !fieldConfig.getValue().isEmpty()) {
            return fieldConfig.getValue();
        }
        
        // 生成随机颜色（十六进制格式）
        Random random = new Random();
        return String.format("#%02x%02x%02x", 
                random.nextInt(256), 
                random.nextInt(256), 
                random.nextInt(256));
    }
}