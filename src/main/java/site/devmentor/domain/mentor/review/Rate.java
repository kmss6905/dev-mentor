package site.devmentor.domain.mentor.review;

public enum Rate {
    ZERO_POINT_FIVE(0.5),
    ONE(1.0),
    ONE_POINT_FIVE(1.5),
    TWO(2.0),
    TWO_POINT_FIVE(2.5),
    THREE(3.0),
    THREE_POINT_FIVE(3.5),
    FOUR(4.0),
    FOUR_POINT_FIVE(4.5),
    FIVE(5.0);

    private final double value;

    Rate(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }
}