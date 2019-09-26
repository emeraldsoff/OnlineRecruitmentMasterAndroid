package inc.emeraldsoff.onlinerecruitmentmaster.ui_data.diary;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Source;
import com.google.firebase.perf.metrics.Trace;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import inc.emeraldsoff.onlinerecruitmentmaster.R;
import inc.emeraldsoff.onlinerecruitmentmaster.activity_main;
import inc.emeraldsoff.onlinerecruitmentmaster.sqlite_manager.sqlite_adapters.diary_manager.diary_adapter;
import inc.emeraldsoff.onlinerecruitmentmaster.sqlite_manager.sqlite_basecolumns;
import inc.emeraldsoff.onlinerecruitmentmaster.sqlite_manager.sqlite_helper;
import inc.emeraldsoff.onlinerecruitmentmaster.ui_data.fragment_Home.activity_home;
import inc.emeraldsoff.onlinerecruitmentmaster.ui_data.people.activity_addpeople;

public class activity_diary extends activity_main {

    FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build();
    SharedPreferences mpref;
    RecyclerView id_recycleview;
    //    ScrollView scrollview;
    FirebaseFirestore fdb = FirebaseFirestore.getInstance();
    Source cache = Source.CACHE;
    FloatingActionButton fab;
    boolean fabexpand = false;
    CardView addpeople, policy, diary;
    int fab_ht;
    private Context mcontext;
    SQLiteDatabase sqlite;
    sqlite_helper sqliteHelper;
    private diary_adapter adapter;
    Calendar myCalendar = Calendar.getInstance();
    TextView search;
    Button clear_search;
    Trace trace;

    private SimpleDateFormat fullFormat_time = new SimpleDateFormat("YYYY-MMMM-dd', 'EEEE', 'hh:mm:ss a", Locale.US);
    private SimpleDateFormat fullFormat_onlytime = new SimpleDateFormat("hh:mm:ss a", Locale.US);
    private SimpleDateFormat fullFormat_onlydate = new SimpleDateFormat("dd MMMM yyyy', 'EEEE", Locale.US);
    private SimpleDateFormat folderdoc = new SimpleDateFormat("YYYY-MMMM-dd', 'EEEE", Locale.US);
    private SimpleDateFormat foldername = new SimpleDateFormat("YYYY-MMMM-dd-EEEE", Locale.US);
    private SimpleDateFormat fullFormat_time_doc = new SimpleDateFormat("YYYY-MMMM-dd-EEEE-hh-mm-ss-a", Locale.US);
    private SimpleDateFormat fullFormat_prime_onlydate = new SimpleDateFormat("dd MMMM yyyy", Locale.US);

    @Override
    protected void onStart() {
        super.onStart();
//        mAuth = FirebaseAuth.getInstance();
        int SPLASH_DISPLAY_LENGTH = 0;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
         //       firestore_adapter.startListening();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    @Override
    protected void onResume() {
        super.onResume();
       // firestore_adapter.startListening();
    }

    @Override
    protected void onPause() {
        super.onPause();
       // firestore_adapter.stopListening();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       // firestore_adapter.stopListening();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.analytics();
        super.trace(trace);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        mcontext = this;
        super.menucreate();
        setupitems();
        fab_action();
        mpref = getSharedPreferences("User", MODE_PRIVATE);
        //setDiary_firestore();
        setup_db();
        db_recyclerview();
        custom_date();

    }

    public void setdate() {
        Date date_selected;
        date_selected = myCalendar.getTime();
        search.setText(fullFormat_onlydate.format(date_selected));
        db_recyclerview_query(fullFormat_onlydate.format(date_selected));
        //get_search_item(fullFormat_prime_onlydate.format(date_selected));
        //search.setOnQueryTextListener();
        //folder_name = foldername.format(timestamp);
        //folder_doc = folderdoc.format(timestamp);
        //date_view.setText(fullFormat_onlytime.format(timestamp));
        //searchaction_sqlite();
    }

    private void custom_date() {

        final DatePickerDialog.OnDateSetListener dateget = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                setdate();
            }
        };

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(mcontext, dateget, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

       clear_search.setOnClickListener(new View.OnClickListener() {
            @Override
           public void onClick(View v) {
                search.setText("");
                db_recyclerview();
            }
        });

    }

