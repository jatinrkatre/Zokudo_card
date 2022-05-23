package com.cards.zokudo.dto.request;

import org.springframework.web.multipart.MultipartFile;

import com.cards.zokudo.enums.Status;

import lombok.Data;

@Data
public class CardInventoryDTO {

    private MultipartFile file;
    private Long programId;
    private String orginalFileName;

    private String fileName;
    
    private Long kit;
    private String maskedNumber;
    private String expiryDate;
    private Status status;
    
    private Long assignedCount;
    private Long unassignedCount;
    private Long totalCount;
    private String dateOfCreation;
    
    // UI variables
    private String date;
    private String cardStatus;
    
    
}
