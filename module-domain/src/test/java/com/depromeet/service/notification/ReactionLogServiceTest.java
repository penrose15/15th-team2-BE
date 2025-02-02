package com.depromeet.service.notification;

import static org.assertj.core.api.Assertions.*;

import com.depromeet.fixture.domain.member.MemberFixture;
import com.depromeet.fixture.domain.memory.MemoryFixture;
import com.depromeet.fixture.domain.reaction.ReactionFixture;
import com.depromeet.member.domain.Member;
import com.depromeet.memory.domain.Memory;
import com.depromeet.mock.notification.FakeReactionLogRepository;
import com.depromeet.notification.domain.ReactionLog;
import com.depromeet.notification.event.ReactionLogEvent;
import com.depromeet.notification.port.out.ReactionLogPersistencePort;
import com.depromeet.notification.service.ReactionLogService;
import com.depromeet.reaction.domain.Reaction;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ReactionLogServiceTest {
    private ReactionLogPersistencePort reactionLogPersistencePort;
    private ReactionLogService reactionLogService;
    private Reaction reaction;
    private Member member1;
    private Member member2;

    @BeforeEach
    void init() {
        reactionLogPersistencePort = new FakeReactionLogRepository();
        reactionLogService = new ReactionLogService(reactionLogPersistencePort);
        member1 = MemberFixture.make(1L);
        member2 = MemberFixture.make(2L);
        Memory memory = MemoryFixture.make(member2, null);
        reaction = ReactionFixture.make(member1, memory);

        var event = ReactionLogEvent.of(member2, reaction);
        reactionLogService.save(event);
    }

    @Test
    public void 응원_로그를_저장합니다() throws Exception {
        // given
        var reactionLogEvent = ReactionLogEvent.of(member2, reaction);

        // when
        reactionLogService.save(reactionLogEvent);

        // then
        List<ReactionLog> reactionLogs = reactionLogService.getReactionsLogs(member2.getId(), null);
        assertThat(reactionLogs.size()).isEqualTo(2);
        assertThat(reactionLogs.getFirst().getReceiver().getId()).isEqualTo(2L);
        assertThat(reactionLogs.getFirst().getReaction().getMember().getId()).isEqualTo(1L);
    }

    @Test
    public void 응원_로그를_조회합니다() throws Exception {
        // when
        List<ReactionLog> reactionLogs = reactionLogService.getReactionsLogs(member2.getId(), null);

        // then
        assertThat(reactionLogs.size()).isEqualTo(1);
        assertThat(reactionLogs.getFirst().getReceiver().getId()).isEqualTo(2L);
        assertThat(reactionLogs.getFirst().getReaction().getMember().getId()).isEqualTo(1L);
    }

    @Test
    public void 미확인_로그_수를_확인합니다() throws Exception {
        // when
        Long unreadReactionLogCount = reactionLogService.getUnreadReactionLogCount(2L);

        // then
        assertThat(unreadReactionLogCount).isEqualTo(1L);
    }

    @Test
    public void 로그_조회_및_미확인_로그카운트_변경을_확인합니다() throws Exception {
        // given
        reactionLogService.markAsReadReactionLogs(2L);

        // when
        Long unreadReactionLogCount = reactionLogService.getUnreadReactionLogCount(2L);

        // then
        assertThat(unreadReactionLogCount).isEqualTo(0L);
    }

    @Test
    public void 응원_로그_삭제를_수행합니다() throws Exception {
        // given
        reactionLogService.deleteAllByReactionId(List.of(1L));

        // when
        List<ReactionLog> reactionLogs = reactionLogService.getReactionsLogs(member2.getId(), null);

        // then
        assertThat(reactionLogs.size()).isEqualTo(0);
    }
}
