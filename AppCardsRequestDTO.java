package com.cards.zokudo.dto.request;

import lombok.Data;

@Data
public class AppCardsRequestDTO {
    String mobile;
    String cardHashId;
    String programId;
    String clientId;
    String walletId;
    String customerHashId;
    String hostUrl;
    String mcc;
    String kitNumber;
    Long programIdOfProgramURL;
}
