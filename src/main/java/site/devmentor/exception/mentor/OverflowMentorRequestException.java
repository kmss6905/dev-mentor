package site.devmentor.exception.mentor;

public class OverflowMentorRequestException extends RuntimeException{

  public OverflowMentorRequestException() {
    super("더 이상 멘토 신청을 할 수 없습니다.");
  }
}
