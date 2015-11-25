package com.aiwsolutions.mongo.changes;

/**
 * Created by choang on 11/25/15.
 */
public class ChangeSetExecutionException extends Exception {
    private String error;

    public ChangeSetExecutionException(String error) {
        this.error = error;
    }

    public ChangeSetExecutionException(String error, Throwable cause) {
        super(error, cause);
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
