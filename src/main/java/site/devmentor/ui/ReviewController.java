package site.devmentor.ui;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.devmentor.application.review.ReviewService;
import site.devmentor.auth.AppUser;
import site.devmentor.auth.LoginUser;
import site.devmentor.dto.mentor.review.ReviewRequest;

@RestController
@RequestMapping("/api/mentor/review")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<Void> createReview(
        @LoginUser AppUser appUser,
        @Valid @RequestBody ReviewRequest reviewRequest
    ) {
        reviewService.createReview(appUser, reviewRequest);
        return ResponseEntity.noContent().build();
    }
}
