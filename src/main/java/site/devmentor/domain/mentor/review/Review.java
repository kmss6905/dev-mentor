package site.devmentor.domain.mentor.review;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import site.devmentor.domain.BaseEntity;
import site.devmentor.domain.mentor.schedule.Schedule;
import site.devmentor.domain.user.User;

import java.util.Objects;

@Table(name = "MENTOR_REVIEWS")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class Review extends BaseEntity {

    private static final int MAX_REVIEW_LENGTH = 1000;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Column(name = "content", length = MAX_REVIEW_LENGTH)
    private String content;

    @Column(name = "rate", nullable = false)
    @Convert(converter=RateConverter.class)
    private Rate rate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", insertable = false, updatable = false, nullable = false)
    private Schedule schedule;

    @Builder
    private Review(final User author, final Rate rate, final String content) {
        validateRate(rate);
        this.author = author;
        this.rate = rate;
        if (isContentEmpty(content)) {
            this.content = content;
        }
    }

    private boolean isContentEmpty(String content) {
        return Objects.nonNull(content) && content.trim().isEmpty();
    }

    private void validateRate(Rate rate) {
        if (Objects.isNull(rate)) {
            throw new IllegalStateException();
        }
    }
}
