package com.cards.zokudo.controllers;


import com.cards.zokudo.dto.response.ApiResponse;
import com.cards.zokudo.exceptions.BizException;
import com.cards.zokudo.services.card.approverequest.ApprovePrefundRequest;
import com.cards.zokudo.services.card.approverequest.ApprovePrefundRequestService;
import com.cards.zokudo.services.card.download.DownloadService;
import com.cards.zokudo.services.card.prefundlist.PrefundListService;
import com.cards.zokudo.services.card.prefundlistbasedonfilter.PrefundListBasedOnFilters;
import com.cards.zokudo.services.card.prefundlistbasedonfilter.PrefundListBasedOnFiltersDTO;
import com.cards.zokudo.services.card.prefundrequest.PrefundRequestDTO;
import com.cards.zokudo.services.card.prefundrequest.PrefundRequestService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("{programUrl}/api/v1")
public class PrefundRequestController {

    @Autowired
    private PrefundRequestService prefundRequestService;

    @Autowired
    private PrefundListService prefundListService;

    @Autowired
    private ApprovePrefundRequestService approvePrefundRequestService;

    @Autowired
    private PrefundListBasedOnFilters prefundListBasedOnFilters;

    private final DownloadService downloadService;


    public PrefundRequestController(PrefundRequestService prefundRequestService,PrefundListBasedOnFilters prefundListBasedOnFilters,PrefundListService prefundListService,
                                    DownloadService downloadService) {
        this.prefundRequestService = prefundRequestService;
        this.prefundListService=prefundListService;
        this.prefundListBasedOnFilters=prefundListBasedOnFilters;
        this.downloadService = downloadService;
    }

    @ApiOperation(value = "Prefund Request", authorizations = {@Authorization("basicAuth")})
    @CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
    @PostMapping(value = "/prefundRequest", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> prefundRequest(HttpServletRequest request, @PathVariable("programUrl") String programUrl,
                                         @RequestBody PrefundRequestDTO prefundRequestDTO) {
        return prefundRequestService.execute(request, programUrl, prefundRequestDTO);
    }

    @ApiOperation(value = "Approve Prefund Request", authorizations = {@Authorization("basicAuth")})
    @CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
    @PostMapping(value = "/approveRequest", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> approveRequest(HttpServletRequest request, @PathVariable("programUrl") String programUrl,
                                         @RequestBody ApprovePrefundRequest approvePrefundRequest) throws JSONException {
        return approvePrefundRequestService.execute(request, programUrl, approvePrefundRequest);
    }

    @ApiOperation(value = "Fetch Prefund Transcation  By Program", authorizations = {@Authorization(value = "basicAuth")})
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    @GetMapping(value = "/fetchPrefundTranscation", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Object  getCustomerByProgramId(HttpServletRequest request, HttpServletResponse response, @PathVariable("programUrl") String programUrl) {
        return prefundListService.execute(programUrl , request.getHeader("role"),request.getHeader ("page"),request.getHeader ("size"));
    }

    @ApiOperation(value = "Get Card List Based On Filters", authorizations = {@Authorization("basicAuth")})
    @CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
    @PostMapping(value = "/getPrefundListBasedOnfilter", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Object getprefundListBasedOnfilters(HttpServletRequest request, @PathVariable("programUrl") String programUrl, @RequestBody PrefundListBasedOnFiltersDTO prefundListBasedOnFiltersDTO) {
        return prefundListBasedOnFilters.execute(programUrl,prefundListBasedOnFiltersDTO , request.getHeader("role"));
    }

    @ApiOperation(value = "Fetch Prefund Transcation  By Program", authorizations = {@Authorization(value = "basicAuth")})
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    @GetMapping(value = "/prefundHistoryList", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Object  prefundHistoryList(HttpServletRequest request, HttpServletResponse response, @PathVariable("programUrl") String programUrl) {
        return prefundListService.execute(programUrl , request.getHeader("role"),request.getHeader ("page"),request.getHeader ("size"));
    }

    @ApiOperation(value = "download Prefund List", authorizations = {@Authorization(value = "basicAuth")})
    @CrossOrigin(origins = {"*"}, allowCredentials = "true", allowedHeaders = "*", methods = RequestMethod.GET)
    @GetMapping(value = "/downloadPrefundList" , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void downloadPrefundList(HttpServletRequest request, HttpServletResponse response,
                                          @PathVariable("programUrl") String programUrl,
                                          @RequestParam final Map<String, String> requestParams) throws Exception {
        try {
            downloadService.downloadPrefundList(request, response, programUrl, requestParams);
        }catch (BizException e){
            response.sendRedirect(request.getHeader("Referer"));
        }
    }

}
