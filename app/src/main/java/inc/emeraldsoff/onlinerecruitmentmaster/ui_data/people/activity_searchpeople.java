package inc.emeraldsoff.onlinerecruitmentmaster.ui_data.people;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.perf.metrics.Trace;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import inc.emeraldsoff.onlinerecruitmentmaster.R;
import inc.emeraldsoff.onlinerecruitmentmaster.activity_main;
import inc.emeraldsoff.onlinerecruitmentmaster.sqlite_manager.sqlite_adapters.contact_manager.contact_adapter;
import inc.emeraldsoff.onlinerecruitmentmaster.sqlite_manager.sqlite_basecolumns;
import inc.emeraldsoff.onlinerecruitmentmaster.sqlite_manager.sqlite_helper;
import inc.emeraldsoff.onlinerecruitmentmaster.ui_data.diary.activity_diary_add_page;
import inc.emeraldsoff.onlinerecruitmentmaster.ui_data.fragment_Home.activity_home;

import static inc.emeraldsoff.onlinerecruitmentmaster.Constants.MY_PERMISSIONS_CALL_PHONE;

public class activity_searchpeople extends activity_main {

    FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build();

    SQLiteDatabase sqlite;
    sqlite_helper sqliteHelper;
    SharedPreferences mpref;
    SearchView search;
    RecyclerView id_recycleview;
    //    ScrollView scrollview;
    FirebaseFirestore fdb = FirebaseFirestore.getInstance();
    FloatingActionButton fab;
    boolean fabexpand = false;
    CardView addpeople, policy, diary;
    private Context mcontext;
    private contact_adapter adapter;
    Trace trace;

    @Override
    protected void onStart() {
        super.onStart();
//        mAuth = FirebaseAuth.getInstance();
        int SPLASH_DISPLAY_LENGTH = 0;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                access_data_firebase();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.analytics();
        super.trace(trace);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchpeople);
        super.menucreate();
        setupitems();
        mcontext = this;
        setupitems();
        fab_action();
        setup_db();
//        searchaction_firebase();
        searchaction_sqlite();
//        db_recyclerview();

    }

    private void setup_db() {
        sqliteHelper = new sqlite_helper(mcontext);
        sqlite = sqliteHelper.getWritableDatabase();
    }

    private void db_recyclerview() {
        id_recycleview = findViewById(R.id.id_recycle_view);
        id_recycleview.setHasFixedSize(true);
        id_recycleview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new contact_adapter(this, getallitems());
        id_recycleview.setAdapter(adapter);
        String x;
        if (getallitems().getCount() == 0) {
            x = "Found No Contact.";
        } else if (getallitems().getCount() == 1) {
            x = "Found Only " + getallitems().getCount() + " Contact.";
        } else {
            x = "Found " + getallitems().getCount() + " Contacts.";
        }
        Snackbar.make(id_recycleview, x, 3000).show();
    }

    private void db_recyclerview_query(String q) {
        id_recycleview = findViewById(R.id.id_recycle_view);
        id_recycleview.setHasFixedSize(true);
        id_recycleview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new contact_adapter(this, get_search_item(q));
        id_recycleview.setAdapter(adapter);
        String x;
        if (get_search_item(q).getCount() == 0) {
            x = "Found No Contact With '" + q + "' In It.";
        } else if (get_search_item(q).getCount() == 1) {
            x = "Found Only " + get_search_item(q).getCount() + " Contact With '" + q + "' In It.";
        } else {
            x = "Found " + get_search_item(q).getCount() + " Contacts With '" + q + "' In It.";
        }
        Snackbar.make(id_recycleview, x, 3000).show();
    }

    public void searchaction_sqlite() {
        fdb.setFirestoreSettings(settings);
        search = findViewById(R.id.search);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.equals("")) {
                    db_recyclerview();
                } else {
                    db_recyclerview_query(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equals("")) {
                    db_recyclerview();
                } else {
                    db_recyclerview_query(newText);
                }
                return false;
            }
        });
        db_recyclerview();
    }

    private Cursor getallitems() {
        return sqlite.query(sqlite_basecolumns.contacts.CONTACTS_TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                sqlite_basecolumns.contacts.client_name + " ASC"
        );
    }

    private Cursor get_search_item(String q) {
        String[] whereargs = {"%" + q + "%"};
        return sqlite.query(sqlite_basecolumns.contacts.CONTACTS_TABLE_NAME + "",
                null,
                sqlite_basecolumns.contacts.client_name + " LIKE ?",
                whereargs,
                null,
                null,
                sqlite_basecolumns.contacts.client_name + " ASC"
        );
    }

    private void setupitems() {
        fab = findViewById(R.id.fab_main);
        addpeople = findViewById(R.id.addpeople);
        policy = findViewById(R.id.policy_insurance);
        diary = findViewById(R.id.diary_page);

    }

    private void fab_action() {
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

    private void fabexpanded() {
        if (!fabexpand) {
            openSubMenusFab();
        } else {
            closeSubMenusFab();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_CALL_PHONE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
                break;
//            case MY_PERMISSIONS_REQUEST_SEND_SMS:
//                if(grantResults.length>0
//                        &&grantResults[1]==PackageManager.PERMISSION_GRANTED){
//
//                }
//                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(mcontext, activity_home.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }
}
