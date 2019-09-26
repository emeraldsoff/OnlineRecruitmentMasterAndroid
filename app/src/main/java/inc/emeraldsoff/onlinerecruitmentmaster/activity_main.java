package inc.emeraldsoff.onlinerecruitmentmaster;

import android.annotation.SuppressLint;
import android.app.backup.BackupManager;
import android.app.backup.RestoreObserver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.Trace;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;
import com.microsoft.appcenter.distribute.Distribute;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import es.dmoral.toasty.Toasty;
import inc.emeraldsoff.onlinerecruitmentmaster.appcontrol_ui.activity_aboutus;
import inc.emeraldsoff.onlinerecruitmentmaster.appcontrol_ui.activity_settings;
import inc.emeraldsoff.onlinerecruitmentmaster.billing_ui.activity_gopro;
import inc.emeraldsoff.onlinerecruitmentmaster.remote_local_helpers.distributor_ms;
import inc.emeraldsoff.onlinerecruitmentmaster.remote_local_helpers.pushlistener_ms;
import inc.emeraldsoff.onlinerecruitmentmaster.ui_data.diary.activity_diary;
import inc.emeraldsoff.onlinerecruitmentmaster.ui_data.fragment_Home.activity_home;
import inc.emeraldsoff.onlinerecruitmentmaster.ui_data.people.activity_searchpeople;

import static inc.emeraldsoff.onlinerecruitmentmaster.Constants.DATABASE_EXTERNAL_PATH;
import static inc.emeraldsoff.onlinerecruitmentmaster.Constants.DATABASE_EXTERNAL_PATH_ZIP_NAME;
import static inc.emeraldsoff.onlinerecruitmentmaster.Constants.DATABASE_INTERNAL_PATH;

//import static com.crashlytics.android.Crashlytics.TAG;

public class activity_main extends AppCompatActivity {
    //    SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.US);
//    SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.US);
//    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.US);
//    SimpleDateFormat day_monFormat = new SimpleDateFormat("dd-MM", Locale.US);
    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    NavigationView navigationView;
    androidx.appcompat.widget.Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    TextView user, phn, email, validinfo;
    //    SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.US);
//    SimpleDateFormat hh_mm_ss = new SimpleDateFormat("hh:mm:ss", Locale.US);
    //    Date futuredate = null;
//    Date currentdate = null;
    SimpleDateFormat fullFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
    private DrawerLayout drawer;
    private Context mcontext;
    private SharedPreferences mpref;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
//    private FirebaseFirestore fdb = FirebaseFirestore.getInstance();
//    private final long ONE_DAY = 24 * 60 * 60 * 1000;
//    Date now = new Date();
//    String dateString = formatter.format(now);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        analytics();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mcontext = this;
    }

