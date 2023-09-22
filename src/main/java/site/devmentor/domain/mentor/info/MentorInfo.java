package site.devmentor.domain.mentor.info;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import site.devmentor.domain.BaseEntity;
import site.devmentor.exception.mentor.OverflowMentorRequestException;

@Table(name = "MENTOR_INFO")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
@ToString
public class MentorInfo extends BaseEntity {

  @Column(name = "user_id")
  private long userId;

  @Column(name = "max_mentees")
  private long maxMentees;

  @Column(name = "current_mentees")
  private long currentMentees;

  @Builder
  private MentorInfo(final long userId, final long maxMentees, final long currentMentees) {
    this.userId = userId;
    this.maxMentees = maxMentees;
    this.currentMentees = currentMentees;
  }

  public long getCurrentMentees() {
    return currentMentees;
  }

  public void increaseMentee() {
    if (currentMentees >= maxMentees) {
      throw new OverflowMentorRequestException();
    }
    this.currentMentees = this.currentMentees + 1;
    log.info("this.currentMentees : {}", this.currentMentees);
  }
}
