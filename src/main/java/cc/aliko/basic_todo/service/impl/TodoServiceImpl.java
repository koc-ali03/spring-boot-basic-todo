package cc.aliko.basic_todo.service.impl;

import cc.aliko.basic_todo.dto.TodoRequest;
import cc.aliko.basic_todo.dto.TodoResponse;
import cc.aliko.basic_todo.entity.Todo;
import cc.aliko.basic_todo.exception.DuplicateException;
import cc.aliko.basic_todo.exception.NotFoundException;
import cc.aliko.basic_todo.mapper.TodoMapper;
import cc.aliko.basic_todo.repository.TodoRepository;
import cc.aliko.basic_todo.service.TodoService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TodoServiceImpl implements TodoService {

    private final TodoRepository repository;
    private final TodoMapper mapper;

    public TodoServiceImpl(TodoRepository repository, TodoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public TodoResponse create(TodoRequest request) {
        Optional<Todo> existing = repository.findByTitle(request.getTitle());
        if (existing.isPresent()) {
            throw new DuplicateException("Bu başlığa sahip bir ToDo zaten var");
        }

        Todo saved = repository.save(mapper.toEntity(request));
        return mapper.toResponse(saved);
    }

    @Override
    public List<TodoResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TodoResponse getById(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Bu ID'ye sahip bir ToDo bulunamadı: " + id);
        }
        Todo todo = repository.findById(id).get();
        return mapper.toResponse(todo);
    }

    @Override
    public TodoResponse update(Long id, TodoRequest request) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Bu ID'ye sahip bir ToDo bulunamadı: " + id);
        }

        Todo existing = repository.findById(id).get();
        existing.setTitle(request.getTitle());
        existing.setCompleted(request.isCompleted());

        Todo updated = repository.save(existing);
        return mapper.toResponse(updated);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Bu ID'ye sahip bir ToDo bulunamadı: " + id);
        }
        repository.deleteById(id);
    }
}
