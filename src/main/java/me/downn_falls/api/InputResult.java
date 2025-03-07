package me.downn_falls.api;

public class InputResult {
    public static InputResult SUCCESS = new InputResult(null, false);
    public static InputResult ERROR = new InputResult(null, true);
    public static InputResult success(String message) { return new InputResult(message, false); }
    public static InputResult error(String message) { return new InputResult(message, true); }

    private final String message;
    private final boolean error;

    private InputResult(String message, boolean error) {
        this.message = message;
        this.error = error;
    }

    public String getMessage() {
        return message;
    }
    public boolean isError() {
        return error;
    }
}
