package cc.aliko.basic_todo.mapper;

import cc.aliko.basic_todo.dto.TodoRequest;
import cc.aliko.basic_todo.dto.TodoResponse;
import cc.aliko.basic_todo.entity.Todo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TodoMapper {

    TodoMapper INSTANCE = Mappers.getMapper(TodoMapper.class);

    Todo toEntity(TodoRequest request);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "completed", source = "completed")
    @Mapping(target = "createdAt", source = "createdAt")
    TodoResponse toResponse(Todo todo);
}
