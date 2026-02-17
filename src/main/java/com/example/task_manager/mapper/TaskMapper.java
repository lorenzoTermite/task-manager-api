package com.example.task_manager.mapper;

  import com.example.task_manager.dto.TaskCreateRequest;
  import com.example.task_manager.dto.TaskResponse;
  import com.example.task_manager.entity.Task;
  import org.springframework.stereotype.Component;

  import java.util.List;

  @Component
  public class TaskMapper {

      public Task toEntity(TaskCreateRequest request) {
          Task task = new Task();
          task.setTitle(request.title());
          task.setDescription(request.description());
          return task;
      }

      public TaskResponse toResponse(Task task) {
          return new TaskResponse(
              task.getId(),
              task.getTitle(),
              task.getDescription(),
              task.isCompleted(),
              task.getCreatedAt(),
              task.getVersion(),
              task.getUpdatedAt()
          );
      }

      public List<TaskResponse> toResponseList(List<Task> tasks) {
        //“Per ogni elemento della lista tasks, 
      // trasformalo usando toResponse, e poi rimetti tutto in una nuova lista.”
          return tasks.stream().map(this::toResponse).toList();
      }
  }