//    public void analytics(){
////        mpref=getSharedPreferences("User",MODE_PRIVATE);
//        AppCenter.setEnabled(true);
//        AppCenter.getSdkVersion();
//        AppCenter.getInstallId();
//        AppCenter.setUserId(mpref.getString("userID",""));
//        String activity = mcontext.getClass().getSimpleName();
////        Push.setListener(new pushlistener_ms());
//        AppCenter.start(getApplication(), "bbcf040f-8803-43ba-b9d5-878adb5e4ea6",
//                Analytics.class, Crashes.class);
////        Crashes.notifyUserConfirmation(MY_MS_CRASH_PERMISSION);
////        Crashes.getLastSessionCrashReport();
////        Push.enableFirebaseAnalytics(mcontext);
////        Analytics.trackEvent(activity+"");
////        AppCenter.start(getApplication(), "bbcf040f-8803-43ba-b9d5-878adb5e4ea6", );
//
//
//        //For Firebase
//        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
//        Trace myTrace = FirebasePerformance.getInstance().newTrace(activity+"");
//        myTrace.start();
//    }

    public void analytics() {
        String uid = mAuth.getUid();
        String activity = this.getClass().getSimpleName();
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Push.enableFirebaseAnalytics(getApplication());
        AppCenter.start(getApplication(), "bbcf040f-8803-43ba-b9d5-878adb5e4ea6",
                Distribute.class, Push.class, Analytics.class, Crashes.class);
        AppCenter.setEnabled(true);
//        AppCenter.getSdkVersion();
        AppCenter.getInstallId();
        AppCenter.setUserId(uid);
        mFirebaseAnalytics.setUserId(uid + "");
        Crashes.getLastSessionCrashReport();
        Push.enableFirebaseAnalytics(mcontext);
        Distribute.setListener(new distributor_ms());
        Push.setListener(new pushlistener_ms());
        Analytics.trackEvent(activity + "");
    }

    public void trace(Trace trace) {
        String uid = mAuth.getUid();
        String activity = this.getClass().getSimpleName();
        trace = FirebasePerformance.getInstance().newTrace(uid + "-" + activity + "");
        trace.start();
    }

    public void menucreate() {
//        AppCenter.start(getApplication(), appsecret, );
//        AppCenter.start(getApplication(), appsecret, Push.class, Analytics.class, Crashes.class);
        toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
//        View nav_header = navigationView.getHeaderView(0);
//        user = nav_header.findViewById(R.id.user);
//        phn = nav_header.findViewById(R.id.user_phn);
//        email = nav_header.findViewById(R.id.user_email);
//        validinfo = nav_header.findViewById(R.id.validinfo);
//        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        toggle.syncState();

        getactivityname();
        userdatafetch();
//        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Harlow.ttf");
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(mcontext, activity_home.class));
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                getSupportFragmentManager()
//                        .beginTransaction()
//                        .setCustomAnimations(R.anim.fui_slide_in_right, R.anim.fui_slide_out_left)
//                        .replace(R.id.fragment_container, new fragment_home())
//                        .addToBackStack(null)
//                        .commit();
//                navigationView.setCheckedItem(R.id.home);
                        break;
//                    case R.id.addpeople:
//                        startActivity(new Intent(mcontext, activity_addpeople.class));
//                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                        break;
                    case R.id.searchpeople:
                        startActivity(new Intent(mcontext, activity_searchpeople.class));
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        break;
                    case R.id.policy_insurance:
//                        startActivity(new Intent(mcontext, activity_addpeople.class));
//                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        break;
                    case R.id.settings:
                        startActivity(new Intent(mcontext, activity_settings.class));
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        break;
                    case R.id.aboutus:
                        startActivity(new Intent(mcontext, activity_aboutus.class));
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        break;
                    case R.id.gopro:
                        startActivity(new Intent(mcontext, activity_gopro.class));
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        break;
                    case R.id.diary_content:
                        startActivity(new Intent(mcontext, activity_diary.class));
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        break;
                    case R.id.share:
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_TEXT, "Use EmeraldSoff Mega Prospects Pro" +
                                " to increase interaction with your friends, families and clients. " +
                                "Click on https://play.google.com/store/apps/details?id=inc.emeraldsoff.onlinerecruitmentmaster" +
                                "to download the app");
                        shareIntent.putExtra(Intent.EXTRA_TITLE, "EmeraldSoff Mega Prospects Pro");
                        startActivity(Intent.createChooser(shareIntent, "Share Mega Prospects Pro"));

//                Toasty.success(mcontext, "Message Shared..!!", 4).show();
                        break;
                }
                drawer.closeDrawer(GravityCompat.START, true);
                return false;
            }
        });
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        toggle.syncState();
    }

