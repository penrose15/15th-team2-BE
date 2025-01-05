package com.depromeet.withdrawal.port.out.persistence;

import com.depromeet.withdrawal.domain.ReasonType;

public interface WithdrawalReasonPort {
    void writeWithdrawalToSheet(ReasonType reasonType, String feedback);
}
