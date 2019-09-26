package inc.emeraldsoff.onlinerecruitmentmaster;

import android.os.Environment;

import java.io.File;

/**
 * Created by dev on 11/20/17.
 */

public class Constants {
    //GOOGLE DRIVE

    public static final int REQUEST_CODE_SIGN_IN = 0;
    public static final int REQUEST_CODE_OPENING = 1;
    public static final int REQUEST_CODE_CREATION = 2;
    public static final int REQUEST_CODE_PERMISSIONS = 2;

    //PERMISSION CODES
    public static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    public static final int MY_PERMISSIONS_READ_EXTERNAL_STORAGE = 0;
    public static final int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 0;
    public static final int MY_PERMISSIONS_CALL_PHONE = 0;
    public static final int MY_PERMISSIONS_REQUEST_RECEIVE_SMS = 0;
    public static final int MY_PERMISSIONS_REQUEST_ACCESS_NETWORK_STATE = 0;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 0;
    public static final int MY_PERMISSIONS_READ_PHONE_STATE = 0;
    public static final int MY_PERMISSIONS_READ_PHONE_NUMBERS = 0;
    public static final int MY_PERMISSIONS_READ_CONTACT = 0;
    public static final int MY_MS_CRASH_PERMISSION = 1;
    public static final int ASK_MULTIPLE_PERMISSION_REQUEST_CODE = 6;



    //GOOGLE PAY ID
    public static final String MONTHLY = "30days_activation";
    public static final String QUATERLY = "90days_activation";
    public static final String HALFYEARLY = "180days_activation";
    public static final String YEARLY = "365days_activation";
    public static final String FAKE_PURCHACE_ID = "android.test.purchased";


    //OAuth Details
    // For GDrive
    public static final String gd_client_id ="700436683586-qk0dj2fur49vl1ta1ofptgo9adbsnrp3.apps.googleusercontent.com";
    public static final String gd_client_secret = "zLKnl3ZgxUqpp8BUyX9RorDc";


    //DATABASE DETAILS
    public static final String DATABASE_EXTERNAL_PATH = Environment.getExternalStorageDirectory() + File.separator + "MegaProspects"
            + File.separator + "databases";
    public static final String DATABASE_INTERNAL_PATH = File.separator + "data" + File.separator +
            "data" + File.separator + "inc.emeraldsoff.megaprospectsnext" + File.separator + "databases";
    public static final String DATABASE_EXTERNAL_PATH_ZIP_NAME = DATABASE_EXTERNAL_PATH + File.separator +"megaprospects_backup"+".zip";
    public static final String DATABASE_INTERNAL_PATH_DB = DATABASE_INTERNAL_PATH+File.separator+"megaprospectsnext"+".db";
    private static final String TAG = "Google Drive Activity";
    //variable for decide if i need to do a backup or a restore.
    //True stands for backup, False for restore
    //To be transferred to the reqd activity
    private boolean isBackup = true;


    //AppCenter
    public static final String appsecret = "bbcf040f-8803-43ba-b9d5-878adb5e4ea6";

    //URL Links
    public static final String play_store_app_link = "https://play.google.com/store/apps/details?id=inc.emeraldsoff.megaprospectsnext";
    public static final String version_code_fetch_link = "https://github.com/emeraldsoff/EmeraldSoff/blob/master/docs/Mega_Prospects_Next/docs/updates.xml";
    public static final String version_code_fetch_link_test = "https://github.com/emeraldsoff/EmeraldSoff/blob/master/docs/Mega_Prospects_Next/docs/debugpro.xml";
    public static final String github_app_link = "https://emeraldsoff.github.io/EmeraldSoff/Mega_Prospects_Next/release/app-release.apk";
    public static final String github_app_link_test = "https://emeraldsoff.github.io/EmeraldSoff/Mega_Prospects_Next/debugpro/app-release.apk";
    public static final String privacy_policy = "https://emeraldsoff.github.io/EmeraldSoff/Mega_Prospects_Next/docs/privacy_policy.html";

    //FIREBASE REMOTE CONFIG
//    public static final String KEY_UPDATE_REQUIRED = "force_update_required";
//    public static final String KEY_CURRENT_VERSION = "force_update_current_version";
//    public static final String KEY_UPDATE_URL = "force_update_store_url";


    //ADS APP IDS
    public static final String admob_app_test = "ca-app-pub-3940256099942544/1033173712";
    public static final String admob_app_id = "ca-app-pub-5856240631845937~6964843037";

    //BANNER ADS IDS
    public static final String test_banner = "ca-app-pub-3940256099942544/6300978111";
    public static final String setting_banner = "ca-app-pub-5856240631845937/3659597818";
    public static final String editdata_banner = "ca-app-pub-5856240631845937/7982899903";
    public static final String home_banner = "ca-app-pub-5856240631845937/9759016007";
    public static final String addclient_banner = "ca-app-pub-5856240631845937/1688954292";
    public static final String showdata_banner = "ca-app-pub-5856240631845937/4861912541";
    public static final String showdata_vivid_banner = "ca-app-pub-5856240631845937/1503738575";
    public static final String aboutus_banner = "ca-app-pub-5856240631845937/1716874450";
    public static final String premium_banner = "ca-app-pub-5856240631845937/7580054153";

    //INTERSTITIAL ADS IDS
    public static final String test_interstitial = "ca-app-pub-3940256099942544/1033173712";
    public static final String home_interstitial = "ca-app-pub-5856240631845937/9332640988";
}
