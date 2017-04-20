package com.edgelab.marketdata;

import com.edgelab.marketdata.inmem.Container;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.h2.server.web.WebServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.ByteArrayOutputStream;

@SpringBootApplication
@EnableFeignClients
@EnableCaching
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    ServletRegistrationBean h2servletRegistration() {
        ServletRegistrationBean registration = new ServletRegistrationBean(new WebServlet());
        registration.addUrlMappings("/console/*");
        return registration;
    }

    @Bean
    RedisTemplate redisTemplate(RedisTemplate redisTemplate, RedisSerializer<Container> redisSerializer) {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(redisSerializer);
        return redisTemplate;
    }

    @Bean
    RedisSerializer<Container> redisSerializer() {
        return new RedisSerializer<Container>() {

            @Override
            public byte[] serialize(Container container) throws SerializationException {
                Kryo kryo = new Kryo();
                ByteArrayOutputStream bStream = new ByteArrayOutputStream();
                Output output = new Output(bStream);
                kryo.writeObject(output, container);
                output.close();
                return bStream.toByteArray();
            }

            @Override
            public Container deserialize(byte[] bytes) throws SerializationException {
                Kryo kryo = new Kryo();
                return kryo.readObject(new Input(bytes), Container.class);
            }

        };
    }

}
