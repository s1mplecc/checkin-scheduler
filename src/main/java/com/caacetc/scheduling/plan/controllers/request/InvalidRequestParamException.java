package com.caacetc.scheduling.plan.controllers.request;

public class InvalidRequestParamException extends RuntimeException {
    public InvalidRequestParamException(String message) {
        super(message);
    }

    public InvalidRequestParamException(String message, Throwable cause) {
        super(message, cause);
    }
}
