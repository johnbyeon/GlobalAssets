package com.fourMan.GlobalAssets.service;

import com.fourMan.GlobalAssets.client.NaverNewsClient;
import com.fourMan.GlobalAssets.dto.NewsItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NaverNewsClient client;

    // 메인에 뱃지로 노출할 순서 및 한글명(고정 순서)
    private static final LinkedHashMap<String, String> DISPLAY = new LinkedHashMap<>();
    // 키 → 네이버 검색 쿼리
    private static final Map<String, String> KEY_TO_QUERY = new HashMap<>();

    static {
        // 주식
        DISPLAY.put("samsung", "삼성전자");
        DISPLAY.put("skhynix", "SK하이닉스");
        DISPLAY.put("lgenergy", "LG에너지솔루션");
        DISPLAY.put("samsungbio", "삼성바이오로직스");
        DISPLAY.put("hanwhaaero", "한화에어로스페이스");

        // 코인
        DISPLAY.put("btc", "비트코인");
        DISPLAY.put("sol", "솔라나");
        DISPLAY.put("doge", "도지코인");
        DISPLAY.put("xrp", "엑스알피(리플)");
        DISPLAY.put("etc", "이더리움클래식");

        // 검색 쿼리
        KEY_TO_QUERY.put("samsung", "삼성전자 주가");
        KEY_TO_QUERY.put("skhynix", "하이닉스 주가");
        KEY_TO_QUERY.put("lgenergy", "LG에너지솔루션 주가");
        KEY_TO_QUERY.put("samsungbio", "삼성바이오로직스 주가");
        KEY_TO_QUERY.put("hanwhaaero", "한화에어로스페이스 방산");

        KEY_TO_QUERY.put("btc", "비트코인 시세");
        KEY_TO_QUERY.put("sol", "솔라나 코인");
        KEY_TO_QUERY.put("doge", "도지코인 시세");
        KEY_TO_QUERY.put("xrp", "리플 코인");
        KEY_TO_QUERY.put("etc", "이더리움클래식 코인");
    }

    @Override
    public List<NewsItemDto> searchEconomy(int limit) {
        String query = "(경제 OR 증시 OR 금리 OR 환율 OR 물가 OR 코스피 OR 코스닥)";
        return client.search(query, limit);
    }

    @Override
    public List<String> keys() {
        return new ArrayList<>(DISPLAY.keySet());
    }

    @Override
    public Map<String, String> displayNamesMap() {
        return Collections.unmodifiableMap(DISPLAY);
    }

    @Override
    public String displayNameFor(String key) {
        return DISPLAY.getOrDefault(key, key);
    }

    @Override
    public List<NewsItemDto> searchByKey(String key, int limit) {
        String q = KEY_TO_QUERY.getOrDefault(key, "(경제 OR 증시)");
        return client.search(q, limit);
    }
}
