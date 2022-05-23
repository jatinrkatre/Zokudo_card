/*
 * InstaRem Overseas Money Transfer.
 * https://www.instarem.com/en-in/
 *
 * Copyright (c) 2014-2019 InstaReM
 *
 * InstaRem is an acronym of Instant Remittance.
 * InstaRem Software is designed and developed to ease the Overseas Money Transfer.
 * It allows you to transfer your money overseas with minimum cost and time.
 *
 *
 * This file is licensed and cannot be accessed by outside InstaRem.
 * It can only be accessed and modified by the authorized InstaRem Technical Teams.
 * If any unauthorized, outside of the InstaRem, user found to be unlawfully modified
 * the content of this file,  will be prosecuted under the Copyright Act
 *
 */
package com.cards.zokudo.dto.response;

import org.springframework.http.HttpStatus;

public class ApiError {

    private HttpStatus status;
    private String message;
    private Object details;

    public ApiError(final HttpStatus status, final String message) {
        this.status = status;
        this.message = message;
    }

    public ApiError(HttpStatus status, String message, Object details) {
        this(status, message);
        this.details = details;
        this.message=message;
        this.status=status;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getDetails() {
        return details;
    }

    public void setDetails(Object details) {
        this.details = details;
    }
}
