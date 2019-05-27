package model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AuthResponse {
    @JsonProperty("two_factor_enabled")
    int twoFactor;
    @JsonProperty("access_token")
    String accessToken;
    @JsonProperty("default_load")
    Boolean defaultLoad;
}
