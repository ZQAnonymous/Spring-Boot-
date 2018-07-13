package com.crmeeting;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ServletComponentScan
@MapperScan("com.crmeeting.mapper")
@EnableTransactionManagement
@ImportResource(locations = { "classpath:druid-bean.xml" })
public class CrApplication {
	public static void main(String[] args) {
		SpringApplication.run(CrApplication.class, args);
	}
}
