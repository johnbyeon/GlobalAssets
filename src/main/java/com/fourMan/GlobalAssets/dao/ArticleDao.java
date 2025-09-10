package com.fourMan.GlobalAssets.dao;

import com.fourMan.GlobalAssets.dto.ArticleDto;
import com.fourMan.GlobalAssets.entity.ArticleEntity;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Transactional
public class ArticleDao {
    @Autowired
    EntityManager em;

    public List<ArticleEntity> findAllArticle() {
        String sql = "SELECT a FROM Article a " +
                "ORDER BY a.id DESC";
        List<ArticleEntity> articles = em.createQuery(sql).getResultList();
        return articles;
    }

    public ArticleEntity getOneArticle(Long id) {
        return em.find(ArticleEntity.class,id);
    }

    public void deleteArticle(Long id) {

        ArticleEntity article = em.find(ArticleEntity.class,id);
        em.remove(article);
    }

    public void insertArticle(ArticleEntity article) {
        em.persist(article);
        em.flush();
    }

    public void updateArticle(ArticleDto dto) {
        ArticleEntity article = em.find(ArticleEntity.class,dto.getArticleId());
        article.setUpdateTime(Timestamp.valueOf(LocalDateTime.now()));
        article.setUserId(dto.getUserId());
        article.setTitle(dto.getTitle());
        article.setBody(dto.getBody());
        article.setArticleStatus(dto.getArticleStatus());
    }
}
