package com.example.demo.alarm;


import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Builder
@Data
public class PageDto {
    @NonNull
    private Boolean isNextPage; //
    @NonNull private Long tenFirstPageNum;          // 11
    @NonNull private Long tenFinalPageNum;   // 20
    @NonNull private Long allFinalPageNum; // 이거랑 몇개는 활용하면 낫긴한데 복잡하게 나와서 안할꺼
}
