package model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ErrorMeta {
    int code;
    @JsonProperty("error_detail")
    String errorDetail;
    @JsonProperty("developer_friendly")
    String DeveloperFriendly;
    @JsonProperty("error_type")
    String errorType;
    @JsonProperty("response_time")
    Time responseTime;
}
