package inc.emeraldsoff.onlinerecruitmentmaster.ui_data.fragment_Home;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;
import inc.emeraldsoff.onlinerecruitmentmaster.R;
import inc.emeraldsoff.onlinerecruitmentmaster.retrofit_helper.interfaces.GetDataService;
import inc.emeraldsoff.onlinerecruitmentmaster.retrofit_helper.instances.RetrofitClientInstance_localhost;
import inc.emeraldsoff.onlinerecruitmentmaster.retrofit_helper.adapter.employeeAdapter;
import inc.emeraldsoff.onlinerecruitmentmaster.retrofit_helper.model.employee_model;
import inc.emeraldsoff.onlinerecruitmentmaster.sqlite_manager.sqlite_adapters.contact_manager.anniversary_adapter;
import inc.emeraldsoff.onlinerecruitmentmaster.sqlite_manager.sqlite_helper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class fragment_anniversaries_today extends Fragment {

    SQLiteDatabase sqlite;
    sqlite_helper sqliteHelper;

    private final int day = 1000 * 60 * 60 * 24;
    private Context mcontext;

    private Date now = new Date();
    SimpleDateFormat fullFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
    private String currentdate = null;
    private SimpleDateFormat day_monFormat = new SimpleDateFormat("dd-MM", Locale.US);

    private RecyclerView anni_list;
    private anniversary_adapter adapter;
    private employeeAdapter adapter1;
    ProgressDialog progressDoalog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home_events_viewer, container, false);


        mcontext = getActivity();
        anni_list = v.findViewById(R.id.id_recycle_view);
        setup_db();
        progressDoalog = new ProgressDialog(mcontext);
        progressDoalog.setMessage("Loading....");
        progressDoalog.show();
        try {
            //setupannirecycle_sqlite();
            //x();
            GetDataService service = RetrofitClientInstance_localhost.getRetrofitInstance().create(GetDataService.class);
            Call<List<employee_model>> call = service.getemployees();
            call.enqueue(new Callback<List<employee_model>>() {
                @Override
                public void onResponse(Call<List<employee_model>> call, Response<List<employee_model>> response) {
                    progressDoalog.dismiss();
                    generateDataList(response.body());
                }

                @Override
                public void onFailure(Call<List<employee_model>> call, Throwable t) {
                    progressDoalog.dismiss();
                    Toast.makeText(mcontext, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
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

    private void x(){
        GetDataService service = RetrofitClientInstance_localhost.getRetrofitInstance().create(GetDataService.class);
        Call<List<employee_model>> call = service.getemployees();
        call.enqueue(new Callback<List<employee_model>>() {
            @Override
            public void onResponse(Call<List<employee_model>> call, Response<List<employee_model>> response) {
                progressDoalog.dismiss();
                generateDataList(response.body());
            }

            @Override
            public void onFailure(Call<List<employee_model>> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(mcontext, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*private void setupannirecycle_mysql() {


        anni_list.setHasFixedSize(true);
        anni_list.setLayoutManager(new LinearLayoutManager(mcontext));
        adapter = new anniversary_adapter(mcontext, getallitems(q));
        anni_list.setAdapter(adapter);
    }*/

    /*private void setupannirecycle_sqlite() {
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
    }*/

    private void generateDataList(List<employee_model> employee_list) {
        adapter1 = new employeeAdapter(mcontext,employee_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mcontext);
        anni_list.setLayoutManager(layoutManager);
        anni_list.setAdapter(adapter1);
    }

    /*private Cursor getallitems(String[] q) {
        return sqlite.query(sqlite_basecolumns.contacts.CONTACTS_TABLE_NAME,
                null,
                sqlite_basecolumns.contacts.anni_code + " = ?",
                q,
                null,
                null,
                sqlite_basecolumns.contacts.client_name + " ASC"
        );
    }*/
}
