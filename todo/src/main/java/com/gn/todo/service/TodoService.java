package com.gn.todo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gn.todo.dto.PageDto;
import com.gn.todo.dto.SearchDto;
import com.gn.todo.dto.TodoDto;
import com.gn.todo.entity.Todo;
import com.gn.todo.repository.TodoRepository;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;

    public Page<Todo> selectTodoAll(SearchDto searchDto, PageDto pageDto) {
        Pageable pageable = PageRequest.of(pageDto.getNowPage() - 1, pageDto.getNumPerPage());
        
        if (searchDto.getSearch_text() != null && !searchDto.getSearch_text().isEmpty()) {
            return todoRepository.findByContentContaining(searchDto.getSearch_text(), pageable);
        }
        return todoRepository.findAll(pageable);
    }

    public int createTodo(TodoDto dto) {
        try {
            Todo todo = new Todo();
            todo.setContent(dto.getContent());
            todo.setFlag(dto.getFlag() != null ? dto.getFlag() : "N");
            todoRepository.save(todo);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    public int deleteTodo(Long no) {
        try {
            todoRepository.deleteById(no);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    public int toggleFlag(Long no) {
        try {
            Optional<Todo> optionalTodo = todoRepository.findById(no);
            if (optionalTodo.isPresent()) {
                Todo todo = optionalTodo.get();
                todo.setFlag(todo.getFlag().equals("Y") ? "N" : "Y");
                todoRepository.save(todo);
                return 1;
            }
            return 0;
        } catch (Exception e) {
            return 0;
        }
    }
}