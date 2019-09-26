package inc.emeraldsoff.onlinerecruitmentmaster.ui_data.diary;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.perf.metrics.Trace;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import es.dmoral.toasty.Toasty;
import inc.emeraldsoff.onlinerecruitmentmaster.R;
import inc.emeraldsoff.onlinerecruitmentmaster.activity_main;
import inc.emeraldsoff.onlinerecruitmentmaster.sqlite_manager.sqlite_basecolumns;
import inc.emeraldsoff.onlinerecruitmentmaster.sqlite_manager.sqlite_commands;
import inc.emeraldsoff.onlinerecruitmentmaster.sqlite_manager.sqlite_helper;
import inc.emeraldsoff.onlinerecruitmentmaster.ui_data.people.activity_addpeople;

public class activity_diary_show_page extends activity_main {

    androidx.appcompat.widget.Toolbar toolbar;
    SQLiteDatabase sqlite;
    sqlite_helper sqliteHelper;
    FirebaseFirestore fdb = FirebaseFirestore.getInstance();
    String folder_name, docid, path, app_userid;
    private Context mcontext;
    FloatingActionButton fab;
    boolean fabexpand = false;
    CardView addpeople, policy, diary;
    private SharedPreferences mpref;
    private TextView note, time;
    String dt = null;
    String t = null;
    private SimpleDateFormat fullFormat_time = new SimpleDateFormat("YYYY-MMMM-dd', 'EEEE', 'hh:mm:ss a", Locale.US);
    private SimpleDateFormat fullFormat_onlytime = new SimpleDateFormat("hh:mm:ss a", Locale.US);
    private SimpleDateFormat fullFormat_onlydate = new SimpleDateFormat("dd MMMM yyyy', 'EEEE", Locale.US);
    private SimpleDateFormat folderdoc = new SimpleDateFormat("YYYY-MMMM-dd', 'EEEE", Locale.US);
    private SimpleDateFormat foldername = new SimpleDateFormat("YYYY-MMMM-dd-EEEE", Locale.US);
    private SimpleDateFormat fullFormat_time_doc = new SimpleDateFormat("YYYY-MMMM-dd-EEEE-hh-mm-ss-a", Locale.US);
    SimpleDateFormat timestamp_format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:SSS");
    Trace trace;
    String xdocid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.analytics();
        super.trace(trace);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_page_show);
        mcontext = this;
        super.menucreate();
        setupitems();
        fab_action();
        //fetchingdata_firebase();
        fetchingdata_sqlite();
        setup_toolbar_menu();
    }

    @SuppressLint("SetTextI18n")
    protected void fetchingdata_sqlite() {

        final String[] docid = {getIntent().getStringExtra("docid")};
        final String doc_id = getIntent().getStringExtra("docid");
        xdocid = doc_id;
        sqliteHelper = new sqlite_helper(mcontext);
        sqlite = sqliteHelper.getWritableDatabase();

//        Cursor doc = sqlite.rawQuery("SELECT * FROM "+sqlite_basecolumns.contacts.CONTACTS_TABLE_NAME+
//                " WHERE "+sqlite_basecolumns.contacts._ID+" = "+docid,null);

        @SuppressLint("Recycle")
        Cursor doc = sqlite.query(sqlite_basecolumns.diary.DIARY_TABLE_NAME + "",
                null,
                sqlite_basecolumns.diary._ID + " = ?",
                docid,
                null,
                null,
                null);

        doc.moveToFirst();
        toolbar = findViewById(R.id.toolbar);
        time = findViewById(R.id.diary_date);
        note = findViewById(R.id.diary_text);
        toolbar.setTitle(doc.getString(sqlite_commands.diary_created_date));
        time.setText(doc.getString(sqlite_commands.diary_created_time));
        note.setText(doc.getString(sqlite_commands.diary_content));
    }

    protected void fetchingdata_firebase() {
        mpref = getSharedPreferences("User", MODE_PRIVATE);
        toolbar = findViewById(R.id.toolbar);
        time = findViewById(R.id.diary_date);
        note = findViewById(R.id.diary_text);

        folder_name = getIntent().getStringExtra("folder_name");
        docid = getIntent().getStringExtra("docid");
//        path = getIntent().getStringExtra("path");

        app_userid = mpref.getString("userID", "");
        String collection = "prospect" + "/" + app_userid;
        DocumentReference user = fdb.collection(collection + "/" + "personal_diary" + "/" +
                folder_name + "/" + "pages").document(docid);
        user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();

//                    StringBuilder note_text = new StringBuilder();

                    if (doc != null) {
                        Date x = Objects.requireNonNull(doc.getTimestamp("timestmp")).toDate();
//                        note_text.append(doc.get("data"));

                        toolbar.setTitle(fullFormat_onlydate.format(x));
                        time.setText(fullFormat_onlytime.format(x));
                        note.setText(Objects.requireNonNull(doc.get("data")).toString());

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


//        edit.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//
//                if (isOnline() || mpref.getBoolean("IF_VALID", true)) {
//                    startActivity(new Intent(mcontext, activity_editpeople.class).putExtra("docid", docid));
//                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                    finish();
//                } else {
////                    startActivity(new Intent(mcontext, activity_editpeople.class).putExtra("docid", docid));
//                    Toasty.error(mcontext, "Turn on wifi or mobile data to edit..!!",
//                            4, true).show();
//                }
//                return false;
//            }
//        });
//
//        del.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {

//                if (isOnline() || mpref.getBoolean("IF_VALID", true)) {
//                    new AlertDialog.Builder(mcontext).setIcon(R.drawable.ic_warning_pink_24dp).setTitle("Delete")
//                            .setMessage("Are you sure to delete this document?")
//                            .setPositiveButton("yes", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
////                                    mAuth = FirebaseAuth.getInstance();
////                                    String app_userid = mAuth.getUid();
//                                    String collection = "prospect" + "/" + app_userid;
//
//                                    DocumentReference user = fdb.collection(collection + "/" + "personal_diary" + "/" +
//                                            folder_name + "/" + "pages").document(docid);
////                                fdb.document(collection+"/"+docid)
//                                    user.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//                                        @Override
//                                        public void onSuccess(Void aVoid) {
//                                            Toasty.success(mcontext, "Diary page deleted From DataBase Successfully..!!",
//                                                    Toast.LENGTH_LONG, true).show();
//
//                                            startActivity(new Intent(mcontext, activity_contentdiary_pages.class));
//                                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                                            finish();
//                                        }
//                                    }).addOnFailureListener(new OnFailureListener() {
//                                        @Override
//                                        public void onFailure(@NonNull Exception e) {
//                                            Toasty.error(mcontext, "Error deleting document..!!",
//                                                    Toast.LENGTH_LONG, true).show();
//                                        }
//                                    });
//                                }
//                            }).setNegativeButton("No", null).show();
//                } else {
//                    Toasty.error(mcontext, "Turn on wifi or mobile data to edit..!!",
//                            4, true).show();
//                }
//                return false;
//            }
//        });
    }

    private void setup_toolbar_menu() {
        toolbar.inflateMenu(R.menu.menu_copy_edit_delete_share);
        toolbar.getMenu().hasVisibleItems();
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_copy:
                        menu_copy();
                        return true;
                    case R.id.menu_edit:
                        menu_edit();
                        return true;
                    case R.id.menu_delete:
                        menu_del();
                        return true;
                    case R.id.menu_share:
                        menu_share();
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    private void menu_copy() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(dt + ", " + t, note.getText().toString().trim());
        clipboard.setPrimaryClip(clip);
    }

    public void menu_edit() {
        if (isOnline() || mpref.getBoolean("IF_VALID", true)) {
            startActivity(new Intent(mcontext, activity_diary_edit_page.class)
                    .putExtra("docid", xdocid));
            //.putExtra("folder_name", folder_name));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        } else {
//                    startActivity(new Intent(mcontext, activity_editpeople.class).putExtra("docid", docid));
            Toasty.error(mcontext, "Turn on wifi or mobile data to edit..!!",
                    4, true).show();
        }
    }

    public void menu_del() {
        if (isOnline() || mpref.getBoolean("IF_VALID", true)) {
            new AlertDialog.Builder(mcontext).setIcon(R.drawable.ic_warning_pink_24dp).setTitle("Delete")
                    .setMessage("Are you sure to delete this document?")
                    .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                                    mAuth = FirebaseAuth.getInstance();
//                                    String app_userid = mAuth.getUid();
                            String collection = "prospect" + "/" + app_userid;

                            DocumentReference user = fdb.collection(collection + "/" + "personal_diary" + "/" +
                                    folder_name + "/" + "pages").document(docid);
//                                fdb.document(collection+"/"+docid)
                            user.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toasty.success(mcontext, "Diary page deleted From DataBase Successfully..!!",
                                            Toast.LENGTH_LONG, true).show();

//                                    startActivity(new Intent(mcontext, activity_contentdiary_pages.class));
//                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toasty.error(mcontext, "Error deleting document..!!",
                                            Toast.LENGTH_LONG, true).show();
                                }
                            });
                        }
                    }).setNegativeButton("No", null).show();
        } else {
            //TO DO comment 1st line
            Toasty.error(mcontext, "Turn on wifi or mobile data to edit..!!",
                    4, true).show();
        }
    }

    private void menu_share() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "" + note.getText().toString().trim());
        shareIntent.putExtra(Intent.EXTRA_TITLE, dt + ", " + t);
        startActivity(Intent.createChooser(shareIntent, dt + ", " + t));
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Toast.makeText(this,"startActivity(new Intent(mcontext, activity_searchpeople.class));",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(mcontext, activity_diary.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }
}
