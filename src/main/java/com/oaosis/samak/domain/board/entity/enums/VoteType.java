package com.oaosis.samak.domain.board.entity.enums;

import lombok.Getter;

@Getter
public enum VoteType {
    FRAUD,      // 맞다 (사기)
    NOT_FRAUD;  // 아니다 (사기 아님)
}
