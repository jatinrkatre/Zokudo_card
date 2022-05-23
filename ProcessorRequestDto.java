package com.cards.zokudo.dto.request;

import com.cards.zokudo.entities.Cards;
import com.cards.zokudo.enums.ProgramPlans;
import lombok.Data;
import org.codehaus.jettison.json.JSONObject;

@Data
public class ProcessorRequestDto {
    private JSONObject customerDetails;
    private JSONObject walletDetails;
    private JSONObject programDetails;
    private JSONObject cardDetails;
    private JSONObject commercialDetails;

    private Cards cards;
    private ProgramPlans programPlans;

    // Build Processor Request

    private String kitNumber;
    private String maskedCardNumber;
    private double amount;
    private String clientTransactionId;
    private String zokudoTransactionId;
    private String shortDescription;
    private String cardHashId;
    private String customerHashId;
    private String programUrl;
    private String cardType;
    private String expiryDate;
    private double cardPrice;
    private double programBalance;
    private String fromDate;
    private String toDate;
    private String perPage;
    private String pageNo;
    private String zaggleCardId;
    private String businessId;
    private long walletId;
    private boolean programHasDistributor;
    private String agentHashId;
    private String agentName;
    private String agentCompanyName;
    private String distributorHashId;
    private long agentId;

    private String startDate;
    private String endDate;
}
