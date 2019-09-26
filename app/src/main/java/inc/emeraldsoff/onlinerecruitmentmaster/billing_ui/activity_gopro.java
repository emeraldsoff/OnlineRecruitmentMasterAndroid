package inc.emeraldsoff.onlinerecruitmentmaster.billing_ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.perf.metrics.Trace;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import es.dmoral.toasty.Toasty;
import inc.emeraldsoff.onlinerecruitmentmaster.R;
import inc.emeraldsoff.onlinerecruitmentmaster.activity_main;
import inc.emeraldsoff.onlinerecruitmentmaster.ui_data.fragment_Home.activity_home;

import static com.crashlytics.android.Crashlytics.TAG;
import static inc.emeraldsoff.onlinerecruitmentmaster.Constants.FAKE_PURCHACE_ID;
import static inc.emeraldsoff.onlinerecruitmentmaster.Constants.HALFYEARLY;
import static inc.emeraldsoff.onlinerecruitmentmaster.Constants.MONTHLY;
import static inc.emeraldsoff.onlinerecruitmentmaster.Constants.QUATERLY;
import static inc.emeraldsoff.onlinerecruitmentmaster.Constants.YEARLY;

public class activity_gopro extends activity_main implements BillingProcessor.IBillingHandler {

    private static final String MERCHANT_ID = "14477458231036107980";
    private static final String Base_64_RSA_Public_Key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQ" +
            "EA1uIThBzm0tEyS+HkdzrwtrdNMipo55QxtSdLauk9gCaMLGVZ6+//ELGhwKeZ0mFx+fUqVEFWah2wLUkEuWakn" +
            "KZFs373Y2jgvxB9VTKyjkB+irdGxl7WfnX6FcVP8ZoF15hVzweryfsnUqa0B34xGq4o4oU34MKpSJpQq00S3Sbn" +
            "WkC0JP944AEqtIPJrNloWOio94pc5572uHHkFCEPJOPZZ6hUmuLQ+y2rziAB23Lod7SizT3uTwCYpD+DEhRPgGE" +
            "4btFLZM3zaAJaRxDffiPshkV0uYPFtaL7pURD9HM41s7Jd0DX9sQcJ94przVVTEKawQFTDiNtEqj16YLtuQIDAQAB";
    Trace trace;
    private RadioGroup subscription;
    private Button pay;
    private TextView validinfo;
    private Context mcontext;
    private SharedPreferences mpref;
    private int x = 0;
    private FirebaseFirestore fdb = FirebaseFirestore.getInstance();
    private boolean readyToPurchase = false;
    private BillingProcessor bp;
    private String PURCHASE_ID;
//    BillingManager billingManager;

    @Override
    protected void onStart() {
        super.onStart();
        try {
            datcal();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.analytics();
        super.trace(trace);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gopro);
        super.menucreate();
        mcontext = this;
        mpref = getSharedPreferences("User", MODE_PRIVATE);
//        billinginit();
        setupapppayment();

    }

//    public void billinginit() {
//        bp = new BillingProcessor(mcontext,Base_64_RSA_Public_Key+"", this);
//        bp.initialize();
//    }

