package com.cards.zokudo.controllers;


import com.cards.zokudo.dto.request.AppCardsRequestDTO;
import com.cards.zokudo.dto.response.ApiResponse;
import com.cards.zokudo.enums.ProgramPlans;
import com.cards.zokudo.services.card.generatecvv.GenerateCVVService;
import com.cards.zokudo.services.card.getcarddetails.GetCardDetailsService;
import com.cards.zokudo.services.card.lock.LockCardDto;
import com.cards.zokudo.services.card.lock.LockCardInf;
import com.cards.zokudo.services.card.persist.ActivateCardDto;
import com.cards.zokudo.services.card.persist.PersistInf;
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
@RequestMapping("{programUrl}/api/v1")
public class AppCardsApiController {

    private final GetCardDetailsService getCardDetailsService;
    private final LockCardInf lockCardInf;
    private final SetPinInf setPinInf;
    private final GenerateCVVService generateCVVService;
    private final PersistInf persistInf;
    private final String PROGRAM_URL = "appuser";

    @Autowired
    public AppCardsApiController(final GetCardDetailsService getCardDetailsService,
                                 final LockCardInf lockCardInf,
                                 final SetPinInf setPinInf,
                                 final GenerateCVVService generateCVVService,
                                 final PersistInf persistInf) {
        this.getCardDetailsService = getCardDetailsService;
        this.lockCardInf = lockCardInf;
        this.setPinInf = setPinInf;
        this.generateCVVService = generateCVVService;
        this.persistInf = persistInf;
    }

    @ApiOperation(value = "Create Card and assign it to customer at processor(M2P) end for Zokudo B2C program", authorizations = {@Authorization("basicAuth")})
    @CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
    @PostMapping(value = "/createB2cCard", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> createB2cCard(@RequestBody ActivateCardDto activateCardDto) {
        return persistInf.createB2cCard(activateCardDto);
    }

    @ApiOperation(value = "GET CARDS FROM ALL PROGRAM FOR A CUSTOMER", authorizations = {@Authorization("basicAuth")})
    @CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
    @PostMapping(value = "/getAllCardsByCustomer", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Object getAllProgramCardsByCustomer(HttpServletRequest request, @RequestBody AppCardsRequestDTO appCardsRequestDTO,
                                               @PathVariable("programUrl")String programUrl) {
        return getCardDetailsService.getAllProgramCardsByCustomer(appCardsRequestDTO, ProgramPlans.DEFAULT,programUrl);
    }

    @ApiOperation(value = "GET ALL GPR CARDS FROM ALL PROGRAM FOR A CUSTOMER", authorizations = {@Authorization("basicAuth")})
    @CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
    @PostMapping(value = "/getAllGPRCardsByCustomer", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Object getAllGPRCardsByCustomer(HttpServletRequest request, @RequestBody AppCardsRequestDTO appCardsRequestDTO) {
        return getCardDetailsService.getAllGPRCardsByCustomer(appCardsRequestDTO, ProgramPlans.DEFAULT,PROGRAM_URL);
    }

    @ApiOperation(value = "GET ALL CARDS BY MOBILE AND PROGRAM ID", authorizations = {@Authorization("basicAuth")})
    @CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
    @PostMapping(value = "/getAllCardsByMobileAndProgramId", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Object getAllCardsByMobileAndProgramId(HttpServletRequest request, @RequestBody AppCardsRequestDTO appCardsRequestDTO,
                                                  @PathVariable("programUrl")String programUrl) {
        return getCardDetailsService.getAllCardsByMobileAndProgramId(appCardsRequestDTO,programUrl);
    }

    @ApiOperation(value = "Block Card", authorizations = {@Authorization("basicAuth")})
    @CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
    @PostMapping(value = "/lockUnlockCard", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> lockUnlockCard(HttpServletRequest request,
                                    @RequestBody LockCardDto lockCardDto) {
        return lockCardInf.lockUnlockCard(request, lockCardDto,PROGRAM_URL);
    }

    @ApiOperation(value = "Set PIN", authorizations = {@Authorization("basicAuth")})
    @CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
    @PostMapping(value = "/setPin", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> setPin(HttpServletRequest request, @RequestBody SetPinDto setPinDto) {
        return setPinInf.setPin(request, setPinDto,PROGRAM_URL);
    }

    @ApiOperation(value = "FETCH CARD NUMBER & CVV FROM PROCESSOR", authorizations = {@Authorization("basicAuth")})
    @CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
    @PostMapping(value = "/fetchCardDetails", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> fetchCardDetails(@RequestBody AppCardsRequestDTO appCardsRequestDTO) {
        return getCardDetailsService.fetchCardDetails(appCardsRequestDTO,PROGRAM_URL);
    }

    @ApiOperation(value = "GET CARDS FROM ALL PROGRAM FOR A CUSTOMER", authorizations = {@Authorization("basicAuth")})
    @CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
    @PostMapping(value = "/getAllCardsWithBalanceByCustomer", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Object getAllCardsWithBalanceByCustomer(HttpServletRequest request, @RequestBody AppCardsRequestDTO appCardsRequestDTO,
                                                   @PathVariable("programUrl")final String programUrl) {
        return getCardDetailsService.getAllCardsWithBalanceByCustomer(appCardsRequestDTO, ProgramPlans.DEFAULT,programUrl);
    }

    @ApiOperation(value = "FETCH CARD NUMBER & CVV FROM PROCESSOR", authorizations = {@Authorization("basicAuth")})
    @CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
    @PostMapping(value = "/fetchCardDetailsByKit", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> fetchCardDetailsByKit(@RequestBody AppCardsRequestDTO appCardsRequestDTO) {
        return getCardDetailsService.fetchCardDetailsByKit(appCardsRequestDTO,PROGRAM_URL);
    }
}
