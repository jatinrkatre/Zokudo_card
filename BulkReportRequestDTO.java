package com.cards.zokudo.dto.request;

import lombok.Data;

@Data
public class BulkReportRequestDTO {

    String dateRange;
    String fileName;
    String kitNumber;
    String cardType;
    String userHashId;
    String programHashId;
}
