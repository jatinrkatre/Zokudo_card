package com.cards.zokudo.services.card.approverequest;


import com.cards.zokudo.dto.response.ApiResponse;
import com.cards.zokudo.entities.PrefundRequest;
import com.cards.zokudo.entities.Transactions;
import com.cards.zokudo.enums.BizErrors;
import com.cards.zokudo.enums.Services;
import com.cards.zokudo.enums.Status;
import com.cards.zokudo.exceptions.BizException;
import com.cards.zokudo.repositories.PrefundRequestRepository;
import com.cards.zokudo.repositories.TransactionsRepository;
import com.cards.zokudo.services.card.load.LoadImpl;
import com.cards.zokudo.services.card.notification.NotificationImpl;
import com.cards.zokudo.services.card.notification.SMSAlertImpl;
import com.cards.zokudo.services.card.persist.PersistImpl;
import com.cards.zokudo.templates.email.EmailTemplates;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;


@Slf4j
@Service
public class ApprovePrefundRequestServiceImpl implements ApprovePrefundRequestService {

    private final PrefundRequestRepository prefundRequestRepository;
    private final PersistImpl persist;
    private final TransactionsRepository transactionsRepository;
    private final LoadImpl loadImpl;
    private final SMSAlertImpl smsAlert;
    private final EmailTemplates emailTemplates;


    public ApprovePrefundRequestServiceImpl(final PrefundRequestRepository prefundRequestRepository,
                                            final PersistImpl persist,
                                            final TransactionsRepository transactionsRepository,
                                            final LoadImpl loadImpl,
                                            final SMSAlertImpl smsAlert,
                                            final EmailTemplates emailTemplates) {
        this.prefundRequestRepository = prefundRequestRepository;
        this.persist = persist;
        this.transactionsRepository = transactionsRepository;
        this.loadImpl = loadImpl;
        this.smsAlert = smsAlert;
        this.emailTemplates = emailTemplates;
    }

    @Override
    public ApiResponse<?> execute(HttpServletRequest request, String programUrl, ApprovePrefundRequest approvePrefundRequest) {


        PrefundRequest prefundRequest = prefundRequestRepository.findById(approvePrefundRequest.getId());

        JSONObject programDetails = persist.getProgramByHashId(prefundRequest.getProgramHashId());

        if (prefundRequest == null)
            throw new BizException(BizErrors.APPLICATION_ERROR.getValue(), "Prefund Request Should Not Be Null");

        if (!Status.PENDING.equals(prefundRequest.getStatus()))
            throw new BizException(BizErrors.APPLICATION_ERROR.getValue(), "Prefund Request Already Updated");

        if (approvePrefundRequest.getFlag().equals(Status.APPROVED)) {
            try {
                Transactions transactions = programPrefundCreditTransaction(prefundRequest, programDetails);

                double currentBalance = programBalance(transactions, prefundRequest);

                updateTransactionStatus(transactions, Status.SUCCESS, currentBalance);

                prefundRequest.setStatus(Status.APPROVED);
                prefundRequest.setAutobotsReferenceNumber(transactions.getAutobotsTxnId());
                prefundRequest.setApprovalRemarks(approvePrefundRequest.getRemarks());

                prefundRequestRepository.save(prefundRequest);


            } catch (JSONException e) {
                log.error(e.getMessage());
            }
        } else {
            prefundRequest.setStatus(Status.REJECTED);
            prefundRequest.setApprovalRemarks(approvePrefundRequest.getRemarks());
            prefundRequestRepository.save(prefundRequest);
        }

        sendEmailWithRemarks(prefundRequest);


        return ApiResponse.builder().build();

    }

    private void updateTransactionStatus(Transactions transactions, Status status, double currentBalance) {
        transactions.setStatus(status);
        transactions.setBalanceAfterTxn(currentBalance);
        transactionsRepository.save(transactions);
    }

    private Transactions programPrefundCreditTransaction(PrefundRequest prefundRequest, JSONObject programDetails) {
        try {
            Transactions transaction = new Transactions();
            transaction.setAutobotsTxnId(Services.PREFUND_REQUEST.toString() + System.currentTimeMillis());
            transaction.setBalanceAfterTxn(programDetails.getDouble("balance"));
            transaction.setBalanceTypeAtProcessor(programDetails.getJSONObject("corporateProcessor").getJSONObject("cardProcessor").getString("balanceTypeAtProcessor"));
            transaction.setDebit(false);
            transaction.setClientTxnId(prefundRequest.getReferenceNumber());
            transaction.setProcessorCode(programDetails.getJSONObject("corporateProcessor").getJSONObject("cardProcessor").getString("code"));
            transaction.setProfit(0);
            transaction.setProgramHashId(programDetails.getString("programHashId"));
            transaction.setClientHashId(prefundRequest.getClientHashId());
            transaction.setServiceCode(Services.PREFUND_REQUEST);
            transaction.setShortDescription(prefundRequest.getClientRemarks());
            transaction.setTxnAmount(prefundRequest.getAmount());
            transaction.setStatus(Status.PENDING);
            transaction.setSettled(false);
            transactionsRepository.save(transaction);
            return transaction;
        } catch (JSONException e) {
            throw new BizException(BizErrors.APPLICATION_ERROR.getValue(), "transaction can be initiate!please try later");
        }
    }

    private void sendEmailWithRemarks(PrefundRequest prefundRequest) {
        try {

            String body = getBody(prefundRequest);
            String[] email = {prefundRequest.getRequestEmail()};
            smsAlert.sendEmail(email, "Prefund Request Status", body);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getBody(PrefundRequest prefundRequest) {
        return emailTemplates.getApprovedRemarks(prefundRequest);
    }

    private double programBalance(Transactions transactions, PrefundRequest prefundRequest) throws JSONException {
        try {
            JSONObject programDetails = persist.getProgramByHashId(prefundRequest.getProgramHashId());
            double amount = transactions.getTxnAmount() + programDetails.getDouble("balance");
            loadImpl.updateProgramBalance(amount, programDetails.getString("programHashId"), programDetails.getString("hostUrl"));
            return amount;
        } catch (Exception e) {
            updateTransactionStatus(transactions, Status.FAILURE, transactions.getBalanceAfterTxn());
            throw new BizException(BizErrors.APPLICATION_ERROR.getValue(), "error!while updating program balance");
        }
    }


}
