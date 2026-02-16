package com.example.task_manager.service;

import com.example.task_manager.dto.TaskCreateRequest;
import com.example.task_manager.dto.TaskResponse;
import com.example.task_manager.dto.TaskUpdateRequest;
import com.example.task_manager.entity.Task;
import com.example.task_manager.exception.TaskNotFoundException;
import com.example.task_manager.mapper.TaskMapper;
import com.example.task_manager.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository repository;
    private final TaskMapper mapper;
   
    public TaskService(TaskRepository repository,TaskMapper mapper) {
        this.repository = repository;
        this.mapper=mapper;
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> getAllTasks() {
        return mapper.toResponseList(repository.findAll());
    }
   @Transactional(readOnly = true)
    public TaskResponse getTaskById(Long id) {
       Task task = findTaskOrThrow(id);
          return mapper.toResponse(task);
    }
    @Transactional
    public TaskResponse createTask(TaskCreateRequest request) {
        Task task=mapper.toEntity(request);
        Task saved=repository.save(task);
        return mapper.toResponse(saved);
    }
    @Transactional
    public TaskResponse updateTask(Long id, TaskUpdateRequest request) {
         Task existing = findTaskOrThrow(id);
        if (request.title() != null){
            existing.setTitle(request.title());
        }
          if (request.description() != null) {
              existing.setDescription(request.description());
          }
          if (request.completed() != null) {
              existing.setCompleted(request.completed());
          }

          Task saved = repository.save(existing);
          return mapper.toResponse(saved);
       
    }
    @Transactional
    public void deleteTask(Long id) {
        if (!repository.existsById(id)) {
            throw new TaskNotFoundException("Task non trovato con id " + id);
        }
        repository.deleteById(id);
    }
    @Transactional(readOnly = true)
    public List<TaskResponse> findByCompleted(boolean completed) {
        return mapper.toResponseList((repository.findByCompleted(completed)));
    }
      @Transactional(readOnly = true)
    public List<TaskResponse> findByTitleContaining(String keyword) {
        return mapper.toResponseList(repository.findByTitleContaining(keyword));
    }
      @Transactional(readOnly = true)
    public long countByCompleted(boolean completed) {
        return repository.countByCompleted(completed);
    }
     private Task findTaskOrThrow(Long id) {
          return repository.findById(id)
                  .orElseThrow(() -> new TaskNotFoundException("Task non trovato con id " + id));
      }
}
