package com.cards.zokudo.exceptions;

import org.springframework.core.NestedRuntimeException;

public class BadRequestException extends NestedRuntimeException {

    private static final long serialVersionUID = 1L;
    private String errorMessage;
    private String additionalMessage;
    private String status;


    public BadRequestException(String errorMessage, String additionalMessage) {
        super(additionalMessage);
        this.errorMessage = errorMessage;
        this.additionalMessage = additionalMessage;
    }

    public BadRequestException(String additionalMessage) {
        super(additionalMessage);
        this.additionalMessage = additionalMessage;
    }


    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getAdditionalMessage() {
        return additionalMessage;
    }

    public void setAdditionalMessage(String additionalMessage) {
        this.additionalMessage = additionalMessage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
