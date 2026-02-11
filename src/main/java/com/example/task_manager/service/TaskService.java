package com.example.task_manager.service;

import com.example.task_manager.Entity.Task;
import com.example.task_manager.exception.TaskNotFoundException;
import com.example.task_manager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TaskService {

    private final TaskRepository repository;

   
    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public List<Task> getAllTasks() {
        return repository.findAll();
    }

    public Task getTaskById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task non trovato con id " + id));
    }

    public Task createTask(Task task) {
        return repository.save(task);
    }

    public Task updateTask(Long id, Task updated) {
        Task existing = getTaskById(id);
        existing.setTitle(updated.getTitle());
        existing.setDescription(updated.getDescription());
        existing.setCompleted(updated.isCompleted());
        return repository.save(existing);
    }

    public void deleteTask(Long id) {
        if (!repository.existsById(id)) {
            throw new TaskNotFoundException("Task non trovato con id " + id);
        }
        repository.deleteById(id);
    }

    public List<Task> findByCompleted(boolean completed) {
        return repository.findByCompleted(completed);
    }

    public List<Task> findByTitleContaining(String keyword) {
        return repository.findByTitleContaining(keyword);
    }

    public long countByCompleted(boolean completed) {
        return repository.countByCompleted(completed);
    }

}
