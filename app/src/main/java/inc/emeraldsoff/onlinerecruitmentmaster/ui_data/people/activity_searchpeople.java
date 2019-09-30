package inc.emeraldsoff.onlinerecruitmentmaster.ui_data.people;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.perf.metrics.Trace;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import inc.emeraldsoff.onlinerecruitmentmaster.R;
import inc.emeraldsoff.onlinerecruitmentmaster.activity_main;
import inc.emeraldsoff.onlinerecruitmentmaster.retrofit_helper.interfaces.GetDataService;
import inc.emeraldsoff.onlinerecruitmentmaster.retrofit_helper.instances.RetrofitClientInstance_localhost;
import inc.emeraldsoff.onlinerecruitmentmaster.retrofit_helper.adapter.employeeAdapter;
import inc.emeraldsoff.onlinerecruitmentmaster.retrofit_helper.model.employee_model;
import inc.emeraldsoff.onlinerecruitmentmaster.sqlite_manager.sqlite_adapters.contact_manager.contact_adapter;
import inc.emeraldsoff.onlinerecruitmentmaster.ui_data.diary.activity_diary_add_page;
import inc.emeraldsoff.onlinerecruitmentmaster.ui_data.fragment_Home.activity_home;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static inc.emeraldsoff.onlinerecruitmentmaster.Constants.MY_PERMISSIONS_CALL_PHONE;

public class activity_searchpeople extends activity_main {

    FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build();

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
    private employeeAdapter adapter1;
    ProgressDialog progressDoalog;

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchpeople);
        super.menucreate();
        setupitems();
        mcontext = this;
        setupitems();
        fab_action();
        /*StringRequest stringRequest = new StringRequest(Request.Method.GET, base_url + employee_url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray()


                } catch (JSONException x) {
                    x.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });*/
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
                //progressDoalog.dismiss();
                Toast.makeText(mcontext, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
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
                //progressDoalog.dismiss();
                Toast.makeText(mcontext, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generateDataList(List<employee_model> employee_list) {
        adapter1 = new employeeAdapter(mcontext,employee_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mcontext);
        id_recycleview.setLayoutManager(layoutManager);
        id_recycleview.setAdapter(adapter1);
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
