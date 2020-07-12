package api.core.errors;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@SuppressWarnings({"unchecked", "rawtypes"})
@Data
@NoArgsConstructor
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
  @Value("${GIV_EXCEPTION}")
  private String givException;

  @Value("${VALIDATION_FAILED}")
  private String validationFailed;

  @ExceptionHandler(Exception.class)
  public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
    List<String> details = new ArrayList<>();
    details.add(ex.getLocalizedMessage());
    ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), givException, details);
    return new ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST);
  }

  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                HttpHeaders headers, HttpStatus status,
                                                                WebRequest request) {
    List<String> details = new ArrayList<>();

    for (ObjectError error : ex.getBindingResult().getAllErrors()) {
      details.add(error.getDefaultMessage());
    }
    ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), validationFailed, details);
    return new ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST);
  }
}
