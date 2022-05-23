package com.cards.zokudo.util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;

public class Constants {

    public static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static final DateFormat dateFormat1 = new SimpleDateFormat("ddMMyyyy");
    public static final DateFormat dateFormat_YYMM = new SimpleDateFormat("yyMM");
    public static final DateFormat dateFormat_MMYY = new SimpleDateFormat("MMyy");
    public static final DecimalFormat decimalFormat = new DecimalFormat("0.00");
    public static final String BUCKET_NAME = "blobmsewacontainer";

    /* Azure blob storage details */
    public static final String BULK_CARD_LOAD_DIR = "bulkloadfile";
    public static final String BULK_CARD_CREATE_DIR = "bulkcardfile";
    public static final String BULK_KIT_ASSIGN_DIR = "bulkkitassignfile";
    public static final String BULK_CARD_INV = "bulkcardinv";
    public static final String CONTAINER_NAME = "blobmsewacontainer";
    public static final String STORAGE_CONNECTION_STRING = "DefaultEndpointsProtocol=https;" +
                                                            "AccountName=blobmsewa;" +
                                                            "AccountKey=ngYBCnZF6d/Bp+zRdb0JTcyCo8ZmhSjcU4OSCxhcxeLKYc90pX5H1Mw8mPcMyb2Tgw/yEEvBJ/BNDix6nzXqgQ==;";

    public static final String SECRET_KEY = "HY1GmIgvO9fTA5zwBMs36O/nvHceLPdBUYBVtwoh";
    public static final String ACCESS_KEY = "AKIAQ7MTW3TEOGMUG4U4";
    public static final String zaggle_product_code ="MEALMW";
    public static final String debit = "D";
    public static final String credit = "C";
    public static final boolean debit_true = true;
    public static final boolean debit_false = false;
    public static final String urlEscapeConstant = "\\{\\}";

    //Zokudo Email notification Service details
    public static final String SENDER_EMAIL = "support@zokudo.com";
    public static final String SENDER = "zokudo";
    public static final String SENDER_PASSWORD = "Z0kud@14";
    public static String host = "smtp.falconide.com";

    //Zokudo SMS notification service details
    public static final String SMS_GATEWAY_URL = "http://bulkpush.mytoday.com/BulkSms/SingleMsgApi";
    public static final String SMS_GATEWAY_FEEDID = "345312";
    public static final String SMS_GATEWAY_USERNAME = "8655000444";
    public static final String SMS_GATEWAY_PASSWORD = "HMGroup@123";
    public static final String SMS_GATEWAY_SENDERID = "ZOKUDO_TRANS";

    public static String defaultProgram = "mss";

    public static String txnType(boolean isDebit) {
        return isDebit ? debit : credit;
    }

    public static Properties getProperties() {
        Properties props = System.getProperties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", SENDER);
        props.put("mail.smtp.password", SENDER_PASSWORD);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        return props;
    }


}
