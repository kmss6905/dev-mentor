package site.devmentor.exception.schedule;

import site.devmentor.exception.ResourceNotFoundException;

public class ScheduleDetailNotFoundException extends ResourceNotFoundException {
  public ScheduleDetailNotFoundException(String message) {
    super(message);
  }
}
