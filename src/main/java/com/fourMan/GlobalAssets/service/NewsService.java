package com.fourMan.GlobalAssets.service;

import com.fourMan.GlobalAssets.dto.NewsItemDto;

import java.util.List;
import java.util.Map;

public interface NewsService {
    // 메인(경제 뉴스)
    List<NewsItemDto> searchEconomy(int limit);

    // 종목 키 목록/표시명
    List<String> keys();
    Map<String, String> displayNamesMap();
    String displayNameFor(String key);

    // 종목별 뉴스
    List<NewsItemDto> searchByKey(String key, int limit);
}
