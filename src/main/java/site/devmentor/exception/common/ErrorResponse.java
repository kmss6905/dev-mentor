package site.devmentor.exception.common;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {
  private final boolean success = false;
  private String message;

  private ErrorResponse(String message) {
    this.message = message;
  }
  public static ErrorResponse of(String message) {
    return new ErrorResponse(message);
  }
}
