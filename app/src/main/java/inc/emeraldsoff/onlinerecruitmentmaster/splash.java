package inc.emeraldsoff.onlinerecruitmentmaster;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;
import com.microsoft.appcenter.push.Push;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import inc.emeraldsoff.onlinerecruitmentmaster.appcontrol_ui.activity_entry;
import inc.emeraldsoff.onlinerecruitmentmaster.login.activity_login;
import inc.emeraldsoff.onlinerecruitmentmaster.login.activity_user_reg;
import inc.emeraldsoff.onlinerecruitmentmaster.ui_data.fragment_Home.activity_home;

import static inc.emeraldsoff.onlinerecruitmentmaster.Constants.DATABASE_EXTERNAL_PATH;
import static inc.emeraldsoff.onlinerecruitmentmaster.Constants.DATABASE_INTERNAL_PATH;

public class splash extends Activity {

    private Context mcontext;
    TextView brand;
    FirebaseAuth.AuthStateListener mAuthlistener;
    FirebaseAuth mAuth;
    String userid;
    private FirebaseFirestore fdb = FirebaseFirestore.getInstance();
    SharedPreferences mpref;

    @Override
    protected void onStart() {
        super.onStart();

        if (isOnline()) {

            check_auth();

        } else {

            recheck_network();

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mpref = getSharedPreferences("User", MODE_PRIVATE);
        //appcenter();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        brand = findViewById(R.id.textView);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Harlow.ttf");
        brand.setTypeface(typeface);

        mcontext = this;
    }

    public void appcenter() {
        AppCenter.setEnabled(true);
        AppCenter.getSdkVersion();
        AppCenter.getInstallId();
        AppCenter.setUserId(mpref.getString("userID", ""));
        String activity = this.getClass().getSimpleName();
        AppCenter.start(getApplication(), "bbcf040f-8803-43ba-b9d5-878adb5e4ea6",
                Push.class, Analytics.class, Crashes.class);
        Analytics.trackEvent(activity + "");
//        AppCenter.start(getApplication(), "bbcf040f-8803-43ba-b9d5-878adb5e4ea6", );
    }

    private void datasync() {
        DocumentReference docref = fdb.document("prospect_users" + "/" + userid);
        docref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if (doc != null && doc.exists()) {
//                        Log.d(TAG, "DocumentSnapshot data: " + doc.getData());
//                                            StringBuilder uidf = new StringBuilder();
//                                            StringBuilder usrf = new StringBuilder();
//                                            StringBuilder validtime = new StringBuilder();
                        StringBuilder ufname = new StringBuilder();
                        StringBuilder umname = new StringBuilder();
                        StringBuilder ulname = new StringBuilder();
                        StringBuilder validitydate = new StringBuilder();
                        StringBuilder ifval = new StringBuilder();
                        StringBuilder user = new StringBuilder();
                        StringBuilder expire = new StringBuilder();
                        StringBuilder install = new StringBuilder();
                        StringBuilder uemail = new StringBuilder();
                        StringBuilder uphone = new StringBuilder();

                        validitydate.append(doc.get("ValidityDate"));
                        install.append(doc.get("InstallDate"));
                        expire.append(doc.get("ExpiryDate"));
                        ufname.append(doc.get("FirstName"));
                        umname.append(doc.get("MiddleName"));
                        ulname.append(doc.get("LastName"));
                        user.append(doc.get("user"));
                        uemail.append(doc.get("EmailId"));
                        uphone.append(doc.get("MobileNo"));
                        ifval.append(doc.getBoolean("IF_VALID"));

                        Boolean b = Boolean.parseBoolean(String.valueOf(ifval));

                        String pno = "+917003564171";
                        if (pno.equals(String.valueOf(uphone))) {
                            mpref = getSharedPreferences("User", MODE_PRIVATE);
                            SharedPreferences.Editor editor = mpref.edit();
                            editor.putString("userID", userid);
                            editor.putString("user", String.valueOf(user));
                            editor.putString("FirstName", String.valueOf(ufname));
                            editor.putString("MiddleName", String.valueOf(umname));
                            editor.putString("LastName", String.valueOf(ulname));
                            editor.putString("MobileNo", String.valueOf(uphone));
                            editor.putString("EmailId", String.valueOf(uemail));
                            editor.putString("ValidityDate", String.valueOf(validitydate));
                            editor.putString("InstallDate", String.valueOf(install));
                            editor.putString("ExpiryDate", String.valueOf(expire));
                            editor.putBoolean("IF_VALID", b);
                            editor.apply();
                        } else {
                            mpref = getSharedPreferences("User", MODE_PRIVATE);
                            SharedPreferences.Editor editor = mpref.edit();
                            editor.putString("userID", userid);
                            editor.putString("user", String.valueOf(user));
                            editor.putString("FirstName", String.valueOf(ufname));
                            editor.putString("MiddleName", String.valueOf(umname));
                            editor.putString("LastName", String.valueOf(ulname));
                            editor.putString("MobileNo", String.valueOf(uphone));
                            editor.putString("EmailId", String.valueOf(uemail));
                            editor.putString("ValidityDate", String.valueOf(validitydate));
                            editor.putString("InstallDate", String.valueOf(install));
                            editor.putString("ExpiryDate", String.valueOf(expire));
                            editor.putBoolean("IF_VALID", b);
                            editor.apply();
                        }
                    }
                }
            }
        });
    }

    private void basic_check() {
        datasync();
        mpref = getSharedPreferences("User", MODE_PRIVATE);
        final String fname, lname, mob;
        fname = mpref.getString("FirstName", "");
        lname = mpref.getString("LastName", "");
        mob = mpref.getString("MobileNo", "");
        if (Objects.requireNonNull(fname).equals("") ||
                Objects.requireNonNull(lname).equals("") ||
                Objects.requireNonNull(mob).equals("")) {
            basic_check1();
        }
    }

    private void basic_check1() {
        datasync();
        mpref = getSharedPreferences("User", MODE_PRIVATE);
        final String fname, lname, mob;
        fname = mpref.getString("FirstName", "");
        lname = mpref.getString("LastName", "");
        mob = mpref.getString("MobileNo", "");
        if (Objects.requireNonNull(fname).equals("") ||
                Objects.requireNonNull(lname).equals("") ||
                Objects.requireNonNull(mob).equals("")) {
            startActivity(new Intent(splash.this, activity_user_reg.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    public void check_auth() {
//        if_restore_reqd();
        mAuth = FirebaseAuth.getInstance();
        userid = mAuth.getUid();
        mpref = getSharedPreferences("User", MODE_PRIVATE);
        final String fname, lname, mob;
        fname = mpref.getString("FirstName", "");
        lname = mpref.getString("LastName", "");
        mob = mpref.getString("MobileNo", "");
        mAuthlistener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    userid = mAuth.getUid();
                    if (Objects.requireNonNull(fname).equals("") ||
                            Objects.requireNonNull(lname).equals("") ||
                            Objects.requireNonNull(mob).equals("")) {
                        try {
                            basic_check();
                        } catch (Exception e) {
//                                    Crashlytics.getInstance();
//                                    Crashlytics.log(e.getMessage());
                        }
                    } else {
                        if (mpref.getBoolean("IF_SECURE", true)) {
                            if (Objects.requireNonNull(mpref.getString("PIN", "")).isEmpty() ||
                                    Objects.requireNonNull(mpref.getString("PIN", "")).equals("")) {
                                startActivity(new Intent(splash.this, activity_home.class));
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                finish();
                            } else {
                                int SPLASH_DISPLAY_LENGTH = 3000;
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        startActivity(new Intent(splash.this, activity_entry.class));
                                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                        finish();
                                    }
                                }, SPLASH_DISPLAY_LENGTH);
                            }
                        } else {
                            int SPLASH_DISPLAY_LENGTH = 3000;
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(splash.this, activity_home.class));
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                    finish();
                                }
                            }, SPLASH_DISPLAY_LENGTH);
                        }
                    }
                } else {
                    int SPLASH_DISPLAY_LENGTH = 3000;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(splash.this, activity_login.class));
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            finish();
                        }
                    }, SPLASH_DISPLAY_LENGTH);
                }
            }
        };
        mAuth.addAuthStateListener(mAuthlistener);
    }

    public void recheck_network() {
        new AlertDialog.Builder(mcontext)
                .setTitle("Connectivity Error..!!")
                .setMessage("Check your net connection..!!")
                .setPositiveButton("Retry..!!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isOnline()) {
                            check_auth();
                        } else {
                            startActivity(new Intent(mcontext, splash.class));
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            finish();
                        }
                    }
                })
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).show();
    }

    public void backup_db(String mAuth) {
        File megaprospect = new File(Environment.getExternalStorageDirectory() + File.separator + "MegaProspects");
        File database = new File(DATABASE_EXTERNAL_PATH + "");
        if (database.exists() && database.isDirectory()) {
            try {
                db_encryption(DATABASE_EXTERNAL_PATH, mAuth);
            } catch (ZipException e) {
                e.printStackTrace();
            }
        } else {
            database.mkdirs();
            try {
                db_encryption(DATABASE_EXTERNAL_PATH, mAuth);
            } catch (ZipException e) {
                e.printStackTrace();
            }
        }
    }

    public void if_restore_reqd() throws ZipException, IOException {
        File file = new File(DATABASE_EXTERNAL_PATH + File.separator + "megaprospects_backup.zip");
        File file2 = new File(DATABASE_INTERNAL_PATH + File.separator + "megaprospectsnext.db");
        mpref = getSharedPreferences("User", MODE_PRIVATE);
        if (!file2.exists() || !mpref.getBoolean("new_install", true)) {
            if (file.exists()) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                db_decryption(auth.getUid());
                SharedPreferences.Editor editor = mpref.edit();
                editor.putBoolean("new_install", true);
                editor.apply();
            }
        }
    }

    private void db_decryption(String x) throws ZipException, IOException {
        ZipFile zipFile = new ZipFile(DATABASE_EXTERNAL_PATH + File.separator + "megaprospects_backup" + ".zip");
        zipFile.setPassword(x);
        try {
            zipFile.extractAll(DATABASE_EXTERNAL_PATH + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        restore();
    }

    private String getFileName(String filePath) {
        // Get the folder name from the zipped file by removing .zip extension
        return filePath.substring(0, filePath.lastIndexOf("."));
    }

    @SuppressLint("SdCardPath")
    public void restore() throws IOException {
        File dbFile1 = new File(DATABASE_EXTERNAL_PATH, "megaprospectsnext.db");
        File dbFile2 = new File(DATABASE_EXTERNAL_PATH, "megaprospectsnext.db-journal");
        FileInputStream fis1 = null;
        FileInputStream fis2 = null;

        fis1 = new FileInputStream(dbFile1);
        fis2 = new FileInputStream(dbFile2);

        String outFileName1 = DATABASE_INTERNAL_PATH + File.separator + "megaprospectsnext.db";
        String outFileName2 = DATABASE_INTERNAL_PATH + File.separator + "megaprospectsnext.db-journal";

        // Open the empty db as the output stream
        OutputStream output1 = null;
        OutputStream output2 = null;

        output1 = new FileOutputStream(outFileName1);
        output2 = new FileOutputStream(outFileName2);


        // Transfer bytes from the inputfile to the outputfile
        byte[] buffer1 = new byte[1024];
        int length1;
        byte[] buffer2 = new byte[1024];
        int length2;
        while ((length1 = Objects.requireNonNull(fis1).read(buffer1)) > 0) {

            Objects.requireNonNull(output1).write(buffer1, 0, length1);

        }

        while ((length2 = Objects.requireNonNull(fis2).read(buffer2)) > 0) {

            Objects.requireNonNull(output2).write(buffer2, 0, length2);

        }

        // Close the streams

        if (output1 != null) {
            output1.flush();
        }
        Objects.requireNonNull(output1).close();
        fis1.close();


        if (output2 != null) {
            output2.flush();
        }
        Objects.requireNonNull(output2).close();
        fis2.close();


        if (dbFile1.exists()) {
            dbFile1.delete();
        }

        if (dbFile2.exists()) {
            dbFile2.delete();
        }

    }

    @SuppressLint("SdCardPath")
    public void db_encryption(String db_path_encrypted, String mAuth) throws ZipException {
        final String db_path = DATABASE_INTERNAL_PATH + File.separator + "megaprospectsnext.db";
        final String db_journalpath = DATABASE_INTERNAL_PATH + File.separator + "megaprospectsnext.db-journal";

        String outFileName = db_path_encrypted + File.separator + "megaprospects_backup.zip";
        ZipFile zipFile = new ZipFile(outFileName);
        ArrayList<File> fileList = new ArrayList<>();
        fileList.add(new File(db_path));
        fileList.add(new File(db_journalpath));
        // Setting parameters
        ZipParameters zipParameters = new ZipParameters();
        zipParameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
        zipParameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_ULTRA);
        zipParameters.setEncryptFiles(true);
        zipParameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
        zipParameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
        // Setting password
        zipParameters.setPassword(mAuth);

        zipFile.addFiles(fileList, zipParameters);
//        zipFile.getProgressMonitor();
    }

    private void saveFileToDrive() {
        // Start by creating a new contents, and setting a callback.
//        Log.i(TAG, "Creating new contents.");
        String db_path = DATABASE_EXTERNAL_PATH + File.separator + "megaprospects_backup";
        final File db = new File(db_path + ".zip");

    }

    @SuppressWarnings("deprecation")
    private boolean isOnline() {
//        ConnectivityManager cm = (ConnectivityManager) mcontext.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo netInfo = cm.getActiveNetworkInfo();
//        return netInfo != null && netInfo.isConnectedOrConnecting();
        return true;
    }
}