    private void setupapppayment() {
        subscription = findViewById(R.id.subscription);
        pay = findViewById(R.id.pay);
        Button unpay = findViewById(R.id.unpay);
        validinfo = findViewById(R.id.info);
        Button sync = findViewById(R.id.sync);
//        mpref = getSharedPreferences("User", MODE_PRIVATE);

        if (super.admin_tester()) {
            unpay.setVisibility(View.VISIBLE);
            unpay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (activity_gopro.super.admin_tester()) {
//                        mpref = getSharedPreferences("User", MODE_PRIVATE);
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        String exp_date = mpref.getString("ExpiryDate", "");
                        Date finalexp = null;
                        try {
                            finalexp = format.parse(exp_date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(finalexp);
                        calendar.add(Calendar.DAY_OF_YEAR, -30);
                        Date newDate = calendar.getTime();
//                        String valid = format.format(now);
                        final String ExpiryDate = format.format(newDate);
                        calendar.add(Calendar.DAY_OF_YEAR, -30);
                        Date newDate2 = calendar.getTime();
                        final String valid = format.format(newDate2);
                        SharedPreferences.Editor editor = mpref.edit();
                        editor.putString("ValidityDate", valid);
                        editor.putString("ExpiryDate", ExpiryDate);
                        editor.apply();
//                    String collection = "prospect_users" + "/" + mpref.getString("userID","") + "/" + "prospect_users";
                        DocumentReference user = fdb.document("prospect_users" + "/" + mpref.getString("userID", ""));
                        user.update("ExpiryDate", ExpiryDate);
                        user.update("ValidityDate", valid)
                                .isSuccessful();
                        Toasty.success(mcontext, "App Subscription updated to: " + ExpiryDate,
                                Toast.LENGTH_LONG, true).show();
                    }
                }
            });
            x = 30;
            PURCHASE_ID = FAKE_PURCHACE_ID;
//            Toasty.info(mcontext,"You will get "+x+" days of ad-free service",
//                    Toast.LENGTH_LONG,true).show();
        }

        subscription.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.onemonth:
                        x = 30;
                        PURCHASE_ID = MONTHLY;
//                        PURCHASE_ID = "30days_prospect";
                        Toasty.info(mcontext, "You will get " + x + " days of ad-free service",
                                Toast.LENGTH_LONG, true).show();
                        break;

                    case R.id.threemonth:
                        x = 90;
//                        PURCHASE_ID = threemon_PURCHACE_ID;
                        PURCHASE_ID = QUATERLY;
//                        PURCHASE_ID = "90days_prospects";
                        Toasty.info(mcontext, "You will get " + x + " days of ad-free service",
                                Toast.LENGTH_LONG, true).show();
                        break;

                    case R.id.sixmonth:
                        x = 180;
//                        PURCHASE_ID = sixmon_PURCHACE_ID;
                        PURCHASE_ID = HALFYEARLY;
//                        PURCHASE_ID = "180days_prospects";
                        Toasty.info(mcontext, "You will get " + x + " days of ad-free service",
                                Toast.LENGTH_LONG, true).show();
                        break;

                    case R.id.twelvemonth:
                        x = 365;
//                        PURCHASE_ID = oneyear_PURCHACE_ID;
                        PURCHASE_ID = YEARLY;
//                        PURCHASE_ID = "365days_prospects";
                        Toasty.info(mcontext, "You will get " + x + " days of ad-free service",
                                Toast.LENGTH_LONG, true).show();
                        break;
                }
            }
        });

        bp = new BillingProcessor(mcontext, Base_64_RSA_Public_Key + "", MERCHANT_ID + "", this);
        bp.initialize();


        pay.setEnabled(false);

        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fdb.document("prospect_users" + "/" + mpref.getString("userID", ""))
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();
                            if (doc != null && doc.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + doc.getData());
                                //                                    StringBuilder uidf = new StringBuilder();
                                //                                    StringBuilder usrf = new StringBuilder();
                                //                                    StringBuilder validtime = new StringBuilder();
                                StringBuilder ufname = new StringBuilder();
                                StringBuilder umname = new StringBuilder();
                                StringBuilder ulname = new StringBuilder();
                                StringBuilder validitydate = new StringBuilder();
                                StringBuilder ifval = new StringBuilder();
                                StringBuilder expire = new StringBuilder();
                                StringBuilder install = new StringBuilder();
                                StringBuilder uemail = new StringBuilder();
                                StringBuilder uphone = new StringBuilder();
//                                            StringBuilder secure = new StringBuilder();
//                                            StringBuilder pin = new StringBuilder();

