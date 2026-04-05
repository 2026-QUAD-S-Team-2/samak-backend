package com.oaosis.samak.domain.board.exception;

import com.oaosis.samak.global.exception.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum BoardErrorCode implements BaseErrorCode {
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "BOARD-001", "게시글을 찾을 수 없습니다."),
    INVALID_VOTE_CATEGORY(HttpStatus.BAD_REQUEST, "BOARD-002", "사기 의심 투표 카테고리의 게시글에만 투표할 수 있습니다."),
    DUPLICATE_VOTE(HttpStatus.CONFLICT, "BOARD-003", "이미 투표한 게시글입니다.");

    private final HttpStatus status;
    private final String value;
    private final String message;
}
