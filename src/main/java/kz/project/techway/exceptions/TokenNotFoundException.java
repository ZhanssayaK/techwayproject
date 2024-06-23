package kz.project.techway.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Token not found")
public class TokenNotFoundException extends RuntimeException {
  private final String message;

  public TokenNotFoundException(String message) {
    super(message);
    this.message = message;
  }
}
