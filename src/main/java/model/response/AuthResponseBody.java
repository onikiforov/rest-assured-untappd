package model.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AuthResponseBody extends CommonResponseBody {
    AuthResponse response;
}
