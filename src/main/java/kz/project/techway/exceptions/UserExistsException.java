package kz.project.techway.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "User is exists")
public class UserExistsException extends RuntimeException{
    public UserExistsException(String message) {
        super(message);
    }
}
