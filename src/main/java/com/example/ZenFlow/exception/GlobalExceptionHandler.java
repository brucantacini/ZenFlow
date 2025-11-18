package com.example.ZenFlow.exception;

import com.example.ZenFlow.dto.ErrorResponseDTO;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Hidden
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {
        
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleEntityNotFoundException(
            EntityNotFoundException ex, WebRequest request) {
        
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationException(
            MethodArgumentNotValidException ex, WebRequest request) {
        
        List<ErrorResponseDTO.FieldErrorDTO> fieldErrors = new ArrayList<>();
        
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.add(new ErrorResponseDTO.FieldErrorDTO(
                    fieldError.getField(),
                    fieldError.getDefaultMessage(),
                    fieldError.getRejectedValue()
            ));
        }
        
        ErrorResponseDTO error = new ErrorResponseDTO();
        error.setTimestamp(java.time.LocalDateTime.now());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setError("Validation Failed");
        error.setMessage("Erro de validação nos campos");
        error.setPath(request.getDescription(false).replace("uri=", ""));
        error.setFieldErrors(fieldErrors);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleConstraintViolationException(
            ConstraintViolationException ex, WebRequest request) {
        
        List<ErrorResponseDTO.FieldErrorDTO> fieldErrors = new ArrayList<>();
        
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String field = violation.getPropertyPath().toString();
            fieldErrors.add(new ErrorResponseDTO.FieldErrorDTO(
                    field,
                    violation.getMessage(),
                    violation.getInvalidValue()
            ));
        }
        
        ErrorResponseDTO error = new ErrorResponseDTO();
        error.setTimestamp(java.time.LocalDateTime.now());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setError("Validation Failed");
        error.setMessage("Erro de validação nos campos");
        error.setPath(request.getDescription(false).replace("uri=", ""));
        error.setFieldErrors(fieldErrors);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<ErrorResponseDTO> handlePropertyReferenceException(
            PropertyReferenceException ex, WebRequest request) {
        
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                "Campo de ordenação inválido: " + ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(
            Exception ex, WebRequest request) {
        
        // Em desenvolvimento, mostrar a mensagem real do erro
        String message = "Ocorreu um erro inesperado. Tente novamente mais tarde.";
        if (ex.getMessage() != null && !ex.getMessage().isEmpty()) {
            message = ex.getMessage();
        }
        
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                message,
                request.getDescription(false).replace("uri=", "")
        );
        
        // Log do erro completo
        System.err.println("=== ERRO 500 ===");
        System.err.println("Mensagem: " + ex.getMessage());
        System.err.println("Causa: " + (ex.getCause() != null ? ex.getCause().getMessage() : "N/A"));
        ex.printStackTrace();
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}

