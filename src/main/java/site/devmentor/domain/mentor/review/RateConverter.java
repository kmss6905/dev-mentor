package site.devmentor.domain.mentor.review;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RateConverter implements AttributeConverter<Rate, Double> {

    @Override
    public Double convertToDatabaseColumn(Rate rate) {
        return rate.getValue();
    }

    @Override
    public Rate convertToEntityAttribute(Double value) {
        if (value == 0.5) {
            return Rate.ZERO_POINT_FIVE;
        } else if (value == 1.0) {
            return Rate.ONE;
        } else if (value == 1.5) {
            return Rate.ONE_POINT_FIVE;
        } else if (value == 2.0) {
            return Rate.TWO;
        } else if (value == 2.5) {
            return Rate.TWO_POINT_FIVE;
        } else if (value == 3.0) {
            return Rate.THREE;
        } else if (value == 3.5) {
            return Rate.THREE_POINT_FIVE;
        } else if (value == 4.0) {
            return Rate.FOUR;
        } else if (value == 4.5) {
            return Rate.FOUR_POINT_FIVE;
        } else if (value == 5.0) {
            return Rate.FIVE;
        } else {
            return null;
        }
    }
}