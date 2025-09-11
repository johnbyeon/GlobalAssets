package com.fourMan.GlobalAssets.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 리스트 렌더링에 쓰는 간단 DTO */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsItemDto {
    private int no;       // 1,2,3...
    private String title; // 정제된 제목
    private String link;  // 기사 URL
    private String time;  // yyyy-MM-dd HH:mm
}