    private void setup_db() {
        sqliteHelper = new sqlite_helper(mcontext);
        sqlite = sqliteHelper.getWritableDatabase();
    }

    private void db_recyclerview() {
        id_recycleview = findViewById(R.id.id_recycle_view);
        id_recycleview.setHasFixedSize(true);
        id_recycleview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new diary_adapter(this, getallitems());
        id_recycleview.setAdapter(adapter);
        String x;
        if (getallitems().getCount() == 0) {
            x = "Found No Contact.";
        } else if (getallitems().getCount() == 1) {
            x = "Found Only " + getallitems().getCount() + " Contents.";
        } else {
            x = "Found " + getallitems().getCount() + " Contents.";
        }
        Snackbar.make(id_recycleview, x, 3000).show();
    }

    private void db_recyclerview_query(String q) {
        id_recycleview = findViewById(R.id.id_recycle_view);
        id_recycleview.setHasFixedSize(true);
        id_recycleview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new diary_adapter(this, get_search_item(q));
        id_recycleview.setAdapter(adapter);
        String x;
        if (get_search_item(q).getCount() == 0) {
            x = "Found No Data From Date '" + q + "'.";
        } else if (get_search_item(q).getCount() == 1) {
            x = "Found Only " + get_search_item(q).getCount() + " Document From Date '" + q + "' .";
        } else {
            x = "Found " + get_search_item(q).getCount() + " Documents From Date'" + q + "' .";
        }
        Snackbar.make(id_recycleview, x, 3000).show();
    }


    private Cursor getallitems() {
        return sqlite.query(sqlite_basecolumns.diary.DIARY_TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                sqlite_basecolumns.diary.created_at + " DESC"
        );
    }

    private Cursor get_search_item(String q) {
        //String[] whereargs = {"%" + q + "%"};
        String[] whereargs = {q + "%"};
        return sqlite.query(sqlite_basecolumns.diary.DIARY_TABLE_NAME + "",
                null,
                sqlite_basecolumns.diary.created_date + " LIKE ?",
                whereargs,
                null,
                null,
                sqlite_basecolumns.diary.created_at + " DESC"
        );
    }
/*
    private void setDiary_firestore() {
        fdb.setFirestoreSettings(settings);
        mpref = getSharedPreferences("User", MODE_PRIVATE);

        final String app_userid = mpref.getString("userID", "");
        Query query, next;
        String collection = "prospect" + "/" + app_userid;
        final CollectionReference cliref = fdb.collection(collection + "/" + "personal_diary");
        try {
            query = cliref.orderBy("timestamp", Query.Direction.DESCENDING)
            ;
            FirestoreRecyclerOptions<diarycard_folder_gen> options = new FirestoreRecyclerOptions.Builder<diarycard_folder_gen>()
                    .setQuery(query, diarycard_folder_gen.class)
                    .build();
            firestore_adapter = new diary_folder_adapter(options);
            id_recycleview = findViewById(R.id.id_recycle_view);
            id_recycleview.setHasFixedSize(true);
            id_recycleview.setLayoutManager(new LinearLayoutManager(this));
            id_recycleview.setAdapter(firestore_adapter);
        } catch (Exception e) {
            Toast.makeText(mcontext, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
*/
    public void setupitems() {
        fab = findViewById(R.id.fab_main);
        addpeople = findViewById(R.id.addpeople);
        policy = findViewById(R.id.policy_insurance);
        diary = findViewById(R.id.diary_page);
        search = findViewById(R.id.search);
        clear_search=findViewById(R.id.clear_search);
    }

    public void fab_action() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabexpanded();
            }
        });
        addpeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mcontext, activity_addpeople.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
        policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mcontext, activity_addpeople.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
        diary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mcontext, activity_diary_add_page.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
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

    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(mcontext, activity_home.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        isDestroyed();

    }
}
