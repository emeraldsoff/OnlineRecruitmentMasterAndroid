package inc.emeraldsoff.onlinerecruitmentmaster.home;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.perf.metrics.Trace;

import inc.emeraldsoff.onlinerecruitmentmaster.R;
import inc.emeraldsoff.onlinerecruitmentmaster.activity_main;

import static inc.emeraldsoff.onlinerecruitmentmaster.Constants.ASK_MULTIPLE_PERMISSION_REQUEST_CODE;

public class activity_home extends activity_main {
    //    SQLiteDatabase sqlite;
//    sqlite_helper sqliteHelper;
    //    Calendar myCalendar = Calendar.getInstance();
//    Calendar calendar = Calendar.getInstance();
//    Date now = new Date();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final int day = 1000 * 60 * 60 * 24;
    private Context mcontext;
    private SharedPreferences mpref;
    FloatingActionButton fab;
    boolean fabexpand = false;
    CardView addpeople, policy, diary;
    Trace trace;

    @Override
    protected void onStart() {
        super.onStart();
//        mAuth = FirebaseAuth.getInstance();
        int SPLASH_DISPLAY_LENGTH = 0;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //if_restore();

            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mcontext = this;
        super.menucreate();
        //fab_action();
        permissioncheck();

    }

    @Override
    public void setFinishOnTouchOutside(boolean finish) {
        super.setFinishOnTouchOutside(finish);
        //fabexpanded();
    }

    private void permissioncheck() {
        if (ContextCompat.checkSelfPermission(mcontext, Manifest.permission_group.PHONE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(mcontext, Manifest.permission.ACCESS_NETWORK_STATE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(mcontext, Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(mcontext, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(mcontext, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(mcontext, Manifest.permission.ACCESS_NETWORK_STATE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(mcontext, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(mcontext, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(mcontext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(mcontext, Manifest.permission.READ_CALENDAR)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(mcontext, Manifest.permission.WRITE_CALENDAR)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(mcontext, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(mcontext, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity_home.this, new String[]
                    {
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.CALL_PHONE,
                            Manifest.permission.ACCESS_NETWORK_STATE,
//                                    Manifest.permission_group.CONTACTS,
//                                    Manifest.permission.READ_SMS,
//                                    Manifest.permission.SEND_SMS,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                                    Manifest.permission.READ_CALENDAR,
//                                    Manifest.permission.WRITE_CALENDAR,
                            Manifest.permission.READ_CONTACTS,
                            Manifest.permission.WRITE_CONTACTS
//                                    Manifest.permission.CAMERA,
//                                    Manifest.permission.ACCESS_COARSE_LOCATION,
//                                    Manifest.permission.ACCESS_FINE_LOCATION
                    }, ASK_MULTIPLE_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setIcon(R.drawable.ic_warning_pink_24dp).setTitle("Exit")
                .setMessage("Are you sure to exit?")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAndRemoveTask();
                    }
                }).setNegativeButton("No", null).show();
    }
}