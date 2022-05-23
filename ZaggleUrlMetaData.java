package com.cards.zokudo.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ZaggleUrlMetaData {

    public final String REPLACE_CARD;
    public final String LOCK_UNLOCK_CARD;
    public final String EQUITAS_CONTEXT;
    public final String ADD_CUSTOMER;
    public final String ADD_CARD;
    public final String LOAD_WALLET;
    public final String DEBIT_WALLET;
    public final String FETCH_BALANCE;
    public final String FETCH_TRANSACTION;
    public final String FETCH_TRANSACTION_BY_ID;
    public final String SHOW_TRANSACTION;
    public final String UPDATE_ENTITY;
    public final String GET_CARD_DETAILS;
    public final String PIN_SET;
    public final String GET_CVV;
    public final String UPDATE_KYC;
    public static final String dynamicUrl = "{}/api/";

    public ZaggleUrlMetaData(@Value(value = "${version}") String version,
                             @Value("${url.context.zagglecontext}") String ZAGGLE_CONTEXT) {
        this.EQUITAS_CONTEXT = ZAGGLE_CONTEXT;
        this.ADD_CUSTOMER = ZAGGLE_CONTEXT + dynamicUrl + version + "/createEmployee";
        this.LOAD_WALLET = ZAGGLE_CONTEXT + dynamicUrl + version + "/loadAmount";
        this.DEBIT_WALLET = ZAGGLE_CONTEXT + dynamicUrl + version + "/debitWallet";
        this.FETCH_BALANCE = ZAGGLE_CONTEXT + dynamicUrl + version + "/fetchBalance";
        this.FETCH_TRANSACTION = ZAGGLE_CONTEXT + dynamicUrl + version + "/transaction";
        this.FETCH_TRANSACTION_BY_ID = ZAGGLE_CONTEXT + dynamicUrl + version + "/fetchTransactionById";
        this.ADD_CARD = ZAGGLE_CONTEXT + dynamicUrl + version + "/addCard";
        this.UPDATE_KYC = ZAGGLE_CONTEXT + dynamicUrl + version + "/updateKyc";
        this.LOCK_UNLOCK_CARD = ZAGGLE_CONTEXT + dynamicUrl + version + "/blockUnblockCard";
        this.REPLACE_CARD = ZAGGLE_CONTEXT + dynamicUrl + version + "/replaceCard";
        this.SHOW_TRANSACTION = ZAGGLE_CONTEXT + dynamicUrl + version + "/ShowTransaction";
        this.UPDATE_ENTITY = ZAGGLE_CONTEXT + dynamicUrl + version + "/updateEntity";
        this.GET_CARD_DETAILS = ZAGGLE_CONTEXT + dynamicUrl + version + "/cardDetails";
        this.PIN_SET = ZAGGLE_CONTEXT + dynamicUrl + version + "/resetPin";
        this.GET_CVV = ZAGGLE_CONTEXT + dynamicUrl + version + "/getCvv";
    }

}
