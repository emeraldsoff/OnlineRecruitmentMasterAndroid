package inc.emeraldsoff.onlinerecruitmentmaster.appcontrol_ui;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.perf.metrics.Trace;

import net.lingala.zip4j.exception.ZipException;

import org.w3c.dom.Document;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import es.dmoral.toasty.Toasty;
import inc.emeraldsoff.onlinerecruitmentmaster.BuildConfig;
import inc.emeraldsoff.onlinerecruitmentmaster.R;
import inc.emeraldsoff.onlinerecruitmentmaster.activity_main;
import inc.emeraldsoff.onlinerecruitmentmaster.login.activity_login;
import inc.emeraldsoff.onlinerecruitmentmaster.ui_data.fragment_Home.activity_home;

import static inc.emeraldsoff.onlinerecruitmentmaster.Constants.github_app_link;
import static inc.emeraldsoff.onlinerecruitmentmaster.Constants.github_app_link_test;
import static inc.emeraldsoff.onlinerecruitmentmaster.Constants.play_store_app_link;
import static inc.emeraldsoff.onlinerecruitmentmaster.Constants.privacy_policy;
import static inc.emeraldsoff.onlinerecruitmentmaster.Constants.version_code_fetch_link;
import static inc.emeraldsoff.onlinerecruitmentmaster.Constants.version_code_fetch_link_test;

public class activity_settings extends activity_main {

    Switch lockswitch, admin_access1;
    Button pinreset, pinremove, privacy, logout, update, backup, restore;
    TextView appver;
    Trace trace;
    private SharedPreferences mpref;

    private Context mcontext;

