package inc.emeraldsoff.onlinerecruitmentmaster.remote_local_helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.StrictMode;

import com.crashlytics.android.Crashlytics;

import org.w3c.dom.Document;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import es.dmoral.toasty.Toasty;
import inc.emeraldsoff.onlinerecruitmentmaster.BuildConfig;
import inc.emeraldsoff.onlinerecruitmentmaster.R;

import static inc.emeraldsoff.onlinerecruitmentmaster.Constants.github_app_link;
import static inc.emeraldsoff.onlinerecruitmentmaster.Constants.github_app_link_test;
import static inc.emeraldsoff.onlinerecruitmentmaster.Constants.play_store_app_link;
import static inc.emeraldsoff.onlinerecruitmentmaster.Constants.version_code_fetch_link;
import static inc.emeraldsoff.onlinerecruitmentmaster.Constants.version_code_fetch_link_test;

public class updater {
    private Context context;

    public updater(Context context) {
        this.context = context;

    }

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

    private void check_update_forced(final Context mcontext) {
        final SharedPreferences mpref;
        mpref = mcontext.getSharedPreferences("User", Context.MODE_PRIVATE);
        final Date now = new Date();
        final SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.US);
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
            Crashlytics.getInstance();
            Crashlytics.log(e.getMessage());
        }
//        mpref = getSharedPreferences("User", MODE_PRIVATE);
        String current_date = format.format(now);
        Toasty.info(mcontext, Objects.requireNonNull(mpref.getString("next_update_check", "")),
                4, false).show();
        String next_update_check = mpref.getString("next_update_check", "");
        int c, n;
        int x = 0;
        if (Objects.requireNonNull(mpref.getString("next_update_check", "")).equals("") ||
                Objects.requireNonNull(mpref.getString("next_update_check", "")).isEmpty()) {
            next_update_check = format.format(now);
            SharedPreferences.Editor editor = mpref.edit();
            editor.putString("next_update_check", next_update_check);
            editor.apply();
            for (int i = 0; i <= 1; i++) {
                check_update_forced(mcontext);
            }
        } else {
            c = Integer.parseInt(current_date);
            n = Integer.parseInt(Objects.requireNonNull(next_update_check));
            x = n - c;
        }

        if (x < 0 || x == 0) {
            if (update_version > current_version) {
//            final int finalUpdate_version = update_version;
                final int finalUpdate_version = update_version;
                new AlertDialog.Builder(mcontext)
                        .setCancelable(false)
                        .setIcon(R.drawable.ic_warning_pink_24dp)
                        .setTitle("New Update Available..!!")
                        .setMessage("Do you want to checkout the latest version??\nCurrent Version: " + current_version +
                                "\nNew Version: " + update_version)
                        .setPositiveButton("Get Update From PlayStore", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                            Toasty.info(mcontext,"Current Version: "+current_version+
//                                    " Final Version: "+finalUpdate_version,4,false).show();
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(play_store_app_link));
                                mcontext.startActivity(i);
//                            update_check_time();
                            }
                        })
                        .setNeutralButton("Remind Me Later", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                update_check_time();
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(now);
                                calendar.add(Calendar.DAY_OF_YEAR, +1);
                                Date newDate = calendar.getTime();
                                String next_update_check = format.format(newDate);
                                SharedPreferences.Editor editor = mpref.edit();
                                editor.putString("next_update_check", next_update_check);
                                editor.apply();
                            }
                        })
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
//                            update_check_time();
                            }
                        })
                        .show();
            }
        }

    }
}