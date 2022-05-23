package com.cards.zokudo.util;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EquitasUrlMetaData {

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
    public final String SET_CARD_PREFERENCES;

    public static final String dynamicUrl = "{}/api/";

    public EquitasUrlMetaData(@Value(value = "${version}") String version,
                              @Value("${url.context.equitascontext}") String EQUITAS_CONTEXT) {
        this.EQUITAS_CONTEXT = EQUITAS_CONTEXT;
        this.ADD_CUSTOMER = EQUITAS_CONTEXT + dynamicUrl + version + "/addCustomer";
        this.LOAD_WALLET = EQUITAS_CONTEXT + dynamicUrl + version + "/loadWallet";
        this.DEBIT_WALLET = EQUITAS_CONTEXT + dynamicUrl + version + "/debitWallet";
        this.FETCH_BALANCE = EQUITAS_CONTEXT + dynamicUrl + version + "/fetchBalance";
        this.FETCH_TRANSACTION = EQUITAS_CONTEXT + dynamicUrl + version + "/fetchTransaction";
        this.FETCH_TRANSACTION_BY_ID = EQUITAS_CONTEXT + dynamicUrl + version + "/fetchTransactionById";
        this.ADD_CARD = EQUITAS_CONTEXT + dynamicUrl + version + "/addCard";
        this.LOCK_UNLOCK_CARD = EQUITAS_CONTEXT + dynamicUrl + version + "/lockUnlock";
        this.REPLACE_CARD = EQUITAS_CONTEXT + dynamicUrl + version + "/replaceCard";
        this.SHOW_TRANSACTION = EQUITAS_CONTEXT + dynamicUrl + version + "/ShowTransaction";
        this.UPDATE_ENTITY = EQUITAS_CONTEXT + dynamicUrl + version + "/updateEntity";
        this.GET_CARD_DETAILS = EQUITAS_CONTEXT + dynamicUrl + version + "/cardDetails";
        this.PIN_SET = EQUITAS_CONTEXT + dynamicUrl + version + "/pinSet";
        this.GET_CVV = EQUITAS_CONTEXT + dynamicUrl + version + "/getCvv";
        this.SET_CARD_PREFERENCES = EQUITAS_CONTEXT + dynamicUrl + version+"/setCardPreferences";
    }

}


