package com.fourMan.GlobalAssets;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan  // << naver.search 바인딩 활성화
public class GlobalAssetsApplication {
	public static void main(String[] args) {
		SpringApplication.run(GlobalAssetsApplication.class, args);
	}
}
