package com.fourMan.GlobalAssets.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NaverApiProperties {

    @Value("${naver.search.news-url:https://openapi.naver.com/v1/search/news.json}")
    private String newsUrl;

    @Value("${naver.search.client-id:${naver.api.client-id:${naver.api.clientId:${NAVER_API_CLIENT_ID:}}}}")
    private String clientId;

    @Value("${naver.search.client-secret:${naver.api.client-secret:${naver.api.clientSecret:${NAVER_API_CLIENT_SECRET:}}}}")
    private String clientSecret;

    public String getNewsUrl() { return newsUrl; }
    public String getClientId() { return clientId; }
    public String getClientSecret() { return clientSecret; }

    public boolean hasKeys() {
        return clientId != null && !clientId.isBlank()
                && clientSecret != null && !clientSecret.isBlank();
    }
}
