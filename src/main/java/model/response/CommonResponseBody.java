package model.response;

import lombok.Data;

import java.util.List;

@Data
abstract class CommonResponseBody {
    Meta meta;
    List<Object> notifications;
}
