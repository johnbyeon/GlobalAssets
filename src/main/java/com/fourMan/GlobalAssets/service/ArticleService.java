package com.fourMan.GlobalAssets.service;


import com.fourMan.GlobalAssets.dao.ArticleDao;
import com.fourMan.GlobalAssets.dto.ArticleDto;


import com.fourMan.GlobalAssets.entity.ArticleEntity;
import com.fourMan.GlobalAssets.repository.ArticleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.List;



@Service
@RequiredArgsConstructor
@Transactional
public class ArticleService {
    private final ArticleDao dao;
    private final ArticleRepository articleRepository;

    public List<ArticleDto> getAllArticle() {
        List<ArticleEntity> articles = dao.findAllArticle();
        if(ObjectUtils.isEmpty(articles)){
            return Collections.emptyList();
        } else{
            return articles
                    .stream()
                    .map(x->ArticleDto.fromEntity(x))
                    .toList();
        }
    }

    public Page<ArticleDto> getArticlePage(Pageable pageable) {
        Page<ArticleEntity> articles = articleRepository.findAll(pageable);
        if(ObjectUtils.isEmpty(articles)){
            return null;
        }else{
            return articles.map(x->ArticleDto.fromEntity(x));
        }
    }

    public ArticleDto getOneArticle(Long id) {
        ArticleEntity article = dao.getOneArticle(id);
        if(ObjectUtils.isEmpty(article))
        {
            return null;
        }
        return ArticleDto.fromEntity(article);
    }

    public void deleteArticle(Long id){
        dao.deleteArticle(id);
    }

    public void insertArticle(ArticleDto dto) {

        dao.insertArticle(ArticleDto.fromDto(dto));

    }
    public void updateArticle(ArticleDto dto) {
        dao.updateArticle(dto);
    }


}
