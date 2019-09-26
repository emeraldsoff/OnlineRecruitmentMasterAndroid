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

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.perf.metrics.Trace;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import androidx.appcompat.widget.Toolbar;
import es.dmoral.toasty.Toasty;
import inc.emeraldsoff.onlinerecruitmentmaster.R;
import inc.emeraldsoff.onlinerecruitmentmaster.activity_main;
import inc.emeraldsoff.onlinerecruitmentmaster.sqlite_manager.sqlite_basecolumns;
import inc.emeraldsoff.onlinerecruitmentmaster.sqlite_manager.sqlite_commands;
import inc.emeraldsoff.onlinerecruitmentmaster.sqlite_manager.sqlite_helper;
import inc.emeraldsoff.onlinerecruitmentmaster.ui_data.people.activity_showpeopledetails;

public class activity_diary_edit_page extends activity_main {

    Toolbar toolbar;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    SQLiteDatabase sqlite;
    sqlite_helper sqliteHelper;
    FirebaseFirestore fdb = FirebaseFirestore.getInstance();
    //String folder_name, docid, path, app_userid;
    String diary_page, purpose, date;
    String dt = null;
    String t = null;
    //String collection;
    private Context mcontext;
    private SharedPreferences mpref;
    private TextView date_view;

//    private SimpleDateFormat fullFormat_onlydate = new SimpleDateFormat("dd MMMM yyyy', 'EEEE", Locale.US);

    //private SimpleDateFormat fullFormat_time = new SimpleDateFormat("YYYY-MMMM-dd', 'EEEE', 'hh:mm:ss a", Locale.US);
    private SimpleDateFormat fullFormat_onlytime = new SimpleDateFormat("hh:mm:ss a", Locale.US);
    private SimpleDateFormat fullFormat_onlydate = new SimpleDateFormat("dd MMMM yyyy', 'EEEE", Locale.US);
    //private SimpleDateFormat folderdoc = new SimpleDateFormat("YYYY-MMMM-dd', 'EEEE", Locale.US);
    //private SimpleDateFormat foldername = new SimpleDateFormat("YYYY-MMMM-dd-EEEE", Locale.US);
    //private SimpleDateFormat fullFormat_time_doc = new SimpleDateFormat("YYYY-MMMM-dd-EEEE-hh-mm-ss-a", Locale.US);
    //private SimpleDateFormat fullFormat_prime_onlydate = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
    Trace trace;
    private TextInputEditText note;
    private Date timestamp;
    private Calendar myCalendar;

