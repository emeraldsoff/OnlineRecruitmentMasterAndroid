package inc.emeraldsoff.onlinerecruitmentmaster.ui_data.people;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.perf.metrics.Trace;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.NonNull;
import es.dmoral.toasty.Toasty;
import inc.emeraldsoff.onlinerecruitmentmaster.R;
import inc.emeraldsoff.onlinerecruitmentmaster.activity_main;
import inc.emeraldsoff.onlinerecruitmentmaster.sqlite_manager.sqlite_basecolumns;
import inc.emeraldsoff.onlinerecruitmentmaster.sqlite_manager.sqlite_helper;
import inc.emeraldsoff.onlinerecruitmentmaster.ui_data.fragment_Home.activity_home;

import static android.content.ContentValues.TAG;

public class activity_addpeople extends activity_main {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore fdb = FirebaseFirestore.getInstance();
    SQLiteDatabase sqlite;
    sqlite_helper sqliteHelper;
    //    FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
//            .setTimestampsInSnapshotsEnabled(true)
//            .setPersistenceEnabled(true)
//            .build();
    Calendar myCalendar = Calendar.getInstance();
    //    Calendar myCalendar2 = Calendar.getInstance();
    SimpleDateFormat fullFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
    //    SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.US);
//    SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.US);
//    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.US);
    SimpleDateFormat day_monFormat = new SimpleDateFormat("dd-MM", Locale.US);
    String client_name, spouse, children;
    String address_i, address_ii, city, country, post_office, areapin, dist, state, employer;

    //    Date now = new Date();
//    String nowdate = dayformat.format(now);
//    String month = monthFormat.format(now);
    String std, mobile_no, smobile_no, telephoneno, emailid;
    String gender, date, app_userid;
    String g;
    String anni_dd, bday_dd;
    String bday_code, anni_code;
    String occu, occupation, note, qualification;
    private Context mcontext;
    private SharedPreferences mpref;
    private EditText clientname_e, spous, child;
    private EditText adri_road_house, adrii_vlg_area, adriii_city, adriv_po, adrv_pin, adrvi_dist, adrvii_state, adrviii_country;
    private EditText std_e, mob, smob, tele, email, not;
    private EditText ann_dd, bda_dd, qualifi, emplyr;
    private RadioButton male, female, unspecified, sel, gov, stud, unemp, hom, otr, serv;
    private RadioGroup gender_grp, occu_grp;
    private Button update, save, clear_bday, clear_anni;
    Trace trace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addpeople);
        mcontext = this;
        super.menucreate();
        setupitems();
        addingperson_sqlite();
//        addingperson_firestore();

    }

    protected void addingperson_sqlite() {
        mpref = getSharedPreferences("User", MODE_PRIVATE);

        final DatePickerDialog.OnDateSetListener dateget1 = new DatePickerDialog.OnDateSetListener() {
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
                annidate();
            }
        };
        final DatePickerDialog.OnDateSetListener dateget2 = new DatePickerDialog.OnDateSetListener() {
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
                bdadate();
            }
        };

        ann_dd.setOnClickListener(new View.OnClickListener() {
            Calendar myCalendar = Calendar.getInstance();

            @Override
            public void onClick(View v) {
                new DatePickerDialog(mcontext, dateget1, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        bda_dd.setOnClickListener(new View.OnClickListener() {
            Calendar myCalendar = Calendar.getInstance();

            @Override
            public void onClick(View v) {
                new DatePickerDialog(mcontext, dateget2, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        clear_bday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bda_dd.setText("");
                bday_code = null;
            }
        });

        clear_anni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ann_dd.setText("");
                anni_code = null;
            }
        });

        occu_grp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.unemployed:
                        occu = unemp.getText().toString();
                        break;
                    case R.id.student:
                        occu = stud.getText().toString();
                        break;
                    case R.id.home:
                        occu = hom.getText().toString();
                        break;
                    case R.id.self:
                        occu = sel.getText().toString();
                        break;
                    case R.id.govtservice:
                        occu = gov.getText().toString();
                        break;
                    case R.id.service:
                        occu = serv.getText().toString();
                        break;
                    case R.id.other:
                        occu = otr.getText().toString();
                        break;
                }
            }
        });

        gender_grp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.g_male:
                        g = male.getText().toString();
                        break;
                    case R.id.g_female:
                        g = female.getText().toString();
                        break;
                    case R.id.g_unspecified:
                        g = unspecified.getText().toString();
                        break;
