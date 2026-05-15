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
    DUPLICATE_VOTE(HttpStatus.CONFLICT, "BOARD-003", "이미 투표한 게시글입니다."),
    DUPLICATE_SCRAP(HttpStatus.CONFLICT, "BOARD-004", "이미 스크랩한 게시글입니다."),
    SCRAP_NOT_FOUND(HttpStatus.NOT_FOUND, "BOARD-005", "스크랩 내역을 찾을 수 없습니다."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "BOARD-006", "댓글을 찾을 수 없습니다."),
    REPLY_DEPTH_EXCEEDED(HttpStatus.BAD_REQUEST, "BOARD-007", "대댓글에는 댓글을 달 수 없습니다."),
    ANALYSIS_ITEM_REQUIRED(HttpStatus.BAD_REQUEST, "BOARD-008", "AI 분석 결과 공유 게시글에는 분석 아이템 ID가 필요합니다."),
    ANALYSIS_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "BOARD-009", "존재하지 않는 분석 아이템입니다."),
    ANALYSIS_ITEM_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "BOARD-010", "해당 카테고리에서는 분석 아이템을 연결할 수 없습니다."),
    ANALYSIS_ITEM_NOT_COMPLETED(HttpStatus.BAD_REQUEST, "BOARD-011", "분석이 완료된 아이템만 게시글에 연결할 수 있습니다.");

    private final HttpStatus status;
    private final String value;
    private final String message;
}
