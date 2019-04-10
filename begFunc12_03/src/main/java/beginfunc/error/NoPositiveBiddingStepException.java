package beginfunc.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = "Bidding step must be positive!")
public class NoPositiveBiddingStepException extends RuntimeException {

    private int statusCode;

    public NoPositiveBiddingStepException() {
        this.statusCode = 406;
    }

    public NoPositiveBiddingStepException(String message) {
        super(message);
        this.statusCode = 406;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