//                    default:
//                        g = male.getText().toString();
//                        break;
                }

            }
        });

        sqliteHelper = new sqlite_helper(mcontext);
        sqlite = sqliteHelper.getWritableDatabase();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data_save_sqlite();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                data_save_sqlite();
            }
        });
    }

    protected void data_save_sqlite() {
        try {
            data_allocation();
            if (validateInputs(client_name, mobile_no)) {
                if (!duplicate_check()) {
                    on_no_duplicate_entry();
                } else {
                    on_duplicate_entry();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean duplicate_check() {
        String[] docid = {client_name + "_" + mobile_no};
        @SuppressLint("Recycle")
        Cursor doc = sqlite.query(sqlite_basecolumns.contacts.CONTACTS_TABLE_NAME + "",
                null,
                sqlite_basecolumns.contacts._ID + " = ?",
                docid,
                null,
                null,
                null);
        return doc.getCount() != 0;
    }

    private void on_no_duplicate_entry() {
        long result = 0;
        ContentValues client = new ContentValues();
        client.put(sqlite_basecolumns.contacts._ID, client_name + "_" + mobile_no);
        client.put(sqlite_basecolumns.contacts.client_name, client_name);
        client.put(sqlite_basecolumns.contacts.spouse, spouse);
        client.put(sqlite_basecolumns.contacts.children, children);
        client.put(sqlite_basecolumns.contacts.gender, gender);
        client.put(sqlite_basecolumns.contacts.address_i, address_i);
        client.put(sqlite_basecolumns.contacts.address_ii, address_ii);
        client.put(sqlite_basecolumns.contacts.city, city);
        client.put(sqlite_basecolumns.contacts.post_office, post_office);
        client.put(sqlite_basecolumns.contacts.areapin, areapin);
        client.put(sqlite_basecolumns.contacts.dist, dist);
        client.put(sqlite_basecolumns.contacts.state, state);
        client.put(sqlite_basecolumns.contacts.country, country);
        client.put(sqlite_basecolumns.contacts.std, std);
        client.put(sqlite_basecolumns.contacts.mobile_no, mobile_no);
        client.put(sqlite_basecolumns.contacts.smobile_no, smobile_no);
        client.put(sqlite_basecolumns.contacts.telephoneno, telephoneno);
        client.put(sqlite_basecolumns.contacts.emailid, emailid);
        client.put(sqlite_basecolumns.contacts.anni_dd, anni_dd);
        client.put(sqlite_basecolumns.contacts.bday_dd, bday_dd);
        client.put(sqlite_basecolumns.contacts.note, note);
        client.put(sqlite_basecolumns.contacts.bday_code, bday_code);
        client.put(sqlite_basecolumns.contacts.anni_code, anni_code);
        client.put(sqlite_basecolumns.contacts.qualification, qualification);
        client.put(sqlite_basecolumns.contacts.occupation, occupation);
        client.put(sqlite_basecolumns.contacts.employer, employer);
//                client.put(sqlite_basecolumns.contacts.created_at, date);
        try {
            result = sqlite.insert(sqlite_basecolumns.contacts.CONTACTS_TABLE_NAME, null, client);
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
        final String docid = client_name + "_" + mobile_no;
        new AlertDialog.Builder(mcontext).setIcon(R.drawable.ic_warning_pink_24dp)
                .setTitle("Document Conflict..!!")
                .setMessage("Document is already available for this person..!! \n" +
                        "Do you want to delete that and save new one??")
                .setPositiveButton("Yes..!! Please Delete Old Contact.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete_old_entry();
                        on_no_duplicate_entry();
                    }
                })
                .setNegativeButton("No..!! Please Don't Delete Old Contact.", null)
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
        String doc_id = client_name + "_" + mobile_no;
        sqlite.delete(sqlite_basecolumns.contacts.CONTACTS_TABLE_NAME + "",
                sqlite_basecolumns.contacts._ID + " = '" + doc_id + "'", null
        );
    }

    protected void addingperson_firestore() {

        mpref = getSharedPreferences("User", MODE_PRIVATE);

        final DatePickerDialog.OnDateSetListener dateget1 = new DatePickerDialog.OnDateSetListener() {
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
                annidate();
            }
        };
        final DatePickerDialog.OnDateSetListener dateget2 = new DatePickerDialog.OnDateSetListener() {
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
                bdadate();
            }
        };

        ann_dd.setOnClickListener(new View.OnClickListener() {
            Calendar myCalendar = Calendar.getInstance();

            @Override
            public void onClick(View v) {
                new DatePickerDialog(mcontext, dateget1, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        bda_dd.setOnClickListener(new View.OnClickListener() {
            Calendar myCalendar = Calendar.getInstance();

            @Override
            public void onClick(View v) {
                new DatePickerDialog(mcontext, dateget2, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        clear_bday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bda_dd.setText("");
                bday_code = null;
            }
        });

        clear_anni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ann_dd.setText("");
                anni_code = null;
            }
        });

        occu_grp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.unemployed:
                        occu = unemp.getText().toString();
                        break;
                    case R.id.student:
                        occu = stud.getText().toString();
                        break;
                    case R.id.home:
                        occu = hom.getText().toString();
                        break;
                    case R.id.self:
                        occu = sel.getText().toString();
                        break;
                    case R.id.govtservice:
                        occu = gov.getText().toString();
                        break;
                    case R.id.service:
                        occu = serv.getText().toString();
                        break;
                    case R.id.other:
                        occu = otr.getText().toString();
                        break;
                }
            }
        });

        gender_grp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.g_male:
                        g = male.getText().toString();
                        break;
                    case R.id.g_female:
                        g = female.getText().toString();
                        break;
                    case R.id.g_unspecified:
                        g = unspecified.getText().toString();
                        break;
