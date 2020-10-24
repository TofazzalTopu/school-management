package com.grailslab

import grails.converters.JSON
import grails.core.GrailsApplication
import grails.gorm.transactions.Transactional
import okhttp3.*
import org.grails.web.json.JSONObject

import javax.annotation.PostConstruct
import java.util.regex.Pattern

@Transactional
class PowerSmsService {
    GrailsApplication grailsApplication
    OkHttpClient client
    SchoolService schoolService

    @PostConstruct
    void init() {
        client = new OkHttpClient();
    }

    def sendSMSPost(String message, String cellNumber){
        String senderId = schoolService.getNonMaskingNumber()
        if(senderId) {
            try {
                boolean isError = false
                String insertedSmsIds
                String messageStr
                String smsOperator = getOperatorFrmNumber(senderId)
                if (smsOperator == "powerSms") {
                    RequestBody body = smsUrlBody(message, cellNumber)
                    Request request = new Request.Builder()
                            .url(POWER_SMS_API_ENDPOINT+SEND_SMS_API)
                            .post(body)
                            .addHeader("content-type", "application/x-www-form-urlencoded")
                            .build();

                    Response response = client.newCall(request).execute();
                    String jsonData = response.body().string();
                    JSONObject jsonObj = JSON.parse(jsonData)
                    isError = jsonObj.isError
                    if (isError) {
                        messageStr = jsonObj.message
                    } else {
                        insertedSmsIds = jsonObj.insertedSmsIds
                    }
                }else if (smsOperator == "mySms") {
                    RequestBody body = mySmsUrlBody(message, cellNumber)
                    Request request = new Request.Builder()
                            .url(MY_SMS_API_ENDPOINT+SEND_SMS_API)
                            .post(body)
                            .addHeader("content-type", "application/x-www-form-urlencoded")
                            .build();

                    Response response = client.newCall(request).execute();
                    String jsonData = response.body().string();
                    JSONObject jsonObj = JSON.parse(jsonData)
                    isError = jsonObj.isError
                    if (isError) {
                        messageStr = jsonObj.message
                    } else {
                        insertedSmsIds = jsonObj.insertedSmsIds
                    }
                } else {
                    String msgType = isAlphaNumeric(message) ? "text" : "unicode"
                    RequestBody body = sendSmsUrlElitBuzz(message, cellNumber, senderId, msgType)
                    Request request = new Request.Builder()
                            .url(ELIT_BUZZ_SMS_API_ENDPOINT+ELIT_BUZZ_SEND_SMS_API)
                            .post(body)
                            .addHeader("content-type", "application/x-www-form-urlencoded")
                            .build();

                    Response response = client.newCall(request).execute();
                    String jsonData = response.body().string();
                    if (jsonData.startsWith("SMS SUBMITTED: ID - ")) {
                        isError = false
                        insertedSmsIds = jsonData.replaceAll("SMS SUBMITTED: ID - ","")
                    } else {
                        isError = true
                        messageStr = "Can't send SMS now. Please contact with admin"
                    }
                }

                if (isError) {
                    return [isError: isError, errorMessage: messageStr]
                }
                return [isError: isError, errorMessage: messageStr, insertedSmsIds: insertedSmsIds]
            } catch (Exception ex) {
                log.error("SMS SEND FAIL: "+ex.getMessage())
                return ['isError': true, errorMessage:"Can't send message now due to server error. Please contact admin"]
            }
        } else {
            return ['isError': true, errorMessage:"Your message service isn't enabled. Please contact admin"]
        }
    }

    protected RequestBody smsUrlBody(String smsText, String mobileNos) {
        String userId = grailsApplication.config.powersms.api.userid
        String password = grailsApplication.config.powersms.api.password
        RequestBody formBody = new FormBody.Builder()
                .add("userId", userId)
                .add("password", password)
                .add("smsText", smsText)
                .add("commaSeperatedReceiverNumbers", mobileNos)
                .build();
        return formBody
    }
    protected RequestBody mySmsUrlBody(String smsText, String mobileNos) {
        String userId = grailsApplication.config.mysms.api.userid
        String password = grailsApplication.config.mysms.api.password
        RequestBody formBody = new FormBody.Builder()
                .add("userId", userId)
                .add("password", password)
                .add("smsText", smsText)
                .add("commaSeperatedReceiverNumbers", mobileNos)
                .build();
        return formBody
    }
    protected RequestBody sendSmsUrlElitBuzz(String smsText, String mobileNos, String senderId, String msgType) {
        RequestBody formBody = new FormBody.Builder()
                .add("api_key", "R60002055a38aab9b7a4a7.24515993")
                .add("type", msgType)
                .add("senderid", senderId)
                .add("msg", smsText)
                .add("contacts", mobileNos)
                .build();
        return formBody
    }
    public boolean isAlphaNumeric(String s){
        return Pattern.matches(REGEX_PATTERN, s);
    }
    String getOperatorFrmNumber(String senderId) {
        if (!senderId) return "elitBuzz"
        if (senderId.equals("03590000043")) {
            return "powerSms"
        } else if (senderId.equals("03590006485")) {
            return "mySms"
        }else {
            return "elitBuzz"
        }
        return "elitBuzz"
    }

    private static final String REGEX_PATTERN = "[\\p{ASCII}]+";
    public static final String SEND_SMS_API = "/sendsms"
    public static final String POWER_SMS_API_ENDPOINT = "https://powersms.banglaphone.net.bd/httpapi"
    public static final String MY_SMS_API_ENDPOINT = "http://mysms.powersms.net.bd/httpapi"

    public static final String ELIT_BUZZ_SMS_API_ENDPOINT = "http://bangladeshsms.com"
    public static final String ELIT_BUZZ_SEND_SMS_API = "/smsapi"
    public static final String ELIT_BUZZ_SMS_HISTORY_API = "/miscapi"
}