//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.home:
//                startActivity(new Intent(mcontext, Main2Activity.class));
//                getSupportFragmentManager()
//                        .beginTransaction()
//                        .setCustomAnimations(R.anim.fui_slide_in_right, R.anim.fui_slide_out_left)
//                        .replace(R.id.fragment_container, new fragment_home())
//                        .addToBackStack(null)
//                        .commit();
//                navigationView.setCheckedItem(R.id.home);
//                break;
//            case R.id.addpeople:
//                getSupportFragmentManager()
//                        .beginTransaction()
//                        .setCustomAnimations(R.anim.fui_slide_in_right, R.anim.fui_slide_out_left)
//                        .replace(R.id.fragment_container, new fragment_addpeople())
//                        .addToBackStack(null)
//                        .commit();
////                navigationView.setCheckedItem(R.id.addpeople);
//                break;
//            case R.id.searchpeople:
//                getSupportFragmentManager()
//                        .beginTransaction()
//                        .setCustomAnimations(R.anim.fui_slide_in_right, R.anim.fui_slide_out_left)
//                        .replace(R.id.fragment_container, new fragment_searchpeople())
//                        .addToBackStack(null)
//                        .commit();
////                navigationView.setCheckedItem(R.id.searchpeople);
//                break;
//            case R.id.settings:
//                getSupportFragmentManager()
//                        .beginTransaction()
//                        .setCustomAnimations(R.anim.fui_slide_in_right, R.anim.fui_slide_out_left)
//                        .replace(R.id.fragment_container, new fragment_settings())
//                        .addToBackStack(null)
//                        .commit();
////                navigationView.setCheckedItem(R.id.settings);
//                break;
//            case R.id.aboutus:
//                getSupportFragmentManager()
//                        .beginTransaction()
//                        .setCustomAnimations(R.anim.fui_slide_in_right, R.anim.fui_slide_out_left)
//                        .replace(R.id.fragment_container, new fragment_aboutus())
//                        .addToBackStack(null)
//                        .commit();
////                navigationView.setCheckedItem(R.id.aboutus);
//                break;
//            case R.id.gopro:
//                getSupportFragmentManager()
//                        .beginTransaction()
//                        .setCustomAnimations(R.anim.fui_slide_in_right, R.anim.fui_slide_out_left)
//                        .replace(R.id.fragment_container, new fragment_gopro())
//                        .addToBackStack(null)
//                        .commit();
////                navigationView.setCheckedItem(R.id.gopro);
//                break;
//            case R.id.share:
//                Intent shareIntent = new Intent(Intent.ACTION_SEND);
//                shareIntent.setType("text/plain");
//                shareIntent.putExtra(Intent.EXTRA_TEXT, "Use EmeraldSoff Mega Prospects Pro" +
//                        " to increase interaction with your friends, families and clients. " +
//                        "Click on https://play.google.com/store/apps/details?id=inc.emeraldsoff.onlinerecruitmentmaster" +
//                        "to download the app");
//                shareIntent.putExtra(Intent.EXTRA_TITLE, "EmeraldSoff Mega Prospects Pro");
//                startActivity(Intent.createChooser(shareIntent, "Share Mega Prospects Pro"));
//                getSupportFragmentManager()
//                        .beginTransaction()
//                        .setCustomAnimations(R.anim.fui_slide_in_right, R.anim.fui_slide_out_left)
//                        .commit();
////                Toasty.success(mcontext, "Message Shared..!!", 4).show();
//                break;
//        }
//        drawer.closeDrawer(GravityCompat.START, true);
//        return false;
//    }


