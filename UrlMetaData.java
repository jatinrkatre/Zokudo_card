
package com.cards.zokudo.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UrlMetaData {

    private static final String dynamicUrl = "{}/api/";
    public final String AUTHENTICATE_AND_AUTHORIZE_USER;
    public final String PROGRAM_DETAILS;
    public final String CLIENT_DETAILS_BY_ID;
    public final String PROGRAM_DETAILS_BY_ID;
    public final String CUSTOMER_BY_ID;
    public final String UPDATE_CUSTOMER;
    public final String WALLET_DETAILS;
    public final String UPDATE_PROGRAM_BALANCE;
    public final String CUSTOMER_BY_PROGRAM;
    public final String CUSTOMER_COUNTS;
    public final String PROGRAM_BALANCE_SUM;
    public final String COMMERCIAL_BY_PROGRAM;
    public final String PROGRAM_DETAILS_BY_HASH_ID;
    public final String GET_CUSTOMER_BY_EMAIL;
    public final String GET_CUSTOMER_BY_MOBILE_AND_PROGRAMPLAN;
    public final String GET_CUSTOMER_BY_MOBILE_AND_PROGRAM_ID;
    public final String CHECK_BULK_LOAD_LIMIT;
    public final String WALLET_LOAD_AMOUNT;
    public final String CHECK_CUST_KYC_LIMIT;
    public final String CREATE_WALLET;
    public final String CHECK_BULK_VALIDATION_AND_SAVE_RECORD;
    public final String ADD_ON_UPDATE_LIMIT;
    public final String FEE_DEDUCTION;
    public final String DETAIL_BY_HASH_ID;
    public final String GET_CHANNEL_DETAILS;
    public final String FETCH_CARD_BALANCE;
    public final String GET_CUSTOMER_BY_MOBILE;
    public final String UPDATE_DEFAULT_ENTRY_TXN;
    public final String UPDATE_CARD_STATUS_AT_SOR;
    public final String FETCH_CARD_BALANCE_BY_MCC_WALLET_ID;
    public final String FETCH_KIT_BALANCE;

    public UrlMetaData(@Value(value = "${version}") String version,
                       @Value(value = "${url.context.authcontext}") String AUTH_CONTEXT,
                       @Value("${url.context.productcontext}") String PRODUCT_CONTEXT,
                       @Value("${url.context.customercontext}") String CUSTOMER_CONTEXT,
                       @Value("${url.context.walletcontext}") String WALLET_CONTEXT,
                       @Value("${url.context.sorcontext}") String SOR_CONTEXT) {

        AUTH_CONTEXT = AUTH_CONTEXT + version;
        //PRODUCT_CONTEXT = PRODUCT_CONTEXT + dynamicUrl + version;

        this.CUSTOMER_BY_ID = CUSTOMER_CONTEXT + dynamicUrl + version + "/customerById";
        this.AUTHENTICATE_AND_AUTHORIZE_USER = AUTH_CONTEXT + "/authentication/authrequest";
        this.PROGRAM_DETAILS = PRODUCT_CONTEXT + dynamicUrl + version + "/product/programDetails";
        this.WALLET_DETAILS = WALLET_CONTEXT + dynamicUrl + version + "/getWallet";
        this.UPDATE_PROGRAM_BALANCE = PRODUCT_CONTEXT + dynamicUrl + version + "/product/updateProgramBalance";
        this.UPDATE_CUSTOMER = PRODUCT_CONTEXT + dynamicUrl + version + "/updateCustomer";
        this.PROGRAM_DETAILS_BY_ID = PRODUCT_CONTEXT + dynamicUrl + version + "/product/{}/programById";
        this.CUSTOMER_BY_PROGRAM = CUSTOMER_CONTEXT + dynamicUrl + version + "/fetchCustomerByProgram";
        this.CUSTOMER_COUNTS = CUSTOMER_CONTEXT + dynamicUrl + version + "/customerCounts";
        this.PROGRAM_BALANCE_SUM = PRODUCT_CONTEXT + dynamicUrl + version + "/product/getProgramBalance";
        this.COMMERCIAL_BY_PROGRAM = PRODUCT_CONTEXT + dynamicUrl + version + "/commercial/getCommercialByProgramId";
        this.PROGRAM_DETAILS_BY_HASH_ID = PRODUCT_CONTEXT + dynamicUrl + version + "/product/programByHashId";
        this.GET_CUSTOMER_BY_EMAIL = CUSTOMER_CONTEXT + dynamicUrl + version + "/getCustomerByEmail";
        this.GET_CUSTOMER_BY_MOBILE_AND_PROGRAMPLAN = CUSTOMER_CONTEXT + dynamicUrl + version + "/getCustomerByMobileAndProgramPlan";
        this.CHECK_BULK_LOAD_LIMIT = WALLET_CONTEXT + dynamicUrl + version + "/client/";
        this.WALLET_LOAD_AMOUNT = WALLET_CONTEXT + dynamicUrl + version + "/loadWallet";
        this.CHECK_CUST_KYC_LIMIT = WALLET_CONTEXT + dynamicUrl + version + "/client/";
        this.CREATE_WALLET = WALLET_CONTEXT + dynamicUrl + version + "/createWallet";
        this.CHECK_BULK_VALIDATION_AND_SAVE_RECORD = WALLET_CONTEXT + dynamicUrl + version + "/client/";
        this.ADD_ON_UPDATE_LIMIT = WALLET_CONTEXT + dynamicUrl + version + "/channel/customer/addOnUpdateLimit";
        this.FEE_DEDUCTION = WALLET_CONTEXT + dynamicUrl + version + "/feemanagement/DebitFee";
        this.CLIENT_DETAILS_BY_ID = PRODUCT_CONTEXT + dynamicUrl + version + "/client/getClientById";
        this.DETAIL_BY_HASH_ID = PRODUCT_CONTEXT + dynamicUrl + version + "/client/customerOnboardingData"; // this method has been used in customer service also
        this.GET_CHANNEL_DETAILS = PRODUCT_CONTEXT + "/product/getChannelDetails";
        this.FETCH_CARD_BALANCE = WALLET_CONTEXT + dynamicUrl + version + "/fetchCardBalance";
        this.GET_CUSTOMER_BY_MOBILE = CUSTOMER_CONTEXT + dynamicUrl + version + "/customer/getCustomersByMobNumber";
        this.UPDATE_DEFAULT_ENTRY_TXN = WALLET_CONTEXT+ dynamicUrl + version +"/loadCard/setDefaultValue";
        this.UPDATE_CARD_STATUS_AT_SOR = SOR_CONTEXT + "mss/api/" + version +"/cards/updateStatus";
        this.GET_CUSTOMER_BY_MOBILE_AND_PROGRAM_ID = CUSTOMER_CONTEXT + dynamicUrl + version + "/getCustomerByMobileAndProgramId";
        this.FETCH_CARD_BALANCE_BY_MCC_WALLET_ID = WALLET_CONTEXT+ dynamicUrl + version +"/fetchCardBalanceByMccAndWalletId";
        this.FETCH_KIT_BALANCE = WALLET_CONTEXT + dynamicUrl + version + "/fetchKitBalance";
    }
}
