package com.depromeet.mock.reaction;

import com.depromeet.reaction.domain.Reaction;
import com.depromeet.reaction.domain.vo.ReactionCount;
import com.depromeet.reaction.port.out.persistence.ReactionPersistencePort;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class FakeReactionRepository implements ReactionPersistencePort {
    private Long reactionAutoGeneratedId = 0L;
    private Map<Long, Reaction> reactionDatabase = new HashMap<>();

    @Override
    public Reaction save(Reaction reaction) {
        if (reaction.getId() == null) {
            Reaction newReaction =
                    Reaction.builder()
                            .id(++reactionAutoGeneratedId)
                            .member(reaction.getMember())
                            .memory(reaction.getMemory())
                            .emoji(reaction.getEmoji())
                            .comment(reaction.getComment())
                            .createdAt(reaction.getCreatedAt())
                            .build();
            reactionDatabase.put(newReaction.getId(), newReaction);
            return newReaction;
        }
        return reactionDatabase.replace(reaction.getId(), reaction);
    }

    @Override
    public List<Reaction> getAllByMemberAndMemory(Long memberId, Long memoryId) {
        return reactionDatabase.values().stream()
                .filter(reaction -> reaction.getMember().getId().equals(memberId))
                .filter(reaction -> reaction.getMemory().getId().equals(memoryId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Reaction> getAllByMemoryId(Long memoryId) {
        return reactionDatabase.values().stream()
                .filter(reaction -> reaction.getMemory().getId().equals(memoryId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Reaction> getPagingReactions(Long memoryId, Long cursorId) {
        return reactionDatabase.values().stream()
                .filter(
                        reaction ->
                                memoryId == null || reaction.getMemory().getId().equals(memoryId))
                .filter(reaction -> cursorId == null || reaction.getId() <= cursorId)
                .sorted((a, b) -> b.getId().compareTo(a.getId()))
                .limit(11)
                .collect(Collectors.toList());
    }

    @Override
    public Long getAllCountByMemoryId(Long memoryId) {
        return reactionDatabase.values().stream()
                .filter(reaction -> reaction.getMemory().getId().equals(memoryId))
                .count();
    }

    @Override
    public List<ReactionCount> getAllCountByMemoryIds(List<Long> memoryIds) {
        return reactionDatabase.values().stream()
                .filter(reaction -> memoryIds.contains(reaction.getMemory().getId()))
                .collect(
                        Collectors.groupingBy(
                                reaction -> reaction.getMemory().getId(), Collectors.counting()))
                .entrySet()
                .stream()
                .map(
                        entry ->
                                ReactionCount.builder()
                                        .memoryId(entry.getKey())
                                        .reactionCount(entry.getValue())
                                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Reaction> getReactionById(Long reactionId) {
        return Optional.ofNullable(reactionDatabase.get(reactionId));
    }

    @Override
    public void deleteById(Long reactionId) {
        reactionDatabase.remove(reactionId);
    }

    @Override
    public List<Reaction> getPureReactionsByMemberAndMemory(Long memberId, Long memoryId) {
        return reactionDatabase.values().stream()
                .filter(reaction -> reaction.getMember().getId().equals(memberId))
                .filter(reaction -> reaction.getMemory().getId().equals(memoryId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> findAllIdByMemoryIdOrMemberId(List<Long> memoryIds, Long memberId) {
        return reactionDatabase.values().stream()
                .filter(
                        reaction ->
                                memoryIds == null
                                        || memoryIds.contains(reaction.getMemory().getId()))
                .filter(
                        reaction ->
                                memberId == null || reaction.getMember().getId().equals(memberId))
                .map(Reaction::getId)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAllById(List<Long> reactionIds) {
        reactionIds.forEach(reactionDatabase::remove);
    }

    @Override
    public List<Long> findAllIdByMemoryId(Long memoryId) {
        return reactionDatabase.values().stream()
                .filter(reaction -> reaction.getMemory().getId().equals(memoryId))
                .map(Reaction::getId)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteReactionLogsById(Long reactionId) {
        // do nothing
    }
}
