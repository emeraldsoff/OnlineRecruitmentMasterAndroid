package inc.emeraldsoff.onlinerecruitmentmaster.ui_data.fragment_Home;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.Trace;

import net.lingala.zip4j.exception.ZipException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import inc.emeraldsoff.onlinerecruitmentmaster.R;
import inc.emeraldsoff.onlinerecruitmentmaster.activity_main;
import inc.emeraldsoff.onlinerecruitmentmaster.ui_data.diary.activity_diary_add_page;
import inc.emeraldsoff.onlinerecruitmentmaster.ui_data.people.activity_addpeople;

import static inc.emeraldsoff.onlinerecruitmentmaster.Constants.ASK_MULTIPLE_PERMISSION_REQUEST_CODE;

public class activity_home extends activity_main implements View.OnClickListener {
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
//    SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.US);
//    Date futuredate = null;
//    Date currentdate = null;
    //    SimpleDateFormat fullFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
//    SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.US);
//    SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.US);
//    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.US);
//    SimpleDateFormat day_monFormat = new SimpleDateFormat("dd-MM", Locale.US);
    //    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//    private final long ONE_DAY = 24 * 60 * 60 * 1000;
//    Date now = new Date();
//    String dateString = formatter.format(now);

    @Override
    protected void onStart() {
        super.onStart();
//        mAuth = FirebaseAuth.getInstance();
        int SPLASH_DISPLAY_LENGTH = 0;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if_restore();

            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mcontext = this;
        super.menucreate();
        fab_action();
        permissioncheck();
        attachingfragmenttohome();
//        if_restore();
    }

    public void if_restore() {
        mpref = getSharedPreferences("User", MODE_PRIVATE);
        if (!mpref.getBoolean("new_install", true)) {
            try {
                super.if_restore_reqd();
            } catch (ZipException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
//            startActivity(new Intent(mcontext, activity_home.class));
        }
    }



    @Override
    public void setFinishOnTouchOutside(boolean finish) {
        super.setFinishOnTouchOutside(finish);
        fabexpanded();
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

    public void setupfabitems() {
        fab = findViewById(R.id.fab_main);
        addpeople = findViewById(R.id.addpeople);
        policy = findViewById(R.id.policy_insurance);
        diary = findViewById(R.id.diary_page);

    }

    public void fab_action() {
        setupfabitems();
        fab.setOnClickListener(this);
        addpeople.setOnClickListener(this);
        policy.setOnClickListener(this);
        diary.setOnClickListener(this);
    }

    private void closeSubMenusFab() {
        addpeople.setVisibility(View.GONE);
        policy.setVisibility(View.GONE);
        diary.setVisibility(View.GONE);
        fab.setImageResource(R.drawable.ic_floating_add_24dp);
        fabexpand = false;
    }

    private void openSubMenusFab() {
        addpeople.setVisibility(View.VISIBLE);
        //policy.setVisibility(View.VISIBLE);
        diary.setVisibility(View.VISIBLE);
        //Change settings icon to 'X' icon
        fab.setImageResource(R.drawable.ic_cancel_24dp);
        fabexpand = true;
    }

    public void fabexpanded() {
        if (!fabexpand) {
            openSubMenusFab();
        } else {
            closeSubMenusFab();
        }
    }

    private void attachingfragmenttohome() {
        ViewPager viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        // Set Tabs inside Toolbar
        TabLayout tabs = findViewById(R.id.tabs_actionbar);
        tabs.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        activity_home.Adapter adapter = new activity_home.Adapter(getSupportFragmentManager());
//        adapter.addFragment(new fragment_today_events(), "TODAY");
//        adapter.addFragment(new fragment_upcoming_events(), "UPCOMING");
        adapter.addFragment(new fragment_birthdays_today(), "Today's Birthdays");
        adapter.addFragment(new fragment_anniversaries_today(), "Today's Anniversaries");
        adapter.addFragment(new fragment_birthdays_upcoming(), "Upcoming Birthdays");
        adapter.addFragment(new fragment_anniversaries_upcoming(), "Upcoming Anniversaries");

        viewPager.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_main:
                fabexpanded();
                break;
            case R.id.addpeople:
                fabexpanded();
                startActivity(new Intent(mcontext, activity_addpeople.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                break;
            case R.id.policy_insurance:
                fabexpanded();
                startActivity(new Intent(mcontext, activity_addpeople.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                break;
            case R.id.diary_page:
                fabexpanded();
                startActivity(new Intent(mcontext, activity_diary_add_page.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                break;
            default:
                fabexpanded();
                break;
        }
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        Adapter(FragmentManager manager) {
            super(manager);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setIcon(R.drawable.ic_warning_pink_24dp).setTitle("Exit")
                .setMessage("Are you sure to exit?")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        Intent intent = new Intent(Intent.ACTION_MAIN);
//                        intent.addCategory(Intent.CATEGORY_HOME);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);

                        finishAndRemoveTask();
//                        System.exit(0);
                        trace.stop();
                    }
                }).setNegativeButton("No", null).show();
    }
}