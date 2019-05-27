package model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CreateWishListResponse {
    String method;
    @JsonProperty("list_id")
    int listId;
    @JsonProperty("list_name")
    String listName;
    String results;
    String key;
}
