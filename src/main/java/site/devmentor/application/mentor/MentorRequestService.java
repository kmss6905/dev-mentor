package site.devmentor.application.mentor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.devmentor.auth.AuthenticatedUser;
import site.devmentor.domain.mentor.info.MentorInfo;
import site.devmentor.domain.mentor.info.MentorInfoRepository;
import site.devmentor.domain.mentor.request.MentorRequest;
import site.devmentor.domain.mentor.request.MentorRequestRepository;
import site.devmentor.domain.user.UserRepository;
import site.devmentor.dto.mentor.MentorRequestDto;
import site.devmentor.dto.mentor.MentorRequestStatusDto;
import site.devmentor.exception.ResourceNotFoundException;

@Service
public class MentorRequestService {

  private final MentorRequestRepository mentorRequestRepository;

  private final MentorInfoRepository mentorInfoRepository;

  public MentorRequestService(MentorRequestRepository mentorRequestRepository, MentorInfoRepository mentorInfoRepository) {
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

  @Transactional
  public void delete(AuthenticatedUser authUser, long id) {
    MentorRequest mentorRequest = findMentorRequest(id);
    mentorRequest.verifyCanDelete(authUser);
    mentorRequestRepository.deleteById(id);
  }

  private MentorRequest findMentorRequest(long id) {
    return mentorRequestRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("can't find mentor request, id=" + id));
  }

  @Transactional
  public void update(AuthenticatedUser authUser, MentorRequestStatusDto requestStatus, long id) {
    MentorRequest mentorRequest = findMentorRequest(id);
    mentorRequest.changeStatus(authUser, requestStatus);
  }
}
