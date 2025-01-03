package com.depromeet.type.report;

import com.depromeet.type.ErrorType;

public enum ReportErrorType implements ErrorType {
    CANNOT_REPORT_OWN_MEMORY("REPORT_1", "자신의 기록을 신고할 수 없습니다"),
    FAILED_TO_INSERT_DATA_TO_SPREADSHEET("REPORT_2", "스프레드시트(신고) 업데이트에 실패하였습니다");

    private final String code;
    private final String message;

    ReportErrorType(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
