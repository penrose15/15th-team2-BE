package com.depromeet.memory.mock;

import com.depromeet.memory.Stroke;
import com.depromeet.memory.repository.StrokeRepository;
import java.util.ArrayList;
import java.util.List;

public class FakeStrokeRepository implements StrokeRepository {
    private Long autoGeneratedId = 0L;
    private final List<Stroke> data = new ArrayList<>();

    @Override
    public Stroke save(Stroke stroke) {
        if (stroke.getId() == null || stroke.getId() == 0) {
            Stroke newStroke =
                    Stroke.builder()
                            .id(++autoGeneratedId)
                            .memory(stroke.getMemory())
                            .name(stroke.getName())
                            .laps(stroke.getLaps())
                            .meter(stroke.getMeter())
                            .build();
            data.add(newStroke);
            return newStroke;
        } else {
            data.removeIf(item -> item.getId().equals(stroke.getId()));
            data.add(stroke);
            return stroke;
        }
    }

    @Override
    public List<Stroke> findAllByMemoryId(Long memoryId) {
        return data.stream().filter(item -> item.getMemory().getId().equals(memoryId)).toList();
    }

    @Override
    public void deleteById(Long id) {
        data.removeIf(item -> item.getId().equals(id));
    }
}
