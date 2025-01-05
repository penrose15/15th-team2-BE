package com.depromeet.type.withdrawal;

import com.depromeet.type.ErrorType;

public enum WithdrawalReasonErrorType implements ErrorType {
    FAILED_TO_INSERT_DATA_TO_SPREADSHEET("WITHDRAWAL_1", "스프레드시트(탈퇴 사유) 업데이트에 실패하였습니다"),
    REASON_CODE_NOT_FOUND("WITHDRAWAL_2", "해당 사유 코드는 존재하지 않는 코드입니다");

    private final String code;
    private final String message;

    WithdrawalReasonErrorType(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
