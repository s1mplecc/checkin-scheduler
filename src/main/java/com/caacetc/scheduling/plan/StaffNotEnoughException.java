package com.caacetc.scheduling.plan;

public class StaffNotEnoughException extends Exception {
    public StaffNotEnoughException(String message) {
        super(message);
    }

    public StaffNotEnoughException(String message, Throwable cause) {
        super(message, cause);
    }

    public StaffNotEnoughException() {
        super();
    }
}
