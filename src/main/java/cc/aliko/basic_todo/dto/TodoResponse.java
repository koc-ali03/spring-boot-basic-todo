package cc.aliko.basic_todo.dto;

import java.time.LocalDateTime;

public record TodoResponse(Long id, String title, boolean completed, LocalDateTime createdAt) {
}
