package com.fourMan.GlobalAssets.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
public class PagenationService {
    private static final int BAR_LENGTH = 5;
    public List<Integer> getPagenationBarNumber(int currentPageNumber,
                                                int totalPageNumber)
    {
        int startNumber = Math.max(currentPageNumber-(BAR_LENGTH / 2),0);
        int endNumber = Math.min(startNumber + BAR_LENGTH, totalPageNumber);
        //endnumber 보다 1 작은 값 까지
        if(startNumber > totalPageNumber-5 )
        {
            startNumber = totalPageNumber-5;
        }
        return IntStream.range(startNumber,endNumber)
                .boxed().toList();
    }

    public int currentBarLength(){
        return BAR_LENGTH;
    }

}
