package com.cards.zokudo.services.card.wallet;

import com.cards.zokudo.enums.BizErrors;
import com.cards.zokudo.enums.ProgramPlans;
import com.cards.zokudo.exceptions.BizException;
import com.cards.zokudo.util.Constants;
import com.cards.zokudo.util.SecurityUtil;
import com.cards.zokudo.util.UrlMetaData;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

@Service
@Slf4j
public class WalletServiceImpl implements WalletService {

    private final UrlMetaData urlMetaData;
    private final Client client;
    private final String applicationLevelUserName;
    private final String applicationLevelUserPassword;
    private final SecurityUtil securityUtil;

    @Autowired
    public WalletServiceImpl(@Qualifier(value = "client") Client client,
                             @Value("${applicationLevel.user.name}") String applicationLevelUserName,
                             @Value("${applicationLevel.user.password}") String applicationLevelUserPassword,
                             UrlMetaData urlMetaData, SecurityUtil securityUtil) {
        this.applicationLevelUserName = applicationLevelUserName;
        this.applicationLevelUserPassword = applicationLevelUserPassword;
        this.client = client;
        this.urlMetaData = urlMetaData;
        this.securityUtil = securityUtil;
    }

    @Override
    public long createWallet(final long customerId, final long clientId, final long programId, final String userHashId, final String programUrl, final String cardType, final ProgramPlans programPlans) {
        try {
            log.info("Wallet creation for : {} initiated...",customerId);
            final JSONObject requestParameters = new JSONObject();
            requestParameters.put("customerId", customerId);
            requestParameters.put("clientId", clientId);
            requestParameters.put("programId", programId);
            requestParameters.put("cardType", cardType);
            requestParameters.put("programPlan", programPlans.getValue());

            final MultivaluedMap<String, Object> headerMap = new MultivaluedHashMap<>();
            headerMap.add("Authorization", securityUtil.getAuthorizationHeader());
            headerMap.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
            headerMap.add("User-Hash-Id", userHashId);
            String str = urlMetaData.CREATE_WALLET.replaceAll(Constants.urlEscapeConstant, programUrl + "");

            Response clientResponse = client.target(str)
                    .request()
                    .headers(headerMap)
                    .post(Entity.entity(requestParameters.toString(), MediaType.APPLICATION_JSON_VALUE));

            String programDetails = clientResponse.readEntity(String.class);
            if (clientResponse.getStatus() != 200)
                throw new BizException(BizErrors.DATA_NOT_FOUND.getValue(), "wallet creation failed!");
            JSONObject jsonObject = new JSONObject(programDetails);

            long walletId = jsonObject.getJSONObject("body").getLong("walletId");
            log.info("Wallet creation with walletId: {} is successful for: {}",walletId, customerId);
            return walletId;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BizException(BizErrors.APPLICATION_ERROR.getValue(), "wallet creation failed");
        }
    }

    @Override
    public void addOnChannelLimit(final String customerHashId, final String programUrl, final String programPlans,
                                  String cardHashId,String proxyCardNo) {
        try {
            final MultivaluedMap<String, Object> headerMap = new MultivaluedHashMap<>();
            headerMap.add("Authorization", securityUtil.getAuthorizationHeader());
            headerMap.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
            headerMap.add("programPlan", programPlans);
            String strURl = urlMetaData.ADD_ON_UPDATE_LIMIT.replaceAll(Constants.urlEscapeConstant, programUrl + "");
            JSONObject channelLimitPayLoad = new JSONObject();
            channelLimitPayLoad.put("customerHashId",customerHashId);
            channelLimitPayLoad.put("cardHashId",cardHashId);
            channelLimitPayLoad.put("proxyCardNo",proxyCardNo);

            Response clientResponse = client.target(String.format(strURl, customerHashId))
                    .request()
                    .headers(headerMap)
                    .post(Entity.entity(channelLimitPayLoad.toString(), MediaType.APPLICATION_JSON_VALUE));

            String programDetails = clientResponse.readEntity(String.class);
            if (clientResponse.getStatus() != 200)
                log.error("Unable to update the channel limit");
//            JSONObject jsonObject = new JSONObject(programDetails);

        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("Unable to update the channel limit");
        }
    }

