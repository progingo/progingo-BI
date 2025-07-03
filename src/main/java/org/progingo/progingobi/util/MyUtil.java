package org.progingo.progingobi.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Component
public class MyUtil {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    private static final long BEGIN_TIMESTAMP = 1097942400L;
    private static final int COUNT_BITS = 32;//序列号位数

    public String nextId(String keyPrefix){
        //获取时间戳
        long nowSecond = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        long timestamp = nowSecond - BEGIN_TIMESTAMP;

        //获取当前的天
        String day = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));
        //生成序列号
        long count = stringRedisTemplate.opsForValue().increment(RedisPrefix.NEXT_ID + keyPrefix + ":" + day);

        //拼接并返回
        return String.valueOf(timestamp << COUNT_BITS | count);
    }

}


