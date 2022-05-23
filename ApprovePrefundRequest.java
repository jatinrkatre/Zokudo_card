package com.cards.zokudo.services.card.approverequest;

import com.cards.zokudo.enums.Status;
import lombok.Data;

@Data
public class ApprovePrefundRequest {

    private String remarks;

    private long id;

    private Status flag;

}
