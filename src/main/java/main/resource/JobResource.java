package main.resource;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobResource {

    private String name;
    private JobType type;
    private int interval;
    private Unit intervalUnit;
    private Date start;
    private Date end;
    private String content;

    public enum Unit {
        @JsonProperty("MILLIS")
        MILLIS,
        @JsonProperty("SECONDS")
        SECONDS,
        @JsonProperty("MINUTES")
        MINUTES,
        @JsonProperty("HOURS")
        HOURS;

        @JsonCreator
        public static Unit fromString(String string) {
            return Unit.valueOf(string);
        }
    }

}