//                                            uidf.append(doc.get("UserID"));
//                                            usrf.append(doc.get("user"));
//                                            validtime.append(doc.get("ValidTime"));
                                validitydate.append(doc.get("ValidityDate"));
                                install.append(doc.get("InstallDate"));
                                expire.append(doc.get("ExpiryDate"));
                                ufname.append(doc.get("FirstName"));
                                umname.append(doc.get("MiddleName"));
                                ulname.append(doc.get("LastName"));
                                uemail.append(doc.get("EmailId"));
                                uphone.append(doc.get("MobileNo"));
                                ifval.append(doc.getBoolean("IF_VALID"));
//                                            secure.append(doc.getBoolean("IF_SECURE"));
//                                            pin.append(doc.get("PIN"));

                                Boolean b = Boolean.parseBoolean(String.valueOf(ifval));
//                                            Boolean s = Boolean.parseBoolean(String.valueOf(secure));
//                                String pno = "+917003564171";
                                //                                    SharedPreferences mPreferences;
                                mpref = getSharedPreferences("User", MODE_PRIVATE);
                                SharedPreferences.Editor editor = mpref.edit();
                                //                                editor.putString("userID", userID);
                                //                                editor.putString("user", String.valueOf(user));
                                editor.putString("FirstName", String.valueOf(ufname));
                                editor.putString("MiddleName", String.valueOf(umname));
                                editor.putString("LastName", String.valueOf(ulname));
                                editor.putString("MobileNo", String.valueOf(uphone));
                                editor.putString("EmailId", String.valueOf(uemail));

                                //                                    editor.putString("ValidTime", String.valueOf(validtime));
                                editor.putString("ValidityDate", String.valueOf(validitydate));
                                editor.putString("InstallDate", String.valueOf(install));
                                editor.putString("ExpiryDate", String.valueOf(expire));
                                editor.putBoolean("IF_VALID", b);
                                //                                editor.putBoolean("IF_SECURE", false);
                                //                                editor.putString("PIN", "");
                                //                                    editor.putBoolean("IF_SECURED", s);
                                //                                    editor.putString("PIN", String.valueOf(pin));
                                editor.apply();
                                try {
                                    datcal();
                                } catch (ParseException e) {
                                    Toasty.error(mcontext, "Something went wrong..!!",
                                            Toast.LENGTH_LONG, true).show();
//                                    e.printStackTrace();
                                }
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                            Toasty.error(mcontext, "Something went wrong..!!",
                                    Toast.LENGTH_LONG, true).show();
                        }
                    }
                });
            }
        });

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (readyToPurchase) {
//                    billingManager.initiatePurchaseFlow(PURCHASE_ID, null, BillingClient.SkuType.INAPP);
                    bp.purchase(activity_gopro.this, PURCHASE_ID);
                    try {
                        datcal();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
//                    bp.subscribe(premiumActivity.this, PURCHASE_ID);
//                    bp.purchase(premiumActivity.this, "30days_activation");
                } else {
                    Toasty.error(mcontext, "Unable to initiate purchase",
                            Toast.LENGTH_SHORT, true).show();
                }
            }
        });

        //        x = 1;
