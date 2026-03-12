package com.example.task_manager.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(
      @NotBlank(message = "Email is required")
      @Email(message = "Invalid email format")
      String email,

      @NotBlank(message = "Username is required")
      @Size(max = 100, message = "Username cannot exceed 100 characters")
      String username

) {
    
}
