package bauction.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Auction not found!")
public class AuctionNotFoundException extends RuntimeException {

    private int statusCode;

    public AuctionNotFoundException() {
        this.statusCode = 404;
    }

    public AuctionNotFoundException(String message) {
        super(message);
        this.statusCode = 404;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
