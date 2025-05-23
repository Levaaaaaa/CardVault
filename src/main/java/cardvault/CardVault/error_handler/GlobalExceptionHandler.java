package cardvault.CardVault.error_handler;

import cardvault.CardVault.error_handler.dto.ErrorDTO;
import cardvault.CardVault.error_handler.error_factory.ErrorFactory;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private ErrorFactory errorFactory;

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ErrorDTO accessDeniedException(AccessDeniedException e, WebRequest request) {
        return buildErrorMessage(e);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ErrorDTO entityNotFoundException(EntityNotFoundException e) {
        return buildErrorMessage(e);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(EntityExistsException.class)
    public ErrorDTO entityAlreadyExistsException(EntityExistsException e) {
        return buildErrorMessage(e);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<ErrorDTO> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        return e.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .map(this::buildErrorMessage)
                .toList();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public List<ErrorDTO> constrainViolationException(ConstraintViolationException e) {
        return e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .map(this::buildErrorMessage)
                .toList();
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(BadCredentialsException.class)
    public ErrorDTO badCredentialsException(BadCredentialsException e) {
        return buildErrorMessage(e);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalStateException.class)
    public ErrorDTO illegalStateException(IllegalStateException e) {
        return buildErrorMessage(e);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConversionFailedException.class)
    public ErrorDTO conversionFailedException(ConversionFailedException e) {
        return buildErrorMessage("ERROR_CODE_29");
    }
    private ErrorDTO buildErrorMessage(Exception e) {
        return new ErrorDTO(errorFactory.buildError(e.getMessage()).getDescription());
    }

    private ErrorDTO buildErrorMessage(String s) {
        return new ErrorDTO(errorFactory.buildError(s).getDescription());
    }
}
