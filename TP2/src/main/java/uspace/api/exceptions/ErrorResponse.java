package uspace.api.exceptions;

public class ErrorResponse {
    private final String error;
    private final String description;

    public ErrorResponse(String error, String description) {
        this.error = error;
        this.description = description;
    }

    public String error() { return error; }
    public String description() { return description; }

    public String getError() { return error; }
    public String getDescription() { return description; }
}
