package com.xuecheng.manage_cms;

import lombok.Data;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
* @Description:    测试RestTemplate
* @Author:         zhangzhiqiang
* @CreateDate:     2019/10/13
* @Version:        1.0
*/
@Data
@SpringBootTest
@RunWith(SpringRunner.class)
public class RestTemplateTest {

    @Autowired
    RestTemplate restTemplate;
    @Test
    public void testRestTemplate(){
        //通过
        ResponseEntity<Map> forEntity = restTemplate.getForEntity("http://localhost:31001/cms/config/getModel/5a791725dd573c3574ee666f", Map.class);
        System.out.println(forEntity);
    }
}
