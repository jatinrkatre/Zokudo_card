package com.cards.zokudo.controllers;

import com.cards.zokudo.dto.request.AppCardsRequestDTO;
import com.cards.zokudo.dto.response.ApiResponse;
import com.cards.zokudo.enums.ProgramPlans;
import com.cards.zokudo.exceptions.BizException;
import com.cards.zokudo.services.card.cardlistbasedonfilters.CardListBasedOnFiltersDTO;
import com.cards.zokudo.services.card.cardlistbasedonfilters.CardListBasedOnfilters;
import com.cards.zokudo.services.card.dashboardcount.GetDashboardCountService;
import com.cards.zokudo.services.card.debit.DebitInf;
import com.cards.zokudo.services.card.download.DownloadService;
import com.cards.zokudo.services.card.fetchbalance.FetchBalanceInf;
import com.cards.zokudo.services.card.generatecvv.GenerateCVVService;
import com.cards.zokudo.services.card.getcarddetails.GetCardDetailsService;
import com.cards.zokudo.services.card.load.LoadCardDto;
import com.cards.zokudo.services.card.load.LoadInf;
import com.cards.zokudo.services.card.lock.LockCardDto;
import com.cards.zokudo.services.card.lock.LockCardInf;
import com.cards.zokudo.services.card.notification.NotificationInf;
import com.cards.zokudo.services.card.persist.ActivateCardDto;
import com.cards.zokudo.services.card.persist.PersistInf;
import com.cards.zokudo.services.card.replacecard.ReplaceCardRequestDTO;
import com.cards.zokudo.services.card.replacecard.ReplaceCardService;
import com.cards.zokudo.services.card.setpin.SetPinDto;
import com.cards.zokudo.services.card.setpin.SetPinInf;
import com.cards.zokudo.services.card.showtransaction.ShowTransactionService;
import com.cards.zokudo.services.card.transaction.TransactionInf;
import com.cards.zokudo.services.card.transactionbasedonfilters.TransactionBasedOnFilters;
import com.cards.zokudo.services.card.transactionbasedonfilters.TransactionBasedOnFiltersDTO;
import com.cards.zokudo.services.card.updateEntity.UpdateEntityRequestDTO;
import com.cards.zokudo.services.card.updateEntity.UpdateEntityService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("{programUrl}/api/v1")
public class CardApiController {

    private final PersistInf persistInf;
    private final LoadInf loadInf;
    private final FetchBalanceInf fetchBalanceInf;
    private final TransactionInf transactionInf;
    private final DebitInf debitInf;
    private final LockCardInf lockCardInf;
    private final GenerateCVVService generateCVVService;
    private final ReplaceCardService replaceCardService;
    private final ShowTransactionService showTransactionService;
    private final UpdateEntityService updateEntityService;
    private final GetCardDetailsService getCardDetailsService;
    private final SetPinInf setPinInf;
    private final NotificationInf notificationInf;
    private final GetDashboardCountService getDashboardCountService;
    private final TransactionBasedOnFilters transactionBasedOnFilters;
    private final CardListBasedOnfilters cardListBasedOnfilters;
    private final DownloadService downloadService;

    @Autowired
    public CardApiController(PersistInf persistInf, LoadInf loadInf, FetchBalanceInf fetchBalanceInf,
                             TransactionInf transactionInf, DebitInf debitInf, LockCardInf lockCardInf,
                             GenerateCVVService generateCVVService, ReplaceCardService replaceCardService, ShowTransactionService showTransactionService, UpdateEntityService updateEntityService, GetCardDetailsService getCardDetailsService,
                             SetPinInf setPinInf, NotificationInf notificationInf,
                             GetCardDetailsService getCardDetailService, GetDashboardCountService getDashboardCountService, TransactionBasedOnFilters transactionBasedOnFilters,
                             CardListBasedOnfilters cardListBasedOnfilters, DownloadService downloadService) {
        this.persistInf = persistInf;
        this.loadInf = loadInf;
        this.fetchBalanceInf = fetchBalanceInf;
        this.transactionInf = transactionInf;
        this.debitInf = debitInf;
        this.lockCardInf = lockCardInf;
        this.generateCVVService = generateCVVService;
        this.replaceCardService = replaceCardService;
        this.showTransactionService = showTransactionService;
        this.updateEntityService = updateEntityService;
        this.getCardDetailsService = getCardDetailsService;
        this.setPinInf = setPinInf;
        this.notificationInf = notificationInf;
        this.getDashboardCountService = getDashboardCountService;
        this.transactionBasedOnFilters = transactionBasedOnFilters;
        this.cardListBasedOnfilters = cardListBasedOnfilters;
        this.downloadService = downloadService;
    }

