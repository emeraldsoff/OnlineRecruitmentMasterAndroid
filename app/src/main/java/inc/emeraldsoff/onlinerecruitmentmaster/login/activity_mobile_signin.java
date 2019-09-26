package inc.emeraldsoff.onlinerecruitmentmaster.login;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import es.dmoral.toasty.Toasty;
import inc.emeraldsoff.onlinerecruitmentmaster.R;
import inc.emeraldsoff.onlinerecruitmentmaster.splash;
//import inc.emeraldsoff.onlinerecruitmentmaster.ui_data.activity_main;

//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.firestore.EventListener;
//import com.google.firebase.storage.FirebaseStorage;


@SuppressLint("SetTextI18n")
public class activity_mobile_signin extends Activity implements
        View.OnClickListener {
    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    //    private final long ONE_DAY = 24 * 60 * 60 * 1000;
    Date now = new Date();
    String InstallDate = formatter.format(now);
//    int validtime = 60;

    private static final String TAG = "PhoneAuthActivity";

    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";

    private static final int STATE_INITIALIZED = 1;
    private static final int STATE_CODE_SENT = 2;
    private static final int STATE_VERIFY_FAILED = 3;
    private static final int STATE_VERIFY_SUCCESS = 4;
    private static final int STATE_SIGNIN_FAILED = 5;
    private static final int STATE_SIGNIN_SUCCESS = 6;
    //    int back = Color.parseColor("#70e2aa23");
//    int pink = Color.parseColor("#70dd2476");
    // [START declare_auth]
    private FirebaseAuth mAuth;
    //    FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
//            .setPersistenceEnabled(true)
//            .build();
    // [END declare_auth]
    private FirebaseFirestore fdb = FirebaseFirestore.getInstance();
    private FirebaseAuth.AuthStateListener mAuthListener;
//    private DatabaseReference myRef;

//    private ViewGroup mPhoneNumberViews;
//    private ViewGroup mSignedInViews;

    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private Button mStartButton;
    private Button mVerifyButton;
    private Button mResendButton;

    private EditText mPhoneNumberField;
    private EditText mVerificationField;

    private CountDownTimer timer;

    private TextView show_time, code_sent, instruction, instruction2;
//    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_signin);

        // Restore instance state
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }

// Assign views

        show_time = findViewById(R.id.show_time);
        code_sent = findViewById(R.id.code_sent);
        mPhoneNumberField = findViewById(R.id.mob_no);
        mVerificationField = findViewById(R.id.code);
        mStartButton = findViewById(R.id.mobno_submit);
        mVerifyButton = findViewById(R.id.submitcode);
        mResendButton = findViewById(R.id.coderesend);
        instruction = findViewById(R.id.instruction);
        instruction2 = findViewById(R.id.instruction2);

        Button mSignOutButton = findViewById(R.id.sign_out_button);

        // Assign click listeners
        mStartButton.setOnClickListener(this);
        mVerifyButton.setOnClickListener(this);
        mResendButton.setOnClickListener(this);
        mSignOutButton.setOnClickListener(this);

        Toasty.info(activity_mobile_signin.this, "Login using you mobile number.",
                Toast.LENGTH_LONG, true).show();

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
//        mFirebaseDatabase = FirebaseDatabase.getInstance();
//        myRef = mFirebaseDatabase.getReference();
        // Initialize phone auth callbacks
// [START phone_auth_callbacks]
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verificaiton without
                //     user action.
//                Log.d(TAG, "onVerificationCompleted:" + credential);
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                // [END_EXCLUDE]

                // [START_EXCLUDE silent]
                // Update the UI and attempt sign in with the phone credential
                updateUI(STATE_VERIFY_SUCCESS, credential);
                // [END_EXCLUDE]
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
//                Log.w(TAG, "onVerificationFailed", e);
                // [START_EXCLUDE silent]

                mVerificationInProgress = false;
                // [END_EXCLUDE]

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // [START_EXCLUDE]
                    mPhoneNumberField.setError("Invalid phone number.");
                    // [END_EXCLUDE]
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // [START_EXCLUDE]
                    Toasty.error(activity_mobile_signin.this, "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();
                    // [END_EXCLUDE]

                }

                // Show a message and update the UI
                // [START_EXCLUDE]
                updateUI(STATE_VERIFY_FAILED);
                // [END_EXCLUDE]
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);
                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                // [START_EXCLUDE]
                // Update UI
                updateUI(STATE_CODE_SENT);
                // [END_EXCLUDE]
            }
        };
