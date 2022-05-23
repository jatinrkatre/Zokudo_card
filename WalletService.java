package com.cards.zokudo.services.card.wallet;

import com.cards.zokudo.enums.ProgramPlans;
import org.codehaus.jettison.json.JSONObject;

public interface WalletService {

    long createWallet(long customerId, long clientId, long programId, String userHashId, String programUrl, String cardType, ProgramPlans programPlan);

    void addOnChannelLimit(String customerHashId, String programUrl, String programPlans,String cardHashId,String proxyCardNo);

    boolean feeDeduction(String feeType, long programId, String clientId, String programUrl, String proxyCardNumber, String customerHashId, String agentHashId, String distributorHashId, ProgramPlans programPlans, long walletId);

    double fetchCardBalance(String proxyCardNumber, String programUrl);

    double fetchKitBalance(Long walletId, String programUrl);

    JSONObject fetchCardBalanceByMccAndWalletId(String proxyCardNumber,String mccCode,long walletId, JSONObject programUrl);

}
