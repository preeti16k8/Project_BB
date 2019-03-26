package com.kre8tives.bareboneneww.Util;

/**
 * Created by ASUS on 08-12-2016.
 */

public class Config {

    //URLs to register.php and confirm.php file
    public static final String REGISTER_URL = "http://riterp.com/barebones/user_registrationapi.php";
    public static final String CONFIRM_URL = "http://simplifiedcoding.16mb.com/AndroidOTP/confirm.php";

    //Keys to send username, password, phone and otp
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_OTP = "otp";

    //JSON Tag from response from server
    public static final String TAG_RESPONSE= "ErrorMessage";

    // server URL configuration
    public static final String URL_REQUEST_SMS = "http://192.168.0.101:8888/android_sms/msg91/request_sms.php";
    public static final String URL_VERIFY_OTP = "http://192.168.0.101:8888/android_sms/msg91/verify_otp.php";

    // SMS provider identification
    // It should match with your SMS gateway origin
    // You can use  MSGIND, TESTER and ALERTS as sender ID
    // If you want custom sender Id, approve MSG91 to get one
    public static final String SMS_ORIGIN = "AD-RYTCRM";

    // special character to prefix the otp. Make sure this character appears only once in the sms
    public static final String OTP_DELIMITER = ":";


}