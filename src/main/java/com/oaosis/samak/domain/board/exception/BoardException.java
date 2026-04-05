package com.oaosis.samak.domain.board.exception;

import com.oaosis.samak.global.exception.BaseException;
import lombok.Getter;

@Getter
public class BoardException extends BaseException {

    public BoardException(BoardErrorCode errorCode) {
        super(errorCode);
    }
}
