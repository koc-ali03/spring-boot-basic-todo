package cc.aliko.basic_todo.service;

import cc.aliko.basic_todo.dto.TodoRequest;
import cc.aliko.basic_todo.dto.TodoResponse;

import java.util.List;

public interface TodoService {
    TodoResponse create(TodoRequest request);
    List<TodoResponse> getAll();
    TodoResponse getById(Long id);
    TodoResponse update(Long id, TodoRequest request);
    void delete(Long id);
}
