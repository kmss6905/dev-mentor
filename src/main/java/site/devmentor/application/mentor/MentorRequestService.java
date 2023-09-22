package site.devmentor.application.mentor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import site.devmentor.auth.AuthenticatedUser;
import site.devmentor.domain.mentor.info.MentorInfo;
import site.devmentor.domain.mentor.info.MentorInfoRepository;
import site.devmentor.domain.mentor.request.MentorRequest;
import site.devmentor.domain.mentor.request.MentorRequestRepository;
import site.devmentor.domain.user.UserRepository;
import site.devmentor.dto.mentor.MentorRequestDto;
import site.devmentor.exception.ResourceNotFoundException;

@Service
public class MentorRequestService {

  private final UserRepository userRepository;
  private final MentorRequestRepository mentorRequestRepository;

  private final MentorInfoRepository mentorInfoRepository;

  public MentorRequestService(UserRepository userRepository, MentorRequestRepository mentorRequestRepository, MentorInfoRepository mentorInfoRepository) {
    this.userRepository = userRepository;
    this.mentorRequestRepository = mentorRequestRepository;
    this.mentorInfoRepository = mentorInfoRepository;
  }

  @Transactional
  public void request(AuthenticatedUser authUser, MentorRequestDto mentorRequestDto) {
    MentorInfo mentorInfo = findMentorInfo(mentorRequestDto);
    MentorRequest mentorRequest = MentorRequest.create(authUser, mentorRequestDto);
    saveMentorRequest(mentorRequest);
    mentorInfo.increaseMentee();
    mentorInfoRepository.save(mentorInfo);
  }

  private void saveMentorRequest(MentorRequest mentorRequest) {
    mentorRequestRepository.save(mentorRequest);
  }

  private MentorInfo findMentorInfo(MentorRequestDto mentorRequestDto) {
    return mentorInfoRepository.findMentorInfoByUserId(mentorRequestDto.mentorUserId());
  }
}