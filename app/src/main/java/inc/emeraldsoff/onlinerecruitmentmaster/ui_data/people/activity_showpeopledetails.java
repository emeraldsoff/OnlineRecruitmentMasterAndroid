package inc.emeraldsoff.onlinerecruitmentmaster.ui_data.people;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.perf.metrics.Trace;

import androidx.cardview.widget.CardView;
import es.dmoral.toasty.Toasty;
import inc.emeraldsoff.onlinerecruitmentmaster.R;
import inc.emeraldsoff.onlinerecruitmentmaster.activity_main;
import inc.emeraldsoff.onlinerecruitmentmaster.sqlite_manager.sqlite_basecolumns;
import inc.emeraldsoff.onlinerecruitmentmaster.sqlite_manager.sqlite_commands;
import inc.emeraldsoff.onlinerecruitmentmaster.sqlite_manager.sqlite_helper;
import inc.emeraldsoff.onlinerecruitmentmaster.ui_data.diary.activity_diary_add_page;

public class activity_showpeopledetails extends activity_main {

    androidx.appcompat.widget.Toolbar toolbar;
    SQLiteDatabase sqlite;
    sqlite_helper sqliteHelper;
    private Context mcontext;
    private SharedPreferences mpref;
    Trace trace;
    FloatingActionButton fab;
    boolean fabexpand = false;
    CardView addpeople, policy, diary;

    private TextView clientname_e, spous, child;
    private TextView adri_road_house, adrii_vlg_area, adriii_city, adriv_po, adrv_pin, adrvi_dist, adrvii_state, adrviii_country;
    private TextView std_e, mob, smob, tele, email, not;
    private TextView ann, bda, gender, occupation, qualifi, emplyr;
    private Button edit, delete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showpeopledetails);
        mcontext = this;
        super.menucreate();
        setupitems();
        fab_action();
