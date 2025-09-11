package com.fourMan.GlobalAssets.service;

import com.fourMan.GlobalAssets.entity.AssetsEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AssetNewsAdapter {
    public List<String> buildQueries(AssetsEntity a, int max) {
        List<String> q = new ArrayList<>();
        if (a.getSymbol() != null) q.add(a.getSymbol());
        if (a.getCode() != null && !a.getCode().equals(a.getSymbol())) q.add(a.getCode());
        if (a.getName() != null) q.add(a.getName());
        return q.stream().limit(max).toList();
    }
}
