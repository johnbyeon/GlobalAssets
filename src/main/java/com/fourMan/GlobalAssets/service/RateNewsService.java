package com.fourMan.GlobalAssets.service;

import com.fourMan.GlobalAssets.dto.RateNewsItem;
import com.fourMan.GlobalAssets.entity.AssetsEntity;
import com.fourMan.GlobalAssets.repository.RateAssetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RateNewsService {

    private final RateAssetRepository assets;
    private final com.fourMan.GlobalAssets.config.client.RateNaverSearchClient naver;

    /** 메인 경제 뉴스 */
    public List<RateNewsItem> searchEconomy(int limit) {
        return search("환율 OR 금리 OR 달러 OR 경제", limit);
    }

    /** 차트 옆(자산 기준) */
    public List<RateNewsItem> searchByAssetId(long assetId, int limit) {
        AssetsEntity a = assets.findById(assetId)
                .orElseThrow(() -> new IllegalArgumentException("Asset not found: " + assetId));
        String q = prefer(a.getSymbol(), a.getCode(), a.getName());
        return search(q, limit);
    }

    /** 관리자/차트에서 자유검색 */
    public List<RateNewsItem> searchForChart(String q, int limit) {
        return search(q, limit);
    }

    /** 실제 네이버 API 호출 */
    public List<RateNewsItem> search(String query, int limit) {
        com.fourMan.GlobalAssets.config.client.RateNaverSearchClient.NaverNewsResponse res = naver.searchNews(query, limit, 1, "date");
        List<RateNewsItem> out = new ArrayList<>();
        if (res != null && res.getItems() != null) {
            int no = 0;
            for (com.fourMan.GlobalAssets.config.client.RateNaverSearchClient.NaverItem it : res.getItems()) {
                RateNewsItem dto = new RateNewsItem();
                dto.setNo(++no);
                dto.setTitle(it.getTitle());
                dto.setLink(it.getLink() != null && !it.getLink().isBlank() ? it.getLink() : it.getLink());
                dto.setTime(it.getPubDate());
                out.add(dto);
            }
        }
        return out;
    }

    private static String prefer(String a, String b, String c) {
        if (a != null && !a.isBlank()) return a;
        if (b != null && !b.isBlank()) return b;
        return c;
    }
}
