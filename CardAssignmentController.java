package com.cards.zokudo.controllers;


import com.cards.zokudo.dto.request.CardAssignmentDTO;
import com.cards.zokudo.dto.response.ApiResponse;
import com.cards.zokudo.entities.BulkCardIssuanceDetails;
import com.cards.zokudo.exceptions.BizException;
import com.cards.zokudo.services.card.assigment.CardAssignmentInf;
import com.cards.zokudo.services.card.cardlistbasedonfilters.KitNumbersFilterDTO;
import com.cards.zokudo.services.card.download.DownloadService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "{programUrl}/api/v1")
public class CardAssignmentController {

    private final CardAssignmentInf cardAssignmentInf;
    private final DownloadService downloadService;

    @Autowired
    public CardAssignmentController(CardAssignmentInf cardAssignmentInf, DownloadService downloadService) {
        this.cardAssignmentInf = cardAssignmentInf;
        this.downloadService = downloadService;
    }

    @ApiOperation(value = "Single/Bulk Kit Assignment", authorizations = {@Authorization("basicAuth")})
    @CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
    @PostMapping(value = "/bulkUpload", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object uploadBulkUploadFile(@ModelAttribute CardAssignmentDTO cardAssignmentDTO, @PathVariable String programUrl, @RequestHeader String role) {
        return cardAssignmentInf.uploadBulkFile(cardAssignmentDTO, programUrl, role);
    }

    @ApiOperation(value = "Get Card List Based On Filters", authorizations = {@Authorization("basicAuth")})
    @CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
    @PostMapping(value = "/clientKitNumbers", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Object clientKitNumbers(HttpServletRequest request, @PathVariable("programUrl") String programUrl, @RequestBody KitNumbersFilterDTO kitNumbersFilterDTO) {
        return cardAssignmentInf.clientKitNumbers(programUrl, kitNumbersFilterDTO, request.getHeader("role"));
    }

    @ApiOperation(value = "Get Filtered Card List Based On Filters", authorizations = {@Authorization("basicAuth")})
    @CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
    @PostMapping(value = "/filteredClientKitNumbers", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Object filteredClientKitNumbers(HttpServletRequest request, @PathVariable("programUrl") String programUrl, @RequestBody KitNumbersFilterDTO kitNumbersFilterDTO) {
        return cardAssignmentInf.filteredClientKitNumbers(programUrl, kitNumbersFilterDTO, request);
    }

    @ApiOperation(value = "Kits to be auto populate while CardIssuance - get all the assigned kits to the program/businessType", authorizations = {@Authorization("basicAuth")})
    @CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
    @GetMapping(value = "/getKitNumbersByProgram", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<Object> getKitNumbersByProgram(HttpServletRequest request, @PathVariable("programUrl") String programUrl) {
        return cardAssignmentInf.getKitNumbersByProgram(request.getHeader("businessType"));
    }

    /**
     * @param request
     * @return list of BulkCardIssuanceDetails(Kit) object
     */
    @ApiOperation(value = "Get FUll kit info by business type", authorizations = {@Authorization("basicAuth")})
    @CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
    @GetMapping(value = "/cardissuance/getKitsByProgram")
    public List<BulkCardIssuanceDetails> getKitByProgram(HttpServletRequest request, @PathVariable("programUrl") String programUrl) {
        return cardAssignmentInf.getKitByProgram(request.getHeader("businessType"));
    }

    /**
     * @param request
     * @return list of BulkCardIssuanceDetails(Kit) object by distributor id
     */
    @ApiOperation(value = "Get FUll kit info by business type", authorizations = {@Authorization("basicAuth")})
    @CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
    @GetMapping(value = "/cardissuance/getKitsByDistributorId")
    public List<BulkCardIssuanceDetails> getKitByDistributorId(HttpServletRequest request, @PathVariable("programUrl") String programUrl) {
        return cardAssignmentInf.getKitByDistributorId(request.getHeader("distributorId"));
    }

    /**
     * @param request
     * @return list of BulkCardIssuanceDetails(Kit) object by Retailer id
     */
    @ApiOperation(value = "Get FUll kit info by retailer id ", authorizations = {@Authorization("basicAuth")})
    @CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
    @GetMapping(value = "/cardissuance/getKitsByRetailerId")
    public List<BulkCardIssuanceDetails> getKitsByReatailerId(HttpServletRequest request, @PathVariable("programUrl") String programUrl) {
        return cardAssignmentInf.getKitsByRetailerId(request.getHeader("retailerId"));
    }

    @ApiOperation(value = "download assgined kit", authorizations = {@Authorization(value = "basicAuth")})
    @CrossOrigin(origins = {"*"}, allowCredentials = "true", allowedHeaders = "*", methods = RequestMethod.GET)
    @GetMapping(value = "/kitAssign/downloadCardList", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void downloadCardList(HttpServletRequest request, HttpServletResponse response,
                                 @PathVariable("programUrl") String programUrl,
                                 @RequestParam final Map<String, String> requestParams) throws Exception {
        downloadService.downloadAssignedKitList(programUrl, requestParams, response);

    }

}
