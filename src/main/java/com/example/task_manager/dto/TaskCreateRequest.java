 package com.example.task_manager.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

  public record TaskCreateRequest(
      @NotBlank(message = "Title is required")
      @Size(max = 200, message = "Title cannot exceed 200 characters")
      String title,

      @Size(max = 1000, message = "Description cannot exceed 1000 characters")
      String description
  ) {}