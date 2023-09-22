package site.devmentor.domain.mentor.info;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface MentorInfoRepository extends JpaRepository<MentorInfo, Long> {

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("select m from MentorInfo m where m.userId = :userId")
  MentorInfo findMentorInfoByUserId(long userId);

  @Query("select m from MentorInfo m where m.userId = :userId")
  MentorInfo findByUserId(long userId);
}
