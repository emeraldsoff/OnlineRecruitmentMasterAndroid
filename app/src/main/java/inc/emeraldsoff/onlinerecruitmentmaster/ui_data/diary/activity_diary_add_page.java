package inc.emeraldsoff.onlinerecruitmentmaster.ui_data.diary;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.perf.metrics.Trace;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import es.dmoral.toasty.Toasty;
import inc.emeraldsoff.onlinerecruitmentmaster.R;
import inc.emeraldsoff.onlinerecruitmentmaster.activity_main;
import inc.emeraldsoff.onlinerecruitmentmaster.sqlite_manager.sqlite_basecolumns;
import inc.emeraldsoff.onlinerecruitmentmaster.sqlite_manager.sqlite_helper;
import inc.emeraldsoff.onlinerecruitmentmaster.ui_data.fragment_Home.activity_home;
import inc.emeraldsoff.onlinerecruitmentmaster.ui_data.people.activity_showpeopledetails;

public class activity_diary_add_page extends activity_main {
    String diary_page, purpose, app_userid, t;
    androidx.appcompat.widget.Toolbar toolbar;
    private Context mcontext;
    //    private Source cache = Source.CACHE;
    private SharedPreferences mpref;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore fdb = FirebaseFirestore.getInstance();
    private SimpleDateFormat fullFormat_time = new SimpleDateFormat("YYYY-MMMM-dd', 'EEEE', 'hh:mm:ss a", Locale.US);
    private SimpleDateFormat fullFormat_onlytime = new SimpleDateFormat("hh:mm:ss a", Locale.US);
    //    private Button save;
    Trace trace;
    private SimpleDateFormat folderdoc = new SimpleDateFormat("YYYY-MMMM-dd', 'EEEE", Locale.US);
    private SimpleDateFormat foldername = new SimpleDateFormat("YYYY-MMMM-dd-EEEE", Locale.US);
    private SimpleDateFormat fullFormat_time_doc = new SimpleDateFormat("YYYY-MMMM-dd-EEEE-hh-mm-ss-a", Locale.US);
    private TextInputEditText note;
    private TextView date_view;
    private String folder_name, folder_doc, collection;
    private Date timestamp;
    private SimpleDateFormat fullFormat_onlydate = new SimpleDateFormat("dd MMMM yyyy', 'EEEE", Locale.US);
    private SimpleDateFormat fullFormat_prime_onlydate = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
    private Calendar myCalendar;
    SQLiteDatabase sqlite;
    sqlite_helper sqliteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_add_edit_page);
        mcontext = this;
        super.menucreate();
        mpref = getSharedPreferences("User", MODE_PRIVATE);
        writeDiary();
        setup_toolbar_menu();
    }

    public void setdate() {
        timestamp = myCalendar.getTime();
        toolbar.setTitle(fullFormat_onlydate.format(timestamp));
        //folder_name = foldername.format(timestamp);
        //folder_doc = folderdoc.format(timestamp);
        date_view.setText(fullFormat_onlytime.format(timestamp));
    }

    private void custom_date() {
        final TimePickerDialog.OnTimeSetListener timeget = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);
                setdate();
            }
        };

        final DatePickerDialog.OnDateSetListener dateget = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//                myCalendar.set(Calendar.YEAR, 0);
