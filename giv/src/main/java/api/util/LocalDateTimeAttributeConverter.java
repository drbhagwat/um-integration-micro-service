package api.util;

import java.time.LocalDateTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import java.sql.Timestamp;

/**
 * Converts Java DateTime to (and from) database DateTime.
 *
 * @author : Dinesh Bhagwat
 * @version : 1.0
 * @since : 2019-04-15
 */
@Converter(autoApply = true)
public class LocalDateTimeAttributeConverter implements AttributeConverter<LocalDateTime, Timestamp> {
     
    @Override
    public Timestamp convertToDatabaseColumn(LocalDateTime locDateTime) {
        return locDateTime == null ? null : Timestamp.valueOf(locDateTime);
    }
 
    @Override
    public LocalDateTime convertToEntityAttribute(Timestamp sqlTimestamp) {
        return sqlTimestamp == null ? null : sqlTimestamp.toLocalDateTime();
    }
}
