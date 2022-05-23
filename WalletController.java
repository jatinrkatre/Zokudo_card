package com.cards.zokudo.controllers;

import com.cards.zokudo.services.card.getcarddetails.GetCardDetailsService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/api/v1/wallet")
public class WalletController {

    private final GetCardDetailsService getCardDetailsInf;

    @Autowired
    public WalletController(final GetCardDetailsService getCardDetailsInf) {
        this.getCardDetailsInf = getCardDetailsInf;
    }

    @ApiOperation(value = "Get Card Details by Proxy Number ", authorizations = {@Authorization("basicAuth")})
    @CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
    @GetMapping(value = "/getCardDetails")
    public Object getCardDetails(HttpServletRequest request) {
        String proxyNumber = request.getParameter("proxyNumber");
        log.info("Proxy Number - " + proxyNumber);
        return getCardDetailsInf.getCardsByProxyNumber(proxyNumber);
    }

    @ApiOperation(value = "Get Card Details by Proxy Number and Customer Hash ID ", authorizations = {@Authorization("basicAuth")})
    @CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
        @GetMapping(value = "/getCardDetailsByProxyAndCustomerHash")
    public Object getCardDetailsByProxyAndCustomerHash(HttpServletRequest request) {
        String proxyNumber = request.getParameter("proxyNumber");
        String customerHashId = request.getParameter("customerHashId");
        log.info("Proxy Number:{}, customerHashId:{} ",proxyNumber, customerHashId);
        return getCardDetailsInf.getCardsByProxyNumberAndCustomerHashId(proxyNumber, customerHashId);
    }

    @ApiOperation(value = "Get Card Details by Customer Hash ID ", authorizations = {@Authorization("basicAuth")})
    @CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
    @GetMapping(value = "/getCardDetailsByCustomerHash")
    public Object getCardDetailsByCustomerHash(HttpServletRequest request) {
        String customerHashId = request.getParameter("customerHashId");
        log.info("customerHashId:{} ", customerHashId);
        return getCardDetailsInf.getCardsByCustomerHashId(customerHashId);
    }

    @ApiOperation(value = "Get card details list by pagination", authorizations = {@Authorization("basicAuth")})
    @CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
    @GetMapping(value = "/getCardListByPagination")
    public Object getCardDetailsListByPagination(HttpServletRequest request) {
        return getCardDetailsInf.getCardsDetailsByPagination(request.getHeader("page"), request.getHeader("size"));
    }

    @ApiOperation(value = "Get card details list by pagination", authorizations = {@Authorization("basicAuth")})
    @CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
    @GetMapping(value = "/getAllProxyNumbers")
    public Object getAllProxyNumbers() {
        return getCardDetailsInf.getAllProxyNumbers();
    }

    @ApiOperation(value = "Get card details list by pagination", authorizations = {@Authorization("basicAuth")})
    @CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
    @GetMapping(value = "/getAllProxyNumbersByPagination")
    public Object getAllProxyNumbersByPagination(HttpServletRequest request) {
        return getCardDetailsInf.getAllProxyNumbersByPagination(request.getHeader("page"), request.getHeader("size"));
    }
}
