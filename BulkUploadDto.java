package com.cards.zokudo.dto.request;

import lombok.Data;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

@Data
public class BulkUploadDto {
    private MultipartFile file;
    private String operationType;
    private JSONObject programJson;
    private String directory;
    private String category;
    private String filePath;
    private String orginalFileName;
    private String clientHashId;
    private String walletHashId;
    private String customerHashId;
}
