package com.fourMan.GlobalAssets.config;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@Configuration  // 이 클래스가 설정 클래스라고 스프링에게 알려줌
@PropertySource("classpath:config/api-keys.properties")  // /resources/config/api-keys.properties 파일을 불러옴
public class ApiConfig {

    @Value("${grantType}")
    private String grantType;

    @Value("${appKey}")  // 파일에서 appKey 값을 읽어옴
    private String appKey;

    @Value("${secretKey}")  // 파일에서 secretKey 값을 읽어옴
    private String secretKey;

    @Value("${approvalKey}")// 연결 세션 관리
    private String approvalKey; // approval_key

}