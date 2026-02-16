package com.example.task_manager.controller;

import com.example.task_manager.dto.TaskCreateRequest;
import com.example.task_manager.dto.TaskResponse;
import com.example.task_manager.dto.TaskUpdateRequest;

import com.example.task_manager.service.TaskService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController  // ← @Controller + @ResponseBody (ritorna JSON)
@RequestMapping("/api/tasks")  // ← Base path: tutti gli endpoint iniziano con /api/tasks
public class TaskController {
    
    private final TaskService service;
    
    // Constructor Injection (NO @Autowired necessario)
    public TaskController(TaskService service) {
        this.service = service;
    }
    
    // ============================================
    // GET /api/tasks
    // Ottieni tutte le task
    // ============================================
    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        List<TaskResponse> tasks = service.getAllTasks();
        return ResponseEntity.ok(tasks);
    }
    
    // ============================================
    // GET /api/tasks/{id}
    // Ottieni task per IDS
    // ============================================
    @GetMapping("/{id:\\d+}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) {
        TaskResponse task = service.getTaskById(id);
        return ResponseEntity.ok(task);
        
        // Se task non esiste, TaskNotFoundException viene lanciata
        // GlobalExceptionHandler la gestisce automaticamente!
    }
    
    // ============================================
    // POST /api/tasks
    // Crea nuova task
    // ============================================
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskCreateRequest request) {
        TaskResponse createdTask = service.createTask(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
        //                            ^
        //                            └─ HTTP 201 Created
    }
    
    // ============================================
    // PUT /api/tasks/{id}
    // Aggiorna task esistente
    // ============================================
    @PutMapping("/{id:\\d+}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskUpdateRequest request) {
        
        TaskResponse updatedTask = service.updateTask(id, request);
        return ResponseEntity.ok(updatedTask);
    }
    
    // ============================================
    // DELETE /api/tasks/{id}
    // Cancella task
    // ============================================
          @DeleteMapping("/{id:\\d+}")
      public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
          service.deleteTask(id);
          return ResponseEntity.noContent().build();
      }
    
    // ============================================
    // GET /api/tasks/search?keyword=xxx
    // Cerca task per keyword nel titolo
    // ============================================
    @GetMapping("/search")
    public ResponseEntity<List<TaskResponse>> searchTasks(
            @RequestParam(name = "keyword") String keyword) {
        
        List<TaskResponse> tasks = service.findByTitleContaining(keyword);
        return ResponseEntity.ok(tasks);
    }
    
    // ============================================
    // GET /api/tasks/status?completed=true
    // Filtra task per stato (completate/non completate)
    // ============================================
    @GetMapping("/status")
    public ResponseEntity<List<TaskResponse>> getTasksByStatus(
            @RequestParam(name = "completed") boolean completed) {
        
        List<TaskResponse> tasks = service.findByCompleted(completed);
        return ResponseEntity.ok(tasks);
    }
    
    // ============================================
    // GET /api/tasks/count?completed=true
    // Conta task per stato
    // ============================================
   @GetMapping("/count")
      public ResponseEntity<Map<String, Object>> countTasks(
              @RequestParam(name = "completed") boolean completed) {
          long count = service.countByCompleted(completed);
          return ResponseEntity.ok(Map.of("completed", completed, "count", count));
      }
    

}
