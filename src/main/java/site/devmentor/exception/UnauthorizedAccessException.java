package site.devmentor.exception;

public class UnauthorizedAccessException extends RuntimeException {
  public UnauthorizedAccessException() {
    super("Not authorized to this resource");
  }
}