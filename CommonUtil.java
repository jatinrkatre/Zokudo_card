package com.cards.zokudo.util;


import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import com.cards.zokudo.enums.BizErrors;
import com.cards.zokudo.exceptions.BizException;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.OperationContext;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.BlobContainerPublicAccessType;
import com.microsoft.azure.storage.blob.BlobRequestOptions;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class CommonUtil {
    public static final String startTime = " 00:00:00";
    public static final String endTime = " 23:59:59";
    public static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat dateFormatterSlash = new SimpleDateFormat("dd/MM/yyyy");
    public static SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat dateFormate2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    public static SimpleDateFormat formatter3 = new SimpleDateFormat("ddMMyyyy");
    public static final int GC_COUNT_LIMIT_FINANCIAL_YEAR = 250;

    private static final String ADMIN_PROGRAM_URL ="mss";
    private static final String ZOKUDO_APP_USER_PROGRAM_URL= "appuser";

    public static String[] getProgramAndRequestUrl(HttpServletRequest request) {
        return request.getRequestURI().split("/");
    }

    public static String getBasicAuthorization(String applicationLevelUserName, String applicationLevelUserPassword) {
        String result = "Basic ";
        String credentials = applicationLevelUserName + ":" + applicationLevelUserPassword;
        result = result + DatatypeConverter.printBase64Binary(credentials.getBytes(StandardCharsets.UTF_8));
        return result;
    }

    public static String getString(String description) {
        if (description != null) {
            return description.replaceAll("\\W", " ");
        }
        return null;
    }

    public static boolean isValidMobileNumber(String str) {
        try {
            return str.trim().matches("^(?=(?:[6-9]){1})(?=[0-9]{10}).*");
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isNumeric(String str) {
        try {
            return str.trim().matches("[0-9]+");
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isValidTitle(String str) {
        try {
            switch (str) {
                case "Mr":
                case "Ms":
                case "Mrs":
                    return true;
            }
        } catch (Exception e) {
            throw new BizException(BizErrors.NULL_ERROR.getValue(), "title should not be empty");
        }
        return false;
    }

    public static boolean isValidGender(String str) {
        try {
            switch (str) {
                case "M":
                case "F":
                    return true;
            }
        } catch (Exception e) {
            throw new BizException(BizErrors.NULL_ERROR.getValue(), "Gender should not be empty");
        }
        return false;
    }

    // date format should be like YYYY-MM-DD
    public static boolean dobValidate(String date) {
        try {
            Constants.dateFormat.parse(date);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    public static boolean validIdType(String idType) {
        switch (idType) {
            case "aadhaar":
            case "pan":
            case "driver_id":
                return true;
        }
        return false;
    }

    public static boolean isAlphanumeric(String str) {
        String temp = str.trim();
        if (temp.matches("[A-Za-z0-9]+")) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean validatepaymentMethod(String paymentMethod) {
        try {
            switch (paymentMethod) {
                case "NEFT":
                case "IMPS":
                case "RTGS":
                case "CASH":
                    return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }


    public static boolean validCountry(String countryOfIssue) {
        return "India".equals(countryOfIssue);
    }

    public static String getParsedExpiryDate(String expiryDate, DateFormat dateFormat) {

        if (StringUtils.isBlank(expiryDate))
            throw new BizException(BizErrors.APPLICATION_ERROR.getValue(), "Card Expiry Date is empty!");

        try {
            return getFormatedDate(dateFormat.parse(expiryDate), Constants.dateFormat_MMYY);
        } catch (ParseException e) {
            log.error("date format failed!can not format the given date :=" + expiryDate);
            log.error(e.getMessage());
            throw new BizException(BizErrors.APPLICATION_ERROR.getValue(), "operation failed!");
        }
    }

    public static String getFormatedDate(Date date, DateFormat dateFormat_MMYY) {

        try {
            return dateFormat_MMYY.format(date);
        } catch (Exception e) {
            log.error("date format failed!can not format the given date :=" + date);
            log.error(e.getMessage());
            throw new BizException(BizErrors.APPLICATION_ERROR.getValue(), "date format failed");
        }
    }

    /**
     * UPLOADING FILES TO AZURE BLOB STORAGE
     * file - file to be uploaded,
     * directory - azure container name
     * id - id to be applied to make create unique reference at azure storage
     **/
    public static String uploadFile(MultipartFile file, String id, String directory) {

        try {
            CloudStorageAccount storageAccount;
            String fileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            String fileName =  generateFileKey(id) + fileExtension;

            // Parse the connection string and create a blob client to interact with Blob storage
            storageAccount = CloudStorageAccount.parse(Constants.STORAGE_CONNECTION_STRING);
            CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
            CloudBlobContainer container = blobClient.getContainerReference(directory);

            // Create the container if it does not exist with public access
            container.createIfNotExists(BlobContainerPublicAccessType.CONTAINER, new BlobRequestOptions(), new OperationContext());

            //Getting a blob reference, create a unique blob reference
            CloudBlockBlob blob = container.getBlockBlobReference(fileName);
            
            //Creating blob and uploading file to it
            log.info("Uploading file to Azure storage "+ blob);
            blob.upload(file.getInputStream(), file.getSize());
            log.info("Downloadable File Path : "+blob.getUri().toString());
            return  blob.getUri().toString();

        } catch (StorageException ex)
        {
            log.error(String.format("Error returned from the service. Http code: %d and error code: %s", ex.getHttpStatusCode(), ex.getErrorCode()));
            throw new BizException(BizErrors.APPLICATION_ERROR.getValue(), "file upload operation failed!");
        }
        catch (Exception e)
        {
            log.error(e.getMessage());
            throw new BizException(BizErrors.APPLICATION_ERROR.getValue(), "file upload operation failed!");
        }

    }


    public static String generateFileKey(String id) {
        try {
            String systemMillis = String.valueOf(System.currentTimeMillis());
            return id + "_" + systemMillis;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void generateExcelSheet(final List<String> headers, final List<Map<String, String>> dataList, String resultantFileName, HttpServletResponse response) {
        try {
            if (headers == null || headers.size() == 0) {
                return;
            }
            if (dataList == null || dataList.size() == 0) {
                return;
            }
            final XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFCellStyle cellStyle = workbook.createCellStyle();
            XSSFFont font = workbook.createFont();
            font.setBold(true);
            cellStyle.setFont(font);

            final XSSFSheet sheet = workbook.createSheet();
            XSSFRow xssfRow = sheet.createRow(0);
            for (int i = 0; i < headers.size(); i++) {
                XSSFCell xssfCell = xssfRow.createCell(i);
                xssfCell.setCellStyle(cellStyle);
                xssfCell.setCellValue(headers.get(i));
            }
            for (int j = 0; j < dataList.size(); j++) {
                XSSFRow row = sheet.createRow((j + 1));
                Map<String, String> eachDataMap = dataList.get(j);
                for (int k = 0; k < headers.size(); k++) {
                    XSSFCell cell = row.createCell(k);
                    String key = headers.get(k);
                    String value = eachDataMap.get(key);
                    cell.setCellValue(value);
                }
            }
            response.setHeader("Content-Type", MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setHeader("Content-Disposition", "attachment; filename=" + resultantFileName);

            final ServletOutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            workbook.close();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return;
        }
    }

    public static Date atStartOfDay(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
        return localDateTimeToDate(startOfDay);
    }

    public static Date atEndOfDay(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
        return localDateTimeToDate(endOfDay);
    }

    private static LocalDateTime dateToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    private static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static String[] getUserNamePassword(String authorizationHeader) {
        if (StringUtils.isEmpty(authorizationHeader)) {
            throw new BizException("Unauthorized access!");
        }
        return (new String(Base64.getDecoder().decode((authorizationHeader.replaceAll("Basic ", ""))))).split(":");

    }

    public static String[] getDefaultDateRange(SimpleDateFormat dateFormate, int count) {
        String range[] = new String[2];
        Date today = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(today);
        cal.add(Calendar.DAY_OF_MONTH, count);
        Date today30 = cal.getTime();
        range[0] = dateFormate.format(today30);
        range[1] = dateFormate.format(today);
        System.out.println(range[0] + ":" + range[1]);
        return range;
    }

    public static String generateFourDigitNumericString() {
        int number = (int) (Math.random() * 10000);
        return String.format("%04d", number);
    }

    public static String getExpriyDate(String expiryDate) {
        try {
            return Constants.dateFormat_MMYY.format(Constants.dateFormat.parse(expiryDate));
        } catch (Exception e) {
            return "0521";
        }
    }

    /**
     * change date format from MMYY to YYMM for getCVV
     * **/
    public static String reverseGetExpriyDate(String expiryDate) {
        try {
            if(expiryDate.length()==4) {
                expiryDate = expiryDate.substring(2) + expiryDate.substring(0, 2);
                return expiryDate;
            }
            else {
                return Constants.dateFormat_YYMM.format(Constants.dateFormat.parse(expiryDate));
            }
        } catch (Exception e) {
            throw new BizException("Invalid expiry date");
        }
    }

    
    /**
     * 
     * @param pDate 
     * @return date in yyyy-MM-dd format is Valid
     * @throws ParseException
     * @author vsahoo@msewa.com
     */
    public static String getDateYYYMMDD(String pDate) throws ParseException {
    	
    	log.info("Into getDateYYYMMDD() CommonUtil for date parsing. Unparsed Date: "+pDate);
    	String pattern = null;
    	SimpleDateFormat simpleDateFormat2 = null;
    	
    	//Regex for dd/MM/yyyy and dd-MM-yyyy
    	if(pDate.matches("^(((0[1-9]|[12][0-9]|30)[-/]?(0[13-9]|1[012])|31[-/]?(0[13578]|1[02])|(0[1-9]|1[0-9]|2[0-8])[-/]?02)[-/]?[0-9]{4}|29[-/]?02[-/]?([0-9]{2}(([2468][048]|[02468][48])|[13579][26])|([13579][26]|[02468][048]|0[0-9]|1[0-6])00))$")) {
			if(pDate.contains("/")) 
				 pattern = "dd/MM/yyyy";
			else 
				 pattern = "dd-MM-yyyy";
		} //Regex for MM/dd/yyyy and MM-dd-yyyy
		else if(pDate.matches("^(((0[13-9]|1[012])[-/]?(0[1-9]|[12][0-9]|30)|(0[13578]|1[02])[-/]?31|02[-/]?(0[1-9]|1[0-9]|2[0-8]))[-/]?[0-9]{4}|02[-/]?29[-/]?([0-9]{2}(([2468][048]|[02468][48])|[13579][26])|([13579][26]|[02468][048]|0[0-9]|1[0-6])00))$")){
			
			if(pDate.contains("/")) 
				pattern = "MM/dd/yyyy";
			else 
				pattern = "MM-dd-yyyy";
			
		}else if(pDate.matches("^([0-9]{4}[-/]?((0[13-9]|1[012])[-/]?(0[1-9]|[12][0-9]|30)|(0[13578]|1[02])[-/]?31|02[-/]?(0[1-9]|1[0-9]|2[0-8]))|([0-9]{2}(([2468][048]|[02468][48])|[13579][26])|([13579][26]|[02468][048]|0[0-9]|1[0-6])00)[-/]?02[-/]?29)$")) {
			
			if(pDate.contains("/")) 
				pattern = "yyyy/MM/dd";
			else 
				pattern = "yyyy-MM-dd";
			
		}else {
			return "Invalid";
		}
    	
    	log.info("BEFORE Date parse. Unparsed Date: "+pDate," , Pattern To be Parsed : "+pattern);
    	simpleDateFormat2 = new SimpleDateFormat(pattern);
    	Date date = simpleDateFormat2.parse(pDate);
    	String parsedDate = formatter.format(date);
    	log.info("AFTER Date parse. Parsed Date: "+parsedDate);
    	return parsedDate;
    		
    }

    public static boolean isAtCustomerLevel(String programUrl){
        log.info(" Program URL is : ",programUrl);
        return programUrl.equalsIgnoreCase(ADMIN_PROGRAM_URL) || programUrl.equalsIgnoreCase(ZOKUDO_APP_USER_PROGRAM_URL);
    }
}
