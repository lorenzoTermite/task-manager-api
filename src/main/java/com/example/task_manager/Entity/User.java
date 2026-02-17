package com.example.task_manager.entity;

  import jakarta.persistence.*;
  import lombok.AllArgsConstructor;
  import lombok.Data;
  import lombok.NoArgsConstructor;

  import java.time.LocalDateTime;
  import java.util.ArrayList;
  import java.util.List;

  @Entity
  @Table(name = "users")
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public class User {

      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;

      @Column(nullable = false, unique = true, length = 100)
      private String email;

      @Column(nullable = false, length = 100)
      private String username;

      @OneToMany(mappedBy = "assignee", cascade = CascadeType.ALL)
      private List<Task> tasks = new ArrayList<>();

      @Column(name = "created_at", nullable = false, updatable = false)
      private LocalDateTime createdAt;

      @PrePersist
      protected void onCreate() {
          createdAt = LocalDateTime.now();
      }
  }