// [END phone_auth_callbacks]
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

        // [START_EXCLUDE]
        if (mVerificationInProgress && validatePhoneNumber()) {
            startPhoneNumberVerification(mPhoneNumberField.getText().toString());
        }
        // [END_EXCLUDE]
    }
    // [END on_start_check_user]

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, mVerificationInProgress);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
    }


    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]
        timer = new CountDownTimer(60000, 1000) {
            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {
                show_time.setText("seconds remaining: " + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
//                mResendButton.setVisibility(View.GONE);
            }

            @SuppressLint("SetTextI18n")
            public void onFinish() {
                show_time.setText("Time Up!!");
//                mResendButton.setVisibility(View.VISIBLE);
            }
        }.start();

        mVerificationInProgress = true;
    }

    public String uid;

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }

    // [START resend_verification]
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
//        timer.cancel();
//        code_sent.setText("Code Requested");
        timer = new CountDownTimer(60000, 1000) {
            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {
                show_time.setText("seconds remaining: " + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
//                mResendButton.setVisibility(View.GONE);
            }

            @SuppressLint("SetTextI18n")
            public void onFinish() {
                show_time.setText("Time Up!!");
//                mResendButton.setVisibility(View.VISIBLE);
            }
        }.start();
    }
    // [END resend_verification]

    // [START sign_in_with_phone]
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            final FirebaseUser user = Objects.requireNonNull(task.getResult()).getUser();

                            //check if user exist or not in Database

                            final String userID = user.getUid();
                            uid = userID;

