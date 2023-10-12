package site.devmentor.application.review;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.devmentor.auth.AppUser;
import site.devmentor.domain.mentor.review.Review;
import site.devmentor.domain.mentor.schedule.Schedule;
import site.devmentor.domain.mentor.schedule.ScheduleRepository;
import site.devmentor.domain.user.User;
import site.devmentor.domain.user.UserRepository;
import site.devmentor.dto.mentor.review.ReviewRequest;
import site.devmentor.exception.UnauthorizedAccessException;
import site.devmentor.exception.review.ReviewAlreadyExistedException;
import site.devmentor.exception.schedule.ScheduleNotFoundException;
import site.devmentor.exception.user.UserNotFoundException;

@Service
public class ReviewService {
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    public ReviewService(ScheduleRepository scheduleRepository, UserRepository userRepository) {
        this.scheduleRepository = scheduleRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void createReview(AppUser appUser, ReviewRequest reviewRequest) {
        final User user = findUser(appUser);
        final Schedule schedule = findScheduleMenteeOf(reviewRequest.scheduleId(), user);
        if (schedule.hasReview()) {
            throw new ReviewAlreadyExistedException();
        }
        final Review review = reviewRequest.toReviewWithAuthor(user);
        schedule.addReview(review);
    }

    private User findUser(AppUser appUser) {
        return userRepository.findById(appUser.pid()).orElseThrow(() -> new UserNotFoundException(String.valueOf(appUser.pid())));
    }

    private Schedule findScheduleMenteeOf(long scheduleId, User user) {
        Schedule schedule = findSchedule(scheduleId);
        if (!schedule.isMenteeOf(user)) {
            throw new UnauthorizedAccessException();
        }
        return schedule;
    }

    private Schedule findSchedule(long scheduleId) {
        return scheduleRepository.findById(scheduleId)
            .orElseThrow(() -> new ScheduleNotFoundException(String.valueOf(scheduleId)));
    }
}
