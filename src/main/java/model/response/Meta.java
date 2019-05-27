package model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
class Meta {
    int code;
    @JsonProperty("init_time")
    Time initTime;
    @JsonProperty("response_time")
    Time responseTime;
}
