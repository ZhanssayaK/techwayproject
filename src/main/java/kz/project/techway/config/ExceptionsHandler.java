package kz.project.techway.config;


import kz.project.techway.exceptions.TokenNotFoundException;
import kz.project.techway.exceptions.UserNotFound;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionsHandler {
  @ExceptionHandler(TokenNotFoundException.class)
  public ResponseEntity<?> handleNotFoundException(TokenNotFoundException exception) {
    return ResponseEntity.badRequest().body(exception.getMessage());
  }

  @ExceptionHandler(UserNotFound.class)
  public ResponseEntity<?> handleUserNotFoundException(UserNotFound exception) {
    return ResponseEntity.badRequest().body(exception.getMessage());
  }
}