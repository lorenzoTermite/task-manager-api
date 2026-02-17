package com.example.task_manager.exception;


import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import jakarta.persistence.PersistenceException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;
 import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestControllerAdvice
public class GlobalExceptionHandler {
      private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    // ============================================
    // ECCEZIONI CUSTOM (Task-specific)
    // ============================================
    
    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTaskNotFound(
            TaskNotFoundException ex, 
            HttpServletRequest request) {
        
        ErrorResponse error = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            "Not Found",
            ex.getMessage(),
            request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    
    @ExceptionHandler(InvalidTaskException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTask(
            InvalidTaskException ex,
            HttpServletRequest request) {
        
        ErrorResponse error = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Bad Request",
            ex.getMessage(),
            request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    
    @ExceptionHandler(TaskAlreadyCompletedException.class)
    public ResponseEntity<ErrorResponse> handleTaskAlreadyCompleted(
            TaskAlreadyCompletedException ex,
            HttpServletRequest request) {
        
        ErrorResponse error = new ErrorResponse(
            HttpStatus.CONFLICT.value(),
            "Conflict",
            ex.getMessage(),
            request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
    
    // ============================================
    // ECCEZIONI DATABASE
    // ============================================
    
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(
            DataIntegrityViolationException ex,
            HttpServletRequest request) {
        
        String message = "Data integrity violation";
        
        // Estrai messaggio più specifico se possibile
        if (ex.getRootCause() != null) {
            String rootMessage = ex.getRootCause().getMessage();
            
            // Gestione constraint specifici Oracle
            if (rootMessage.contains("unique constraint")) {
                message = "Duplicate entry: a record with this value already exists";
            } else if (rootMessage.contains("foreign key")) {
                message = "Cannot delete: record is referenced by other data";
            } else if (rootMessage.contains("not null")) {
                message = "Required field cannot be null";
            }
        }
        
        ErrorResponse error = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Bad Request",
            message,
            request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    
    @ExceptionHandler(PersistenceException.class)
    public ResponseEntity<ErrorResponse> handlePersistenceException(
            PersistenceException ex,
            HttpServletRequest request) {
        
        log.error("Database error on {} {}", request.getMethod(), request.getRequestURI(), ex); 

        ErrorResponse error = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Internal Server Error",
            "Database error occurred: ",
            request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
    
    // ============================================
    // ECCEZIONI JSON/REQUEST BODY
    // ============================================
    
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {
        
        String message = "Malformed JSON request";
        
        // Gestione errori specifici di Jackson
        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException) {
            InvalidFormatException ife = (InvalidFormatException) cause;
            message = String.format(
                "Invalid value '%s' for field '%s'. Expected type: %s",
                ife.getValue(),
                ife.getPath().get(0).getFieldName(),
                ife.getTargetType().getSimpleName()
            );
        } else if (cause instanceof MismatchedInputException) {
            MismatchedInputException mie = (MismatchedInputException) cause;
            if (!mie.getPath().isEmpty()) {
                message = String.format(
                    "Missing or invalid field: '%s'",
                    mie.getPath().get(0).getFieldName()
                );
            }
        }
        
        ErrorResponse error = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Bad Request",
            message,
            request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    
    // ============================================
    // ECCEZIONI VALIDATION (@Valid)
    // ============================================
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        
        // Raccoglie tutti gli errori di validazione
        StringBuilder errors = new StringBuilder();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.append(error.getField())
                  .append(": ")
                  .append(error.getDefaultMessage())
                  .append("; ")
        );
        
        ErrorResponse error = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Validation Failed",
            errors.toString(),
            request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    
    // ============================================
    // ECCEZIONI SERVIZI ESTERNI
    // ============================================
    
    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<ErrorResponse> handleResourceAccess(
            ResourceAccessException ex,
            HttpServletRequest request) {
        
                log.error("External service unavaible:{}",request.getRequestURI(),ex);
        ErrorResponse error = new ErrorResponse(
            HttpStatus.BAD_GATEWAY.value(),
            "Bad Gateway",
            "External service unavailable: " ,
            request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(error);
    }


    
@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
public ResponseEntity<ErrorResponse> handleMethodNotSupported(
        HttpRequestMethodNotSupportedException ex,
        HttpServletRequest request) {

    ErrorResponse error = new ErrorResponse(
            HttpStatus.METHOD_NOT_ALLOWED.value(),
            "Method Not Allowed",
            ex.getMessage(),
            request.getRequestURI()
    );

    return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(error);
}
    
    // ============================================
    // ECCEZIONE GENERICA (CATCH-ALL)
    // ============================================
    
    //Gestisce gli update concorrenziali
  @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
  public ResponseEntity<ErrorResponse> handleOptimisticLock(
          ObjectOptimisticLockingFailureException ex,
          HttpServletRequest request) {

      log.warn("Optimistic lock conflict on {}", request.getRequestURI());

      ErrorResponse error = new ErrorResponse(
          HttpStatus.CONFLICT.value(),
          "Conflict",
          "The resource was modified by another user. Please refresh and try again.",
          request.getRequestURI()
      );

      return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
  }
  
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(
            Exception ex,
            HttpServletRequest request) {
        
       log.error("Unhandled exception on {} {}", request.getMethod(), request.getRequestURI(), ex);
        
         log.error("External service unavailable on {}", request.getRequestURI(), ex);
        ErrorResponse error = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Internal Server Error",
            "An unexpected error occurred: " ,
            request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}