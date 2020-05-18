package com.sharing;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Auther: xiang
 * @Date: 2020/5/6 18:23
 * @Description:
 */
@SpringBootApplication
@MapperScan("com.sharing.dao")
public class Application {
    private static Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        logger.info("--------web-application-started--------");
    }
}