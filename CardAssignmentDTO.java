package com.cards.zokudo.dto.request;

import lombok.Data;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CardAssignmentDTO {

    private long clientId;
    private long programId;
    private String businessId;
    private MultipartFile kitNumberFile;
    private boolean bulkUpload;
    private boolean startProcess;
    private String fileName;
    private String kitNumber;
    private JSONObject programDetails;
    private String cardType;
    private String expiryDate;
    private String programHashId;
    private String distributorId;
    private String retailerId;

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String cardNumber;
    private String ipin;
    private String cvv;
    private String clientName;
    private String productType;


}
