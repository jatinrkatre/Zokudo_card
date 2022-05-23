package com.cards.zokudo.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.codehaus.jettison.json.JSONArray;

import com.cards.zokudo.dto.request.CardAssignmentDTO;
import com.cards.zokudo.dto.request.CardInventoryDTO;
import com.cards.zokudo.entities.CardInventoryFileCatalogue;
import com.cards.zokudo.entities.CorporateFileCatalogue;
import com.cards.zokudo.enums.Status;
import com.cards.zokudo.exceptions.BizException;
import com.cards.zokudo.services.card.load.LoadCardDto;
import com.cards.zokudo.services.card.persist.ActivateCardDto;
import com.csvreader.CsvReader;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomCSVReader {

    /**
     * GET FILES FROM AZURE BLOB STORAGE
     * fileName - blob reference to the uploaded file
     **/
    public static List<ActivateCardDto> readCSVBulkRegister(String fileName) {
        try {
            CloudStorageAccount storageAccount = CloudStorageAccount.parse(Constants.STORAGE_CONNECTION_STRING);
            CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
            final CloudBlobContainer container = blobClient.getContainerReference(Constants.BULK_CARD_CREATE_DIR);
            CloudBlockBlob blob = container.getBlockBlobReference(fileName);

            //creating an object of output stream to recieve the file's content from azure blob.
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            blob.download(outputStream);

            //converting it to the inputStream to return
            final byte[] bytes = outputStream.toByteArray();
            InputStream objectData = new ByteArrayInputStream(bytes);
            //InputStream objectData = object.getObjectContent();

            //InputStream objectData = new FileInputStream(new File(Constants.BULK_CARD_DIR + fileName));
            CsvReader fileData = new CsvReader(new InputStreamReader(objectData));
            List<ActivateCardDto> list = new ArrayList<>();

            if (fileData.readHeaders()) {
                while (fileData.readRecord()) {
                    ActivateCardDto apiRequestDto = new ActivateCardDto();
                    apiRequestDto.setKitNumber(fileData.get("kitNumber"));
                    apiRequestDto.setCardType(fileData.get("cardType"));
                    apiRequestDto.setUserHashId(fileData.get("customerHashId"));
                    apiRequestDto.setBusinessId(fileData.get("businessId"));
                    list.add(apiRequestDto);
                }
            }
            fileData.close();
            return list;
        }
        catch (StorageException ex) {
            log.error(String.format("Error returned from the service. Http code: %d and error code: %s", ex.getHttpStatusCode(), ex.getErrorCode()));
            return null;
        }
        catch (Exception e)
        {
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * GET FILES FROM AZURE BLOB STORAGE
     * fileName - blob reference to the uploaded file
     * **/
    public static List<LoadCardDto> readCSVBulkCardLoad(String fileName) {

        try {
            CloudStorageAccount storageAccount = CloudStorageAccount.parse(Constants.STORAGE_CONNECTION_STRING);
            CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
            final CloudBlobContainer container = blobClient.getContainerReference(Constants.BULK_CARD_LOAD_DIR);
            CloudBlockBlob blob = container.getBlockBlobReference(fileName);

            //creating an object of output stream to recieve the file's content from azure blob.
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            blob.download(outputStream);

            //converting it to the inputStream to return
            final byte[] bytes = outputStream.toByteArray();
            InputStream objectData = new ByteArrayInputStream(bytes);

            CsvReader fileData = new CsvReader(new InputStreamReader(objectData));
            List<LoadCardDto> list = new ArrayList<>();

            if (fileData.readHeaders()) {

                while (fileData.readRecord()) {
                	if(fileData.get("pocketName").equalsIgnoreCase(""))
                	    continue;
                    LoadCardDto loadCardDto = new LoadCardDto();

                    loadCardDto.setTransactionId(System.currentTimeMillis() + "LM");
                    loadCardDto.setAmount(fileData.get("amount"));
                    loadCardDto.setPocketName(fileData.get("pocketName"));
                    loadCardDto.setUserHashId(fileData.get("userHashId"));
                    loadCardDto.setFundingChannel(fileData.get("fundingChannel"));
                    loadCardDto.setCurrencyCode(fileData.get("currencyCode"));
                    loadCardDto.setShortDescription("LOAD_CARD_TXN");
                    loadCardDto.setProxyCardNumber(fileData.get("proxyCardNumber"));
                    //loadCardDto.setWalletHashId(fileData.get("walletHashId"));
                    list.add(loadCardDto);
                }
            }

            fileData.close();
            return list;

        }
        catch (StorageException ex) {
            log.error(String.format("Error returned from the service. Http code: %d and error code: %s", ex.getHttpStatusCode(), ex.getErrorCode()));
            return null;
        }
        catch (Exception e)
        {
            log.error(e.getMessage());
            return null;
        }
    }

    public static void writeCreateObjectToCSV(PrintWriter writer, JSONArray jsonArray) {

        try (
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                        .withHeader("userHashId", "kitNumber", "cardType"))
        ) {

            for (int i = 0; i < jsonArray.length(); i++) {
                List<String> data = Arrays.asList(

                        jsonArray.getJSONObject(i).getString("userHashId"),
                        jsonArray.getJSONObject(i).getString("kitNumber"),
                        jsonArray.getJSONObject(i).getString("cardType")
                );

                csvPrinter.printRecord(data);
            }
            csvPrinter.flush();
        } catch (Exception e) {
            System.out.println("Writing CSV error!");
            e.printStackTrace();
        }
    }

    public static void writeLoadObjectToCSV(PrintWriter writer, JSONArray jsonArray) {

        try (
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                        .withHeader("userHashId", "cardHashId", "amount"))
        ) {

            for (int i = 0; i < jsonArray.length(); i++) {
                List<String> data = Arrays.asList(

                        jsonArray.getJSONObject(i).getString("userHashId"),
                        jsonArray.getJSONObject(i).getString("cardHashId"),
                        jsonArray.getJSONObject(i).getString("amount")
                );

                csvPrinter.printRecord(data);
            }
            csvPrinter.flush();
        } catch (Exception e) {
            System.out.println("Writing CSV error!");
            e.printStackTrace();
        }
    }

    public static List<CardAssignmentDTO> readCSVBulkCardAssignment(String fileName, CorporateFileCatalogue corporateFileCatalogue) {

        try {
        	 CloudStorageAccount storageAccount = CloudStorageAccount.parse(Constants.STORAGE_CONNECTION_STRING);
             CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
             final CloudBlobContainer container = blobClient.getContainerReference(Constants.BULK_KIT_ASSIGN_DIR);
             CloudBlockBlob blob = container.getBlockBlobReference(fileName);
             //creating an object of output stream to recieve the file's content from azure blob.
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             log.info("File is getting downloaded from azure, URI :"+blob.getUri().toString());
             blob.download(outputStream);

             //converting it to the inputStream to return
             final byte[] bytes = outputStream.toByteArray();
             InputStream objectData = new ByteArrayInputStream(bytes);
             //InputStream objectData = object.getObjectContent();

             //InputStream objectData = new FileInputStream(new File(Constants.BULK_CARD_DIR + fileName));
             CsvReader fileData = new CsvReader(new InputStreamReader(objectData));
        	
           /* S3Object object = CommonUtil.client.getObject(new GetObjectRequest(Constants.BUCKET_NAME, Constants.BULK_KIT_ASSIGN_DIR + fileName));
            InputStream objectData = object.getObjectContent();
            CsvReader fileData = new CsvReader(new InputStreamReader(objectData));*/
            List<CardAssignmentDTO> list = new ArrayList<>();
            if (fileData.readHeaders()) {

                while (fileData.readRecord()) {
                	if(fileData.get("expiryDate").equalsIgnoreCase(""))
                	    continue;
                    CardAssignmentDTO cardAssignmentDTO = new CardAssignmentDTO();
                    cardAssignmentDTO.setExpiryDate(fileData.get("expiryDate"));
                    cardAssignmentDTO.setClientId(corporateFileCatalogue.getClientId());
                    cardAssignmentDTO.setProgramId(corporateFileCatalogue.getProgramId());
                    cardAssignmentDTO.setBusinessId(corporateFileCatalogue.getBusinessId());
                    cardAssignmentDTO.setCardType(corporateFileCatalogue.getCardType().getValue());
                    cardAssignmentDTO.setKitNumber(fileData.get("kitNumber"));
                    cardAssignmentDTO.setProgramHashId(corporateFileCatalogue.getProgramHashId());
                    
                    /*cardAssignmentDTO.setLastName(fileData.get("expiryDate"));
                    cardAssignmentDTO.setPhoneNumber(fileData.get("phoneNumber"));
                    cardAssignmentDTO.setEmail(fileData.get("email"));
                    cardAssignmentDTO.setCardNumber(fileData.get("cardNumber"));
                    //cardAssignmentDTO.setIpin(fileData.get("ipin"));
                    cardAssignmentDTO.setCvv(fileData.get("cvv"));
                    cardAssignmentDTO.setKitNumber(fileData.get("urn"));
                    //cardAssignmentDTO.setCardType(corporateFileCatalogue.getCardType().getValue());
                    */

                    list.add(cardAssignmentDTO);
                }
            }

            fileData.close();
            return list;

        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage()+" Process failed while reading remote document ");
            throw new BizException("Unable process bulk document.");
        }
    }
    
    public static List<CardInventoryDTO> readCSVBulkCardInventory(String fileName, CardInventoryFileCatalogue iventoryFileCatalogue) {

        try {
        	 CloudStorageAccount storageAccount = CloudStorageAccount.parse(Constants.STORAGE_CONNECTION_STRING);
             CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
             final CloudBlobContainer container = blobClient.getContainerReference(Constants.BULK_CARD_INV);
             CloudBlockBlob blob = container.getBlockBlobReference(fileName);
             //creating an object of output stream to recieve the file's content from azure blob.
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             log.info("File is getting downloaded from azure, URI :"+blob.getUri().toString());
             blob.download(outputStream);

             //converting it to the inputStream to return
             final byte[] bytes = outputStream.toByteArray();
             InputStream objectData = new ByteArrayInputStream(bytes);
             //InputStream objectData = object.getObjectContent();

             //InputStream objectData = new FileInputStream(new File(Constants.BULK_CARD_DIR + fileName));
             CsvReader fileData = new CsvReader(new InputStreamReader(objectData));
        	
           /* S3Object object = CommonUtil.client.getObject(new GetObjectRequest(Constants.BUCKET_NAME, Constants.BULK_KIT_ASSIGN_DIR + fileName));
            InputStream objectData = object.getObjectContent();
            CsvReader fileData = new CsvReader(new InputStreamReader(objectData));*/
            List<CardInventoryDTO> list = new ArrayList<>();
            if (fileData.readHeaders()) {

                while (fileData.readRecord()) {
                	if(fileData.get("expiryDate").equalsIgnoreCase(""))
                	    continue;
                    CardInventoryDTO cardInventory = new CardInventoryDTO();
                    cardInventory.setExpiryDate(fileData.get("expiryDate"));
                    cardInventory.setProgramId(iventoryFileCatalogue.getProgramId());
                    cardInventory.setStatus(Status.UNASSIGNED);
                    cardInventory.setKit(Long.parseLong(fileData.get("kit").trim()));
                    cardInventory.setMaskedNumber(fileData.get("maskedNumber"));
                    list.add(cardInventory);
                }
            }

            fileData.close();
            return list;

        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage()+" Process failed while reading remote document ");
            throw new BizException("Unable process bulk document. "+ e.getMessage());
        }
    }
    
}
