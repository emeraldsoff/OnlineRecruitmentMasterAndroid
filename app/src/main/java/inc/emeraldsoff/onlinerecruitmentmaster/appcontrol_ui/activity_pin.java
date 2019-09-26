package inc.emeraldsoff.onlinerecruitmentmaster.appcontrol_ui;

import android.annotation.SuppressLint;
import android.app.Activity;
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

public class activity_pin extends Activity {

//    final public String TAG = "activity_pin_entry";

    EditText pintext;
    TextView instruction;
    Button ok;

    SharedPreferences pref;

    String pin_1 = "";
//    String pin_2 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_entry);

        pref = getSharedPreferences("User", MODE_PRIVATE);
        pintext = findViewById(R.id.pin_text);
        instruction = findViewById(R.id.instruction);
        ok = findViewById(R.id.submit);

//        pintext.isFocusable();
//        if(pref.getString("PIN","").equals("")||pref.getString("PIN","").isEmpty()){
//         pinadd();
//        }
//        else {
//            pinchange();
//        }
//        pinadd();
        ok.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (pin_1.equals("")) {
                    if (pintext.getText().toString().equals("") || pintext.getText().toString().isEmpty()) {
                        pintext.isFocusable();
                        pintext.setError("PIN CAN NOT BE EMPTY..!!!", getDrawable(R.drawable.ic_warning_pink_24dp));
                    } else if (pintext.getText().toString().length() < 4) {
                        pintext.isFocusable();
                        pintext.setError("PIN MUST BE OF 4 DIGITS..!!!", getDrawable(R.drawable.ic_warning_pink_24dp));
                    } else {
                        pin_1 = pintext.getText().toString();
                        pintext.setText("");
                        instruction.setText("REENTER NEW PIN");
                        pintext.isFocusable();
                    }
                } else {
                    if (pintext.getText().toString().equals("") || pintext.getText().toString().isEmpty()) {
                        pintext.isFocusable();
                        pintext.setError("PIN CAN NOT BE EMPTY..!!!", getDrawable(R.drawable.ic_warning_pink_24dp));
                    } else if (pintext.getText().toString().length() < 4) {
                        pintext.isFocusable();
                        pintext.setText("");
                        pintext.setError("PIN MUST BE OF 4 DIGITS..!!!", getDrawable(R.drawable.ic_warning_pink_24dp));
                    } else {
                        if (!pin_1.equals(pintext.getText().toString())) {
                            instruction.setText("ENTER NEW PIN");
                            pintext.isFocusable();
                            pintext.setError("PIN MISMATCHED, TRY AGAIN..!!!", getDrawable(R.drawable.ic_warning_pink_24dp));
                            pintext.setText("");
                            pin_1 = "";
//                            pin_2="";
                        } else {
                            pref = getSharedPreferences("User", MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("PIN", pin_1);
                            editor.putBoolean("IF_SECURE", true);
//                            editor.apply();
                            editor.apply();
                            Toasty.success(activity_pin.this, "PIN code Successfully Updated and Secured..!!",
                                    Toast.LENGTH_LONG, true).show();
//                            Toast.makeText(activity_pin_entry.this, "PIN code Successfully Updated and Secured..!!" +
//                                    pref.getString("PIN", ""), Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(activity_pin.this, activity_settings.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
            }
        });


    }


}
