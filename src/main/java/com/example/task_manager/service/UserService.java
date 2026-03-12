package com.example.task_manager.service;

import com.example.task_manager.dto.UserCreateRequest;
import com.example.task_manager.dto.UserResponse;
import com.example.task_manager.entity.User;
import com.example.task_manager.exception.UserAlreadyExistsException;
import com.example.task_manager.exception.UserNotFoundException;
import com.example.task_manager.mapper.UserMapper;
import com.example.task_manager.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository repository;
    private final UserMapper mapper;

    public UserService(UserRepository repository, UserMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return mapper.toResponseList(repository.findAll());
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        User user = findUserOrThrow(id);
        return mapper.toResponse(user);
    }

    @Transactional
    public UserResponse createUser(UserCreateRequest request) {
        if (repository.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException("Email già registrata: " + request.email());
        }
        User user = mapper.toEntity(request);
        User saved = repository.save(user);
        log.info("Utente creato con id {}", saved.getId());
        return mapper.toResponse(saved);
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!repository.existsById(id)) {
            throw new UserNotFoundException("Utente non trovato con id " + id);
        }
        repository.deleteById(id);
        log.info("Utente eliminato con id {}", id);
    }

    private User findUserOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Utente non trovato con id " + id));
    }
}
