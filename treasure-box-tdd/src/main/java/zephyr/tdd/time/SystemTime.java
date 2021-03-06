package zephyr.tdd.time;

import java.util.Date;


public class SystemTime {

    private static final TimeSource defaultTimeSource = System::currentTimeMillis;
    private static TimeSource timeSource = null;

    public static long asMillis() {
        return getTimeSource().millis();
    }

    public static Date asDate() {
        return new Date(asMillis());
    }

    public static void reset() {
        setTimeSource(null);
    }

    private static TimeSource getTimeSource() {
        return (timeSource != null ? timeSource : defaultTimeSource);
    }

    public static void setTimeSource(TimeSource timeSource) {
        SystemTime.timeSource = timeSource;
    }
}
