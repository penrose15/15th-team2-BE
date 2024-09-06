package com.depromeet.mock.memory;

import com.depromeet.member.domain.vo.MemberIdAndNickname;
import com.depromeet.memory.domain.Memory;
import com.depromeet.memory.domain.vo.MemoryAndDetailId;
import com.depromeet.memory.domain.vo.MemoryIdAndDiaryAndMember;
import com.depromeet.memory.port.out.persistence.MemoryPersistencePort;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class FakeMemoryRepository implements MemoryPersistencePort {
    private Long autoGeneratedId = 0L;
    private final List<Memory> data = new ArrayList<>();

    @Override
    public Memory save(Memory memory) {
        if (memory.getId() == null || memory.getId() == 0) {
            Memory newMemory =
                    Memory.builder()
                            .id(++autoGeneratedId)
                            .member(memory.getMember())
                            .pool(memory.getPool())
                            .memoryDetail(memory.getMemoryDetail())
                            .recordAt(memory.getRecordAt())
                            .startTime(memory.getStartTime())
                            .endTime(memory.getEndTime())
                            .lane(memory.getLane())
                            .diary(memory.getDiary())
                            .build();
            data.add(newMemory);
            return newMemory;
        } else {
            data.removeIf(item -> item.getId().equals(memory.getId()));
            data.add(memory);
            return memory;
        }
    }

    @Override
    public Optional<Memory> findById(Long memoryId) {
        return data.stream().filter(item -> item.getId().equals(memoryId)).findAny();
    }

    @Override
    public Optional<Memory> findByIdWithMember(Long memoryId) {
        return Optional.empty();
    }

    @Override
    public Optional<Memory> findByRecordAtAndMemberId(LocalDate recordAt, Long memberId) {
        return Optional.empty();
    }

    @Override
    public Optional<Memory> update(Long memoryId, Memory updateMemory) {
        Optional<Memory> md = data.stream().filter(item -> item.getId().equals(memoryId)).findAny();
        if (md.isEmpty()) {
            return Optional.empty();
        } else {
            Memory origin = md.get();
            return Optional.of(
                    Memory.builder()
                            .id(memoryId)
                            .member(origin.getMember())
                            .pool(
                                    updateMemory.getPool() != null
                                            ? updateMemory.getPool()
                                            : origin.getPool())
                            .memoryDetail(
                                    updateMemory.getMemoryDetail() != null
                                            ? updateMemory.getMemoryDetail()
                                            : origin.getMemoryDetail())
                            .strokes(
                                    updateMemory.getStrokes() != null
                                            ? updateMemory.getStrokes()
                                            : origin.getStrokes())
                            .images(
                                    updateMemory.getImages() != null
                                            ? updateMemory.getImages()
                                            : origin.getImages())
                            .recordAt(
                                    updateMemory.getRecordAt() != null
                                            ? updateMemory.getRecordAt()
                                            : origin.getRecordAt())
                            .startTime(
                                    updateMemory.getStartTime() != null
                                            ? updateMemory.getStartTime()
                                            : origin.getStartTime())
                            .endTime(
                                    updateMemory.getEndTime() != null
                                            ? updateMemory.getEndTime()
                                            : origin.getEndTime())
                            .lane(
                                    updateMemory.getLane() != null
                                            ? updateMemory.getLane()
                                            : origin.getLane())
                            .diary(
                                    updateMemory.getDiary() != null
                                            ? updateMemory.getDiary()
                                            : origin.getDiary())
                            .build());
        }
    }

    @Override
    public int findOrderInMonth(Long memberId, Long memoryId, int month) {
        return 0;
    }

    @Override
    public List<Memory> findPrevMemoryByMemberId(Long memberId, LocalDate cursorRecordAt) {
        List<Memory> memories;
        if (cursorRecordAt == null) {
            memories =
                    data.stream()
                            .filter(memory -> memory.getMember().getId().equals(memberId))
                            .toList();
        } else {
            memories =
                    data.stream()
                            .filter(memory -> memory.getMember().getId().equals(memberId))
                            .filter(memory -> memory.getRecordAt().isBefore(cursorRecordAt))
                            .toList();
        }
        memories = new ArrayList<>(memories);
        memories.sort((memory1, memory2) -> memory2.getRecordAt().compareTo(memory1.getRecordAt()));
        if (memories.size() > 11) {
            memories = memories.subList(0, 11);
        }
        return memories;
    }

    @Override
    public List<Memory> getCalendarByYearAndMonth(Long memberId, Integer year, Short month) {
        return null;
    }

    @Override
    public Long findPrevIdByRecordAtAndMemberId(LocalDate recordAt, Long memberId) {
        return data.stream()
                .filter(
                        item ->
                                item.getRecordAt().isBefore(recordAt)
                                        && item.getMember().getId().equals(memberId))
                .max(Comparator.comparing(Memory::getRecordAt))
                .map(Memory::getId)
                .orElse(null);
    }

    @Override
    public Long findNextIdByRecordAtAndMemberId(LocalDate recordAt, Long memberId) {
        return data.stream()
                .filter(
                        item ->
                                item.getRecordAt().isBefore(recordAt)
                                        && item.getMember().getId().equals(memberId))
                .min(Comparator.comparing(Memory::getRecordAt))
                .map(Memory::getId)
                .orElse(null);
    }

    @Override
    public List<Memory> findByMemberId(Long memberId) {
        return data.stream().filter(item -> item.getMember().getId().equals(memberId)).toList();
    }

    @Override
    public void setNullByIds(List<Long> memoryIds) {
        data.forEach(
                item -> {
                    if (memoryIds.contains(item.getId()) && item.getMemoryDetail() != null) {
                        item.setMemoryDetailNull();
                    }
                });
    }

    @Override
    public void deleteAllByMemberId(Long memberId) {
        data.removeIf(item -> item.getMember().getId().equals(memberId));
    }

    @Override
    public MemoryAndDetailId findMemoryAndDetailIdsByMemberId(Long memberId) {
        List<Memory> memories =
                data.stream().filter(item -> item.getMember().getId().equals(memberId)).toList();
        List<Long> memoryIds = memories.stream().map(Memory::getId).toList();
        List<Long> memoryDetailIds =
                memories.stream().map(item -> item.getMemoryDetail().getId()).toList();
        return new MemoryAndDetailId(memoryIds, memoryDetailIds);
    }

    @Override
    public void deleteById(Long memoryId) {
        data.removeIf(item -> item.getId().equals(memoryId));
    }

    @Override
    public Optional<MemoryIdAndDiaryAndMember> findIdAndNicknameById(Long memberId) {
        return findById(memberId)
                .map(
                        item ->
                                new MemoryIdAndDiaryAndMember(
                                        item.getId(),
                                        item.getDiary(),
                                        new MemberIdAndNickname(
                                                item.getMember().getId(),
                                                item.getMember().getNickname())));
    }
}