//        fetchingpersondetails();
        fetchingpersondetails_sqlite();

    }

    @SuppressLint("SetTextI18n")
    protected void fetchingpersondetails_sqlite() {

        final String[] docid = {getIntent().getStringExtra("docid")};
        final String doc_id = getIntent().getStringExtra("docid");
        sqliteHelper = new sqlite_helper(mcontext);
        sqlite = sqliteHelper.getWritableDatabase();

//        Cursor doc = sqlite.rawQuery("SELECT * FROM "+sqlite_basecolumns.contacts.CONTACTS_TABLE_NAME+
//                " WHERE "+sqlite_basecolumns.contacts._ID+" = "+docid,null);

        @SuppressLint("Recycle")
        Cursor doc = sqlite.query(sqlite_basecolumns.contacts.CONTACTS_TABLE_NAME + "",
                null,
                sqlite_basecolumns.contacts._ID + " = ?",
                docid,
                null,
                null,
                null);

        doc.moveToFirst();

        toolbar.setTitle(doc.getString(sqlite_commands.contacts_client_name));

        clientname_e.setText("Name: " + doc.getString(sqlite_commands.contacts_client_name));
        spous.setText(doc.getString(sqlite_commands.contacts_spouse));
        child.setText(doc.getString(sqlite_commands.contacts_children));

        bda.setText(doc.getString(sqlite_commands.contacts_bday_dd));
        ann.setText(doc.getString(sqlite_commands.contacts_anni_dd));

        adri_road_house.setText("Road name/Area Name/House Number: " + doc.getString(sqlite_commands.contacts_address_i));
        adrii_vlg_area.setText("Village/Area Name: " + doc.getString(sqlite_commands.contacts_address_ii));
        adriii_city.setText("City: " + doc.getString(sqlite_commands.contacts_city));
        adriv_po.setText("Post Office: " + doc.getString(sqlite_commands.contacts_post_office));
        adrv_pin.setText("PIN: " + doc.getString(sqlite_commands.contacts_areapin));
        adrvi_dist.setText("District: " + doc.getString(sqlite_commands.contacts_dist));
        adrvii_state.setText("State: " + doc.getString(sqlite_commands.contacts_state));
        adrviii_country.setText("Country: " + doc.getString(sqlite_commands.contacts_country));

        std_e.setText("STD Code: " + doc.getString(sqlite_commands.contacts_std));
        mob.setText("Mobile Number: " + doc.getString(sqlite_commands.contacts_mobile_no));
        smob.setText("WhatsApp Number: " + doc.getString(sqlite_commands.contacts_smobile_no));
        tele.setText("Telephone Number: " + doc.getString(sqlite_commands.contacts_telephoneno));
        email.setText("Email Id: " + doc.getString(sqlite_commands.contacts_emailid));
        not.setText(doc.getString(sqlite_commands.contacts_note));
        qualifi.setText(doc.getString(sqlite_commands.contacts_qualification));
        gender.setText(doc.getString(sqlite_commands.contacts_gender));
        occupation.setText(doc.getString(sqlite_commands.contacts_occupation));
        emplyr.setText(doc.getString(sqlite_commands.contacts_employer));
//        Toasty.info(mcontext,doc.getString(0),4,true).show();

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (activity_showpeopledetails.super.full_access_bool()) {
                    startActivity(new Intent(mcontext, activity_editpeople.class).putExtra("docid", doc_id));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                } else {
//                    startActivity(new Intent(mcontext, activity_editpeople.class).putExtra("docid", docid));
                    Toasty.error(mcontext, "Turn on wifi or mobile data to edit..!!",
                            4, true).show();
                }
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (activity_showpeopledetails.super.full_access_bool()) {
                    new AlertDialog.Builder(mcontext).setIcon(R.drawable.ic_warning_pink_24dp).setTitle("Delete")
                            .setMessage("Are you sure to delete this document?")
                            .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    delete_sqlite_contacts(doc_id);
                                }
                            }).setNegativeButton("No", null).show();
                } else {
                    Toasty.error(mcontext, "Turn on wifi or mobile data to edit..!!",
                            4, true).show();
                }
            }
        });
    }

    private void delete_sqlite_contacts(String doc_id) {
        sqlite.delete(sqlite_basecolumns.contacts.CONTACTS_TABLE_NAME + "",
                sqlite_basecolumns.contacts._ID + " = '" + doc_id + "'", null
        );
        startActivity(new Intent(mcontext, activity_searchpeople.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

//    private Cursor getData(){
//        return sqlite.query(sqlite_basecolumns.contacts.CONTACTS_TABLE_NAME + "",
//                null,
//                sqlite_basecolumns.contacts._ID + " = ?",
//                docid,
//                null,
//                null,
////                null
//                sqlite_basecolumns.contacts.client_name + " ASC");
//
//    }

    public void setupitems() {
        mpref = getSharedPreferences("User", MODE_PRIVATE);
        toolbar = findViewById(R.id.toolbar);

        edit = findViewById(R.id.edit);
        delete = findViewById(R.id.save);
        clientname_e = findViewById(R.id.clname);
        spous = findViewById(R.id.spouse);
        child = findViewById(R.id.children);

        bda = findViewById(R.id.bday_1);
        ann = findViewById(R.id.anni_1);

        adri_road_house = findViewById(R.id.addressi);
        adrii_vlg_area = findViewById(R.id.addressii);
        adriii_city = findViewById(R.id.addressiii);
        adriv_po = findViewById(R.id.addressiv);
        adrv_pin = findViewById(R.id.addressv);
        adrvi_dist = findViewById(R.id.addressvi);
        adrvii_state = findViewById(R.id.addressvii);
        adrviii_country = findViewById(R.id.addressviii);

        qualifi = findViewById(R.id.qualifi);
        std_e = findViewById(R.id.std_code);
        mob = findViewById(R.id.mob_no);
        smob = findViewById(R.id.sec_mob_no);
        tele = findViewById(R.id.telephone_no);
        email = findViewById(R.id.email_id);
        not = findViewById(R.id.note);

        gender = findViewById(R.id.gender_select);
        occupation = findViewById(R.id.occu_grp);
        emplyr = findViewById(R.id.employer);
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
//        startActivity(new Intent(mcontext, activity_searchpeople.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }
}
