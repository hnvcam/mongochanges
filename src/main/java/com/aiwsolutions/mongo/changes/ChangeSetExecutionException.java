package com.aiwsolutions.mongo.changes;

/**
 * Created by choang on 11/25/15.
 */
public class ChangeSetExecutionException extends RuntimeException {
    private String name;
    private String error;

    public ChangeSetExecutionException(String name, String error) {
        this.name = name;
        this.error = error;
    }

    public ChangeSetExecutionException(String name, String error, Throwable cause) {
        super(error, cause);
        this.name = name;
        this.error = error;
    }

    public String getName() {
        return name;
    }

    public String getError() {
        return error;
    }
}
