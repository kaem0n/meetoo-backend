package kaem0n.meetoo.exceptions;

import lombok.Getter;
import org.springframework.validation.ObjectError;

import java.util.List;

@Getter
public class BadRequestException extends RuntimeException {
    private List<ObjectError> errorList;

    public BadRequestException(String msg) {
        super(msg);
    }

    public BadRequestException(List<ObjectError> errorList) {
        super("Error: invalid payload.");
        this.errorList = errorList;
    }
}