    private String xdocid,new_docid,xtimestamp_date,new_timestamp_date,xtimestamp_time,new_timestamp_time,xtimestamp,newtimestamp,xpurpose,new_purpose;
    private boolean datachanged = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.analytics();
        super.trace(trace);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_add_edit_page);
        mcontext = this;
        super.menucreate();
        //fetchingdata_firebase();
        fetchingdata_sqlite();
        writeDiary();
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
        date_view = findViewById(R.id.diary_date);
        note = findViewById(R.id.diary_text);
        xtimestamp_date = doc.getString(sqlite_commands.diary_created_date);
        xtimestamp_time=doc.getString(sqlite_commands.diary_created_time);
        xpurpose = doc.getString(sqlite_commands.diary_purpose);
        xtimestamp = doc.getString(sqlite_commands.diary_purpose);
        toolbar.setTitle(doc.getString(sqlite_commands.diary_created_date));
        date_view.setText(doc.getString(sqlite_commands.diary_created_time));
        note.setText(doc.getString(sqlite_commands.diary_content));
    }

    public void setdate() {
        timestamp = myCalendar.getTime();
        toolbar.setTitle(fullFormat_onlydate.format(timestamp));
        date_view.setText(fullFormat_onlytime.format(timestamp));
        new_timestamp_date = fullFormat_onlydate.format(timestamp);
        new_timestamp_time = fullFormat_onlytime.format(timestamp);
        new_docid = timestamp + new_purpose;
        newtimestamp = timestamp.toString();
        datachanged = true;
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
        myCalendar = Calendar.getInstance();
        custom_date();
        note.requestFocus();
    }

    private void data_allocation() {
        diary_page = Objects.requireNonNull(note.getText()).toString().trim();
        purpose = "NOTE";
        new_purpose = purpose;
    }

    private boolean validation(String page) {
        if (page.isEmpty()) {
            note.setError("Cannot Save/Copy/Share without any data..!!");
            note.requestFocus();
            return false;
        }
        return true;
    }

    public void timechange() {
        myCalendar.add(Calendar.MILLISECOND, +1000);
        setdate();
    }

    protected void data_update_sqlite() {
        data_allocation();
        if (validation(note.toString())) {
            if(!datachanged)
            {
                update_data_with_no_timestamp_change(xdocid);
            }
            else{
                update_data_with_timestamp_change(xdocid,new_docid);
            }
        }
    }

    private void update_data_with_no_timestamp_change(String doc_id) {
        long result = 0;
        ContentValues client = new ContentValues();
        client.put(sqlite_basecolumns.diary._ID, doc_id);
        client.put(sqlite_basecolumns.diary.content, diary_page);
        client.put(sqlite_basecolumns.diary.purpose, xpurpose);
        client.put(sqlite_basecolumns.diary.created_date, xtimestamp_date);
        client.put(sqlite_basecolumns.diary.created_time, xtimestamp_time);
        client.put(sqlite_basecolumns.diary.created_at, xtimestamp);
        try {
            result = sqlite.update(sqlite_basecolumns.diary.DIARY_TABLE_NAME,
                    client, sqlite_basecolumns.diary._ID + " = '" + doc_id + "'",
                    null);
//                            result = sqlite.insert(sqlite_basecolumns.contacts.CONTACTS_TABLE_NAME, null, client);
        } catch (Exception e) {
            Toasty.error(mcontext, e.getMessage(), 4, true).show();
        }

        if (result == -1) {
            Toasty.error(mcontext, "Something went wrong...!!!",
                    Toast.LENGTH_LONG, true).show();
        } else {

            backup_db(mAuth.getUid());

            startActivity(new Intent(mcontext, activity_diary_show_page.class)
                    .putExtra("docid",doc_id));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
//            Toasty.success(mcontext, mcontext.getDatabasePath("megaprospects.db") + "", 4, true).show();
            Toasty.success(mcontext, "Document saved successfully..!!",
                    Toast.LENGTH_LONG, true).show();
        }
    }

    private void update_data_with_timestamp_change(String doc_id,String newdocid) {
        delete_entry(doc_id);
        if (!duplicate_check()) {
            on_no_duplicate_entry(newdocid);
        } else {
            on_duplicate_entry(newdocid);
        }
    }

    private boolean duplicate_check() {
        String[] docid1 = {timestamp + purpose};
        @SuppressLint("Recycle")
        Cursor doc = sqlite.query(sqlite_basecolumns.diary.DIARY_TABLE_NAME + "",
                null,
                sqlite_basecolumns.diary._ID + " = ?",
                docid1,
                null,
                null,
                null);
        return doc.getCount() != 0;
    }

    private void on_no_duplicate_entry(String newdocid) {
        long result = 0;
        ContentValues client = new ContentValues();
        client.put(sqlite_basecolumns.diary._ID, newdocid);
        client.put(sqlite_basecolumns.diary.content, diary_page);
        client.put(sqlite_basecolumns.diary.purpose, purpose);
        client.put(sqlite_basecolumns.diary.created_date, new_timestamp_date);
        client.put(sqlite_basecolumns.diary.created_time, new_timestamp_time);
        client.put(sqlite_basecolumns.diary.created_at, newtimestamp);
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

            startActivity(new Intent(mcontext, activity_diary_show_page.class)
                            .putExtra("docid",newdocid));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
            Toasty.success(mcontext, "Document updated successfully..!!",
                    Toast.LENGTH_LONG, true).show();
        }
    }

    private void on_duplicate_entry(final String newdocid) {

        //final String docid = timestamp + purpose;
        new AlertDialog.Builder(mcontext).setIcon(R.drawable.ic_warning_pink_24dp)
                .setTitle("Document Conflict..!!")
                .setMessage("Document is already available for this time slot..!! \n" +
                        "Do you want to delete that and save new one??")
                .setPositiveButton("Yes..!! Please Delete Old Document.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete_old_entry();
                        //timechange();
                        on_no_duplicate_entry(newdocid);
                    }
                })
                .setPositiveButton("Keep Both", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        timechange();
                        on_no_duplicate_entry(new_docid);
                    }
                })
                .setNegativeButton("No..!! Please create a new document with modified time..!!", null)
                .setNeutralButton("Please Show The Document..!!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(mcontext, activity_showpeopledetails.class)
                                .putExtra("docid", newdocid));
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

    private void delete_entry(String doc_id) {
        sqlite.delete(sqlite_basecolumns.diary.DIARY_TABLE_NAME + "",
                sqlite_basecolumns.diary._ID + " = '" + doc_id + "'", null
        );
    }

    private void setup_toolbar_menu() {
        toolbar.inflateMenu(R.menu.menu_update_copy_delete_share);
        toolbar.getMenu().hasVisibleItems();
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_update:
                        menu_update();
                        return true;
                    case R.id.menu_copy:
                        menu_copy();
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

    private void menu_update() {
        diary_page = Objects.requireNonNull(note.getText()).toString().trim();
        t = date_view.getText().toString().trim();
        if (validation(diary_page)) {
            data_update_sqlite();
        }
    }

    private void menu_copy() {
        diary_page = Objects.requireNonNull(note.getText()).toString().trim();
        if (validation(diary_page)) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText(dt + ", " + t, note.getText().toString().trim());
            clipboard.setPrimaryClip(clip);
        }
    }

    public void menu_del() {
        final String doc_id = getIntent().getStringExtra("docid");
        if (isOnline() || mpref.getBoolean("IF_VALID", true)) {
            new AlertDialog.Builder(mcontext).setIcon(R.drawable.ic_warning_pink_24dp).setTitle("Delete")
                    .setMessage("Are you sure to delete this document?")
                    .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            delete_entry(doc_id);
                        }
                    }).setNegativeButton("No", null).show();
        } else {
            //TO DO comment 1st line
            Toasty.error(mcontext, "Turn on wifi or mobile data to edit..!!",
                    4, true).show();
        }
    }

    private void menu_share() {
        diary_page = Objects.requireNonNull(note.getText()).toString().trim();
        if (validation(diary_page)) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "" + note.getText().toString().trim());
            shareIntent.putExtra(Intent.EXTRA_TITLE, dt + ", " + t);
            startActivity(Intent.createChooser(shareIntent, dt + ", " + t));
        }
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "" + note.getText().toString().trim());
        shareIntent.putExtra(Intent.EXTRA_TITLE, dt + ", " + t);
        startActivity(Intent.createChooser(shareIntent, dt + ", " + t));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Toast.makeText(this,"startActivity(new Intent(mcontext, activity_searchpeople.class));",Toast.LENGTH_SHORT).show();
//        startActivity(new Intent(mcontext, activity_contentdiary_pages.class).putExtra("folder_name", folder_name));
        startActivity(new Intent(mcontext, activity_diary.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    /*
    protected void fetchingdata_firebase() {
        mpref = getSharedPreferences("User", MODE_PRIVATE);
        toolbar = findViewById(R.id.toolbar);
        date_view = findViewById(R.id.diary_date);
        note = findViewById(R.id.diary_text);

        folder_name = getIntent().getStringExtra("folder_name");
        docid = getIntent().getStringExtra("docid");
//        path = getIntent().getStringExtra("path");

        app_userid = mpref.getString("userID", "");
        collection = "prospect" + "/" + app_userid;
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
                        date_view.setText(fullFormat_onlytime.format(x));
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

    }

    private boolean validation() {
        if (diary_page.isEmpty()) {
            note.setError("Cannot Update/Copy/Share without any data..!!");
            note.requestFocus();
            return false;
        }
        return true;
    }*/

}
