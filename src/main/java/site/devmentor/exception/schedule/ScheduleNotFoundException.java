package site.devmentor.exception.schedule;

import site.devmentor.exception.ResourceNotFoundException;

public class ScheduleNotFoundException extends ResourceNotFoundException {
  public ScheduleNotFoundException(String id) {
    super("기존 스케줄을 찾을 수 없습니다. 스케줄 번호 : " + id);
  }
}
