package com.xuecheng.test.freemarker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @Description: 类作用描述
 * @Author: zhangzhiqiang
 * @CreateDate: 2019/10/13
 * @Version: 1.0
 */
@SpringBootApplication
public class FreemarkerTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(FreemarkerTestApplication.class, args);

    }

    /**
     * 配置RestTemplate ：是Spring提供的用于访问Rest服务的客户端；
     * RestTemplate提供了多种便捷访问远程Http服务的方法,能够大大提高客户端的编写效率。
     */
   @Bean
    public RestTemplate restTemplate(){
       return new RestTemplate(new OkHttp3ClientHttpRequestFactory());
   }
}