    @ApiOperation(value = "Create Card and assign it to customer at processor(M2P) end", authorizations = {@Authorization("basicAuth")})
    @CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
    @PostMapping(value = "/createCard", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> persistCard(HttpServletRequest request, @PathVariable("programUrl") String programUrl,
                                      @RequestBody ActivateCardDto activateCardDto) {
        return persistInf.execute(request.getHeader("User-Hash-Id"), programUrl, activateCardDto, request.getHeader("loggedInUserHashId"));
    }

    @ApiOperation(value = "Create Card and assign it to customer at host end when wallet creation failed", authorizations = {@Authorization("basicAuth")})
    @CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
    @PostMapping(value = "/createCardForWalletFailed", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> createCard(HttpServletRequest request, @PathVariable("programUrl") String programUrl,
                                      @RequestBody ActivateCardDto activateCardDto) {
        return persistInf.executeCreateCardForWalletFailed(request.getHeader("User-Hash-Id"), programUrl, activateCardDto, request.getHeader("loggedInUserHashId"));
    }

    @ApiOperation(value = "Load Amount", authorizations = {@Authorization("basicAuth")})
    @CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
    @PostMapping(value = "/loadAmount", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> loadAmount(HttpServletRequest request, @PathVariable("programUrl") String programUrl,
                                     @RequestBody LoadCardDto loadCardDto, @PathVariable("clientCode") String clientHashId) {
        loadCardDto.setBulkCardLoadType("SINGLE_LOAD");
        return loadInf.execute((String) request.getHeader("User-Hash-Id"), (String) request.getHeader("Card-Hash-Id"),
                programUrl, loadCardDto, clientHashId, request.getHeader("loggedInUserHashId"), request.getHeader("role"));
    }

    @ApiOperation(value = "Debit Amount", authorizations = {@Authorization("basicAuth")})
    @CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
    @DeleteMapping(value = "/debitAmount", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> debitAmount(HttpServletRequest request, @PathVariable("programUrl") String programUrl,
                                      @RequestBody LoadCardDto loadCardDto) {
        return debitInf.execute(request, programUrl, loadCardDto);
    }

    @ApiOperation(value = "Fetch Balance", authorizations = {@Authorization("basicAuth")})
    @CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
    @GetMapping(value = "/fetchBalance", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> fetchBalance(HttpServletRequest request, @PathVariable("programUrl") String programUrl) {
        return fetchBalanceInf.execute(request, programUrl, request.getHeader("role"));
    }


    @ApiOperation(value = "Fetch Transaction", authorizations = {@Authorization("basicAuth")})
    @CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
    @GetMapping(value = "/fetchTransaction", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> fetchTransaction(HttpServletRequest request, @PathVariable("programUrl") String programUrl) {
        return transactionInf.execute(request, programUrl);
    }


    @ApiOperation(value = "Transaction By Id", authorizations = {@Authorization("basicAuth")})
    @CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
    @GetMapping(value = "/transactionById", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> fetchTransactionById(HttpServletRequest request, @PathVariable("programUrl") String programUrl) {
        return showTransactionService.execute(request, programUrl);
    }

    @ApiOperation(value = "Block Card", authorizations = {@Authorization("basicAuth")})
    @CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
    @PostMapping(value = "/blockCard", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> blockCard(HttpServletRequest request, @PathVariable("programUrl") String programUrl,
                                    @RequestBody LockCardDto lockCardDto) {
        return lockCardInf.execute(request, programUrl, lockCardDto);
    }

    /* @// TODO: 01/02/20 Need to be corrected. */
    @ApiOperation(value = "Generate CVV", authorizations = {@Authorization("basicAuth")})
    @CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
    @GetMapping(value = "/getCvv", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> getCVV(HttpServletRequest request, @PathVariable("programUrl") String programUrl) {
        return generateCVVService.execute(request, programUrl);
    }


    /*@ApiOperation(value = "Set PIN", authorizations = {@Authorization("basicAuth")})
    @CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
    @PostMapping(value = "/setPin", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> setPin(HttpServletRequest request, @PathVariable("programUrl") String programUrl,
                                 @RequestBody SetPinDto setPinDto) {
        return setPinInf.execute(request, programUrl, setPinDto);
    }*/

    @ApiOperation(value = "Card replacement", authorizations = {@Authorization("basicAuth")})
    @CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
    @PostMapping(value = "/replaceCard", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> replaceCard(HttpServletRequest request, @PathVariable("programUrl") String programUrl,
                                      @RequestBody ReplaceCardRequestDTO replaceCardRequestDTO) {
        return replaceCardService.execute(request, programUrl, replaceCardRequestDTO);
    }


    @ApiOperation(value = "Update customer entity", authorizations = {@Authorization("basicAuth")})
    @CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
    @PostMapping(value = "/updateEntity", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> updateEntity(HttpServletRequest request, @PathVariable("programUrl") String programUrl,
                                       @RequestBody UpdateEntityRequestDTO updateEntityRequestDTO) {
        return updateEntityService.execute(request, programUrl, updateEntityRequestDTO);
    }

    @ApiOperation(value = "Get card details list", authorizations = {@Authorization("basicAuth")})
    @CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
    @GetMapping(value = "/getCardList", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Object getCardDetailsList(HttpServletRequest request, @PathVariable("programUrl") String programUrl) {
        return getCardDetailsService.execute(programUrl,request.getHeader("dateRange") ,request.getHeader("role"), request.getHeader("page"), request.getHeader("size"), request.getHeader("loggedInUserHashId"));
    }

    @ApiOperation(value = "Get card List by CustomerId", authorizations = {@Authorization("basicAuth")})
    @CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
    @GetMapping(value = "/getCardListByCustomer", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Object getCardListByCustomer(HttpServletRequest request, @PathVariable("programUrl") String programUrl) {
        return getCardDetailsService.getCardListByCustomer(programUrl, request.getHeader("customer_hash_id"), request.getHeader("role"), request.getHeader("page"), request.getHeader("size"));
    }

    @ApiOperation(value = "Get cards by CustomerId", authorizations = {@Authorization("basicAuth")})
    @CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
    @GetMapping(value = "/getCardsByCustomer", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Object getCardsByCustomer(HttpServletRequest request, @PathVariable("programUrl") String programUrl) {
        return getCardDetailsService.getCardsByCustomer(programUrl, request.getHeader("customer_hash_id"), request.getHeader("role"));
    }

    @ApiOperation(value = "Get Pool account details", authorizations = {@Authorization("basicAuth")})
    @CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
    @GetMapping(value = "/getPoolAccount", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Object getPoolAccountDetails(HttpServletRequest request, @PathVariable("programUrl") String programUrl) {
        return getCardDetailsService.getPoolAccount(programUrl);
    }

    @ApiOperation(value = "get details of a specific card ", authorizations = {@Authorization("basicAuth")})
    @CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
    @GetMapping(value = "/getCardDetails", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Object getCardDetails(HttpServletRequest request, @PathVariable("programUrl") String programUrl) {
        return getCardDetailsService.execute(request, programUrl);
    }

    @ApiOperation(value = "Get program transactions", authorizations = {@Authorization("basicAuth")})
    @CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
    @PostMapping(value = "/getProgramTransactionBasedOnfilter", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Object getProgramTransactionsBasedOnfilters(HttpServletRequest request, @PathVariable("programUrl") String programUrl, @RequestBody TransactionBasedOnFiltersDTO transactionBasedOnFiltersDTO) {
        return transactionBasedOnFilters.execute(programUrl, transactionBasedOnFiltersDTO, request.getHeader("role"));
    }


    @ApiOperation(value = "Get dashboard count", authorizations = {@Authorization("basicAuth")})
    @CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
    @GetMapping(value = "/getDashboardCount", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Object getDashboardCounts(HttpServletRequest request, @PathVariable("programUrl") String programUrl) {
        return getDashboardCountService.execute(programUrl, request.getHeader("role"), request.getHeader("dateRange"), request.getHeader("loggedInUserHashId"));
    }

    @ApiOperation(value = "Get Card List Based On Filters", authorizations = {@Authorization("basicAuth")})
    @CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
    @PostMapping(value = "/getCardListBasedOnfilter", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Object getCardListBasedOnfilters(HttpServletRequest request, @PathVariable("programUrl") String programUrl, @RequestBody CardListBasedOnFiltersDTO cardListBasedOnFiltersDTO) {
        return cardListBasedOnfilters.execute(programUrl, cardListBasedOnFiltersDTO, request.getHeader("role"), request.getHeader("loggedInUserHashId"));
    }

    @ApiOperation(value = "download Transaction Report", authorizations = {@Authorization(value = "basicAuth")})
    @CrossOrigin(origins = {"*"}, allowCredentials = "true", allowedHeaders = "*", methods = RequestMethod.GET)
    @GetMapping(value = "/downloadTransactionReport", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void downloadTransactionReport(HttpServletRequest request, HttpServletResponse response,
                                          @PathVariable("programUrl") String programUrl,
                                          @RequestParam final Map<String, String> requestParams) throws Exception {
        try {
            downloadService.downloadTransactionReport(request, response, programUrl, requestParams);
        } catch (BizException e) {
            e.printStackTrace();
            response.sendRedirect(request.getHeader("Referer"));
        }
    }

    @ApiOperation(value = "download cards list", authorizations = {@Authorization(value = "basicAuth")})
    @CrossOrigin(origins = {"*"}, allowCredentials = "true", allowedHeaders = "*", methods = RequestMethod.GET)
    @GetMapping(value = "/downloadCardList", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void downloadCardList(HttpServletRequest request, HttpServletResponse response,
                                 @PathVariable("programUrl") String programUrl,
                                 @RequestParam final Map<String, String> requestParams) throws Exception {
        try {
            downloadService.downloadCardList(request, response, programUrl, requestParams);
        } catch (BizException e) {
            e.printStackTrace();
            response.sendRedirect(request.getHeader("Referer"));
        }
    }

    @ApiOperation(value = "Get transactions graph data", authorizations = {@Authorization("basicAuth")})
    @CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
    @PostMapping(value = "/getTransactionGraphData", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Object getTransactionGraphData(HttpServletRequest request, @PathVariable("programUrl") String programUrl, @RequestBody TransactionBasedOnFiltersDTO transactionBasedOnFiltersDTO) {
        return transactionBasedOnFilters.getDashboardGraphData(programUrl, transactionBasedOnFiltersDTO, request.getHeader("role"));
    }

    @ApiOperation(value = "Get Card graph data", authorizations = {@Authorization("basicAuth")})
    @CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
    @PostMapping(value = "/getCardGraphData", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Object getCardGraphData(HttpServletRequest request, @PathVariable("programUrl") String programUrl, @RequestBody CardListBasedOnFiltersDTO cardListBasedOnFiltersDTO) {
        return cardListBasedOnfilters.getCardGraphData(programUrl, cardListBasedOnFiltersDTO, request.getHeader("role"));
    }

    @ApiOperation(value = "Get Card Details Data", authorizations = {@Authorization("basicAuth")})
    @CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
    @GetMapping(value = "/fetchCardDetailsListByAgentHashId", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void fetchCardDetailsList(HttpServletRequest request, HttpServletResponse response) {
        getCardDetailsService.fetchCardDetailsByAgentHashId(request.getParameter("loggedInUserHashId"),request.getParameter("reportrange"), response);
    }

    @ApiOperation(value = "Get expiry Card List Based On Filters", authorizations = {@Authorization("basicAuth")})
    @CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
    @PostMapping(value = "/getExpiryCardListBasedOnfilter", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Object getExpiryCardListBasedOnfilters(HttpServletRequest request, @PathVariable("programUrl") String programUrl, @RequestBody CardListBasedOnFiltersDTO cardListBasedOnFiltersDTO) {
        return cardListBasedOnfilters.expiryExecute(programUrl, cardListBasedOnFiltersDTO, request.getHeader("role"), request.getHeader("loggedInUserHashId"));
    }

    @ApiOperation(value = "Get Kit Balance List Based On Filters", authorizations = {@Authorization("basicAuth")})
    @CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
    @PostMapping(value = "/getKitBalanceListOnfilter", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Object getKitBalanceListBasedOnfilters(HttpServletRequest request, @PathVariable("programUrl") String programUrl, @RequestBody CardListBasedOnFiltersDTO cardListBasedOnFiltersDTO) {
        return cardListBasedOnfilters.kitBalanceList(programUrl, cardListBasedOnFiltersDTO, request.getHeader("role"), request.getHeader("loggedInUserHashId"));
    }

    @ApiOperation(value = "download Kit Balance list", authorizations = {@Authorization(value = "basicAuth")})
    @CrossOrigin(origins = {"*"}, allowCredentials = "true", allowedHeaders = "*", methods = RequestMethod.GET)
    @GetMapping(value = "/downloadKitBalance", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void downloadCardKitBalance(HttpServletRequest request, HttpServletResponse response,
                                 @PathVariable("programUrl") String programUrl,
                                 @RequestParam final Map<String, String> requestParams) throws Exception {
        try {
            cardListBasedOnfilters.downloadCardKitBalance(request, response, programUrl, requestParams);
        } catch (BizException e) {
            e.printStackTrace();
            response.sendRedirect(request.getHeader("Referer"));
        }
    }

    @ApiOperation(value = "download cards list", authorizations = {@Authorization(value = "basicAuth")})
    @CrossOrigin(origins = {"*"}, allowCredentials = "true", allowedHeaders = "*", methods = RequestMethod.GET)
    @GetMapping(value = "/downloadExpiryCardList", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void downloadExpiryCardList(HttpServletRequest request, HttpServletResponse response,
                                 @PathVariable("programUrl") String programUrl,
                                 @RequestParam final Map<String, String> requestParams) throws Exception {
        try {
            downloadService.downloadExpiryCardList(request, response, programUrl, requestParams);
        } catch (BizException e) {
            e.printStackTrace();
            response.sendRedirect(request.getHeader("Referer"));
        }
    }

    @ApiOperation(value = "Get Card Details Data", authorizations = {@Authorization("basicAuth")})
    @CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
    @GetMapping(value = "/fetchCardDetailsListbyDateTimeAndByAgentHashId", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void fetchCardDetailsbyDateTimefilterList(HttpServletRequest request, HttpServletResponse response) {
        getCardDetailsService.fetchCardDetailsByAgentHashId(request.getParameter("loggedInUserHashId"),request.getParameter("reportrange"),
                request.getParameter("timerange"),response);
    }


    /*@ApiOperation(value = "GET ALL GPR CARDS FROM ALL PROGRAM FOR A CUSTOMER", authorizations = {@Authorization("basicAuth")})
    @CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
    @PostMapping(value = "/getAllGPRCardsByCustomer", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Object getAllGPRCardsByCustomer(HttpServletRequest request, @RequestBody AppCardsRequestDTO appCardsRequestDTO,
                                           @PathVariable("programUrl")final String programUrl) {
        return getCardDetailsService.getAllGPRCardsByCustomer(appCardsRequestDTO, ProgramPlans.DEFAULT,programUrl);
    }*/

    /*@ApiOperation(value = "GET ALL CARDS BY MOBILE AND PROGRAM ID", authorizations = {@Authorization("basicAuth")})
    @CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
    @PostMapping(value = "/getAllCardsByMobileAndProgramId", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Object getAllCardsByMobileAndProgramId(HttpServletRequest request, @RequestBody AppCardsRequestDTO appCardsRequestDTO) {
        return getCardDetailsService.getAllCardsByMobileAndProgramId(appCardsRequestDTO,"appuser");
    }*/

}