//        PURCHASE_ID = FAKE_PURCHACE_ID;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPurchaseHistoryRestored() {
//        updatePurchase();
    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {
        Toasty.error(this, "Unable to process billing", Toast.LENGTH_SHORT, true).show();
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
////        updatePurchase();
//    }

    @Override
    public void onProductPurchased(@NonNull String productId, TransactionDetails details) {
        Toasty.success(this, "Thanks for your Purchased!", Toast.LENGTH_SHORT, true).show();
        try {
            getPurchase();
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        updatePurchase();
    }

//    private void updatePurchase() {
//        if (bp.isPurchased(PURCHASE_ID)) {
////            tvResult.setText("THANK YOU!");
//        }
//    }

    private void getPurchase() throws ParseException {
        if (bp.isPurchased(PURCHASE_ID)) {

            mpref = getSharedPreferences("User", MODE_PRIVATE);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String exp_date = mpref.getString("ExpiryDate", "");
            Date finalexp = format.parse(exp_date);
            Date now = new Date();
//
            int day = 1000 * 60 * 60 * 24;
            int daysleft = (int) ((finalexp.getTime() - now.getTime()) / day);

            if (daysleft > 0) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(finalexp);

                switch (PURCHASE_ID) {
                    case "30days_activation": {
                        calendar.add(Calendar.DAY_OF_YEAR, +30);
                        Date newDate = calendar.getTime();
                        final String ExpiryDate = format.format(newDate);
                        SharedPreferences.Editor editor = mpref.edit();
//                    editor.putString("ValidityDate", InstallDate);
                        editor.putString("ExpiryDate", ExpiryDate);
                        editor.apply();
//                    String collection = "prospect_users" + "/" + mpref.getString("userID","") + "/" + "prospect_users";
                        DocumentReference user = fdb.document("prospect_users" + "/" + mpref.getString("userID", ""));
                        user.update("ExpiryDate", ExpiryDate)
                                .isSuccessful();
                        Toasty.success(mcontext, "App Subscription updated to: " + ExpiryDate,
                                Toast.LENGTH_LONG, true).show();

                        break;
                    }
                    case "90days_activation": {
                        calendar.add(Calendar.DAY_OF_YEAR, +90);
                        Date newDate = calendar.getTime();
                        final String ExpiryDate = format.format(newDate);
                        SharedPreferences.Editor editor = mpref.edit();
//                    editor.putString("ValidityDate", InstallDate);
                        editor.putString("ExpiryDate", ExpiryDate);
                        editor.apply();
//                    String collection = "prospect_users" + "/" + mpref.getString("userID","") + "/" + "prospect_users";
                        DocumentReference user = fdb.document("prospect_users" + "/" + mpref.getString("userID", ""));
                        user.update("ExpiryDate", ExpiryDate)
                                .isSuccessful();
                        Toasty.success(mcontext, "App Subscription updated to: " + ExpiryDate,
                                Toast.LENGTH_LONG, true).show();
                        break;
                    }
                    case "180days_activation": {
                        calendar.add(Calendar.DAY_OF_YEAR, +180);
                        Date newDate = calendar.getTime();
                        final String ExpiryDate = format.format(newDate);
                        SharedPreferences.Editor editor = mpref.edit();
//                    editor.putString("ValidityDate", InstallDate);
                        editor.putString("ExpiryDate", ExpiryDate);
                        editor.apply();
//                    String collection = "prospect_users" + "/" + mpref.getString("userID","") + "/" + "prospect_users";
                        DocumentReference user = fdb.document("prospect_users" + "/" + mpref.getString("userID", ""));
                        user.update("ExpiryDate", ExpiryDate)
                                .isSuccessful();
                        Toasty.success(mcontext, "App Subscription updated to: " + ExpiryDate,
                                Toast.LENGTH_LONG, true).show();
                        break;
                    }
                    case "365days_activation": {
                        calendar.add(Calendar.DAY_OF_YEAR, +365);
                        Date newDate = calendar.getTime();
                        final String ExpiryDate = format.format(newDate);
                        SharedPreferences.Editor editor = mpref.edit();
//                    editor.putString("ValidityDate", InstallDate);
                        editor.putString("ExpiryDate", ExpiryDate);
                        editor.apply();
//                    String collection = "prospect_users" + "/" + mpref.getString("userID","") + "/" + "prospect_users";
                        DocumentReference user = fdb.document("prospect_users" + "/" + mpref.getString("userID", ""));
                        user.update("ExpiryDate", ExpiryDate)
                                .isSuccessful();
                        Toasty.success(mcontext, "App Subscription updated to: " + ExpiryDate,
                                Toast.LENGTH_LONG, true).show();
                        break;
                    }
                    case FAKE_PURCHACE_ID: {
                        calendar.add(Calendar.DAY_OF_YEAR, +30);
                        Date newDate = calendar.getTime();
                        final String ExpiryDate = format.format(newDate);
                        SharedPreferences.Editor editor = mpref.edit();
//                    editor.putString("ValidityDate", InstallDate);
                        editor.putString("ExpiryDate", ExpiryDate);
                        editor.apply();
//                    String collection = "prospect_users" + "/" + mpref.getString("userID","") + "/" + "prospect_users";
                        DocumentReference user = fdb.document("prospect_users" + "/" + mpref.getString("userID", ""));
                        user.update("ExpiryDate", ExpiryDate)
                                .isSuccessful();
                        Toasty.success(mcontext, "App Subscription updated to: " + ExpiryDate,
                                Toast.LENGTH_LONG, true).show();
                        break;
                    }
                }

            } else if (daysleft < 0) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(now);

                switch (PURCHASE_ID) {
                    case "30days_activation": {
                        calendar.add(Calendar.DAY_OF_YEAR, +30);
                        Date newDate = calendar.getTime();
                        String valid = format.format(now);
                        final String ExpiryDate = format.format(newDate);
                        SharedPreferences.Editor editor = mpref.edit();
                        editor.putString("ValidityDate", valid);
                        editor.putString("ExpiryDate", ExpiryDate);
                        editor.apply();
//                    String collection = "prospect_users" + "/" + mpref.getString("userID","") + "/" + "prospect_users";
                        DocumentReference user = fdb.document("prospect_users" + "/" + mpref.getString("userID", ""));
                        user.update("ExpiryDate", ExpiryDate);
                        user.update("ValidityDate", valid)
                                .isSuccessful();
                        Toasty.success(mcontext, "App Subscription updated to: " + ExpiryDate,
                                Toast.LENGTH_LONG, true).show();

                        break;
                    }
                    case "90days_activation": {
                        calendar.add(Calendar.DAY_OF_YEAR, +90);
                        Date newDate = calendar.getTime();
                        String valid = format.format(now);
                        final String ExpiryDate = format.format(newDate);
                        SharedPreferences.Editor editor = mpref.edit();
                        editor.putString("ValidityDate", valid);
                        editor.putString("ExpiryDate", ExpiryDate);
                        editor.apply();
//                    String collection = "prospect_users" + "/" + mpref.getString("userID","") + "/" + "prospect_users";
                        DocumentReference user = fdb.document("prospect_users" + "/" + mpref.getString("userID", ""));
                        user.update("ExpiryDate", ExpiryDate);
                        user.update("ValidityDate", valid)
                                .isSuccessful();
                        Toasty.success(mcontext, "App Subscription updated to: " + ExpiryDate,
                                Toast.LENGTH_LONG, true).show();
                        break;
                    }
                    case "180days_activation": {
                        calendar.add(Calendar.DAY_OF_YEAR, +180);
                        Date newDate = calendar.getTime();
                        String valid = format.format(now);
                        final String ExpiryDate = format.format(newDate);
                        SharedPreferences.Editor editor = mpref.edit();
                        editor.putString("ValidityDate", valid);
                        editor.putString("ExpiryDate", ExpiryDate);
                        editor.apply();
//                    String collection = "prospect_users" + "/" + mpref.getString("userID","") + "/" + "prospect_users";
                        DocumentReference user = fdb.document("prospect_users" + "/" + mpref.getString("userID", ""));
                        user.update("ExpiryDate", ExpiryDate);
                        user.update("ValidityDate", valid)
                                .isSuccessful();
                        Toasty.success(mcontext, "App Subscription updated to: " + ExpiryDate,
                                Toast.LENGTH_LONG, true).show();
                        break;
                    }
                    case "365days_activation": {
                        calendar.add(Calendar.DAY_OF_YEAR, +365);
                        Date newDate = calendar.getTime();
                        String valid = format.format(now);
                        final String ExpiryDate = format.format(newDate);
                        SharedPreferences.Editor editor = mpref.edit();
                        editor.putString("ValidityDate", valid);
                        editor.putString("ExpiryDate", ExpiryDate);
                        editor.apply();
//                    String collection = "prospect_users" + "/" + mpref.getString("userID","") + "/" + "prospect_users";
                        DocumentReference user = fdb.document("prospect_users" + "/" + mpref.getString("userID", ""));
                        user.update("ExpiryDate", ExpiryDate);
                        user.update("ValidityDate", valid)
                                .isSuccessful();
                        Toasty.success(mcontext, "App Subscription updated to: " + ExpiryDate,
                                Toast.LENGTH_LONG, true).show();
                        break;
                    }
                    case FAKE_PURCHACE_ID: {
                        calendar.add(Calendar.DAY_OF_YEAR, +30);
                        Date newDate = calendar.getTime();
                        String valid = format.format(now);
                        final String ExpiryDate = format.format(newDate);
                        SharedPreferences.Editor editor = mpref.edit();
                        editor.putString("ValidityDate", valid);
                        editor.putString("ExpiryDate", ExpiryDate);
                        editor.apply();
//                    String collection = "prospect_users" + "/" + mpref.getString("userID","") + "/" + "prospect_users";
                        DocumentReference user = fdb.document("prospect_users" + "/" + mpref.getString("userID", ""));
                        user.update("ExpiryDate", ExpiryDate);
                        user.update("ValidityDate", valid)
                                .isSuccessful();
                        Toasty.success(mcontext, "App Subscription updated to: " + ExpiryDate,
                                Toast.LENGTH_LONG, true).show();
                        break;
                    }
                }
            } else {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(finalexp);

                switch (PURCHASE_ID) {
                    case "30days_activation": {
                        calendar.add(Calendar.DAY_OF_YEAR, +30);
                        Date newDate = calendar.getTime();
                        final String ExpiryDate = format.format(newDate);
                        SharedPreferences.Editor editor = mpref.edit();
//                    editor.putString("ValidityDate", InstallDate);
                        editor.putString("ExpiryDate", ExpiryDate);
                        editor.apply();
//                    String collection = "prospect_users" + "/" + mpref.getString("userID","") + "/" + "prospect_users";
                        DocumentReference user = fdb.document("prospect_users" + "/" + mpref.getString("userID", ""));
                        user.update("ExpiryDate", ExpiryDate)
                                .isSuccessful();
                        Toasty.success(mcontext, "App Subscription updated to: " + ExpiryDate,
                                Toast.LENGTH_LONG, true).show();

                        break;
                    }
                    case "90days_activation": {
                        calendar.add(Calendar.DAY_OF_YEAR, +90);
                        Date newDate = calendar.getTime();
                        final String ExpiryDate = format.format(newDate);
                        SharedPreferences.Editor editor = mpref.edit();
//                    editor.putString("ValidityDate", InstallDate);
                        editor.putString("ExpiryDate", ExpiryDate);
                        editor.apply();
//                    String collection = "prospect_users" + "/" + mpref.getString("userID","") + "/" + "prospect_users";
                        DocumentReference user = fdb.document("prospect_users" + "/" + mpref.getString("userID", ""));
                        user.update("ExpiryDate", ExpiryDate)
                                .isSuccessful();
                        Toasty.success(mcontext, "App Subscription updated to: " + ExpiryDate,
                                Toast.LENGTH_LONG, true).show();
                        break;
                    }
                    case "180days_activation": {
                        calendar.add(Calendar.DAY_OF_YEAR, +180);
                        Date newDate = calendar.getTime();
                        final String ExpiryDate = format.format(newDate);
                        SharedPreferences.Editor editor = mpref.edit();
//                    editor.putString("ValidityDate", InstallDate);
                        editor.putString("ExpiryDate", ExpiryDate);
                        editor.apply();
//                    String collection = "prospect_users" + "/" + mpref.getString("userID","") + "/" + "prospect_users";
                        DocumentReference user = fdb.document("prospect_users" + "/" + mpref.getString("userID", ""));
                        user.update("ExpiryDate", ExpiryDate)
                                .isSuccessful();
                        Toasty.success(mcontext, "App Subscription updated to: " + ExpiryDate,
                                Toast.LENGTH_LONG, true).show();
                        break;
                    }
                    case "365days_activation": {
                        calendar.add(Calendar.DAY_OF_YEAR, +365);
                        Date newDate = calendar.getTime();
                        final String ExpiryDate = format.format(newDate);
                        SharedPreferences.Editor editor = mpref.edit();
//                    editor.putString("ValidityDate", InstallDate);
                        editor.putString("ExpiryDate", ExpiryDate);
                        editor.apply();
//                    String collection = "prospect_users" + "/" + mpref.getString("userID","") + "/" + "prospect_users";
                        DocumentReference user = fdb.document("prospect_users" + "/" + mpref.getString("userID", ""));
                        user.update("ExpiryDate", ExpiryDate)
                                .isSuccessful();
                        Toasty.success(mcontext, "App Subscription updated to: " + ExpiryDate,
                                Toast.LENGTH_LONG, true).show();
                        break;
                    }
                    case FAKE_PURCHACE_ID: {
                        calendar.add(Calendar.DAY_OF_YEAR, +30);
                        Date newDate = calendar.getTime();
                        final String ExpiryDate = format.format(newDate);
                        SharedPreferences.Editor editor = mpref.edit();
//                    editor.putString("ValidityDate", InstallDate);
                        editor.putString("ExpiryDate", ExpiryDate);
                        editor.apply();
//                    String collection = "prospect_users" + "/" + mpref.getString("userID","") + "/" + "prospect_users";
                        DocumentReference user = fdb.document("prospect_users" + "/" + mpref.getString("userID", ""));
                        user.update("ExpiryDate", ExpiryDate)
                                .isSuccessful();
                        Toasty.success(mcontext, "App Subscription updated to: " + ExpiryDate,
                                Toast.LENGTH_LONG, true).show();
                        break;
                    }
                }

            }

//            tvResult.setText("THANK YOU!");
        }
    }

    @Override
    public void onBillingInitialized() {
        readyToPurchase = true;
        pay.setEnabled(true);

//        updatePurchase();
    }


    @SuppressLint("SetTextI18n")
    private void datcal() throws ParseException {
        mpref = getSharedPreferences("User", MODE_PRIVATE);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//        Calendar calendar = Calendar.getInstance();
//        Calendar calendar1 = Calendar.getInstance();
//        calendar.setTime(now);
//        calendar.add(Calendar.DAY_OF_YEAR, +60);
//        Date newDate = calendar.getTime();
//        String CurrentDate = formatter.format(newDate);
        String exp_date = mpref.getString("ExpiryDate", "");
        Date finalexp = format.parse(exp_date);
        Date now = new Date();
//
        int day = 1000 * 60 * 60 * 24;
        int daysleft = (int) ((finalexp.getTime() - now.getTime()) / day);

        if (daysleft >= 0) {
            if (daysleft > 0) {
                validinfo.setText("Your app will expire in " + daysleft + " days");

//            StyleableToast.makeText(mcontext, "Your app will expire in " + daysleft + " days",
//                    Toast.LENGTH_LONG, R.style.notification).show();
            } else {
                validinfo.setText("Your app will expire today at 11:59:59 pm");
            }
        } else {
            SharedPreferences.Editor editor = mpref.edit();
            editor.putBoolean("IF_VALID", false);
//            editor.putString("ValidTime", String.valueOf(daysleft));
            editor.apply();
            subscription.setTouchscreenBlocksFocus(true);
            validinfo.setText("Your app has expired..!!");
//            validinfo.setVisibility(View.GONE);
//            StyleableToast.makeText(mcontext, "Your app has expired..!!",
//                    Toast.LENGTH_LONG, R.style.notification).show();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(mcontext, activity_home.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        isDestroyed();
        finish();
    }
}
