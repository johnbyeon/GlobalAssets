package com.fourMan.GlobalAssets.service;



import com.fourMan.GlobalAssets.dto.AdvertisementDto;
import com.fourMan.GlobalAssets.entity.AdvertisementEntity;
import com.fourMan.GlobalAssets.repository.AdvertisementRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AdvertisementService {
       @Autowired
        EntityManager em;

    public void addAdvertisement(AdvertisementDto dto){
        em.persist(AdvertisementDto.fromDto(dto));
    }

    public List<AdvertisementDto> getAdverisementList(){

        String sql = "SELECT t FROM advertisement_entity t";
        Query query = em.createQuery(sql);
        List<AdvertisementEntity> advertisementEntityList = query.getResultList();

        return advertisementEntityList.stream()
                .map(x->AdvertisementDto.fromEntity(x))
                .toList();
    }

    public void setAdvertisementList(List<AdvertisementDto> advertisementDtoList){
        advertisementDtoList
                .forEach(x->em.persist(AdvertisementDto.fromDto(x)));

    }

}
