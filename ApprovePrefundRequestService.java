package com.cards.zokudo.services.card.approverequest;

import com.cards.zokudo.dto.response.ApiResponse;


import javax.servlet.http.HttpServletRequest;

public interface ApprovePrefundRequestService {
    ApiResponse<?> execute(HttpServletRequest request, String programUrl, ApprovePrefundRequest approvePrefundRequest);

}

