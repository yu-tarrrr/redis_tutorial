package com.example.redis_tutorial;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/SampleRedis")
public class RedisSampleController {



    @Autowired
    private StringRedisTemplate redisTemplate;

    @PostMapping
    public void post(@RequestBody RedisSampleData redisSampleData) {
        redisTemplate.opsForValue()
                .set("hoge-string:string", redisSampleData.getString());
        redisTemplate.delete("hoge-string:list");
        redisTemplate.opsForList()
                .rightPushAll("hoge-string:list",
                        redisSampleData.getList().toArray(new String[0]));
        redisTemplate.delete("hoge-string:map");
        redisTemplate.opsForHash()
                .putAll("hoge-string:map", redisSampleData.getMap());
    }

    @GetMapping
    public RedisSampleData get() {
        RedisSampleData redisSampleData = new RedisSampleData();
        redisSampleData.setString(
                redisTemplate.opsForValue()
                        .get("hoge-string:string")
        );
        redisSampleData.setList(
                redisTemplate.opsForList()
                        .range("hoge-string:list", 0, -1)
        );
        redisSampleData.setMap(
                redisTemplate.<String, String>opsForHash()
                        .entries("hoge-string:map")
        );
        return redisSampleData;
    }
}
