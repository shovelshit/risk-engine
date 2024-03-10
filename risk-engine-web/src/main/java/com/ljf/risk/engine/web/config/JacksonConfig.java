package com.ljf.risk.engine.web.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ljf.risk.engine.common.utils.CustomDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * @author lijinfeng
 */
@Configuration
public class JacksonConfig {

//    @Autowired
//    private ObjectMapper mapper;

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverterConfiguration() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //全局修改json时间格式
        CustomDateFormat customDateFormat = new CustomDateFormat(mapper.getDateFormat());
        customDateFormat.setCalendar(Calendar.getInstance());
        customDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        mapper.setDateFormat(customDateFormat);
        converter.setObjectMapper(mapper);
        return converter;
    }

    /**
     * 修复前端精度丢失问题 Long -> String
     * @return
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> {
            // Long 会自定转换成 String
            builder.serializerByType(Long.class, ToStringSerializer.instance);
        };
    }

}
