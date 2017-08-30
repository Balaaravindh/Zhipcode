package com.falconnect.zipcode;

import static com.falconnect.zipcode.ConstantIP.CONSTANT_IP;

public class ConstantAPI {

    //Login URL
    public static final String LOGIN_API = CONSTANT_IP + "messengers/login/";

    //Register
    public static final String REGISTER_API =  CONSTANT_IP + "messengers/";

    //Profile_page
    public static final String PROFILE_API =  CONSTANT_IP + "messengers/";

    //Edit
    public static final String EDIT_PROFILE_NUMBER =  CONSTANT_IP + "messengers/";
    public static final String EDIT_PROFILE_NAME =  CONSTANT_IP + "messengers/";
    public static final String PAYMENTS = CONSTANT_IP + "payments/";
    public static final String DASHBOARD = CONSTANT_IP + "errands/available/";
    public static final String ERRAND_ASSIGN = CONSTANT_IP + "errands/";
    public static final String CANCEL_ERRANDS = CONSTANT_IP + "errands/";
    public static final String COMPLETED = CONSTANT_IP + "errands/completed/";


    public static final String CANCEL_ERRANDS_NOEND = CONSTANT_IP + "errands/";

    //SMS Verification
    public static final String SMS = CONSTANT_IP + "verifications/";
    public static final String VERIFY_SMS = CONSTANT_IP + "verifications/validate_code/";

    //Ruta reorder
    public static final String RUTA_API = CONSTANT_IP + "errands/";

}