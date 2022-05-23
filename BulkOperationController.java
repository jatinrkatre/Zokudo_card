package com.cards.zokudo.controllers;

import com.cards.zokudo.dto.request.BulkReportRequestDTO;
import com.cards.zokudo.dto.request.BulkUploadDto;
import com.cards.zokudo.dto.response.ApiResponse;
import com.cards.zokudo.exceptions.BizException;
import com.cards.zokudo.services.card.bulk.BulkUploadInf;
import com.cards.zokudo.services.card.download.DownloadService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("{programUrl}/api/v1")
public class BulkOperationController {

    private final BulkUploadInf bulkUploadInf;
    private final DownloadService downloadService;

    public BulkOperationController(final BulkUploadInf bulkUploadInf, DownloadService downloadService) {
        this.bulkUploadInf = bulkUploadInf;
        this.downloadService = downloadService;
    }


    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = RequestMethod.POST, origins = {"*"})
    @PostMapping(value = "/bulkCardCreate", produces = MediaType.APPLICATION_JSON_VALUE, consumes = {
            "multipart/form-data"})
    public ResponseEntity<?> bulkCardCreate(HttpServletRequest request, @ModelAttribute BulkUploadDto bulkUploadDto,
                                            @PathVariable("programUrl") String programUrl) {
        return new ResponseEntity<ApiResponse>(bulkUploadInf.bulkUpload(request, bulkUploadDto, programUrl), HttpStatus.OK);
    }


    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = RequestMethod.POST, origins = {"*"})
    @PostMapping(value = "/processBulkCardCreate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> processBulkCardCreate(HttpServletRequest request, @RequestBody BulkUploadDto bulkUploadDto,
                                                   @PathVariable("programUrl") String programUrl) {
        return new ResponseEntity<ApiResponse>(bulkUploadInf.processBulkCardCreate(bulkUploadDto, programUrl, request.getHeader("loggedInUserHashId")), HttpStatus.OK);
    }

    @ApiOperation(value = "Report By Program", authorizations = {@Authorization(value = "basicAuth")})
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    @PostMapping(value = "/bulkCardReport", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Object bulkCardReport(HttpServletRequest request, HttpServletResponse response, @PathVariable("programUrl") String programUrl, @RequestBody BulkReportRequestDTO bulkreportRequestDTO) {
        return bulkUploadInf.bulkCardReport(programUrl, request.getHeader("role"), request.getHeader("page"), request.getHeader("size"), bulkreportRequestDTO,request.getHeader("loggedInUserHashId"));
    }

    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = RequestMethod.POST, origins = {"*"})
    @PostMapping(value = "/processBulkCardLoad", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> processBulkCardLoad(HttpServletRequest request, @RequestBody BulkUploadDto bulkUploadDto,
                                                 @PathVariable("programUrl") String programUrl) {
        return new ResponseEntity<ApiResponse>(bulkUploadInf.processBulkCardLoad(bulkUploadDto, programUrl, request.getHeader("loggedInUserHashId"), request.getHeader("role")), HttpStatus.OK);
    }

    @ApiOperation(value = "Report By Program", authorizations = {@Authorization(value = "basicAuth")})
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    @PostMapping(value = "/bulkLoadCardReport", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Object getBulkLoadCardReport(HttpServletRequest request, HttpServletResponse response, @PathVariable("programUrl") String programUrl, @RequestBody BulkReportRequestDTO bulkreportRequestDTO) {
        return bulkUploadInf.getBulkLoadCardReport(programUrl, request.getHeader("role"), request.getHeader("page"), request.getHeader("size"),
                request.getHeader("dateRange"), bulkreportRequestDTO,request.getHeader("loggedInUserHashId"));
    }

    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = RequestMethod.POST, origins = {"*"})
    @PostMapping(value = "/bulkCardLoad", produces = MediaType.APPLICATION_JSON_VALUE, consumes = {
            "multipart/form-data"})
    public ResponseEntity<?> bulkCardLoad(HttpServletRequest request, @ModelAttribute BulkUploadDto bulkUploadDto,
                                          @PathVariable("programUrl") String programUrl) {
        return new ResponseEntity<ApiResponse>(bulkUploadInf.bulkUpload(request, bulkUploadDto, programUrl), HttpStatus.OK);
    }

    @ApiOperation(value = "Report By Program", authorizations = {@Authorization(value = "basicAuth")})
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    @GetMapping(value = "/bulkCardUploadReport", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Object bulkUploadReport(HttpServletRequest request, HttpServletResponse response, @PathVariable("programUrl") String programUrl) {
        return bulkUploadInf.bulkUploadReport(programUrl, request.getHeader("role"), request.getHeader("page"), request.getHeader("size"),
                request.getHeader("dateRange"),request.getHeader("loggedInUserHashId"));
    }

    @ApiOperation(value = "Download card upload List", authorizations = {@Authorization(value = "basicAuth")})
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", origins = {"*"}, methods = RequestMethod.GET)
    @GetMapping(value = "/bulk/create/download", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void downloadReport(HttpServletRequest request, HttpServletResponse response, @PathVariable("programUrl") String programUrl,@RequestParam final Map<String, String> requestParams) throws Exception {
        try {
            downloadService.downloadReport(response,programUrl,requestParams);
        } catch (BizException e) {
            response.sendRedirect(request.getHeader("Referer"));
        }
    }
    
    @ApiOperation(value = "Download card upload List", authorizations = {@Authorization(value = "basicAuth")})
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", origins = {"*"}, methods = RequestMethod.GET)
    @GetMapping(value = "/bulk/load/download", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void downloadCardLoadReport(HttpServletRequest request, HttpServletResponse response, @PathVariable("programUrl") String programUrl,@RequestParam final Map<String, String> requestParams) throws Exception {
        try {
            downloadService.downloadLoadReport(response,programUrl,requestParams);
        } catch (BizException e) {
            response.sendRedirect(request.getHeader("Referer"));
        }
    }

}
