package csu.web.mypetstore;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * MyPetStore Spring Boot 启动类
 * 配置 MyBatis Plus Mapper 扫描路径
 */
@SpringBootApplication
@MapperScan("csu.web.mypetstore.persistence")
public class MyPetStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyPetStoreApplication.class, args);
    }
}