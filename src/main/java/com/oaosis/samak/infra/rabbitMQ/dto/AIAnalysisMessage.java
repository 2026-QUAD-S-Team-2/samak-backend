package com.oaosis.samak.infra.rabbitMQ.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AIAnalysisMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long analysisItemId;     // 분석 아이템 ID
    private String countryCode;      // 국가 코드
    private BigDecimal salary;       // 연봉
    private List<String> imageUrls;  // 이미지 URL 리스트
}