//    public void onBackPress() {
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
////            return onBackPressedListener.onBackPressed();
//            super.onBackPressed();
////            finish();
//        }
//    }


    public void getactivityname() {
        String activityname = this.getClass().getSimpleName();
        switch (activityname) {
            case "activity_home":
                navigationView.setCheckedItem(R.id.home);
//                toolbar.setTitle(Objects.requireNonNull(navigationView.getCheckedItem()).getTitle());
                toolbar.setTitle("Home");
                break;
            case "activity_addpeople":
                navigationView.setCheckedItem(R.id.addpeople);
//                toolbar.setTitle(Objects.requireNonNull(navigationView.getCheckedItem()).getTitle());
                toolbar.setTitle("Add People");
                break;
            case "activity_searchpeople":
                navigationView.setCheckedItem(R.id.searchpeople);
//                toolbar.setTitle(Objects.requireNonNull(navigationView.getCheckedItem()).getTitle());
                toolbar.setTitle("Search People");
                break;
            case "activity_aboutus":
                navigationView.setCheckedItem(R.id.aboutus);
//                toolbar.setTitle(Objects.requireNonNull(navigationView.getCheckedItem()).getTitle());
                toolbar.setTitle("About Us");
                break;
            case "activity_settings":
                navigationView.setCheckedItem(R.id.settings);
//                toolbar.setTitle(Objects.requireNonNull(navigationView.getCheckedItem()).getTitle());
                toolbar.setTitle("Settings");
                break;
            case "activity_gopro":
                navigationView.setCheckedItem(R.id.gopro);
//                toolbar.setTitle(Objects.requireNonNull(navigationView.getCheckedItem()).getTitle());
                toolbar.setTitle("Go Premium");
                break;
            case "activity_diary":
                navigationView.setCheckedItem(R.id.diary_content);
//                toolbar.setTitle(Objects.requireNonNull(navigationView.getCheckedItem()).getTitle());
                toolbar.setTitle("Personal Diary");
                break;
            case "activity_add_diary_content":
                toolbar.setTitle("New Diary Document");
                break;
        }
//        else if(activityname.equals("activity_editpeople"))
//        {
//            toolbar.setTitle("Person");
//        }
//        else if(activityname.equals("activity_showpeopledetails"))
//        {
//            toolbar.setTitle("");
//        }
//        else if(activityname.equals("activity_"))
//        {
//            toolbar.setTitle("");
//        }
//        else
//        {
//            navigationView.
//        }
    }

    @SuppressLint("SetTextI18n")
    public void userdatafetch() {
        navigationView = findViewById(R.id.nav_view);
        View nav_header = navigationView.getHeaderView(0);
        user = nav_header.findViewById(R.id.user);
        phn = nav_header.findViewById(R.id.user_phn);
        email = nav_header.findViewById(R.id.user_email);
        validinfo = nav_header.findViewById(R.id.validinfo);
        mpref = getSharedPreferences("User", MODE_PRIVATE);


        if (mpref.getString("MiddleName", "") != null ||
                !Objects.requireNonNull(mpref.getString("MiddleName", "")).isEmpty() ||
                !Objects.equals(mpref.getString("MiddleName", ""), "")) {
            user.setText(mpref.getString("FirstName", "") + " " + mpref.getString("MiddleName", "") +
                    " " + mpref.getString("LastName", ""));
        } else {
            user.setText(mpref.getString("FirstName", "") +
                    " " + mpref.getString("LastName", ""));
        }

        phn.setText(mpref.getString("MobileNo", ""));

        if (mpref.getString("EmailId", "") != null ||
                !Objects.requireNonNull(mpref.getString("EmailId", "")).isEmpty() ||
                !Objects.equals(mpref.getString("EmailId", ""), "")) {
            email.setText(mpref.getString("EmailId", ""));
        } else {
            email.setVisibility(View.GONE);

        }
//        Date finalexp;
//        String exp_date = "";
//        String time = "";
//        try {
//            exp_date=fullFormat.format(formatter.parse(mpref.getString("ExpiryDate", "")));
//            time = hh_mm_ss.format(formatter.parse(mpref.getString("ExpiryDate", "")));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        Date now = new Date();

        try {
            datcal();
        } catch (ParseException e) {
            e.printStackTrace();
            Toasty.error(mcontext, "Something went wrong..!!1",
                    Toast.LENGTH_LONG, true).show();
//                                    e.printStackTrace();
        }

    }

