package bauction.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = "User already exists!")
public class DuplicatedUserException extends RuntimeException {

    private int statusCode;

    public DuplicatedUserException() {
        this.statusCode = 406;
    }

    public DuplicatedUserException(String message) {
        super(message);
        this.statusCode = 406;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
