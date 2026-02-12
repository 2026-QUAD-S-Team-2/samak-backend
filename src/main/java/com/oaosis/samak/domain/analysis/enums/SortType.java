package com.oaosis.samak.domain.analysis.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SortType {
    LATEST("최신순"),
    RISK_SCORE("신뢰도순");

    private final String description;
}

