package com.fourMan.GlobalAssets.repository;


import com.fourMan.GlobalAssets.entity.ArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<ArticleEntity,Long> {
}
