package com.example.task_manager.dto;

import java.time.LocalDateTime;

public record TaskResponse (

      Long id,
      String title,
      String description,
      boolean completed,
      LocalDateTime createdAt

){
    
}
