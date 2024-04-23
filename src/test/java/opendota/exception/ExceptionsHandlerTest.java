package opendota.exception;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.NoHandlerFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExceptionsHandlerTest {

    @InjectMocks
    private ExceptionsHandler exceptionsHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void handleIllegalArgumentException_HttpClientErrorException_ShouldReturnBadRequestResponse() {
        // Arrange
        HttpClientErrorException ex = new HttpClientErrorException(HttpStatus.BAD_REQUEST);

        // Act
        ErrorResponse response = exceptionsHandler.handleIllegalArgumentException(ex);

        // Assert
        assertEquals("BAD REQUEST", response.getMessage());
    }

    @Test
    void handleIllegalArgumentException_ConstraintViolationException_ShouldReturnBadRequestResponse() {
        // Arrange
        ConstraintViolationException ex = new ConstraintViolationException("message", null, "constraint");

        // Act
        ErrorResponse response = exceptionsHandler.handleIllegalArgumentException(ex);

        // Assert
        assertEquals("BAD REQUEST", response.getMessage());
    }

    @Test
    void handlerFoundException_NoHandlerFoundException_ShouldReturnNotFoundResponse() {
        // Arrange
        NoHandlerFoundException ex = new NoHandlerFoundException("GET", "/url", null);

        // Act
        ErrorResponse response = exceptionsHandler.handlerFoundException(ex);

        // Assert
        assertEquals("NOT FOUND", response.getMessage());
    }

    @Test
    void handleExceptionServer_Exception_ShouldReturnInternalServerErrorResponse() {
        // Arrange
        Exception ex = new Exception();

        // Act
        ErrorResponse response = exceptionsHandler.handleExceptionServer(ex);

        // Assert
        assertEquals("INTERNAL_SERVER_ERROR", response.getMessage());
    }
}