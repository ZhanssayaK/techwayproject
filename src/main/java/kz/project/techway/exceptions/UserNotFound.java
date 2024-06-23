package kz.project.techway.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "User not found")
public class UserNotFound extends RuntimeException {
  private final String message;

  public UserNotFound(String message) {
    super(message);
    this.message = message;
  }
}
