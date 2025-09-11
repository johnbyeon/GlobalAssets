// com.fourMan.GlobalAssets.config.RateBootstrap (선택)
package com.fourMan.GlobalAssets.config;

import com.fourMan.GlobalAssets.entity.AssetsEntity;
import com.fourMan.GlobalAssets.repository.AssetsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RateBootstrap implements CommandLineRunner {

    private final AssetsRepository assets;

    @Override
    public void run(String... args) {
        seed("USD/KRW", "US Dollar / KRW");
        seed("JPY/KRW", "Japanese Yen / KRW");
        seed("CNY/KRW", "Chinese Yuan / KRW");
        seed("EUR/KRW", "Euro / KRW");
    }

    private void seed(String codeOrSymbol, String name) {
        assets.findBySymbol(codeOrSymbol)
                .or(() -> assets.findByCode(codeOrSymbol))
                .orElseGet(() -> {
                    AssetsEntity e = new AssetsEntity();
                    // 엔티티/DTO는 고정 조건이니, 제공된 세터만 사용
                    e.setCode(codeOrSymbol);
                    e.setSymbol(codeOrSymbol);
                    e.setName(name);
                    return assets.save(e);
                });
    }
}
