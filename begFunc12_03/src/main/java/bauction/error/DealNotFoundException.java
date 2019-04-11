package bauction.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Deal not found!")
public class DealNotFoundException extends RuntimeException {

    private int statusCode;

    public DealNotFoundException() {
        this.statusCode = 404;
    }

    public DealNotFoundException(String message) {
        super(message);
        this.statusCode = 404;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
