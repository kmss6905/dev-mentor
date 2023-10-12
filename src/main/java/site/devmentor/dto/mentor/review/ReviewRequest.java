package site.devmentor.dto.mentor.review;

import jakarta.validation.constraints.NotNull;
import site.devmentor.domain.mentor.review.Rate;
import site.devmentor.domain.mentor.review.Review;
import site.devmentor.domain.user.User;

public record ReviewRequest(
    @NotNull
    String content,

    @NotNull
    Rate rate,

    @NotNull
    Long scheduleId
) {
    public Review toReviewWithAuthor(User user) {
        return Review.builder()
            .rate(rate)
            .author(user)
            .content(content)
            .build();
    }
}
