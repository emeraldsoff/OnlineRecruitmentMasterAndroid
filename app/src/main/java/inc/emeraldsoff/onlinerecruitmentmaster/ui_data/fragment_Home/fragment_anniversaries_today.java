package inc.emeraldsoff.onlinerecruitmentmaster.ui_data.fragment_Home;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;
import inc.emeraldsoff.onlinerecruitmentmaster.R;
import inc.emeraldsoff.onlinerecruitmentmaster.sqlite_manager.sqlite_adapters.contact_manager.anniversary_adapter;
import inc.emeraldsoff.onlinerecruitmentmaster.sqlite_manager.sqlite_basecolumns;
import inc.emeraldsoff.onlinerecruitmentmaster.sqlite_manager.sqlite_helper;

public class fragment_anniversaries_today extends Fragment {

    SQLiteDatabase sqlite;
    sqlite_helper sqliteHelper;

    private final int day = 1000 * 60 * 60 * 24;
    private Context mcontext;

    //    Calendar myCalendar = Calendar.getInstance();
//    Calendar calendar = Calendar.getInstance();
    private Date now = new Date();
    SimpleDateFormat fullFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
    //    SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.US);
//    private Date futuredate = null;
    private String currentdate = null;
//    SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.US);
//    SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.US);
//    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.US);
    private SimpleDateFormat day_monFormat = new SimpleDateFormat("dd-MM", Locale.US);
    //    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//    private final long ONE_DAY = 24 * 60 * 60 * 1000;
//    Date now = new Date();
//    String dateString = formatter.format(now);

    private RecyclerView anni_list;
    private anniversary_adapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home_events_viewer, container, false);


        mcontext = getActivity();
        anni_list = v.findViewById(R.id.id_recycle_view);
        setup_db();
        try {
            setupannirecycle_sqlite();
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

    private void setupannirecycle_sqlite() {
        try {
            currentdate = fullFormat.format(day_monFormat.parse(day_monFormat.format(now)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String[] q = {currentdate};
        anni_list.setHasFixedSize(true);
        anni_list.setLayoutManager(new LinearLayoutManager(mcontext));
        adapter = new anniversary_adapter(mcontext, getallitems(q));
        anni_list.setAdapter(adapter);
    }

    private Cursor getallitems(String[] q) {
        return sqlite.query(sqlite_basecolumns.contacts.CONTACTS_TABLE_NAME,
                null,
                sqlite_basecolumns.contacts.anni_code + " = ?",
                q,
                null,
                null,
                sqlite_basecolumns.contacts.client_name + " ASC"
        );
    }
}
