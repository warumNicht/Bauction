package beginfunc.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = "Offered price must be positive!")
public class NoPositiveOfferException extends RuntimeException {

    private int statusCode;

    public NoPositiveOfferException() {
        this.statusCode = 406;
    }

    public NoPositiveOfferException(String message) {
        super(message);
        this.statusCode = 406;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
