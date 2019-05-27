package utils;

import java.time.Instant;

public class DateUtils {
    public static long getCurrentTimeStampSeconds() {
        Instant instant = Instant.now();
        return instant.getEpochSecond();
    }
}
