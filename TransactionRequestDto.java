package com.cards.zokudo.dto.request;

import com.cards.zokudo.enums.Services;
import lombok.Data;

@Data
public class TransactionRequestDto {

    private String clientTxnId;

    private String autobotsTxnId;

    private boolean debit;

    private String programHashId;

    private String clientHashId;

    private String walletHashId;

    private String cardHashId;

    private String balanceTypeAtProcessor;

    private Double txnAmount;

    private Double balanceAfterTxn;

    private double profit;

    private Services serviceCode;

    private String shortDescription;

    private String processorCode;

}
