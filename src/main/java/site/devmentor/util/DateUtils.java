package site.devmentor.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils{

    public static synchronized String toStandardDateFormat(LocalDateTime localDateTime){
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

}
