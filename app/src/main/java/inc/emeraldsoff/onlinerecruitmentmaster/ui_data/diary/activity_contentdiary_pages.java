/*package inc.emeraldsoff.onlinerecruitmentmaster.ui_data.diary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Source;
import com.google.firebase.perf.metrics.Trace;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;
import inc.emeraldsoff.onlinerecruitmentmaster.R;
import inc.emeraldsoff.onlinerecruitmentmaster.activity_main;
import inc.emeraldsoff.onlinerecruitmentmaster.firebase.adapter.diary_adapter;
import inc.emeraldsoff.onlinerecruitmentmaster.firebase.model.diarycard_page_gen;
import inc.emeraldsoff.onlinerecruitmentmaster.ui_data.people.activity_addpeople;

public class activity_contentdiary_pages extends activity_main {
    //    private SimpleDateFormat folderdoc = new SimpleDateFormat("YYYY-MMMM-dd', 'EEEE", Locale.US);
//    private SimpleDateFormat foldername = new SimpleDateFormat("YYYY-MMMM-dd-EEEE", Locale.US);
//    private SimpleDateFormat fullFormat_time_doc = new SimpleDateFormat("YYYY-MMMM-dd-EEEE-hh-mm-ss-a", Locale.US);
    FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build();
    //    private SimpleDateFormat fullFormat_time = new SimpleDateFormat("YYYY-MMMM-dd', 'EEEE', 'hh:mm:ss a", Locale.US);
//    private SimpleDateFormat fullFormat_onlytime = new SimpleDateFormat("hh:mm:ss a", Locale.US);
    private SimpleDateFormat fullFormat_onlydate = new SimpleDateFormat("dd MMMM yyyy', 'EEEE", Locale.US);

    SharedPreferences mpref;
    RecyclerView id_recycleview;
    //    ScrollView scrollview;
    FirebaseFirestore fdb = FirebaseFirestore.getInstance();
    Source cache = Source.CACHE;
    FloatingActionButton fab;
    boolean fabexpand = false;
    CardView addpeople, policy, diary;
    int fab_ht;
    androidx.appcompat.widget.Toolbar toolbar;
    String docid, folder_name, app_userid;
    private Context mcontext;
    private diary_adapter adapter;
    Trace trace;

    @Override
    protected void onStart() {
        super.onStart();
//        mAuth = FirebaseAuth.getInstance();
        int SPLASH_DISPLAY_LENGTH = 0;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.startListening();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.startListening();
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        adapter.stopListening();
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.stopListening();
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
        setDiary();


    }

    private void setDiary() {
        fdb.setFirestoreSettings(settings);
        toolbar = findViewById(R.id.toolbar);
        mpref = getSharedPreferences("User", MODE_PRIVATE);
        folder_name = getIntent().getStringExtra("folder_name");
//        try {
//            Date date = foldername.parse(folder_name);
//            Toasty.info(mcontext,date.toString()
//                    ,4).show();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        app_userid = mpref.getString("userID", "");
        String collection = "prospect" + "/" + app_userid;
        DocumentReference user = fdb.collection(collection + "/" + "personal_diary").document(folder_name);
        try {
            user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot doc = task.getResult();
                        if (doc != null) {
                            Date x = Objects.requireNonNull(doc.getTimestamp("timestamp")).toDate();
//                        note_text.append(doc.get("data"));
                            toolbar.setTitle(fullFormat_onlydate.format(x));

                        } else {
                            Toasty.error(mcontext, "Document is not available..!!",
                                    Toast.LENGTH_LONG, true).show();
                        }

                    }
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toasty.error(mcontext, "Something went wrong..!!",
                                    Toast.LENGTH_LONG, true).show();
                        }
                    });
        } catch (Exception e) {
            Toasty.error(mcontext, e.getMessage(), 4, true).show();
        }
        Query query;
        final CollectionReference cliref = fdb.collection(collection + "/" + "personal_diary" + "/" +
                folder_name + "/" + "pages");
        try {
            query = cliref.orderBy("timestmp", Query.Direction.DESCENDING)
            ;
            FirestoreRecyclerOptions<diarycard_page_gen> options = new FirestoreRecyclerOptions.Builder<diarycard_page_gen>()
                    .setQuery(query, diarycard_page_gen.class)
                    .build();
            adapter = new diary_adapter(options);
            id_recycleview = findViewById(R.id.id_recycle_view);
            id_recycleview.setHasFixedSize(true);
            id_recycleview.setLayoutManager(new LinearLayoutManager(this));
            id_recycleview.setAdapter(adapter);

        } catch (Exception e) {
            Toast.makeText(mcontext, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void setupitems() {
        fab = findViewById(R.id.fab_main);
        addpeople = findViewById(R.id.addpeople);
        policy = findViewById(R.id.policy_insurance);
        diary = findViewById(R.id.diary_page);

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
        policy.setVisibility(View.VISIBLE);
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
        startActivity(new Intent(mcontext, activity_diary.class));
        isDestroyed();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
*/