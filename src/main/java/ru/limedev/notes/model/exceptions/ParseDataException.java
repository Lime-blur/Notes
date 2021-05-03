package ru.limedev.notes.model.exceptions;

import androidx.annotation.NonNull;

public class ParseDataException extends Exception {

    private String reason;

    public ParseDataException() {
        super();
    }

    public ParseDataException(Exception exception, String reason) {
        super(exception);
        this.reason = reason;
    }

    public ParseDataException(String cause, String reason) {
        super(cause);
        this.reason = reason;
    }

    public ParseDataException(String reason) {
        super();
        this.reason = reason;
    }

    @NonNull
    public String toString() {
        String message = getMessage();
        if (message != null && reason != null) {
            return getMessage() + ": " + reason;
        }
        return "Error during parse data!";
    }
}
