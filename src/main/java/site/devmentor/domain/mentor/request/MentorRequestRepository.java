package site.devmentor.domain.mentor.request;

import org.springframework.data.jpa.repository.JpaRepository;
import site.devmentor.domain.mentor.request.MentorRequest;

public interface MentorRequestRepository extends JpaRepository<MentorRequest, Long> {
}
