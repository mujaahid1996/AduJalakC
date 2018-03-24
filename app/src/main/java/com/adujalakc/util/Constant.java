package com.adujalakc.util;

/**
 * Created by Anadara on 12/06/2017.
 */

public class Constant {

    public static final String IP = "http://" + "192.168.56.1:80";
//    public static final String IP = "http://" + "192.168.43.170:80";//wifi
    public static final String DIR = "/adujalak/";

    public static final String GET_LIST_COM = IP + DIR + "_getComplaintC.php";
    public static final String ACC_COMP = IP + DIR + "_accComplaintC.php";
    public static final String GET_ACC_COM = IP + DIR + "_getAccComplaintC.php?id_user=";
    public static final String GET_COM_OVR = IP + DIR + "_getComplaintOverC.php?id_user=";

    public static final String SEARCH_LIST_COM = IP + DIR + "_getSearchComplaintC.php?cLocation=";
     public static final String SEARCH_ACC_COM = IP + DIR + "_getSearchAccComplaintC.php?id_user=";
    public static final String SEARCH_COM_OVR = IP + DIR + "_getSearchComplaintOverC.php?id_user=";

    public static final String URL_IMG_COMPLAINT = IP + DIR + "images_complaint/";
    public static final String URL_IMG_CONFIRM = IP + DIR + "images_confirm/";

    //key for session
    public final static String GET_USER = IP + DIR + "_getUserC.php?user_username=";

    public final static String LOGIN = IP + DIR + "_loginC.php";
//    public final static String REGISTER = IP + DIR + "_registerC.php";

    public static final String UPLOAD_URL = IP + DIR + "_uploadC.php";
    public static final String IMG_DIR = IP + DIR + "images_confirm/";

    //key for param PHP
//    public static final String PARAM_ACCNT_ID = "account_id";
    public static final String PARAM_COMPLAINT_ID = "complaint_id";
    public static final String PARAM_COMPLAINT_DATE = "cDate";
    //    public static final String PARAM_COMPLAINT_LOCATION = "cLocation";
    public static final String PARAM_UPLOAD_FILE = "image";
    public static final String PARAM_UPLOAD_DESCRIPTION = "name";

}
