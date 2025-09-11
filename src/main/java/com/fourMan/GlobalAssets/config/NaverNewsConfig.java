package com.fourMan.GlobalAssets.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
@EnableConfigurationProperties
public class NaverNewsConfig {

    @Value("${naver.search.client-id}")
    private String clientId;

    @Value("${naver.search.client-secret}")
    private String clientSecret;

    @Bean(name = "naverRestTemplate")
    public RestTemplate naverRestTemplate() {
        RestTemplate rt = new RestTemplate();
        ClientHttpRequestInterceptor auth =
                (req, body, ex) -> {
                    req.getHeaders().add("X-Naver-Client-Id", clientId);
                    req.getHeaders().add("X-Naver-Client-Secret", clientSecret);
                    return ex.execute(req, body);
                };
        rt.setInterceptors(List.of(auth));
        return rt;
    }
}
