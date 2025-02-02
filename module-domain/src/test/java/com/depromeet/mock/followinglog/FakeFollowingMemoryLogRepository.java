package com.depromeet.mock.followinglog;

import com.depromeet.fixture.domain.friend.FriendFixture;
import com.depromeet.followinglog.domain.FollowingMemoryLog;
import com.depromeet.followinglog.port.out.persistence.FollowingMemoryLogPersistencePort;
import com.depromeet.friend.domain.Friend;
import com.depromeet.member.domain.Member;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FakeFollowingMemoryLogRepository implements FollowingMemoryLogPersistencePort {
    private Long followingMemoryLogAutoGeneratedId = 1L;
    private Long friendAutoGeneratedId = 1L;
    private List<FollowingMemoryLog> followingMemoryLogs = new ArrayList<>();
    private List<Friend> friends = new ArrayList<>();

    @Override
    public Long save(FollowingMemoryLog followingMemoryLog) {
        if (followingMemoryLog.getId() == null || followingMemoryLog.getId().equals(0L)) {
            FollowingMemoryLog newFollowingMemoryLog =
                    FollowingMemoryLog.builder()
                            .id(followingMemoryLogAutoGeneratedId++)
                            .memory(followingMemoryLog.getMemory())
                            .createdAt(LocalDateTime.now())
                            .build();
            followingMemoryLogs.add(newFollowingMemoryLog);
            return newFollowingMemoryLog.getId();
        } else {
            followingMemoryLogs.removeIf(item -> item.getId().equals(followingMemoryLog.getId()));
            followingMemoryLogs.add(followingMemoryLog);
            return followingMemoryLog.getId();
        }
    }

    @Override
    public List<FollowingMemoryLog> findLogsByMemberIdAndCursorId(Long memberId, Long cursorId) {
        List<Long> friendFollowingIds =
                friends.stream()
                        .filter(item -> item.getMember().getId().equals(memberId))
                        .map(item -> item.getFollowing().getId())
                        .toList();

        List<FollowingMemoryLog> result =
                followingMemoryLogs.stream()
                        .filter(
                                item ->
                                        friendFollowingIds.contains(
                                                item.getMemory().getMember().getId()))
                        .filter(item -> cursorIdLt(cursorId, item))
                        .sorted((o1, o2) -> (int) (o2.getId() - o1.getId()))
                        .toList();
        return result.subList(0, Math.min(result.size(), 11));
    }

    private boolean cursorIdLt(Long cursorId, FollowingMemoryLog followingMemoryLog) {
        if (cursorId == null) return true;
        return followingMemoryLog.getId() < cursorId;
    }

    @Override
    public void deleteAllByMemoryIds(List<Long> memoryIds) {
        followingMemoryLogs.removeIf(item -> memoryIds.contains(item.getMemory().getId()));
    }

    @Override
    public void deleteAllByMemoryId(Long memoryId) {
        followingMemoryLogs.removeIf(item -> item.getMemory().getId().equals(memoryId));
    }

    public List<FollowingMemoryLog> getFollowingMemoryLogs() {
        return this.followingMemoryLogs;
    }

    public void saveFriends(Member member, List<Member> followings) {
        for (Member following : followings) {
            Friend friend = FriendFixture.makeFriends(friendAutoGeneratedId++, member, following);
            friends.add(friend);
        }
    }
}
