package com.example.task_manager.controller;

import com.example.task_manager.entity.Task;
import com.example.task_manager.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController  // ← @Controller + @ResponseBody (ritorna JSON)
@RequestMapping("/api/tasks")  // ← Base path: tutti gli endpoint iniziano con /api/tasks
@CrossOrigin(origins = "*")  // ← Permette chiamate da frontend (React, Angular, ecc.)
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
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = service.getAllTasks();
        return ResponseEntity.ok(tasks);
    }
    
    // ============================================
    // GET /api/tasks/{id}
    // Ottieni task per IDS
    // ============================================
    @GetMapping("/{id:\\d+}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Task task = service.getTaskById(id);
        return ResponseEntity.ok(task);
        
        // Se task non esiste, TaskNotFoundException viene lanciata
        // GlobalExceptionHandler la gestisce automaticamente!
    }
    
    // ============================================
    // POST /api/tasks
    // Crea nuova task
    // ============================================
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task createdTask = service.createTask(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
        //                            ^
        //                            └─ HTTP 201 Created
    }
    
    // ============================================
    // PUT /api/tasks/{id}
    // Aggiorna task esistente
    // ============================================
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(
            @PathVariable Long id,
            @RequestBody Task task) {
        
        Task updatedTask = service.updateTask(id, task);
        return ResponseEntity.ok(updatedTask);
    }
    
    // ============================================
    // DELETE /api/tasks/{id}
    // Cancella task
    // ============================================
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteTask(@PathVariable Long id) {
        service.deleteTask(id);
        
        // Risposta di conferma
        Map<String, String> response = new HashMap<>();
        response.put("message", "Task deleted successfully");
        response.put("id", id.toString());
        
        return ResponseEntity.ok(response);
    }
    
    // ============================================
    // GET /api/tasks/search?keyword=xxx
    // Cerca task per keyword nel titolo
    // ============================================
    @GetMapping("/search")
    public ResponseEntity<List<Task>> searchTasks(
            @RequestParam(name = "keyword") String keyword) {
        
        List<Task> tasks = service.findByTitleContaining(keyword);
        return ResponseEntity.ok(tasks);
    }
    
    // ============================================
    // GET /api/tasks/status?completed=true
    // Filtra task per stato (completate/non completate)
    // ============================================
    @GetMapping("/status")
    public ResponseEntity<List<Task>> getTasksByStatus(
            @RequestParam(name = "completed") boolean completed) {
        
        List<Task> tasks = service.findByCompleted(completed);
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
        
        Map<String, Object> response = new HashMap<>();
        response.put("completed", completed);
        response.put("count", count);
        
        return ResponseEntity.ok(response);
    }
    
    // ============================================
    // GET /api/tasks/health
    // Health check endpoint (utile per monitoring)
    // ============================================
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "Task Manager API");
        return ResponseEntity.ok(response);
    }
}