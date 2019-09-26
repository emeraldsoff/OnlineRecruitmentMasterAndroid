package inc.emeraldsoff.onlinerecruitmentmaster.appcontrol_ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;
import inc.emeraldsoff.onlinerecruitmentmaster.R;
import inc.emeraldsoff.onlinerecruitmentmaster.ui_data.fragment_Home.activity_home;

public class activity_entry extends Activity {
    EditText pintext;
    TextView instruction;
    Button ok;
    String pin_1 = "";
    SharedPreferences pref;
    Context mcontext;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_entry);
        pref = getSharedPreferences("User", MODE_PRIVATE);
        pintext = findViewById(R.id.pin_text);
        instruction = findViewById(R.id.instruction);
        ok = findViewById(R.id.submit);
        mcontext = this;
        instruction.setText(R.string.enter1_pin);


        ok.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (pintext.getText().toString().equals("") || pintext.getText().toString().isEmpty()) {
                    pintext.isFocusable();
                    pintext.setError("PIN CAN NOT BE EMPTY..!!!", getDrawable(R.drawable.ic_warning_pink_24dp));
                } else if (pintext.getText().toString().length() < 4) {
                    pintext.isFocusable();
                    pintext.setError("PIN MUST BE OF 4 DIGITS..!!!", getDrawable(R.drawable.ic_warning_pink_24dp));
                } else {
                    if (!pintext.getText().toString().equals(pref.getString("PIN", ""))) {
                        pintext.setError("PIN MISMATCHED, TRY AGAIN..!!!", getDrawable(R.drawable.ic_warning_pink_24dp));
                        i++;
                        if (i < 3) {
                            pintext.setText("");
                            pintext.setError("PIN MISMATCHED, TRY AGAIN..!!!", getDrawable(R.drawable.ic_warning_pink_24dp));
                        } else {
                            Toasty.error(mcontext, "You Have Exceeded Maximum Number Of Times To Enter The Right Pin..!!" +
                                    "Good Bye...!!", Toast.LENGTH_LONG, true).show();
                            isDestroyed();
                            finish();
                        }

                    } else if (pintext.getText().toString().equals(pref.getString("PIN", ""))) {
                        Intent y = new Intent(mcontext, activity_home.class);
                        y.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        y.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(y);
                    }
                }
            }
        });
    }
}
