package uspace.api.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorResponse {

    @JsonProperty("error")
    public final String error;

    @JsonProperty("message")
    public final String description;

    public ErrorResponse(String error, String description) {
        this.error = error;
        this.description = description;
    }
}