//                myCalendar.set(Calendar.MONTH, month);
//                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//                myCalendar.set(Calendar.HOUR_OF_DAY, 0);
//                myCalendar.set(Calendar.MINUTE, 0);
//                myCalendar.set(Calendar.SECOND, 0);
//                myCalendar.set(Calendar.MILLISECOND, 0);
                setdate();
            }
        };

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(mcontext, dateget, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        date_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(mcontext, timeget, myCalendar
                        .get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), false).show();
            }
        });

    }

    private void writeDiary() {
        toolbar = findViewById(R.id.toolbar);
        date_view = findViewById(R.id.diary_date);
        note = findViewById(R.id.diary_text);
        sqliteHelper = new sqlite_helper(mcontext);
        sqlite = sqliteHelper.getWritableDatabase();
//        timestamp = Calendar.getInstance().getTime();
        myCalendar = Calendar.getInstance();
        setdate();
        custom_date();
//        toolbar.setSubtitle(fullFormat_onlytime.format(timestamp));
        //app_userid = mpref.getString("userID", "");
        //collection = "prospect" + "/" + app_userid;
//        date = com.google.firebase.Timestamp.now().toDate().toString();
        //diary_page = Objects.requireNonNull(note.getText()).toString().trim();
        //diary_page = note.toString();
        note.requestFocus();

    }

    public void timechange() {
        myCalendar.add(Calendar.MILLISECOND, +1000);
        setdate();
    }


    protected void data_save_sqlite() {
        /*try {
            data_allocation();
            if (validation(diary_page)) {
                if (!duplicate_check()) {
                    on_no_duplicate_entry();
                } else {
                    on_duplicate_entry();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }*/
        data_allocation();
        if (validation(note.toString())) {
            if (!duplicate_check()) {
                on_no_duplicate_entry();
            } else {
                on_duplicate_entry();
            }
        }
    }

    private void data_allocation() {
        diary_page = Objects.requireNonNull(note.getText()).toString().trim();
        purpose = "NOTE";
    }

    private boolean duplicate_check() {
        String[] docid = {timestamp + purpose};
        @SuppressLint("Recycle")
        Cursor doc = sqlite.query(sqlite_basecolumns.diary.DIARY_TABLE_NAME + "",
                null,
                sqlite_basecolumns.diary._ID + " = ?",
                docid,
                null,
                null,
                null);
        return doc.getCount() != 0;
    }

    private boolean validation(String page) {
        if (page.isEmpty()) {
            note.setError("Cannot Save/Copy/Share without any data..!!");
            note.requestFocus();
            return false;
        }
        return true;
    }

    private void on_no_duplicate_entry() {

        long result = 0;
        ContentValues client = new ContentValues();
        client.put(sqlite_basecolumns.diary._ID, timestamp + purpose);
        client.put(sqlite_basecolumns.diary.content, diary_page);
        client.put(sqlite_basecolumns.diary.purpose, purpose);
        client.put(sqlite_basecolumns.diary.created_date, fullFormat_onlydate.format(timestamp));
        client.put(sqlite_basecolumns.diary.created_time, fullFormat_onlytime.format(timestamp));
        client.put(sqlite_basecolumns.diary.created_at, timestamp.toString());
        try {
            result = sqlite.insert(sqlite_basecolumns.diary.DIARY_TABLE_NAME, null, client);
        } catch (Exception e) {
            Toasty.error(mcontext, e.getMessage(), 4, true).show();
        }

        if (result == -1) {
            Toasty.error(mcontext, "Something went wrong...!!!",
                    Toast.LENGTH_LONG, true).show();
        } else {

            backup_db(mAuth.getUid());

            startActivity(new Intent(mcontext, activity_home.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
//            Toasty.success(mcontext, mcontext.getDatabasePath("megaprospects.db") + "", 4, true).show();
            Toasty.success(mcontext, "Document saved successfully..!!",
                    Toast.LENGTH_LONG, true).show();
        }
    }

    private void on_duplicate_entry() {

        final String docid = timestamp + purpose;
        new AlertDialog.Builder(mcontext).setIcon(R.drawable.ic_warning_pink_24dp)
                .setTitle("Document Conflict..!!")
                .setMessage("Document is already available for this time slot..!! \n" +
                        "Do you want to delete that and save new one??")
                .setPositiveButton("Yes..!! Please Delete Old Document.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete_old_entry();
                        //timechange();
                        on_no_duplicate_entry();
                    }
                })
                .setPositiveButton("Keep Both", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        timechange();
                        on_no_duplicate_entry();
                    }
                })
                .setNegativeButton("No..!! Please create a new document with modified time..!!", null)
                .setNeutralButton("Please Show The Document..!!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(mcontext, activity_showpeopledetails.class)
                                .putExtra("docid", docid));
                    }
                })
                .show();
    }

    private void delete_old_entry() {
        String doc_id = timestamp + purpose;
        sqlite.delete(sqlite_basecolumns.diary.DIARY_TABLE_NAME + "",
                sqlite_basecolumns.diary._ID + " = '" + doc_id + "'", null
        );
    }

    private void setup_toolbar_menu() {
        toolbar.inflateMenu(R.menu.menu_save_copy_discard_share);
        toolbar.getMenu().hasVisibleItems();
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_save:
                        menu_save();
                        return true;
                    case R.id.menu_copy:
                        menu_copy();
                        return true;
                    case R.id.menu_discard:
                        menu_discard();
                        finish();
                        return true;
                    case R.id.menu_share:
                        menu_share();
                    default:
                        return false;
                }
            }
        });
    }

    private void menu_save() {
        diary_page = Objects.requireNonNull(note.getText()).toString().trim();
        t = date_view.getText().toString().trim();
        if (validation(diary_page)) {
            data_save_sqlite();
            /*
            final DocumentReference folder_check = fdb.collection(collection + "/" + "personal_diary")
                    .document(folder_name);
            folder_check.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        pagesave_check();
                    } else {
                        cretedoc_pagesave();
                    }
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toasty.error(mcontext, "Error in writing document..!!",
                                    Toast.LENGTH_LONG, true).show();
                        }
                    });
            */
        }

    }

    private void menu_copy() {
        if (validation(diary_page)) {
            diary_page = Objects.requireNonNull(note.getText()).toString().trim();
            t = date_view.getText().toString().trim();
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText(toolbar.getTitle() + ", " + t, diary_page);
            clipboard.setPrimaryClip(clip);
        }
    }

    private void menu_discard() {
        startActivity(new Intent(mcontext, activity_diary.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }

    private void menu_share() {
        diary_page = Objects.requireNonNull(note.getText()).toString().trim();
        t = date_view.getText().toString().trim();
        if (validation(diary_page)) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "" + diary_page);
            shareIntent.putExtra(Intent.EXTRA_TITLE, toolbar.getTitle() + ", " + t);
            startActivity(Intent.createChooser(shareIntent, toolbar.getTitle() + ", " + t));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        menu_discard();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    /*
    private void cretedoc_pagesave() {
        folder_name = foldername.format(timestamp);
        Map<String, Object> folder = new HashMap<>();
        folder.put("folder_doc", folder_doc);
        folder.put("timestamp", timestamp);
        fdb.collection(collection + "/" + "personal_diary")
                .document(folder_name)
                .set(folder)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        pagesave_check();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toasty.error(mcontext, "Failed to create folder..!! Something went wrong.",
                                4, true).show();
                        startActivity(new Intent(mcontext, activity_diary.class));
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        finish();
                    }
                });
    }

    private void pagesave_check() {
        final DocumentReference doc_check = fdb
                .collection(collection + "/" + "personal_diary" + "/" + folder_name + "/" + "pages")
                .document(fullFormat_time_doc.format(timestamp));
        doc_check.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    timechange();
                    pagesave_check();
                } else {
                    pagesave();
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toasty.error(mcontext, "Error in writing document..!!",
                                Toast.LENGTH_LONG, true).show();
                    }
                });
    }

    private void pagesave() {
        diary_page = Objects.requireNonNull(note.getText()).toString().trim();
        t = date_view.getText().toString().trim();
        Map<String, Object> client = new HashMap<>();
        client.put("data", diary_page);
        client.put("timestmp", timestamp);
//        client.put("timestmp_mod", fullFormat_time.format(timestamp));
        fdb.collection(collection + "/" + "personal_diary" + "/" + folder_name + "/" + "pages")
                .document(fullFormat_time_doc.format(timestamp))
                .set(client)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toasty.success(mcontext, "Diary Page saved successfully..!!",
                                4, true).show();
                        startActivity(new Intent(mcontext, activity_contentdiary_pages.class)
                                .putExtra("folder_name", folder_name));
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toasty.error(mcontext, "Failed to save Diary Page..!! Something went wrong.",
                                4, true).show();
                        startActivity(new Intent(mcontext, activity_diary.class));
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        finish();
                    }
                });
    }
*/
}
