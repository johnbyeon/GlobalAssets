package com.fourMan.GlobalAssets.dao;

import com.fourMan.GlobalAssets.dto.AssetsDto;
import com.fourMan.GlobalAssets.dto.PricesDto;
import com.fourMan.GlobalAssets.entity.AssetsEntity;
import com.fourMan.GlobalAssets.entity.PricesEntity;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Transactional
public class PricesDao {
    @Autowired
    EntityManager em;
    public List<PricesEntity> findAllByAssetId(Long assetId) {
        String sql = "SELECT a FROM PriceEntity a " +
                "WHERE a.assetId = :assetId " +
                "ORDER BY a.priceId DESC";

        List<PricesEntity> prices = em.createQuery(sql, PricesEntity.class)
                .setParameter("assetId", assetId)
                .getResultList();

        return prices;
    }

    public PricesEntity getOneAssetsEntity(Long id) {
        return em.find(PricesEntity.class,id);
    }

    public void deletePricesEntity(Long id) {

        PricesEntity prices = em.find(PricesEntity.class,id);
        em.remove(prices);
    }

    public void insertPricesEntity(PricesEntity prices) {
        em.persist(prices);
        em.flush();
    }

    public void updatePricesEntity(PricesDto dto) {
        PricesEntity prices = em.find(PricesEntity.class,dto.getPriceId());
        prices.setAssetId(dto.getAssetId());
        prices.setTimestamp(dto.getTimestamp());
        prices.setHigh(dto.getHigh());
        prices.setLow(dto.getLow());
        prices.setOpen(dto.getOpen());
        prices.setClose(dto.getClose());
    }
}
