package com.fourMan.GlobalAssets.service;

import com.fourMan.GlobalAssets.dto.DailySummaryDto;
import com.fourMan.GlobalAssets.entity.AssetsEntity;
import com.fourMan.GlobalAssets.entity.DailySummaryEntity;
import com.fourMan.GlobalAssets.repository.AssetsRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RateFxService {

    private final AssetsRepository assetsRepo;

    @PersistenceContext
    private EntityManager em;

    /**
     * 심볼/코드로 자산을 찾고, 해당 자산의 최근 N개 일간요약을 조회한다.
     * - 엔티티/레포 고정 원칙: 네이티브 쿼리 + 엔티티 매핑으로 조회 (레포 시그니처에 의존 X)
     * - 반환은 DTO로 변환 후 날짜 오름차순(차트용)
     */
    @Transactional(readOnly = true)
    public List<DailySummaryDto> recentDaily(String symbolOrCode, int limit) {
        AssetsEntity asset = assetsRepo.findBySymbol(symbolOrCode)
                .or(() -> assetsRepo.findByCode(symbolOrCode))
                .orElseThrow(() -> new IllegalArgumentException("Asset not found: " + symbolOrCode));

        // MySQL: LIMIT 은 setMaxResults 로 건다
        var query = em.createNativeQuery(
                "SELECT * FROM daily_summary WHERE asset_id = :assetId ORDER BY date DESC",
                DailySummaryEntity.class
        );
        query.setParameter("assetId", asset.getId());
        query.setMaxResults(limit);

        @SuppressWarnings("unchecked")
        List<DailySummaryEntity> rows = query.getResultList();

        return rows.stream()
                .map(DailySummaryDto::fromEntity)
                .sorted(Comparator.comparing(DailySummaryDto::getDate)) // DTO에 getDate()가 있어야 함
                .toList();
    }

    /**
     * 일간 요약 upsert (INSERT ... ON DUPLICATE KEY UPDATE)
     * - 엔티티 생성자/세터에 의존하지 않음
     * - prev_close, change, change_percent 를 SQL에서 계산
     * - 테이블에 (asset_id, date) UNIQUE 가 있다고 가정
     */
    @Transactional
    public void upsertDaily(long assetId, LocalDate date, BigDecimal close) {
        // 이전 종가를 여러 번 서브쿼리로 쓰되, JPA에서 파라미터 바인딩만 한다.
        String sql = """
            INSERT INTO daily_summary (asset_id, date, price, prev_close, `change`, change_percent)
            SELECT
              :assetId AS asset_id,
              :date     AS date,
              :price    AS price,
              (
                SELECT ds.price
                FROM daily_summary ds
                WHERE ds.asset_id = :assetId AND ds.date < :date
                ORDER BY ds.date DESC
                LIMIT 1
              ) AS prev_close,
              CASE
                WHEN (
                  SELECT ds.price FROM daily_summary ds
                  WHERE ds.asset_id = :assetId AND ds.date < :date
                  ORDER BY ds.date DESC LIMIT 1
                ) IS NULL
                  THEN NULL
                ELSE :price - (
                  SELECT ds.price FROM daily_summary ds
                  WHERE ds.asset_id = :assetId AND ds.date < :date
                  ORDER BY ds.date DESC LIMIT 1
                )
              END AS `change`,
              CASE
                WHEN (
                  SELECT ds.price FROM daily_summary ds
                  WHERE ds.asset_id = :assetId AND ds.date < :date
                  ORDER BY ds.date DESC LIMIT 1
                ) IS NULL
                 OR (
                  SELECT ds.price FROM daily_summary ds
                  WHERE ds.asset_id = :assetId AND ds.date < :date
                  ORDER BY ds.date DESC LIMIT 1
                ) = 0
                  THEN NULL
                ELSE (
                  (
                    :price - (
                      SELECT ds.price FROM daily_summary ds
                      WHERE ds.asset_id = :assetId AND ds.date < :date
                      ORDER BY ds.date DESC LIMIT 1
                    )
                  ) / (
                    SELECT ds.price FROM daily_summary ds
                    WHERE ds.asset_id = :assetId AND ds.date < :date
                    ORDER BY ds.date DESC LIMIT 1
                  ) * 100
                )
              END AS change_percent
            ON DUPLICATE KEY UPDATE
              price          = VALUES(price),
              prev_close     = VALUES(prev_close),
              `change`       = VALUES(`change`),
              change_percent = VALUES(change_percent)
            """;

        var q = em.createNativeQuery(sql);
        q.setParameter("assetId", assetId);
        q.setParameter("date", date);
        q.setParameter("price", close);
        q.executeUpdate();
    }

    /**
     * 컨트롤러에서 전체 갱신을 호출할 때 사용할 수 있는 기본 구현.
     * 실제 외부 API 연동은 여기서 채우면 된다.
     */
    @Transactional
    public void refreshAllFromApi() {
        // 필요한 경우 외부 API 호출 로직을 여기에 구현.
        // 컨트롤러가 시그니처만 기대하는 경우 NOP여도 구동/컴파일에는 문제 없음.
    }
}
