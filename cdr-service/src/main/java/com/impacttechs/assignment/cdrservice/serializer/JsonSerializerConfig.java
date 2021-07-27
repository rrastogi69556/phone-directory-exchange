package com.impacttechs.assignment.cdrservice.serializer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;

@Service
public class JsonSerializerConfig implements ApplicationContextAware {
    private static ApplicationContext applicationContext;
    @Value("${rest.dateTimePattern:yyyy-MM-dd'T'HH:mm:ss.SSS'Z'}")
    private String jsonDateTimeFormatPattern;

    public JsonSerializerConfig() {
    }

    public SimpleDateFormat jsonSerialiserDateTimeFormat() {
        return new SimpleDateFormat(this.jsonDateTimeFormatPattern);
    }

    public static JsonSerializerConfig getInstance() {
        return (JsonSerializerConfig)applicationContext.getBean("jsonSerializerConfig");
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        JsonSerializerConfig.applicationContext = applicationContext;
    }

    public static void setApplicationContextStatic(ApplicationContext applicationContext) {
        JsonSerializerConfig.applicationContext = applicationContext;
    }

    public String getJsonDateTimeFormatPattern() {
        return this.jsonDateTimeFormatPattern;
    }

    public void setJsonDateTimeFormatPattern(String jsonDateTimeFormatPattern) {
        this.jsonDateTimeFormatPattern = jsonDateTimeFormatPattern;
    }
}
