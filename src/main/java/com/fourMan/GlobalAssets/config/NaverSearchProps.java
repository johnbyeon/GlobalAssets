package com.fourMan.GlobalAssets.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "naver.search")
public class NaverSearchProps {
    private String newsUrl;      // https://openapi.naver.com/v1/search/news.json
    private String clientId;     // 환경변수 NAVER_CLIENT_ID 로 주입
    private String clientSecret; // 환경변수 NAVER_CLIENT_SECRET 로 주입
}