//    public void sync() {
//        fdb.document("prospect_users" + "/" + mpref.getString("userID", ""))
//                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot doc = task.getResult();
//                    if (doc != null && doc.exists()) {
////                        Log.d(TAG, "DocumentSnapshot data: " + doc.getData());
//                        StringBuilder ufname = new StringBuilder();
//                        StringBuilder umname = new StringBuilder();
//                        StringBuilder ulname = new StringBuilder();
//                        StringBuilder validitydate = new StringBuilder();
//                        StringBuilder ifval = new StringBuilder();
//                        StringBuilder expire = new StringBuilder();
//                        StringBuilder install = new StringBuilder();
//                        StringBuilder uemail = new StringBuilder();
//                        StringBuilder uphone = new StringBuilder();
//
//                        validitydate.append(doc.get("ValidityDate"));
//                        install.append(doc.get("InstallDate"));
//                        expire.append(doc.get("ExpiryDate"));
//                        ufname.append(doc.get("FirstName"));
//                        umname.append(doc.get("MiddleName"));
//                        ulname.append(doc.get("LastName"));
//                        uemail.append(doc.get("EmailId"));
//                        uphone.append(doc.get("MobileNo"));
//                        ifval.append(doc.getBoolean("IF_VALID"));
//
//                        Boolean b = Boolean.parseBoolean(String.valueOf(ifval));
//
//                        mpref = getSharedPreferences("User", MODE_PRIVATE);
//                        SharedPreferences.Editor editor = mpref.edit();
//
//                        editor.putString("FirstName", String.valueOf(ufname));
//                        editor.putString("MiddleName", String.valueOf(umname));
//                        editor.putString("LastName", String.valueOf(ulname));
//                        editor.putString("MobileNo", String.valueOf(uphone));
//                        editor.putString("EmailId", String.valueOf(uemail));
//
//                        editor.putString("ValidityDate", String.valueOf(validitydate));
//                        editor.putString("InstallDate", String.valueOf(install));
//                        editor.putString("ExpiryDate", String.valueOf(expire));
//                        editor.putBoolean("IF_VALID", b);
//                        editor.apply();
//                        try {
//                            datcal();
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                            Toasty.error(mcontext, "Something went wrong..!!2",
//                                    Toast.LENGTH_LONG, true).show();
////                                    e.printStackTrace();
//                        }
//                    }
//                } else {
////                    Log.d(TAG, "get failed with ", task.getException());
//                    Toasty.error(mcontext, "Something went wrong..!!3",
//                            Toast.LENGTH_LONG, true).show();
//                }
//            }
//        });
//    }

    @SuppressLint("SetTextI18n")
    private void datcal() throws ParseException {
        navigationView = findViewById(R.id.nav_view);
        View nav_header = navigationView.getHeaderView(0);
        validinfo = nav_header.findViewById(R.id.validinfo);
        mpref = getSharedPreferences("User", MODE_PRIVATE);
        String exp_date = fullFormat.format(formatter.parse(mpref.getString("ExpiryDate", "")));
        Date finalexp = fullFormat.parse(exp_date);
        Date now = new Date();

        //    Calendar myCalendar = Calendar.getInstance();
        //    Calendar calendar = Calendar.getInstance();
        //    Date now = new Date();
        int day = 1000 * 60 * 60 * 24;
        int daysleft = (int) ((finalexp.getTime() - now.getTime()) / day);

        if (daysleft >= 0) {
            if (daysleft > 0) {
                validinfo.setText("Expiry Date: " + exp_date + "\nDays Left: " +
                        daysleft + " days");
            } else {
                validinfo.setText("Expiry Date: " + exp_date + "\nDays Left: " +
                        daysleft + " days");
            }
        } else {
            SharedPreferences.Editor editor = mpref.edit();
            editor.putBoolean("IF_VALID", false);
            editor.apply();
            validinfo.setText("Your app has expired..!!");
        }
    }

    public boolean admin_tester() {
        return Objects.equals(mpref.getString("MobileNo", ""), "+917003564171") &&
                Objects.equals(mpref.getString("EmailId", ""), "debanjanchakraborty17@gmail.com")
                || BuildConfig.BUILD_TYPE.equals("ALPHA");
    }

    public boolean if_expired() {
        mpref = getSharedPreferences("User", MODE_PRIVATE);
        String exp_date = null;
        Date finalexp = null;
        try {
            exp_date = fullFormat.format(formatter.parse(mpref.getString("ExpiryDate", "")));
            finalexp = fullFormat.parse(exp_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date now = new Date();
        int day = 1000 * 60 * 60 * 24;
        int daysleft = (int) ((Objects.requireNonNull(finalexp).getTime() - now.getTime()) / day);

        return daysleft < 0;
    }

    public boolean ads_bool() {
        //if true ads will be shown
        return if_expired() || !admin_tester();
    }

    public boolean full_access_bool() {

        return !ads_bool() || isOnline();
    }

    @SuppressWarnings("deprecation")
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) mcontext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
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
        request_backup();
    }

    public void backup_db_setting(String mAuth) {
        File megaprospect = new File(Environment.getExternalStorageDirectory() + File.separator + "MegaProspects");
        final File database = new File(DATABASE_EXTERNAL_PATH + "");
        if (database.exists() && database.isDirectory()) {
            try {
                db_encryption(DATABASE_EXTERNAL_PATH, mAuth);
                Toasty.success(this,"Data Backup completed successfully..!!",10).show();
            } catch (ZipException e) {
                e.printStackTrace();
            }
        } else {
            database.mkdirs();
            try {
                db_encryption(DATABASE_EXTERNAL_PATH, mAuth);
                Toasty.success(this,"Data Backup completed successfully..!!",10).show();
            } catch (ZipException e) {
                e.printStackTrace();
            }
        }
        request_backup();
    }

    public void request_backup(){
        BackupManager backupManager = new BackupManager(mcontext);
        backupManager.dataChanged();
        //Toasty.info(mcontext,x.toString(),10,true).show();
    }


    public void request_restore(){
        BackupManager backupManager = new BackupManager(mcontext);
        RestoreObserver observer = new RestoreObserver() {
            /**
             * The restore operation has begun.
             *
             * @param numPackages The total number of packages being processed in
             *                    this restore operation.
             */
            @Override
            public void restoreStarting(int numPackages) {
                super.restoreStarting(numPackages);
            }

            /**
             * An indication of which package is being restored currently, out of the
             * total number provided in the {@link #restoreStarting(int)} callback.  This method
             * is not guaranteed to be called: if the transport is unable to obtain
             * data for one or more of the requested packages, no onUpdate() call will
             * occur for those packages.
             *
             * @param nowBeingRestored The index, between 1 and the numPackages parameter
             *                         to the {@link #restoreStarting(int)} callback, of the package now being
             *                         restored.  This may be non-monotonic; it is intended purely as a rough
             *                         indication of the backup manager's progress through the overall restore process.
             * @param currentPackage   The name of the package now being restored.
             */
            @Override
            public void onUpdate(int nowBeingRestored, String currentPackage) {
                super.onUpdate(nowBeingRestored, currentPackage);
                currentPackage="inc.emeraldsoff.onlinerecruitmentmaster";
            }

            /**
             * The restore process has completed.  This method will always be called,
             * even if no individual package restore operations were attempted.
             *
             * @param error Zero on success; a nonzero error code if the restore operation
             *              as a whole failed.
             */
            @Override
            public void restoreFinished(int error) {
                super.restoreFinished(error);
            }
        };
        backupManager.requestRestore(observer);
        //Toasty.info(mcontext,backupManager.toString()+ " " +observer.toString(),10,true).show();
    }

    public void force_restore() throws ZipException, IOException {
        File file = new File(DATABASE_EXTERNAL_PATH + File.separator + "megaprospects_backup.zip");
            if (file.exists()) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                db_decryption(auth.getUid());
                Toasty.success(mcontext,"Data Restored Successfully..!!",10,true).show();
            }
            else {
                request_restore();
            }

    }

    public void if_restore_reqd() throws ZipException, IOException {
        File file = new File(DATABASE_EXTERNAL_PATH + File.separator + "megaprospects_backup.zip");
        File file2 = new File(DATABASE_INTERNAL_PATH + File.separator + "megaprospectsnext.db");
        mpref = getSharedPreferences("User", MODE_PRIVATE);
        if (!file2.exists()) {
            if (file.exists()) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                db_decryption(auth.getUid());
                SharedPreferences.Editor editor = mpref.edit();
                editor.putBoolean("new_install", true);
                editor.apply();
            }
            else {
                request_restore();
            }
        }
        else if(!mpref.getBoolean("new_install", true))
        {
            if (file.exists()) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                db_decryption(auth.getUid());
                SharedPreferences.Editor editor = mpref.edit();
                editor.putBoolean("new_install", true);
                editor.apply();
            }
            else {
                request_restore();
            }
        }
    }

    public void db_decryption(String x) throws ZipException, IOException {
        ZipFile zipFile = new ZipFile(DATABASE_EXTERNAL_PATH_ZIP_NAME+"");
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

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            return onBackPressedListener.onBackPressed();
            super.onBackPressed();
//            finish();
        }
    }
}
