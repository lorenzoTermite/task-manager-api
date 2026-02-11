package com.example.task_manager.repository;

import com.example.task_manager.Entity.Task;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>    {

    List<Task> findByCompleted(boolean completed);
    List<Task> findByTitleContaining(String keyword);
    long countByCompleted(boolean completed);
    
}
