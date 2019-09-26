package inc.emeraldsoff.onlinerecruitmentmaster.ui_data.fragment_Home;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;
import inc.emeraldsoff.onlinerecruitmentmaster.R;
import inc.emeraldsoff.onlinerecruitmentmaster.sqlite_manager.sqlite_adapters.contact_manager.birthday_adapter;
import inc.emeraldsoff.onlinerecruitmentmaster.sqlite_manager.sqlite_basecolumns;
import inc.emeraldsoff.onlinerecruitmentmaster.sqlite_manager.sqlite_helper;

import static android.content.Context.MODE_PRIVATE;

//import com.crashlytics.android.Crashlytics;

public class fragment_birthdays_upcoming extends Fragment {

    SQLiteDatabase sqlite;
    sqlite_helper sqliteHelper;
    private final int day = 1000 * 60 * 60 * 24;
    private Context mcontext;

    private SharedPreferences mpref;

    //    Calendar myCalendar = Calendar.getInstance();
//    Calendar calendar = Calendar.getInstance();
    private Date now = new Date();
    //    SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.US);
    private String futuredate = "";
    private String currentdate = "";
    private SimpleDateFormat fullFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
//    SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.US);
//    SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.US);
//    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.US);
    private SimpleDateFormat day_monFormat = new SimpleDateFormat("dd-MM", Locale.US);
    //    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//    private final long ONE_DAY = 24 * 60 * 60 * 1000;
//    Date now = new Date();
//    String dateString = formatter.format(now);

    private RecyclerView bday_list;
    private birthday_adapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home_events_viewer, container, false);

        mcontext = getActivity();
        mpref = Objects.requireNonNull(mcontext)
                .getSharedPreferences("User", MODE_PRIVATE);
        bday_list = v.findViewById(R.id.id_recycle_view);
        setup_db();
        try {
            setupbdayrecycle_sqlite();
        } catch (Exception e) {
//            Crashlytics.getInstance();
//            Crashlytics.log(e.getMessage());
//            Toasty.error(mcontext, e+"",
//                    Toast.LENGTH_LONG,true).show();
            e.printStackTrace();
            Toasty.error(mcontext, "Something went wrong..!!",
                    Toast.LENGTH_LONG, true).show();
        }
        return v;
    }

    private void setup_db() {
        sqliteHelper = new sqlite_helper(mcontext);
        sqlite = sqliteHelper.getReadableDatabase();
    }

    private void setupbdayrecycle_sqlite() {
        Calendar calendar = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.DAY_OF_YEAR, +1);
        calendar2.setTime(now);
        calendar2.add(Calendar.DAY_OF_YEAR, +30);
//        String date;
        try {
            futuredate = fullFormat.format(day_monFormat.parse(day_monFormat.format(calendar2.getTime())));
            currentdate = fullFormat.format(day_monFormat.parse(day_monFormat.format(calendar.getTime())));
        } catch (ParseException e) {
            e.printStackTrace();
        }

//        String[] q = {currentdate.toString(),futuredate.toString()};
        String r = currentdate;
        String s = futuredate;

        bday_list.setHasFixedSize(true);
        bday_list.setLayoutManager(new LinearLayoutManager(mcontext));
        adapter = new birthday_adapter(mcontext, getallitems(r, s));
        bday_list.setAdapter(adapter);
    }

    private Cursor getallitems(String r, String s) {
//        String x ="SELECT * FROM "+sqlite_basecolumns.contacts.CONTACTS_TABLE_NAME+ " WHERE "+
//                sqlite_basecolumns.contacts.anni_code+" >= "+"'"+s+"'"+" AND "+
//                sqlite_basecolumns.contacts.anni_code+" <= "+"'"+r+ "'"+" ORDER BY "+
//                sqlite_basecolumns.contacts.anni_code+" ASC";
//
//        String y ="SELECT * FROM "+sqlite_basecolumns.contacts.CONTACTS_TABLE_NAME+ " WHERE "+
//                sqlite_basecolumns.contacts.anni_code+" BETWEEN "+"'"+s+"'"+" AND "+"'"+r+ "'"+
//                " ORDER BY "+ sqlite_basecolumns.contacts.anni_code+" ASC";

//        return sqlite.rawQuery("SELECT * FROM "+sqlite_basecolumns.contacts.CONTACTS_TABLE_NAME+ " WHERE "+
//                sqlite_basecolumns.contacts.anni_code+" BETWEEN ? AND "+s+" ORDER BY "+
//                sqlite_basecolumns.contacts.anni_code+" ASC",q);

//        return sqlite.rawQuery("SELECT * FROM "+sqlite_basecolumns.contacts.CONTACTS_TABLE_NAME+
//                " WHERE "+sqlite_basecolumns.contacts.anni_code+" >= '"+r+"' AND '"+s+
//                "' ORDER BY "+sqlite_basecolumns.contacts.anni_code+" ASC",null);

        return sqlite.query(sqlite_basecolumns.contacts.CONTACTS_TABLE_NAME,
                null,
                sqlite_basecolumns.contacts.bday_code + " >= ?",
                new String[]{r},
                null,
                null,
                sqlite_basecolumns.contacts.bday_code + " ASC"
        );

//        return sqlite.query(sqlite_basecolumns.contacts.CONTACTS_TABLE_NAME,
//                null,
//                sqlite_basecolumns.contacts.anni_code + " BETWEEN "+"'"+r+"'"+" AND "+"'"+s+"'",
//                null,
//                null,
//                null,
//                sqlite_basecolumns.contacts.anni_code+" ASC"
//        );
    }

}
