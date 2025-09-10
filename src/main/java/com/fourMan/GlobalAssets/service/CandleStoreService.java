package com.fourMan.GlobalAssets.service;

import com.fourMan.GlobalAssets.dto.CandleDto;
import com.fourMan.GlobalAssets.entity.CandleEntity;
import com.fourMan.GlobalAssets.repository.CandleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CandleStoreService {
    private final CandleRepository repo;

    private static String makeId(String market, String frame, long t) {
        return market + "|" + frame + "|" + t;
    }

    public CandleEntity toEntity(CandleDto d) {
        return CandleEntity.builder()
                .id(makeId(d.getMarket(), d.getFrame(), d.getT()))
                .market(d.getMarket())
                .frame(d.getFrame())
                .t(d.getT())
                .o(d.getO()).h(d.getH()).l(d.getL()).c(d.getC())
                .v(d.getV())
                .build();
    }

    public CandleDto toDto(CandleEntity e) {
        return CandleDto.builder()
                .market(e.getMarket())
                .frame(e.getFrame())
                .t(e.getT())
                .o(e.getO()).h(e.getH()).l(e.getL()).c(e.getC())
                .v(e.getV())
                .build();
    }

    public void upsert(CandleDto d) {
        repo.save(toEntity(d));
    }

    public void upsertAll(List<CandleDto> list) {
        repo.saveAll(list.stream().map(this::toEntity).toList());
    }

    public List<CandleDto> latest(String market, String frame, int limit) {
        var list = repo.findTop200ByMarketAndFrameOrderByTDesc(market, frame);
        // DB에서 최신→과거 옴 → UI/API는 과거→최신 정렬로 돌려주기
        list.sort((a,b) -> Long.compare(a.getT(), b.getT()));
        if (list.size() > limit) list = list.subList(list.size()-limit, list.size());
        return list.stream().map(this::toDto).toList();
    }

    public boolean hasAny(String market, String frame) {
        return repo.countByMarketAndFrame(market, frame) > 0;
    }
}
