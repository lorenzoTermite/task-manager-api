package com.example.task_manager.dto;

import java.time.LocalDateTime;

public record UserResponse(
      Long id,
      String email,
      String username,
      int taskCount,
      LocalDateTime createdAt

) {

}
