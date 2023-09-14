package site.devmentor.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class DuplicateException extends RuntimeException{
  private final HttpStatus status = HttpStatus.BAD_REQUEST;
  public DuplicateException(String message) {
    super(message);
  }

  public HttpStatus getStatus() {
    return status;
  }
}
