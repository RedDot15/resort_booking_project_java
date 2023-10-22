package t3h.bigproject.controller.backend;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import t3h.bigproject.controller.exception.ForbiddenException;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(value
            = {ForbiddenException.class, IllegalStateException.class})
    protected String forbiddenException(
            RuntimeException ex, WebRequest request) {
        return "/backend/403.html";
    }
}
