package com.cards.zokudo.dto.response;

import lombok.Data;

@Data
public class KycCheckErrorResponse {

    private boolean valid;
    private String errorMessage;
}
