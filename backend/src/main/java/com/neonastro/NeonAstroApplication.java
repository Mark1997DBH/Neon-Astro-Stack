package com.neonastro;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Neon Astro Stack 霓虹占星栈后端服务启动类
 *
 * @author neonastro
 */
@SpringBootApplication
@MapperScan("com.neonastro.modules.**.mapper")
@EnableScheduling
public class NeonAstroApplication {

    public static void main(String[] args) {
        SpringApplication.run(NeonAstroApplication.class, args);
    }
}
