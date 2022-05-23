package com.cards.zokudo.controllers;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cards.zokudo.dto.request.CardInventoryDTO;
import com.cards.zokudo.entities.CardInventory;
import com.cards.zokudo.services.card.download.DownloadService;
import com.cards.zokudo.services.card.inventory.CardInventoryService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

@RestController
@RequestMapping(value = "{programUrl}/api/v1/inventory")
public class CardInventoryApiController {
	
	
	private final CardInventoryService cardInventoryService;
	private final DownloadService downloadService;
	
	@Autowired
	public CardInventoryApiController( CardInventoryService cardInventoryService,DownloadService downloadService) {
		this.cardInventoryService = cardInventoryService;
		this.downloadService = downloadService;
	}
	
	@ApiOperation(value = "Bulk upload cards to inventory", authorizations = {@Authorization("basicAuth")})
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
    @PostMapping(value = "/bulkUpload", produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<String> uploadBulkUploadFile(CardInventoryDTO inventoryDto) {
        return cardInventoryService.uploadBulkFile(inventoryDto);
    }
	
	@ApiOperation(value = "Get Inventory Summary By Program", authorizations = {@Authorization("basicAuth")})
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
    @PostMapping(value = "/getCardDetails", produces = MediaType.APPLICATION_JSON_VALUE )
    public CardInventoryDTO getCardInvSummary(@PathVariable String programUrl,@RequestBody CardInventoryDTO inventoryDto) {
        return cardInventoryService.getCardInvSummary(programUrl,inventoryDto);
    }
	
	@ApiOperation(value = "List Inventory Items", authorizations = {@Authorization("basicAuth")})
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
    @PostMapping(value = "/getCardList", produces = MediaType.APPLICATION_JSON_VALUE )
    public Map<String, Page<CardInventory>> getInventoryList(@RequestBody CardInventoryDTO inventoryDto,@RequestHeader String page,@RequestHeader String size) {
        return cardInventoryService.getInventoryList(inventoryDto,page,size);
    }
	
	@ApiOperation(value = "bulk upload file summary ", authorizations = {@Authorization("basicAuth")})
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
    @GetMapping(value = "/bulkCardUploadReport", produces = MediaType.APPLICATION_JSON_VALUE )
    public Object bulkInventoryUploadReport(@RequestHeader String dateRange,@RequestHeader Long programId, @RequestHeader String page,@RequestHeader String size) {
        return cardInventoryService.bulkInventoryUploadReport(page,size,dateRange,programId);
    }
	
	@ApiOperation(value = "bulk card inventory report ", authorizations = {@Authorization("basicAuth")})
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
	@PostMapping(value = "/bulkCardReport", produces = MediaType.APPLICATION_JSON_VALUE )
    public Object bulkCardInvReport(@RequestBody CardInventoryDTO inventoryDto,@RequestHeader String page,@RequestHeader String size) {
        return cardInventoryService.bulkCardInvReport(page,size,inventoryDto);
    }
	
	@ApiOperation(value = "get kit from inventory by programId", authorizations = {@Authorization("basicAuth")})
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
    @GetMapping(value = "/getKitByProgram", produces = MediaType.APPLICATION_JSON_VALUE )
    public List<CardInventory> getKitByProgram(@RequestHeader Long programId) {
        return cardInventoryService.getKitByProgram(programId);
    }
	
	 @ApiOperation(value = "download assgined kit", authorizations = {@Authorization(value = "basicAuth")})
	    @CrossOrigin(origins = {"*"}, allowCredentials = "true", allowedHeaders = "*", methods = RequestMethod.GET)
	    @GetMapping(value = "/downloadCardList", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	    public void downloadCardList(HttpServletRequest request, HttpServletResponse response,
	                                 @PathVariable("programUrl") String programUrl,
	                                 @RequestParam final Map<String, String> requestParams) throws Exception {
	            downloadService.downloadCardInvKitList(programUrl, requestParams,response);
	        
	    }
	
}
