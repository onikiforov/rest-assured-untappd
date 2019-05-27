package model.response;

import lombok.Data;

import java.util.List;

@Data
public class ErrorResponseBody {
    ErrorMeta meta;
    List<Object> response;
}