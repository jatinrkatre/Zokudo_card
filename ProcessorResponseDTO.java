package com.cards.zokudo.dto.response;

import lombok.Data;

@Data
public class ProcessorResponseDTO {

    private boolean success;
    private String maskedCardNumber;
    private String cardHashId;
    private String status;
    private String proxyNumber;
    private String rrn;
    private String autobots_txn_id;
    private double program_balance;
    private String customerBalance;
    private String cvv;
    private String  errorMesg;
    private String zaggleCardId;
    private String zaggleProductCode;
    
 //wallet amount load
    
    private String systemReferenceNumber;
    private String sourceAmount;
    private String currencyCode;
    private String destinationAmount;
    private Object paymentMethods;
    
    private String customerHashId;
    private Boolean isDefaultUpdated;



}
