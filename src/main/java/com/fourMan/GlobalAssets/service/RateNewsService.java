package com.fourMan.GlobalAssets.service;

import com.fourMan.GlobalAssets.config.RateNaverSearchClient;
import com.fourMan.GlobalAssets.dto.AssetsDto;
import com.fourMan.GlobalAssets.entity.AssetsEntity;
import com.fourMan.GlobalAssets.repository.RateAssetsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RateNewsService {

    private final RateNaverSearchClient naverClient;
    private final RateAssetsRepository rateAssetRepository;

    /** 핵심: assetId 기준으로 자산 + 뉴스 묶어서 반환 */
    public AssetsDto assetWithNews(Long assetId, int limit) {
        AssetsEntity asset = rateAssetsRepository.findById(assetId)
                .orElseThrow(() -> new IllegalArgumentException("asset not found: " + assetId));

        AssetsDto dto = AssetsDto.fromEntity(asset);
        String keyword = pickKeyword(asset);

        RateNaverSearchClient.Response res = naverClient.search(keyword, limit);
        List<RateNewsItem> items = convert(res, keyword, limit);
        sortAndRenumber(items);

        dto.setNews(items);
        return dto;
    }

    /** (기존 유지) 심볼/코드 기반 검색 → 내부에서 assetId로 위 메서드 재사용 */
    public AssetsDto assetWithNewsBySymbolOrCode(String codeOrSymbol, int limit) {
        AssetsEntity asset = rateAssetRepository.findBySymbol(codeOrSymbol)
                .orElseGet(() -> rateAssetRepository.findByCode(codeOrSymbol)
                        .orElseThrow(() -> new IllegalArgumentException(
                                "asset not found by symbol/code: " + codeOrSymbol)));
        return assetWithNews(asset.getId(), limit);
    }

    /* ---------- 내부 유틸 ---------- */

    private String pickKeyword(AssetsEntity a) {
        if (a.getSymbol() != null && !a.getSymbol().isBlank()) return a.getSymbol();
        if (a.getName()   != null && !a.getName().isBlank())   return a.getName();
        return a.getCode();
    }

    private List<RateNewsItem> convert(RateNaverSearchClient.Response res,
                                       String keyword, int limit) {
        AtomicInteger seq = new AtomicInteger(1);
        return res.getItems().stream()
                .limit(limit)
                .map(item -> RateNewsItem.builder()
                        .no(seq.getAndIncrement())
                        .title(clean(item.getTitle()))
                        .link(item.getLink())
                        .time(item.getPubDate())
                        .keyword(keyword)
                        .build())
                .collect(Collectors.toList());
    }

    private void sortAndRenumber(List<RateNewsItem> items) {
        // pubDate 문자열 기준 내림차순, 형식이 RFC1123이면 문자열 비교로도 대체 가능
        items.sort(Comparator.comparing(RateNewsItem::getTime).reversed());
        for (int i = 0; i < items.size(); i++) {
            items.get(i).setNo(i + 1);
        }
    }

    private String clean(String s) {
        if (s == null) return null;
        return s.replaceAll("<[^>]*>", "")
                .replace("&quot;", "\"")
                .replace("&amp;", "&");
    }
}