//                            Date now = new Date();

                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(now);
                            calendar.add(Calendar.DAY_OF_YEAR, +30);
                            Date newDate = calendar.getTime();
                            final String ExpiryDate = formatter.format(newDate);


                            DocumentReference docref = fdb.document("prospect_users" + "/" + userID);
                            docref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot doc = task.getResult();
                                        if (doc != null && doc.exists()) {
                                            Log.d(TAG, "DocumentSnapshot data: " + doc.getData());
//                                            StringBuilder uidf = new StringBuilder();
//                                            StringBuilder usrf = new StringBuilder();
//                                            StringBuilder validtime = new StringBuilder();
                                            StringBuilder ufname = new StringBuilder();
                                            StringBuilder umname = new StringBuilder();
                                            StringBuilder ulname = new StringBuilder();
                                            StringBuilder validitydate = new StringBuilder();
                                            StringBuilder ifval = new StringBuilder();
                                            StringBuilder expire = new StringBuilder();
                                            StringBuilder install = new StringBuilder();
                                            StringBuilder uemail = new StringBuilder();
                                            StringBuilder uphone = new StringBuilder();
//                                            StringBuilder secure = new StringBuilder();
//                                            StringBuilder pin = new StringBuilder();

//                                            uidf.append(doc.get("UserID"));
//                                            usrf.append(doc.get("user"));
//                                            validtime.append(doc.get("ValidTime"));
                                            validitydate.append(doc.get("ValidityDate"));
                                            install.append(doc.get("InstallDate"));
                                            expire.append(doc.get("ExpiryDate"));
                                            ufname.append(doc.get("FirstName"));
                                            umname.append(doc.get("MiddleName"));
                                            ulname.append(doc.get("LastName"));
                                            uemail.append(doc.get("EmailId"));
                                            uphone.append(doc.get("MobileNo"));
                                            ifval.append(doc.getBoolean("IF_VALID"));
//                                            secure.append(doc.getBoolean("IF_SECURE"));
//                                            pin.append(doc.get("PIN"));

                                            Boolean b = Boolean.parseBoolean(String.valueOf(ifval));
//                                            Boolean s = Boolean.parseBoolean(String.valueOf(secure));
//                                            String pno = "+917003564171";
                                            if (String.valueOf(uphone).contains("7003564171") ||
                                                    String.valueOf(uemail).equals("debanjanchakraborty17@gmail.com")) {
                                                SharedPreferences mpref;
                                                mpref = getSharedPreferences("User", MODE_PRIVATE);
                                                SharedPreferences.Editor editor = mpref.edit();
                                                editor.putString("userID", userID);
                                                editor.putString("user", String.valueOf(user));
                                                editor.putString("FirstName", String.valueOf(ufname));
                                                editor.putString("MiddleName", String.valueOf(umname));
                                                editor.putString("LastName", String.valueOf(ulname));
                                                editor.putString("MobileNo", mPhoneNumberField.getText().toString());
                                                editor.putString("EmailId", String.valueOf(uemail));
//                                            editor.putString("ValidTime", String.valueOf(validtime));
                                                editor.putString("ValidityDate", String.valueOf(validitydate));
                                                editor.putString("InstallDate", String.valueOf(install));
                                                editor.putString("ExpiryDate", String.valueOf(expire));
                                                editor.putBoolean("IF_VALID", b);
                                                editor.putBoolean("IF_SECURE", false);
                                                editor.putString("PIN", "");
                                                editor.putBoolean("admin_access", false);
                                                editor.putBoolean("new_install", false);
                                                editor.apply();
                                            } else {
                                                SharedPreferences mpref;
                                                mpref = getSharedPreferences("User", MODE_PRIVATE);
                                                SharedPreferences.Editor editor = mpref.edit();
                                                editor.putString("userID", userID);
                                                editor.putString("user", String.valueOf(user));
                                                editor.putString("FirstName", String.valueOf(ufname));
                                                editor.putString("MiddleName", String.valueOf(umname));
                                                editor.putString("LastName", String.valueOf(ulname));
                                                editor.putString("MobileNo", String.valueOf(uphone));
                                                editor.putString("EmailId", String.valueOf(uemail));
//                                            editor.putString("ValidTime", String.valueOf(validtime));
                                                editor.putString("ValidityDate", String.valueOf(validitydate));
                                                editor.putString("InstallDate", String.valueOf(install));
                                                editor.putString("ExpiryDate", String.valueOf(expire));
                                                editor.putBoolean("IF_VALID", b);
                                                editor.putBoolean("IF_SECURE", false);
                                                editor.putString("PIN", "");
                                                editor.putBoolean("new_install", false);
                                                editor.apply();
                                            }

                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Intent y = new Intent(activity_mobile_signin.this, splash.class);
                                                    y.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    y.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(y);
                                                }
                                            }, 0);

                                        } else {
                                            Map<String, Object> client = new HashMap<>();
//                                            client.put("PIN", "");
//                                            client.put("IF_SECURE", false);
                                            client.put("IF_VALID", true);
//                                            client.put("ValidTime", validtime);
                                            client.put("ValidityDate", InstallDate);
                                            client.put("InstallDate", InstallDate);
                                            client.put("ExpiryDate", ExpiryDate);
                                            client.put("FirstName", "");
                                            client.put("MiddleName", "");
                                            client.put("LastName", "");
                                            client.put("EmailId", "");
                                            client.put("AppInstall", "");
                                            client.put("MobileNo", mPhoneNumberField.getText().toString());
                                            client.put("userID", userID);
                                            client.put("user", String.valueOf(user));
                                            fdb.collection("prospect_users").document(userID)
                                                    .set(client)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Log.d(TAG, "DocumentSnapshot successfully written!");
//                                    Toast.makeText(addclient_activity.this, "Client Added To Database...", Toast.LENGTH_LONG).show();
                                                            String pno = "+917003564171";
//                                                            String email = "debanjanchakraborty17@gmail.com";
                                                            if (mPhoneNumberField.getText().toString().equals(pno)) {
                                                                SharedPreferences mpref;
                                                                mpref = getSharedPreferences("User", MODE_PRIVATE);
                                                                SharedPreferences.Editor editor = mpref.edit();
                                                                editor.putString("userID", userID);
                                                                editor.putString("user", String.valueOf(user));
                                                                editor.putString("MobileNo", mPhoneNumberField.getText().toString());
//                                                            editor.putString("ValidTime", String.valueOf(validtime));
                                                                editor.putString("ValidityDate", InstallDate);
                                                                editor.putString("InstallDate", InstallDate);
                                                                editor.putString("ExpiryDate", ExpiryDate);
                                                                editor.putString("FirstName", "");
                                                                editor.putString("MiddleName", "");
                                                                editor.putString("LastName", "");
                                                                editor.putString("EmailId", "");
                                                                editor.putBoolean("IF_VALID", true);
                                                                editor.putBoolean("IF_SECURE", false);
                                                                editor.putString("PIN", "");
                                                                editor.putBoolean("admin_access", false);
                                                                editor.putBoolean("new_install", true);
                                                                editor.apply();
                                                            } else {
                                                                SharedPreferences mpref;
                                                                mpref = getSharedPreferences("User", MODE_PRIVATE);
                                                                SharedPreferences.Editor editor = mpref.edit();
                                                                editor.putString("userID", userID);
                                                                editor.putString("user", String.valueOf(user));
                                                                editor.putString("MobileNo", mPhoneNumberField.getText().toString());
//                                                            editor.putString("ValidTime", String.valueOf(validtime));
                                                                editor.putString("ValidityDate", InstallDate);
                                                                editor.putString("InstallDate", InstallDate);
                                                                editor.putString("ExpiryDate", ExpiryDate);
                                                                editor.putString("FirstName", "");
                                                                editor.putString("MiddleName", "");
                                                                editor.putString("LastName", "");
                                                                editor.putString("EmailId", "");
                                                                editor.putBoolean("IF_VALID", true);
                                                                editor.putBoolean("IF_SECURE", false);
                                                                editor.putString("PIN", "");
                                                                editor.putBoolean("new_install", true);
                                                                editor.apply();
                                                            }


                                                            Toasty.success(activity_mobile_signin.this, "Logged In Successfully..!!",
                                                                    Toast.LENGTH_LONG, true).show();
                                                            new Handler().postDelayed(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    Intent y = new Intent(activity_mobile_signin.this, splash.class);
                                                                    y.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                    y.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                    startActivity(y);
                                                                }
                                                            }, 0);

                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(TAG, "Error writing document", e);
                                                    Toasty.error(activity_mobile_signin.this, "Failed To Sign Up..!!",
                                                            Toast.LENGTH_LONG, true).show();

                                                }
                                            });
                                        }
                                    } else {
                                        Log.d(TAG, "get failed with ", task.getException());
                                    }
                                }
                            });

                        }
                    }
                });
    }
    // [END sign_in_with_phone]

    private void signOut() {
        mAuth.signOut();
        updateUI(STATE_INITIALIZED);
    }

    private void updateUI(int uiState) {
        updateUI(uiState, mAuth.getCurrentUser(), null);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            updateUI(STATE_SIGNIN_SUCCESS, user);
        } else {
            updateUI(STATE_INITIALIZED);
        }
    }

    private void updateUI(int uiState, FirebaseUser user) {
        updateUI(uiState, user, null);
    }

    private void updateUI(int uiState, PhoneAuthCredential cred) {
        updateUI(uiState, null, cred);
    }

    private void updateUI(int uiState, FirebaseUser user, PhoneAuthCredential cred) {
        switch (uiState) {
            case STATE_INITIALIZED:
                // Initialized state, show only the phone number field and start button
                enableViews(mStartButton, mPhoneNumberField, instruction, instruction2);
                disableViews(mVerifyButton, mResendButton, mVerificationField, code_sent);
//                mDetailText.setText(null);
                break;
            case STATE_CODE_SENT:
                // Code sent state, show the verification field, the
                enableViews(mVerifyButton, mResendButton, mPhoneNumberField, mVerificationField, code_sent);
                disableViews(mStartButton, instruction);
                code_sent.setText("Code Requested");
                //  mDetailText.setText(R.string.status_code_sent);
                Toasty.info(activity_mobile_signin.this, "code sent",
                        Toast.LENGTH_LONG, false).show();
                break;
            case STATE_VERIFY_FAILED:
                timer.cancel();
                // Verification has failed, show all options
                enableViews(mStartButton, instruction, mVerifyButton, mResendButton, mPhoneNumberField, mVerificationField);
                disableViews(code_sent);
                //  mDetailText.setText(R.string.status_verification_failed);
//                mStartButton.setVisibility(View.VISIBLE);
//                mPhoneNumberField.setVisibility(View.VISIBLE);
//                instruction.setVisibility(View.VISIBLE);
//                instruction2.setVisibility(View.VISIBLE);
//                mResendButton.setVisibility(View.VISIBLE);
//                timer.cancel();
                Toasty.error(activity_mobile_signin.this, "verification failed, check if phone number is correct",
                        Toast.LENGTH_LONG, true).show();
                break;
            case STATE_VERIFY_SUCCESS:
                // Verification has succeeded, proceed to firebase sign in
                disableViews(mStartButton, instruction, mVerifyButton, mResendButton, mPhoneNumberField, mVerificationField);
                //  mDetailText.setText(R.string.status_verification_succeeded);
                Toasty.success(activity_mobile_signin.this, "verification success",
                        Toast.LENGTH_LONG, true).show();
                // Set the verification text based on the credential
                if (cred != null) {
                    if (cred.getSmsCode() != null) {
                        mVerificationField.setText(cred.getSmsCode());
                    } else {
                        mVerificationField.setText(R.string.instant_validation);
                    }
                }

                break;
            case STATE_SIGNIN_FAILED:
                // No-op, handled by sign-in check
                //  mDetailText.setText(R.string.status_sign_in_failed);
                break;
            case STATE_SIGNIN_SUCCESS:
                // Np-op, handled by sign-in check
                int SPLASH_DISPLAY_LENGTH = 0;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                Logger.d("Start splash screen");
                        mAuthListener = new FirebaseAuth.AuthStateListener() {
                            @Override
                            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                if (user != null) {
                                    startActivity(new Intent(activity_mobile_signin.this, splash.class));
                                }
                            }
                        };
                        //add listener
                        mAuth.addAuthStateListener(mAuthListener);
                    }
                }, SPLASH_DISPLAY_LENGTH);
                break;
        }

        if (user == null) {
//            Toasty.info(activity_mobile_signin.this,"Login using you phone number.",
//                    Toast.LENGTH_LONG,true).show();
            // Signed out
            Log.d(TAG, "No login data available");
//              mPhoneNumberViews.setVisibility(View.VISIBLE);
//                mSignedInViews.setVisibility(View.GONE);
            //  mStatusText.setText(R.string.signed_out);
        } else {
            Toasty.success(activity_mobile_signin.this, "Signed In Successfully..!! ",
                    Toast.LENGTH_LONG, true).show();
            // Signed in
            //  mStatusText.setText(R.string.signed_in);
            //  mDetailText.setText(getString(R.string.firebase_status_fmt, user.getUid()));
        }
    }

    private boolean validatePhoneNumber() {
        String phoneNumber = mPhoneNumberField.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            mPhoneNumberField.setError("Invalid phone number.");
            return false;
        }

        return true;
    }

    private void enableViews(View... views) {
        for (View v : views) {
            v.setEnabled(true);
        }
    }

    private void disableViews(View... views) {
        for (View v : views) {
            v.setEnabled(false);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mobno_submit:
                if (!validatePhoneNumber()) {
                    return;
                }

//                mStartButton.setBackgroundColor(pink);

                startPhoneNumberVerification(mPhoneNumberField.getText().toString());

//                mResendButton.setVisibility(View.GONE);
//                mVerificationField.setVisibility(View.VISIBLE);
//                mVerifyButton.setVisibility(View.VISIBLE);
//                mStartButton.setVisibility(View.GONE);
//                mPhoneNumberField.setVisibility(View.GONE);
//                instruction.setVisibility(View.GONE);
//                instruction2.setVisibility(View.GONE);


                break;
            case R.id.submitcode:
                String code = mVerificationField.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    mVerificationField.setError("Cannot be empty.");
                    return;
                }

                verifyPhoneNumberWithCode(mVerificationId, code);
                break;
            case R.id.coderesend:
                timer.cancel();
                if (!validatePhoneNumber()) {
                    return;
                }
                code_sent.setText("Requesting Code");
                resendVerificationCode(mPhoneNumberField.getText().toString(), mResendToken);

                break;
            case R.id.sign_out_button:
                signOut();
                break;
        }
    }

}
