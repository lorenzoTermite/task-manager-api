package com.example.task_manager.exception;

public class TaskAlreadyCompletedException extends RuntimeException {
    
    public TaskAlreadyCompletedException(String message) {
        super(message);
    }
    
    public TaskAlreadyCompletedException(String message, Throwable cause) {
        super(message, cause);
    }
}
