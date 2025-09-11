package com.fourMan.GlobalAssets.dao;


import com.fourMan.GlobalAssets.dto.AssetsDto;
import com.fourMan.GlobalAssets.entity.AssetsEntity;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Transactional
public class AssetsDao {
    @Autowired
    EntityManager em;

    public List<AssetsEntity> findAllAssetsEntity() {
        String sql = "SELECT a FROM AssetsEntity a " +
                "ORDER BY a.id DESC";
        List<AssetsEntity> assets = em.createQuery(sql).getResultList();
        return assets;
    }

    public List<AssetsEntity> findAllByCategory(String category) {
        String sql = "SELECT a FROM AssetsEntity a " +
                "WHERE a.category = :category " +
                "ORDER BY a.id DESC";

        List<AssetsEntity> assets = em.createQuery(sql, AssetsEntity.class)
                .setParameter("category", category)
                .getResultList();

        return assets;
    }
    public List<AssetsEntity> findAllByCode(String code) {
        String sql = "SELECT a FROM AssetsEntity a " +
                "WHERE a.code = :code " +
                "ORDER BY a.id DESC";

        List<AssetsEntity> assets = em.createQuery(sql, AssetsEntity.class)
                .setParameter("code", code)
                .getResultList();

        return assets;
    }
    public AssetsEntity getOneAssetsEntity(Long id) {
        return em.find(AssetsEntity.class,id);
    }

    public void deleteAssetsEntity(Long id) {

        AssetsEntity assets = em.find(AssetsEntity.class,id);
        em.remove(assets);
    }

    public void insertAssetsEntity(AssetsEntity assets) {
        em.persist(assets);
        em.flush();
    }

    public void updateAssetsEntity(AssetsDto dto) {
        AssetsEntity assets = em.find(AssetsEntity.class,dto.getId());
        assets.setSymbol(dto.getSymbol());
        assets.setName(dto.getName());
        assets.setCategory(dto.getCategory());
        assets.setCode(dto.getCode());
    }

}
