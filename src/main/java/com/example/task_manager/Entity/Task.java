package com.example.task_manager.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
//Entity
public class Task {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 200)
    private String title;
    
    @Column(length = 1000)
    private String description;
    
    @Column(nullable = false)
    private boolean completed = false;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @Version
    private Long version;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

     @PreUpdate
     protected void onUpdate() {
     updatedAt = LocalDateTime.now();

  }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee_id")
     private User assignee;

}