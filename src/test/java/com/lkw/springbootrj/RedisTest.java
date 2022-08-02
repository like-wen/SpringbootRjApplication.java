package com.lkw.springbootrj;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lkw.springbootrj.entity.Dish;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

public class RedisTest {



    @Test
    void redisGet(){
        StringRedisTemplate stringRedisTemplate=new StringRedisTemplate();

        System.out.println(stringRedisTemplate.opsForValue().get("13699696369"));

    }

    @Test
    void redisSet(){
        StringRedisTemplate stringRedisTemplate=new StringRedisTemplate();

        stringRedisTemplate.opsForValue().set("key","value");
    }

    @Test
    void showJson() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Dish dish=new Dish();

        String s = objectMapper.writeValueAsString(dish);
        System.out.println(s);

    }





}
