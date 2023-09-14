package site.devmentor.exception.post;

public class PostNotFoundException extends RuntimeException{
  public PostNotFoundException(String message) {
    super(message);
  }
}
