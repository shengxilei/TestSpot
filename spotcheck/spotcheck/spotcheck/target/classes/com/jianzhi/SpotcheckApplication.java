package com.jianzhi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import cn.hutool.cron.CronUtil;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

import java.util.ArrayList;

@EnableWebSocket
@SpringBootApplication
@MapperScan("com.jianzhi.mapper")
public class SpotcheckApplication extends SpringBootServletInitializer{
	//继承SpringBootServletInitializer类，并重写configure方法
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		CronUtil.setMatchSecond(true);
		CronUtil.start();
		return application.sources(SpotcheckApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(SpotcheckApplication.class, args);
		CronUtil.setMatchSecond(true);
		CronUtil.start();
	}
}
