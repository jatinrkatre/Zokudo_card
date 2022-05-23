package com.cards.zokudo.controllers;

import com.cards.zokudo.dto.request.AppCardsRequestDTO;
import com.cards.zokudo.dto.response.ApiResponse;
import com.cards.zokudo.enums.ProgramPlans;
import com.cards.zokudo.services.card.generatecvv.GenerateCVVService;
import com.cards.zokudo.services.card.getcarddetails.GetCardDetailsService;
import com.cards.zokudo.services.card.lock.LockCardDto;
import com.cards.zokudo.services.card.lock.LockCardInf;
import com.cards.zokudo.services.card.setpin.SetPinDto;
import com.cards.zokudo.services.card.setpin.SetPinInf;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("{programUrl}/appuser/api/v1")
public class LendingAppCardsAPIController {

    private final GetCardDetailsService getCardDetailsService;
    private final LockCardInf lockCardInf;
    private final SetPinInf setPinInf;
    private final GenerateCVVService generateCVVService;

    @Autowired
    public LendingAppCardsAPIController(final GetCardDetailsService getCardDetailsService,
                                 final LockCardInf lockCardInf,
                                 final SetPinInf setPinInf,
                                 final GenerateCVVService generateCVVService) {

        this.getCardDetailsService = getCardDetailsService;
        this.lockCardInf = lockCardInf;
        this.setPinInf = setPinInf;
        this.generateCVVService = generateCVVService;
    }

    @ApiOperation(value = "GET CARDS FROM ALL PROGRAM FOR A CUSTOMER", authorizations = {@Authorization("basicAuth")})
    @CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
    @PostMapping(value = "/getAllCardsByCustomer", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Object getAllProgramCardsByCustomer(HttpServletRequest request, @RequestBody AppCardsRequestDTO appCardsRequestDTO,
                                               @PathVariable("programUrl") final String programUrl) {
        return getCardDetailsService.getAllProgramCardsByCustomer(appCardsRequestDTO, ProgramPlans.LENDING,programUrl);
    }

    @ApiOperation(value = "Block Card", authorizations = {@Authorization("basicAuth")})
    @CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
    @PostMapping(value = "/lockUnlockCard", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> lockUnlockCard(HttpServletRequest request,
                                         @RequestBody LockCardDto lockCardDto,
                                         @PathVariable("programUrl")final String programUrl) {
        return lockCardInf.lockUnlockCard(request, lockCardDto,programUrl);
    }

    @ApiOperation(value = "Set PIN", authorizations = {@Authorization("basicAuth")})
    @CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
    @PostMapping(value = "/setPin", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> setPin(HttpServletRequest request, @RequestBody SetPinDto setPinDto,
                                 @PathVariable("programUrl")final String programUrl) {
        return setPinInf.setPin(request, setPinDto,programUrl);
    }

    @ApiOperation(value = "FETCH CARD NUMBER & CVV FROM PROCESSOR", authorizations = {@Authorization("basicAuth")})
    @CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
    @PostMapping(value = "/fetchCardDetails", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> fetchCardDetails(@RequestBody AppCardsRequestDTO appCardsRequestDTO,
                                           @PathVariable("programUrl")final String programUrl) {
        return getCardDetailsService.fetchCardDetails(appCardsRequestDTO,programUrl);
    }

    @ApiOperation(value = "FETCH CARD NUMBER & CVV FROM PROCESSOR", authorizations = {@Authorization("basicAuth")})
    @CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
    @PostMapping(value = "/fetchCardDetailsByKit", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> fetchCardDetailsByKit(@RequestBody AppCardsRequestDTO appCardsRequestDTO,
                                                @PathVariable("programUrl")final String programUrl) {
        return getCardDetailsService.fetchCardDetailsByKit(appCardsRequestDTO,programUrl);
    }

    @ApiOperation(value = "GET CARDS FROM ALL PROGRAM FOR A CUSTOMER", authorizations = {@Authorization("basicAuth")})
    @CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
    @PostMapping(value = "/getAllCardsWithBalanceByCustomer", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Object getAllCardsWithBalanceByCustomer(HttpServletRequest request, @RequestBody AppCardsRequestDTO appCardsRequestDTO,
                                                   @PathVariable("programUrl")String programUrl) {
        return getCardDetailsService.getAllCardsWithBalanceByCustomer(appCardsRequestDTO, ProgramPlans.DEFAULT,programUrl);
    }
}
