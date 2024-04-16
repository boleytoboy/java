package opendota.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.format.DateTimeParseException;

@RestControllerAdvice
@Slf4j
@Validated
public class ExceptionsHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({HttpClientErrorException.class, HttpMessageNotReadableException.class,
            MethodArgumentNotValidException.class, MissingServletRequestParameterException.class,
            ConstraintViolationException.class, JsonProcessingException.class,
            BadRequestErrorException.class,
            DateTimeParseException.class, IllegalArgumentException.class, InvalidDataException.class,
            MethodArgumentTypeMismatchException.class})
    public ErrorResponse handleIllegalArgumentException(Exception ex) {
        log.error("error 400");
        return new ErrorResponse("BAD REQUEST");
    }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoResourceFoundException.class)
    public ErrorResponse handlerFoundException(Exception ex) {
        log.error("error 404");
        return new ErrorResponse("NOT FOUND");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleExceptionServer(Exception ex) {
        log.error("error 500");
        return new ErrorResponse("INTERNAL_SERVER_ERROR");
    }
}
