// src/main/java/com/fourMan/GlobalAssets/config/RateBootstrap.java
package com.fourMan.GlobalAssets.config;

import com.fourMan.GlobalAssets.entity.AssetsEntity;
import com.fourMan.GlobalAssets.repository.RateAssetsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RateBootstrap {

    private final RateAssetsRepository rateassetsRepository;

    @Bean
    public CommandLineRunner rateSeed() {
        return args -> {
            // code, symbol, name, category (팀 규칙 필드명)
            createIfAbsent("USDKRW", "USD/KRW", "원/달러", "FX");
            createIfAbsent("JPYKRW", "JPY/KRW", "원/엔",   "FX");
            createIfAbsent("EURKRW", "EUR/KRW", "원/유로", "FX");
        };
    }

    private void createIfAbsent(String code, String symbol, String name, String category) {
        rateassetsRepository.findBySymbol(symbol)
                .or(() -> rateassetsRepository.findByCode(code))
                .orElseGet(() -> {
                    AssetsEntity e = AssetsEntity.builder()
                            .code(code)
                            .symbol(symbol)
                            .name(name)
                            .category(category)
                            .build();
                    return rateassetsRepository.save(e);
                });
    }
}
