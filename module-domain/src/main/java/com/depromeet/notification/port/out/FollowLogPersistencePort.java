package com.depromeet.notification.port.out;

import com.depromeet.notification.domain.FollowLog;
import com.depromeet.notification.domain.FollowType;
import java.time.LocalDateTime;
import java.util.List;

public interface FollowLogPersistencePort {
    FollowLog save(FollowLog followLog);

    List<FollowLog> findByMemberIdAndCursorCreatedAt(Long memberId, LocalDateTime cursorCreatedAt);

    void updateRead(Long memberId, Long followLogId, FollowType type);

    Long countUnread(Long memberId);

    void deleteAllByMemberId(Long memberId);

    boolean existsByReceiverIdAndFollowerId(Long receiverId, Long followerId);

    List<Long> getFriendList(Long memberId, List<Long> followerIds);
}