//                    default:
//                        g = male.getText().toString();
//                        break;
                }

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data_save_firestore();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                data_save_firestore();
            }
        });
    }

    public void data_save_firestore() {
        try {
            data_allocation();
            if (validateInputs(client_name, mobile_no)) {

                final Map<String, Object> client = new HashMap<>();
                client.put("client_name", client_name);
                client.put("spouse", spouse);
                client.put("children", children);
                client.put("gender", gender);
                client.put("address_i", address_i);
                client.put("address_ii", address_ii);
                client.put("city", city);
                client.put("post_office", post_office);
                client.put("areapin", areapin);
                client.put("dist", dist);
                client.put("state", state);
                client.put("country", country);
                client.put("std", std);
                client.put("mobile_no", mobile_no);
                client.put("smobile_no", smobile_no);
                client.put("telephoneno", telephoneno);
                client.put("emailid", emailid);
                client.put("anni_dd", anni_dd);
                client.put("bday_dd", bday_dd);
                client.put("note", note);
                client.put("bday_code", fullFormat.parse(bday_code));
                client.put("anni_code", fullFormat.parse(anni_code));
                client.put("qualification", qualification);
                client.put("occupation", occupation);
                client.put("employer", employer);
                client.put("date", date);
                client.put("app_userid", app_userid);
                final String collection = "prospect" + "/" + app_userid;
//                final String collection2 = "prospect" + "/" + app_userid  + "/client_basic_data";
                final DocumentReference user = fdb.collection(collection + "/" + "client_basic_data")
                        .document(mobile_no + " " + client_name + " ");
                user.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(final DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            new AlertDialog.Builder(mcontext).setIcon(R.drawable.ic_warning_pink_24dp)
                                    .setTitle("Document Conflict..!!")
                                    .setMessage("Document is already available for this person..!! \n" +
                                            "Do you want to delete that and save new one??")
                                    .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
//                                            Toasty.info(mcontext,Objects.requireNonNull(documentSnapshot).toString(),
//                                                    4,true).show();
                                            fdb.collection(collection + "/" + "client_basic_data")
                                                    .document(mobile_no + " " + client_name + " ")
                                                    .set(client)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toasty.warning(mcontext, "Old document is replaced with new document..!!",
                                                                    4, true).show();
                                                            Log.d(TAG, "DocumentSnapshot successfully written!");
                                                            startActivity(new Intent(mcontext, activity_home.class));
                                                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                                            finish();
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
                                    })
//                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    Intent intent = new Intent(mcontext, activity_home.class);
//                                    intent.addCategory(Intent.CATEGORY_HOME);
//                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    startActivity(intent);
//                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                                    finish();
//                                }
//                            })
                                    .setNegativeButton("No", null)
                                    .show();

                        } else {
                            fdb.collection(collection + "/" + "client_basic_data")
                                    .document(mobile_no + " " + client_name + " ")
                                    .set(client)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toasty.success(mcontext, "Document saved successfully",
                                                    4, true).show();
                                            Log.d(TAG, "DocumentSnapshot successfully written!");
                                            startActivity(new Intent(mcontext, activity_home.class));
                                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                            finish();
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
                    }
                })
                ;


            } else {
                Toasty.error(mcontext, "Something went wrong...!!!",
                        Toast.LENGTH_LONG, true).show();
            }
        } catch (Exception e) {
            Toasty.error(mcontext, e.getMessage(),
                    Toast.LENGTH_LONG, true).show();
        }
    }

    private void annidate() {
//        String dd,mm,yyyy;
//        dd=dayFormat.format(c.getTime());
//        mm=monthFormat.format(c.getTime());
//        yyyy=yearFormat.format(c.getTime());
        ann_dd.setText(fullFormat.format(myCalendar.getTime()));
        try {
            anni_code = fullFormat.format(day_monFormat.parse(day_monFormat.format(myCalendar.getTime())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Toast.makeText(mcontext, "Anniversary:" + anni_code, Toast.LENGTH_LONG).show();
//        ann_mm.setText(fullFormat.format(myCalendar.getTime()));
//        ann_yyyy.setText(yearFormat.format(myCalendar.getTime()));
    }

    private void bdadate() {
        bda_dd.setText(fullFormat.format(myCalendar.getTime()));
        try {
            bday_code = fullFormat.format(day_monFormat.parse(day_monFormat.format(myCalendar.getTime())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Toast.makeText(mcontext, "Birthday:" + bday_code, Toast.LENGTH_LONG).show();
//        bda_mm.setText(monthFormat.format(myCalendar.getTime()));
//        bda_yyyy.setText(yearFormat.format(myCalendar.getTime()));
    }

    private void data_allocation() {
        mpref = getSharedPreferences("User", MODE_PRIVATE);
        client_name = clientname_e.getText().toString().toUpperCase().trim();
        spouse = spous.getText().toString().trim();
        children = child.getText().toString().trim();
        gender = g;
        bday_dd = bda_dd.getText().toString().trim();
//        bday_code = bday_dd;
        anni_dd = ann_dd.getText().toString().trim();
//        anni_code = anni_dd;
        address_i = adri_road_house.getText().toString().trim();
        address_ii = adrii_vlg_area.getText().toString().trim();
        city = adriii_city.getText().toString().trim();
        post_office = adriv_po.getText().toString().trim();
        areapin = adrv_pin.getText().toString().trim();
        dist = adrvi_dist.getText().toString().trim();
        state = adrvii_state.getText().toString().trim();
        country = adrviii_country.getText().toString().trim();
        std = std_e.getText().toString().trim();
        mobile_no = mob.getText().toString().trim();
        smobile_no = smob.getText().toString().trim();
        telephoneno = tele.getText().toString().trim();
        emailid = email.getText().toString().trim();
        note = not.getText().toString().trim();
        qualification = qualifi.getText().toString().trim();
        occupation = occu;
        employer = emplyr.getText().toString().trim();
        app_userid = mpref.getString("userID", "");
        date = com.google.firebase.Timestamp.now().toDate().toString();
    }

    private boolean validateInputs(String client_name, String mobile_no) {
        if (client_name.isEmpty()) {
            clientname_e.setError("Name Required..!!");
            clientname_e.requestFocus();
            return false;
        } else if (mobile_no.isEmpty()) {
            mob.setError("Mobile Number Required..!!");
            mob.requestFocus();
            return false;
        }
        return true;
    }

    public void setupitems() {
        save = findViewById(R.id.save_data);

        clientname_e = findViewById(R.id.clname);
        spous = findViewById(R.id.spouse);
        child = findViewById(R.id.children);

        bda_dd = findViewById(R.id.bday_dd);
        ann_dd = findViewById(R.id.anni_dd);
        clear_bday = findViewById(R.id.clear_bday);
        clear_anni = findViewById(R.id.clear_anni);
        qualifi = findViewById(R.id.qualifi);

        adri_road_house = findViewById(R.id.addressi);
        adrii_vlg_area = findViewById(R.id.addressii);
        adriii_city = findViewById(R.id.addressiii);
        adriv_po = findViewById(R.id.addressiv);
        adrv_pin = findViewById(R.id.addressv);
        adrvi_dist = findViewById(R.id.addressvi);
        adrvii_state = findViewById(R.id.addressvii);
        adrviii_country = findViewById(R.id.addressviii);

        std_e = findViewById(R.id.std_code);
        mob = findViewById(R.id.mob_no);
        smob = findViewById(R.id.sec_mob_no);
        tele = findViewById(R.id.telephone_no);
        email = findViewById(R.id.email_id);
        not = findViewById(R.id.note);

        gender_grp = findViewById(R.id.gender_select);
        occu_grp = findViewById(R.id.occu_grp);

        sel = findViewById(R.id.self);
        gov = findViewById(R.id.govtservice);
        stud = findViewById(R.id.student);
        unemp = findViewById(R.id.unemployed);
        hom = findViewById(R.id.home);
        otr = findViewById(R.id.other);
        serv = findViewById(R.id.service);
        male = findViewById(R.id.g_male);
        female = findViewById(R.id.g_female);
        unspecified = findViewById(R.id.g_unspecified);
        emplyr = findViewById(R.id.employer);

        update = findViewById(R.id.save);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Toast.makeText(this,"startActivity(new Intent(show_data_vivid.this, show_data.class));",Toast.LENGTH_SHORT).show();
        if (clientname_e.getText().toString().equals("") || clientname_e.getText().toString().isEmpty()) {
            startActivity(new Intent(mcontext, activity_home.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            finish();
        } else {
            startActivity(new Intent(mcontext, activity_searchpeople.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            finish();
        }

    }

}
