package com.depromeet.memory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Timeline {
    private List<Memory> timelineContents;
    private int pageSize;
    private LocalDate cursorRecordAt;
    private boolean hasNext;

    @Builder
    public Timeline(
            List<Memory> timelineContents,
            int pageSize,
            LocalDate cursorRecordAt,
            boolean hasNext) {
        this.timelineContents = timelineContents != null ? timelineContents : new ArrayList<>();
        this.pageSize = pageSize != 0 ? pageSize : 10;
        this.cursorRecordAt = cursorRecordAt;
        this.hasNext = hasNext;
    }
}
