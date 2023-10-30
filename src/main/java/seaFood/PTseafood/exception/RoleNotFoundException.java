package seaFood.PTseafood.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND)
public class RoleNotFoundException extends RuntimeException{
    public RoleNotFoundException(String message, String roleName) {
        super(message + roleName);
    }
}
