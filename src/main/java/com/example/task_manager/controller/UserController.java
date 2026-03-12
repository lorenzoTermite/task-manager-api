package com.example.task_manager.controller;

import com.example.task_manager.dto.UserCreateRequest;
import com.example.task_manager.dto.UserResponse;
import com.example.task_manager.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    // ============================================
    // GET /api/users
    // Ottieni tutti gli utenti
    // ============================================
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(service.getAllUsers());
    }

    // ============================================
    // GET /api/users/{id}
    // Ottieni utente per ID
    // ============================================
    @GetMapping("/{id:\\d+}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getUserById(id));
    }

    // ============================================
    // POST /api/users
    // Crea nuovo utente
    // ============================================
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createUser(request));
    }

    // ============================================
    // DELETE /api/users/{id}
    // Elimina utente
    // ============================================
    @DeleteMapping("/{id:\\d+}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        service.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
