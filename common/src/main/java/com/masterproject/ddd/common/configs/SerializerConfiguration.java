package com.masterproject.ddd.common.configs;

import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.json.JacksonSerializer;
import org.axonframework.serialization.xml.XStreamSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.thoughtworks.xstream.XStream;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class SerializerConfiguration {

    @Bean
    @Primary
    public Serializer defaultSerializer() {
        // Set the secure types on the xStream instance
        XStream xStream = new XStream();
        xStream.allowTypesByWildcard(new String[] {
                "java.util.**",
                "com.masterproject.ddd.**"
        });
        return XStreamSerializer.builder()
                .xStream(xStream)
                .build();
    }

}
