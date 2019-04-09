package beginfunc.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "No logged in user!")
public class NoLoggedUserException extends RuntimeException {

    private int statusCode;

    public NoLoggedUserException() {
        this.statusCode = 403;
    }

    public NoLoggedUserException(String message) {
        super(message);
        this.statusCode = 403;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
