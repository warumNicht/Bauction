package bauction.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = "Category already exists!")
public class DuplicatedCategoryException extends RuntimeException {

    private int statusCode;

    public DuplicatedCategoryException() {
        this.statusCode = 406;
    }

    public DuplicatedCategoryException(String message) {
        super(message);
        this.statusCode = 406;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
