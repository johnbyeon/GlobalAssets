package com.fourMan.GlobalAssets.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RateNewsItem {
    private int no;          // 표시용 번호(1..n)
    private String title;    // 기사 타이틀
    private String link;     // 기사 링크(클릭시 이동)
    private String time;     // 기사 발행 시각(문자열 그대로)
}
