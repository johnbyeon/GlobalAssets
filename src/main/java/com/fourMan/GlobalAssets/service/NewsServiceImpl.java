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
        // 요청하신 10종목
        DISPLAY.put("coin-eth",      "이더리움");
        DISPLAY.put("coin-doge",     "도지");
        DISPLAY.put("coin-xrp",      "리플");
        DISPLAY.put("coin-btc",      "비트코인");
        DISPLAY.put("coin-sol",      "솔라나");
        DISPLAY.put("stock-005930",  "삼성전자");
        DISPLAY.put("stock-000660",  "SK하이닉스");
        DISPLAY.put("stock-373220",  "LG에너지솔루션");
        DISPLAY.put("stock-aapl",    "애플");
        DISPLAY.put("stock-nvda",    "엔비디아");

        KEY_TO_QUERY.put("coin-eth",     "(이더리움 OR Ethereum OR ETH) (가격 OR 시세 OR 전망 OR 뉴스)");
        KEY_TO_QUERY.put("coin-doge",    "(도지코인 OR Dogecoin OR DOGE) (가격 OR 시세 OR 뉴스)");
        KEY_TO_QUERY.put("coin-xrp",     "(리플 OR XRP) (코인 OR 암호화폐 OR 가격 OR 뉴스)");
        KEY_TO_QUERY.put("coin-btc",     "(비트코인 OR Bitcoin OR BTC) (가격 OR 시세 OR 뉴스)");
        KEY_TO_QUERY.put("coin-sol",     "(솔라나 OR Solana OR SOL) (가격 OR 시세 OR 뉴스)");
        KEY_TO_QUERY.put("stock-005930", "(삼성전자 OR 005930) (주가 OR 실적 OR 반도체 OR 뉴스)");
        KEY_TO_QUERY.put("stock-000660", "(SK하이닉스 OR 하이닉스 OR 000660) (주가 OR 실적 OR 뉴스)");
        KEY_TO_QUERY.put("stock-373220", "(LG에너지솔루션 OR LG에너지 OR 373220) (주가 OR 실적 OR 배터리 OR 뉴스)");
        KEY_TO_QUERY.put("stock-aapl",   "(애플 OR Apple OR AAPL) (주가 OR 아이폰 OR 실적 OR 뉴스)");
        KEY_TO_QUERY.put("stock-nvda",   "(엔비디아 OR Nvidia OR NVDA) (주가 OR AI OR GPU OR 뉴스)");
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
