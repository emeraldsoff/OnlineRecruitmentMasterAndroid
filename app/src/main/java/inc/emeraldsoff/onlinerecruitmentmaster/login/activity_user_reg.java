package inc.emeraldsoff.onlinerecruitmentmaster.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;
import es.dmoral.toasty.Toasty;
import inc.emeraldsoff.onlinerecruitmentmaster.R;
import inc.emeraldsoff.onlinerecruitmentmaster.splash;

public class activity_user_reg extends Activity {
    TextView brand;
    private static final String TAG = "UserReg";
    EditText fnametext, mnametext, lnametext, mobiletext, emailtext;
    FirebaseAuth mAuth;
    String ufname, umname, ulname, umobile, uemail;
    //    String name, email;
    Button submit;
    //    FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
//            .setPersistenceEnabled(true)
//            .build();
    SharedPreferences mpref;
    // [END declare_auth]
    private FirebaseFirestore fdb = FirebaseFirestore.getInstance();
    Context mcontext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Log.d("Error 1", "error");
        setContentView(R.layout.activity_user_reg);
        mcontext = this;
//        final
        mAuth = FirebaseAuth.getInstance();
        mpref = getSharedPreferences("User", MODE_PRIVATE);
        //final String userID = mpref.getString("userID", "");
        final String userID = mAuth.getCurrentUser().getUid();
//        Toasty.info(this, "UID" + userID, Toast.LENGTH_LONG,true).show();
        brand = findViewById(R.id.textView);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Harlow.ttf");
        brand.setTypeface(typeface);
        fnametext = findViewById(R.id.user_fname);
        mnametext = findViewById(R.id.user_mname);
        lnametext = findViewById(R.id.user_lname);
        mobiletext = findViewById(R.id.phone_no);
        emailtext = findViewById(R.id.email_id);
        submit = findViewById(R.id.submit);

        final String mob = mpref.getString("MobileNo", "");
        try {
            if (Objects.requireNonNull(mob).equals("") || mob.isEmpty()) {
                emailtext.setText(mpref.getString("emailid", ""));
                emailtext.setVisibility(View.GONE);
                mobiletext.setVisibility(View.VISIBLE);
            } else {
                mobiletext.setText(mpref.getString("mobileno", ""));
                mobiletext.setVisibility(View.GONE);
                emailtext.setVisibility(View.VISIBLE);
            }
        }
        catch (Exception e){
            Toasty.error(mcontext,"Null Point Exception", Toast.LENGTH_LONG,true).show();
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mpref = getSharedPreferences("User", MODE_PRIVATE);
//                String mob = mpref.getString("MobileNo","");
                ufname = fnametext.getText().toString().trim();
                umname = mnametext.getText().toString().trim();
                ulname = lnametext.getText().toString().trim();
                uemail = emailtext.getText().toString().trim();
                umobile = mobiletext.getText().toString().trim();
                if(!validateInputs(ufname,ulname,umobile)){
                    if (Objects.requireNonNull(mob).equals("") || mob.isEmpty()) {
//                    mpref = getSharedPreferences("User", MODE_PRIVATE);
                        uemail = mpref.getString("EmailId", "");
                        umobile = mobiletext.getText().toString().trim();
//                    Log.d("Error 2", "error");
                        Toasty.info(activity_user_reg.this, "Mobile No: " + umobile + " And Email ID: " +
                                uemail, Toast.LENGTH_LONG).show();
                    } else {
//                    mpref = getSharedPreferences("User", MODE_PRIVATE);
                        umobile = mob;
                        uemail = emailtext.getText().toString().trim();
                        Toasty.info(activity_user_reg.this, "Mobile No: " + umobile + " And Email ID: " +
                                uemail, Toast.LENGTH_LONG).show();
//                    Log.d("Error 3", "error");
                    }
                    if (isOnline()){
//                    Log.d("Error 6", "error");
                        Map<String, Object> client = new HashMap<>();
                        client.put("FirstName", ufname);
                        client.put("MiddleName", umname);
                        client.put("LastName", ulname);
                        client.put("MobileNo", umobile);
                        client.put("EmailId", uemail);
                        fdb.collection("prospect_users").document(userID).set(client)
                        /*DocumentReference user = fdb.document("prospect_users" + "/" + userID);
//                        Toast.makeText(editdataActivity.this, user+" and "+docid, Toast.LENGTH_LONG).show();
                        user.update("FirstName", ufname);
                        user.update("MiddleName", umname);
                        user.update("LastName", ulname);
                        user.update("MobileNo", umobile);
                        user.update("EmailId", uemail)*/

//                            .isSuccessful();
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        mpref = getSharedPreferences("User", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = mpref.edit();
//                            editor.putString("userID", userID);
//                            editor.putString("user", String.valueOf(user));
//                            editor.putString("ValidityDate", String.valueOf(valid));
//                            editor.putString("AppInstall", String.valueOf(install));
//                                    mpref = getSharedPreferences("User", MODE_PRIVATE);
                                        editor.putString("FirstName", ufname);
                                        editor.putString("MiddleName", umname);
                                        editor.putString("LastName", ulname);
                                        editor.putString("MobileNo", umobile);
                                        editor.putString("EmailId", uemail);
//                                    editor.putString("ValidTime", );
                                        editor.apply();
//                                    Log.d("Error 8","error");
                                        Log.d(TAG, "DocumentSnapshot successfully written!");
//                                    Toast.makeText(addclient_activity.this, "Client Added To Database...", Toast.LENGTH_LONG).show();

                                        Toasty.success(activity_user_reg.this, "User Data Updated Successfuly",
                                                Toast.LENGTH_LONG, true).show();
                                        startActivity(new Intent(activity_user_reg.this, splash.class));
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toasty.error(mcontext, "Failed to save data..!! " +
                                        "Please check your network connection and try again.", Toast.LENGTH_LONG, true).show();
                            }
                        });
                    }else {
                        Toasty.error(mcontext, "Turn on your wifi or mobile data to proceed..!!",
                                Toast.LENGTH_LONG, true).show();
                    }
                }
            }
        });
    }

    private boolean validateInputs(String ufname, String ulname, String umobile) {
        if (ufname.isEmpty()) {
            fnametext.setError("First Name can not be empty..!!",getDrawable(R.drawable.ic_warning_pink_24dp));
            fnametext.requestFocus();
            return true;
        }
        else if (ulname.isEmpty()){
            lnametext.setError("Last Name can not be empty..!!",getDrawable(R.drawable.ic_warning_pink_24dp));
            lnametext.requestFocus();
            return true;
        }
        else if (umobile.isEmpty()){
            mobiletext.setError("Mobile number can not be empty..!!",getDrawable(R.drawable.ic_warning_pink_24dp));
            mobiletext.requestFocus();
            return true;
        }
        return false;
    }

    protected boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager)mcontext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        @SuppressWarnings("deprecation")
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
}