    @Override
    public boolean feeDeduction(final String feeType,
                                final long programId,
                                final String clientId,
                                final String programUrl,
                                final String proxyCardNumber,
                                final String customerHashId,
                                final String agentHashId,
                                final String distributorHashId,
                                final ProgramPlans programPlans,
                                final long walletId) {
        log.info("Calling fee deduction api");
        try {
            final JSONObject requestParameters = new JSONObject();
            requestParameters.put("feeType", feeType);
            requestParameters.put("programId", programId);
            requestParameters.put("clientId", clientId);
            requestParameters.put("proxyCardNumber", proxyCardNumber);
            requestParameters.put("customerHashId", customerHashId);
            requestParameters.put("agentHashId", agentHashId);
            requestParameters.put("distributorHashId", distributorHashId);
            requestParameters.put("programPlans", programPlans);
            requestParameters.put("walletId", walletId);

            final MultivaluedMap<String, Object> headerMap = new MultivaluedHashMap<>();
            headerMap.add("Authorization", securityUtil.getAuthorizationHeader());
            headerMap.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
            String str = urlMetaData.FEE_DEDUCTION.replaceAll(Constants.urlEscapeConstant, programUrl);

            Response clientResponse = client.target(String.format(str))
                    .request()
                    .headers(headerMap)
                    .post(Entity.entity(requestParameters.toString(), MediaType.APPLICATION_JSON_VALUE));

//            String programDetails = clientResponse.readEntity(String.class);
            if (clientResponse.getStatus() == 200)
                return true;
        } catch (Exception e) {
            log.error(e.getMessage());
//            throw new BizException(BizErrors.APPLICATION_ERROR.getValue(), "wallet creation failed");
            return false;
        }
        return false;
    }

    @Override
    public double fetchCardBalance(final String proxyCardNumber, final String programUrl) {
        try {
            final JSONObject requestParameters = new JSONObject();
            requestParameters.put("proxyCardNumber", proxyCardNumber);

            final MultivaluedMap<String, Object> headerMap = new MultivaluedHashMap<>();
            headerMap.add("Authorization", securityUtil.getAuthorizationHeader());
            headerMap.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);

            String str = urlMetaData.FETCH_CARD_BALANCE.replaceAll(Constants.urlEscapeConstant, programUrl + "");
            Response clientResponse = client.target(str)
                    .request()
                    .headers(headerMap)
                    .post(Entity.entity(requestParameters.toString(), MediaType.APPLICATION_JSON_VALUE));

            String responseStr = clientResponse.readEntity(String.class);
            if (clientResponse.getStatus() != 200)
                throw new BizException(BizErrors.DATA_NOT_FOUND.getValue(), "fetch card balance failure!");

            JSONObject jsonObject = new JSONObject(responseStr);
            return jsonObject.getDouble("details");
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BizException(BizErrors.APPLICATION_ERROR.getValue(), "fetch card balance failure!");
        }
    }

    @Override
    public JSONObject fetchCardBalanceByMccAndWalletId(final String proxyCardNumber, final String mccCode, final long walletId, final JSONObject program) {
        try {
            final JSONObject requestParameters = new JSONObject();
            requestParameters.put("proxyCardNumber", proxyCardNumber);
            requestParameters.put("walletId", walletId);
            requestParameters.put("mccCode", mccCode);
            requestParameters.put("clientId", program.getString("clientId"));
            requestParameters.put("programId", program.getString("id"));

            final MultivaluedMap<String, Object> headerMap = new MultivaluedHashMap<>();
            headerMap.add("Authorization", securityUtil.getAuthorizationHeader());
            headerMap.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);

            String str = urlMetaData.FETCH_CARD_BALANCE_BY_MCC_WALLET_ID.replaceAll(Constants.urlEscapeConstant, program.getString("hostUrl") + "");
            Response clientResponse = client.target(str)
                    .request()
                    .headers(headerMap)
                    .post(Entity.entity(requestParameters.toString(), MediaType.APPLICATION_JSON_VALUE));

            String responseStr = clientResponse.readEntity(String.class);
            if (clientResponse.getStatus() != 200)
                throw new BizException(BizErrors.DATA_NOT_FOUND.getValue(), "fetch card balance failure!");

            JSONObject jsonObject = new JSONObject(responseStr);
            return jsonObject.getJSONObject("details");
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BizException(BizErrors.APPLICATION_ERROR.getValue(), "fetch card balance failure!");
        }
    }

    @Override
    public double fetchKitBalance(Long walletId, String programUrl){
        log.info("inside fetchKitBalance method");
        try{
            final JSONObject requestParameters = new JSONObject();
            requestParameters.put("walletId", walletId);


            final MultivaluedMap<String, Object> headerMap = new MultivaluedHashMap<>();
            headerMap.add("Authorization", securityUtil.getAuthorizationHeader());
            headerMap.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);

            String str = urlMetaData.FETCH_KIT_BALANCE.replaceAll(Constants.urlEscapeConstant, programUrl + "");
            Response clientResponse = client.target(str)
                    .request()
                    .headers(headerMap)
                    .post(Entity.entity(requestParameters.toString(), MediaType.APPLICATION_JSON_VALUE));

            String responseStr = clientResponse.readEntity(String.class);
            if (clientResponse.getStatus() != 200)
                throw new BizException(BizErrors.DATA_NOT_FOUND.getValue(), "fetch kit balance failure!");

            JSONObject jsonObject = new JSONObject(responseStr);
            return jsonObject.getDouble("details");
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BizException(BizErrors.APPLICATION_ERROR.getValue(), "fetch kit balance failure!");
        }

    }
}