    public static void update_check(final Context mcontext) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        int update_version = 0;
        final int current_version = BuildConfig.VERSION_CODE;
        try {
            URL url;
            if (BuildConfig.BUILD_TYPE.toUpperCase().equals("ALPHA") || BuildConfig.BUILD_TYPE.toUpperCase().equals("DEBUG")) {
                url = new URL(version_code_fetch_link_test);
            } else {
                url = new URL(version_code_fetch_link);
            }
            URLConnection urlConnection = url.openConnection();
            InputStream in = urlConnection.getInputStream();
            //your code
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(in);
            in.close();
            doc.getDocumentElement().normalize();
            update_version = Integer.parseInt(doc.getDocumentElement().getAttribute("new_update"));
        } catch (Exception e) {
            e.printStackTrace();
        }
//        if(!update_version.equals(current_version))
        if (update_version > current_version) {
//            final int finalUpdate_version = update_version;
            final int finalUpdate_version = update_version;
            new AlertDialog.Builder(mcontext)
                    .setCancelable(false)
                    .setIcon(R.drawable.ic_warning_pink_24dp)
                    .setTitle("New Update Available..!!")
                    .setMessage("Do you want to checkout the latest version??\nCurrent Version: " + current_version +
                            "\nNew Version: " + update_version)
                    .setNegativeButton("Get Update From GitHub", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            if (BuildConfig.BUILD_TYPE.toUpperCase().equals("DEBUG_PRO") ||
                                    BuildConfig.BUILD_TYPE.toUpperCase().equals("DEBUG")) {
                                i.setData(Uri.parse(github_app_link_test + finalUpdate_version + "-1.apk"));
                            } else {
                                i.setData(Uri.parse(github_app_link + finalUpdate_version + "-1.apk"));
                            }
                            mcontext.startActivity(i);
                        }
                    })
                    .setPositiveButton("Get Update From PlayStore", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            Toasty.info(mcontext,"Current Version: "+current_version+
//                                    " Final Version: "+finalUpdate_version,4,false).show();
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(play_store_app_link));
                            mcontext.startActivity(i);
                        }
                    })
                    .setNeutralButton("Not Now", null)
                    .show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mcontext = this;
        super.menucreate();
        settingapply();

    }

    @SuppressLint("SetTextI18n")
    public void settingapply() {
        try {
            mpref = getSharedPreferences("User", MODE_PRIVATE);
            appver = findViewById(R.id.appversion);
            privacy = findViewById(R.id.privacy);
            logout = findViewById(R.id.logout);
            lockswitch = findViewById(R.id.lockswitch);
            pinreset = findViewById(R.id.pinreset);
            pinremove = findViewById(R.id.pinremove);
            admin_access1 = findViewById(R.id.admin_access1);
            update = findViewById(R.id.update);
            backup = findViewById(R.id.backup);
            restore = findViewById(R.id.restore);

            backup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    backup_db_setting(mAuth.getUid());
                    /*try
                    {
                        backup_online();
                    }
                    catch (IOException ex)
                    {
                        Toasty.info(mcontext,ex.toString(),10,true).show();
                    }*/
                }
            });

            restore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try
                    {
                       force_restore();
                    }
                    catch (IOException ex)
                    {
                        Toasty.info(mcontext,ex.toString(),10,true).show();
                    } catch (ZipException e) {
                        Toasty.info(mcontext,e.toString(),10,true).show();
                        e.printStackTrace();
                    }
                }
            });

            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    mAuth.signOut();
                    Intent y = new Intent(mcontext, activity_login.class);
                    y.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    y.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    try {
                        ((ActivityManager) Objects.requireNonNull(mcontext.getSystemService(ACTIVITY_SERVICE)))
                                .clearApplicationUserData();
                        // clearing app data

//                    String packageName = getApplicationContext().getPackageName();
//                    Runtime runtime = Runtime.getRuntime();
//                    runtime.exec("pm clear "+packageName);
                    } catch (Exception e) {
                        Toasty.error(mcontext, "Failed to clear app data..!!", Toast.LENGTH_LONG,
                                true).show();
                    }
                    startActivity(y);
                }
            });

            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    update_check(mcontext);
                }
            });

            privacy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(privacy_policy));
                    startActivity(i);
                }
            });

            appver.setText("VERSION:\n" + BuildConfig.VERSION_NAME + " " + BuildConfig.BUILD_TYPE.toUpperCase());

            if (Objects.requireNonNull(mpref.getString("MobileNo", "")).contains("7003564171") &&
                    Objects.requireNonNull(mpref.getString("EmailId", "")).equals("debanjanchakraborty17@gmail.com")) {
                admin_access1.setVisibility(View.VISIBLE);
                admin_access1.setChecked(mpref.getBoolean("admin_access", true));
                admin_access1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            mpref = getSharedPreferences("User", MODE_PRIVATE);
                            SharedPreferences.Editor editor = mpref.edit();
                            editor.putBoolean("admin_access", true);
                            editor.apply();
//                    mpref.getBoolean("admin_access",true);
                            Toasty.success(mcontext, "Admin access is now on.",
                                    Toast.LENGTH_LONG, false).show();
                        } else {
                            mpref = getSharedPreferences("User", MODE_PRIVATE);
                            SharedPreferences.Editor editor = mpref.edit();
                            editor.putBoolean("admin_access", false);
                            editor.apply();
//                    mpref.getBoolean("admin_access",true);
                            Toasty.success(mcontext, "Admin access is now off.",
                                    Toast.LENGTH_LONG, false).show();
                        }
                    }
                });
            } else {
                admin_access1.setVisibility(View.GONE);
            }

            if (mpref.getBoolean("IF_SECURE", true)) {
                lockswitch.setChecked(mpref.getBoolean("IF_SECURE", true));
            } else {
                lockswitch.setChecked(mpref.getBoolean("IF_SECURE", true));
                pinreset.setVisibility(View.GONE);
                pinremove.setVisibility(View.GONE);
            }


            pinremove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences.Editor editor = mpref.edit();
                    editor.putString("PIN", "");
                    editor.putBoolean("IF_SECURE", false);
//                            editor.apply();
                    editor.apply();
                    lockswitch.setChecked(false);
                    pinreset.setVisibility(View.GONE);
                    pinremove.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
            Toasty.error(mcontext, "Something went wrong..!!",
                    Toast.LENGTH_LONG, false).show();
        }
    }


    private void pinset() {
        if (!mpref.getBoolean("IF_SECURE", true)) {
            Toasty.error(this, "App Is Not Secure..!!",
                    Toast.LENGTH_LONG, false).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        lockswitch.setChecked(mpref.getBoolean("IF_SECURE", true));
        if (mpref.getBoolean("IF_SECURE", true)) {
            pinreset.setVisibility(View.VISIBLE);
            pinremove.setVisibility(View.VISIBLE);
        } else {
            pinreset.setVisibility(View.GONE);
            pinremove.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        lockswitch.setChecked(mpref.getBoolean("IF_SECURE", true));
        if (mpref.getBoolean("IF_SECURE", true)) {
            pinreset.setVisibility(View.VISIBLE);
            pinremove.setVisibility(View.VISIBLE);
        } else {
            pinreset.setVisibility(View.GONE);
            pinremove.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(mcontext, activity_home.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
        //TODO add required codes
    }
}
