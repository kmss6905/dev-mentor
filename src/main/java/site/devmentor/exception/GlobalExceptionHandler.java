package site.devmentor.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import site.devmentor.exception.common.ErrorResponse;
import site.devmentor.exception.mentor.MentorException;
import site.devmentor.exception.mentor.OverflowMentorRequestException;
import site.devmentor.exception.post.PostNotFoundException;

import java.text.ParseException;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
    return ResponseEntity.badRequest().body(ErrorResponse.of(ex.getMessage().split(": ")[1]));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationExceptions(
          MethodArgumentNotValidException ex) {
    String defaultMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
    return ResponseEntity.badRequest().body(ErrorResponse.of(defaultMessage));
  }

  @ExceptionHandler(DuplicateException.class)
  public ResponseEntity<ErrorResponse> handleDuplicationExceptions(DuplicateException ex) {
    return ResponseEntity.badRequest().body(ErrorResponse.of(ex.getMessage()));
  }

  @ExceptionHandler(UnauthorizedAccessException.class)
  public ResponseEntity<ErrorResponse> handleUnauthorizedAccessException(UnauthorizedAccessException ex) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse.of(ex.getMessage()));
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFoundException(ResourceNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ErrorResponse.of(ex.getMessage()));
  }

  @ExceptionHandler(MentorException.class)
  public ResponseEntity<ErrorResponse> handleNotFoundException(MentorException ex) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(ErrorResponse.of(ex.getMessage()));
  }

  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<ErrorResponse> handleNotFoundException(IllegalStateException ex) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(ErrorResponse.of(ex.getMessage()));
  }

  @ExceptionHandler(ParseException.class)
  public ResponseEntity<String> handleParseException(ParseException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("날짜 및 시간 형식이 올바르지 않습니다. yyyy-MM-dd hh:mm");
  }
}
